import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

// TODO Implement parsing of the items and enemies layers in the provided map files

public class MapReader {
    private int mapHeight;
    private int mapWidth;
    private int mapTileSize;

    private boolean[][] collisions;
    private boolean[][] death;
    private int[][] mapFloor;
    private int[][] mapCosmetic;
    // TODO Consider how necessary mapFloated is - could be overkill for our small game
    private int[][] mapFloated;
    private boolean[][][] heightCollisions;
    private int[][] heightMap;

    MapReader() throws IOException, InvalidMapException {
        File mapFile = new File("src/maps/demomap.tmx");

        Document mapXML;
        try {
            mapXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(mapFile);
        } catch (ParserConfigurationException | SAXException exception) {
            // If the map cannot be parsed it must be an invalid map
            throw new InvalidMapException();
        }

        // Normalize XML document in memory
        mapXML.getDocumentElement().normalize();

        Element parentObject = mapXML.getDocumentElement();

        // Check for correct map structure and parameters (such as map height/width and tile height/width)
        // :: otherwise throw an exception and halt here
        if(!(parentObject.getTagName() == "map" &&
             parentObject.hasAttribute("version") && parentObject.getAttribute("version").equals("1.2") &&
             parentObject.hasAttribute("orientation") && parentObject.getAttribute("orientation").equals("orthogonal") &&
             parentObject.hasAttribute("height") &&
             parentObject.hasAttribute("width") &&
             parentObject.hasAttribute("tileheight") &&
             parentObject.hasAttribute("tilewidth") &&
             parentObject.getAttribute("tilewidth").equals(parentObject.getAttribute("tileheight")))) {
            throw new InvalidMapException();
        }

        mapHeight = Integer.parseInt(parentObject.getAttribute("height"));
        mapWidth = Integer.parseInt(parentObject.getAttribute("width"));

        // Tile height and width have already been checked to ensure they are equal
        mapTileSize = Integer.parseInt(parentObject.getAttribute("tileheight"));

        collisions = new boolean[mapWidth][mapHeight];
        death = new boolean[mapWidth][mapHeight];
        mapFloor = new int[mapWidth][mapHeight];
        mapCosmetic = new int[mapWidth][mapHeight];
        mapFloated = new int[mapWidth][mapHeight];

        heightCollisions = new boolean[5][mapWidth][mapHeight];
        heightMap = new int[mapWidth][mapHeight];

        XPath xpath = XPathFactory.newInstance().newXPath();

        // >> Parse the 'world_floor_XX' levels
        // This layer of the map is all underneath the character. Multiple layers are used to differentiate heights
        // (i.e. so the player cannot walk off the edge of a ledge, etc)
        for(int i = 0; i < 5; i++) {
            NodeList worldLevelNode;
            try {
                worldLevelNode = (NodeList) xpath.compile("/map/group/layer[@name='world_floor_" + String.format("%02d", i) + "']").evaluate(mapXML, XPathConstants.NODESET);
            } catch(XPathExpressionException ex) {
                // Compulsory to handle invalid XPath expression exception
                throw new InvalidMapException();
            }
            // Ensure there is one result for the XPath expression
            if (worldLevelNode.getLength() != 1) {
                throw new InvalidMapException();
            }
            // This updates mapFloor, the final "image" of the map that has the exact sprite values to be displayed
            // (i.e. this only gets updated by the function if this height has a non-zero value for a tile)
            // F.Y.I :: There should only be one child in the NodeList, which is why we pass child index 0 to first arg
            mapLevelExtractor(worldLevelNode.item(0), mapFloor);

            // Pass a new 2D array in so we get the exact tilemap in the map file
            int[][] tempMap = new int[mapWidth][mapHeight];
            mapLevelExtractor(worldLevelNode.item(0), tempMap);
            for(int y = 0; y < mapHeight; y++) {
                for(int x = 0; x < mapWidth; x++) {
                    if(tempMap[y][x] == 0) {
                        heightCollisions[i][y][x] = true;
                    } else {
                        heightMap[y][x] = i;
                    }
                }
            }
        }


        // >> Parse the 'world_floor_*' layer
        // This layer only exists for the purpose of allowing the character to move on this layer regardless of height.
        // This is used to allow stairs to connect heights. It is also clamped ontop of the other 'world_floor_XX' layers.
        NodeList worldAllLevelsNode;
        try {
            worldAllLevelsNode = (NodeList) xpath.compile("/map/group[@name='world']/layer[@name='world_floor_*']").evaluate(mapXML, XPathConstants.NODESET);
        } catch(XPathExpressionException ex) {
            // Compulsory to handle invalid XPath expression exception
            throw new InvalidMapException();
        }
        // Ensure there is one result for the XPath expression
        if (worldAllLevelsNode.getLength() != 1) {
            throw new InvalidMapException();
        }
        // There should only be one child in the NodeList, so we pass child index 0
        mapLevelExtractor(worldAllLevelsNode.item(0), mapFloor);
        //
        // Pass a new 2D array in so we get the exact tilemap in the map file
        // This is used to ensure tiles on world_floor_* are not collisions on any height level
        int[][] tempMap = new int[mapWidth][mapHeight];
        mapLevelExtractor(worldAllLevelsNode.item(0), tempMap);
        for(int y = 0; y < mapHeight; y++) {
            for(int x = 0; x < mapWidth; x++) {
                if(tempMap[y][x] != 0) {
                    int i = 0;
                    while(i < 5) {
                        heightCollisions[i][y][x] = false;
                        i++;
                    }

                    // Ensure tiles on all layers have a special heightMap value
                    heightMap[y][x] = -1;
                }
            }
        }


        // >> Parse the 'world_floor_cosmetic' layer
        // This layer of the map is still underneath the character but is an overlay over the other layers used for
        // cosmetic additions such as bushes and flower fields
        NodeList worldCosmeticNode;
        try {
            worldCosmeticNode = (NodeList) xpath.compile("/map/group[@name='world']/layer[@name='world_floor_cosmetic']").evaluate(mapXML, XPathConstants.NODESET);
        } catch(XPathExpressionException ex) {
            // Compulsory to handle invalid XPath expression exception
            throw new InvalidMapException();
        }
        // Ensure there is one result for the XPath expression
        if (worldCosmeticNode.getLength() != 1) {
            throw new InvalidMapException();
        }
        // There should only be one child in the NodeList, so we pass child index 0
        mapLevelExtractor(worldCosmeticNode.item(0), mapCosmetic);


        // >> Parse the 'world_floor_floated' layer
        // This layer of the map appears above the character. This is used for cases where you want the character to
        // move under a visible item such as tree leaves. This allows for hidden rooms and other easter eggs.
        NodeList worldFloatedNode;
        try {
            worldFloatedNode = (NodeList) xpath.compile("/map/group[@name='world']/layer[@name='world_floor_floated']").evaluate(mapXML, XPathConstants.NODESET);
        } catch(XPathExpressionException ex) {
            // Compulsory to handle invalid XPath expression exception
            throw new InvalidMapException();
        }
        // Ensure there is one result for the XPath expression
        if (worldFloatedNode.getLength() != 1) {
            throw new InvalidMapException();
        }
        // There should only be one child in the NodeList, so we pass child index 0
        mapLevelExtractor(worldFloatedNode.item(0), mapCosmetic);


        // >> Parse the 'collisions' layer
        // This layer represents tiles that the character cannot move to (and instead collides against).
        NodeList collisionsNode;
        try {
            collisionsNode = (NodeList) xpath.compile("/map/layer[@name='collisions']").evaluate(mapXML, XPathConstants.NODESET);
        } catch(XPathExpressionException ex) {
            // Compulsory to handle invalid XPath expression exception
            throw new InvalidMapException();
        }
        // Ensure there is one result for the XPath expression
        if (collisionsNode.getLength() != 1) {
            throw new InvalidMapException();
        }
        // Create temporary 2D array, as collisions could be represented by any sprite resource integer
        int[][] temp_collisions = new int[mapWidth][mapHeight];
        // There should only be one child in the NodeList, so we pass child index 0
        mapLevelExtractor(collisionsNode.item(0), temp_collisions);
        // Convert the temporary 2D array so any non-zero sprite resource integer represents a collision
        for(int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if(temp_collisions[y][x] != 0) {
                    collisions[y][x] = true;
                }
            }
        }


        // >> Parse the 'death' layer
        // This layer represents tiles that quickly inflicts damage to kill the player
        NodeList deathNode;
        try {
            deathNode = (NodeList) xpath.compile("/map/layer[@name='death']").evaluate(mapXML, XPathConstants.NODESET);
        } catch(XPathExpressionException ex) {
            // Compulsory to handle invalid XPath expression exception
            throw new InvalidMapException();
        }
        // Ensure there is one result for the XPath expression
        if (deathNode.getLength() != 1) {
            throw new InvalidMapException();
        }
        // Create temporary 2D array, as death tiles could be represented by any sprite resource integer
        int[][] temp_death = new int[mapWidth][mapHeight];
        // There should only be one child in the NodeList, so we pass child index 0
        mapLevelExtractor(deathNode.item(0), temp_death);
        // Convert the temporary 2D array so any non-zero sprite resource integer represents a death tile
        for(int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if(temp_death[y][x] != 0) {
                    death[y][x] = true;
                }
            }
        }



        // Print out all parsed maps
        System.out.println(Arrays.deepToString(mapFloor));
        System.out.println(Arrays.deepToString(mapCosmetic));
        System.out.println(Arrays.deepToString(mapFloated));
        System.out.println(Arrays.deepToString(collisions));
        System.out.println(Arrays.deepToString(death));
        System.out.println(Arrays.deepToString(heightCollisions[0]));
        System.out.println(Arrays.deepToString(heightCollisions[1]));
        System.out.println(Arrays.deepToString(heightCollisions[2]));
        System.out.println(Arrays.deepToString(heightCollisions[3]));
        System.out.println(Arrays.deepToString(heightCollisions[4]));
        System.out.println(Arrays.deepToString(heightMap));
    }

    // This takes care of extracting and cleaning all values before outputting back to the passed in 2D array
    // The NodeList passed in is of a <layer name="world_XXXXXXXX"> node in the map XML
    private void mapLevelExtractor(Node layerNode, int[][] map) throws InvalidMapException {
        Element layerElement = (Element) layerNode;

        // Each height slice of the map should equal the map size
        if (!(layerElement.getAttribute("height").equals(Integer.toString(mapHeight)) &&
                layerElement.getAttribute("width").equals(Integer.toString(mapWidth)))) {
            throw new InvalidMapException();
        }

        // Because of newline chars between the <layer> and <data> tags, we are looking at child index 1
        // (of the 3 total children)
        Node layerData = layerNode.getChildNodes().item(1);

        // Ensure we have actually selected the correct data node
        if (!(layerData.getNodeName() == "data" &&
                layerData.getAttributes().getNamedItem("encoding").getNodeValue().equals("csv"))) {
            throw new InvalidMapException();
        }

        String rawLayerData = layerData.getTextContent();
        // Remove all newlines, tabs and spaces
        rawLayerData = rawLayerData.replaceAll("[\\n\\t ]", "");

        String[] rawLayerDataSeparated = rawLayerData.split(",");
        for(int y = 0; y < mapHeight; y++) {
            for(int x = 0; x < mapWidth; x++) {
                int tileNumber  = x + (y * mapWidth);
                int tileValue = Integer.parseInt(rawLayerDataSeparated[tileNumber]);

                // Update the tile at the (x,y) coordinates if new value is non-zero
                if(tileValue != 0) {
                    map[y][x] = tileValue;
                }
            }
        }
    }

    /*
        GETTERS
    */

    public World getWorld() {
        World world = new World();
        world.mapSize = new Dimension(mapWidth, mapHeight);
        world.mapTileSize = mapTileSize;
        world.collisions = collisions;
        world.death = death;
        world.mapFloor = mapFloor;
        world.mapCosmetic = mapCosmetic;
        world.mapFloated = mapFloated;

        world.heightCollisions = heightCollisions;
        world.heightMap = heightMap;

        return world;
    }
}
