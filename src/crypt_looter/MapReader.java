package crypt_looter;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// TODO Implement parsing of the items and enemies layers in the provided map files

public class MapReader {
    private int mapHeight;
    private int mapWidth;
    private int mapTileSize;

    private int spawnX;
    private int spawnY;

    private boolean[][] collisions;
    private boolean[][] death;
    private int[][] mapFloor;
    private int[][] mapCosmetic;
    private boolean[][][] heightCollisions;
    private int[][] heightMap;
    ArrayList<Point2D.Double> enemiesNormal;
    ArrayList<Point2D.Double> enemiesBoss;
    ArrayList<Point2D.Double> itemsHealthPotBig;
    ArrayList<Point2D.Double> itemsHealthPotSmall;
    ArrayList<Point2D.Double> itemsCoin;
    ArrayList<Point2D.Double> itemsFinalChest;
    ArrayList<Point2D.Double> itemsBow;
    ArrayList<Point2D.Double> itemsDagger;

    MapReader(String mapPath, boolean externalOrigin) throws IOException, InvalidMapException {

        Document mapXML;
        try {
            if(externalOrigin) {
                mapXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(new File(mapPath)));
            } else {
                mapXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getClass().getClassLoader().getResourceAsStream(mapPath));
            }
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
             parentObject.getAttribute("infinite").equals("0") &&
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

        heightCollisions = new boolean[5][mapWidth][mapHeight];
        heightMap = new int[mapWidth][mapHeight];

        XPath xpath = XPathFactory.newInstance().newXPath();

        // >> Parse the 'spawn' layer
        // This layer of the map only contains properties for the spawn location of the character
        NodeList world;
        try {
            world = (NodeList) xpath.compile("/map/layer[@name='spawn']/properties").evaluate(mapXML, XPathConstants.NODESET);
        } catch(XPathExpressionException ex) {
            // Compulsory to handle invalid XPath expression exception
            throw new InvalidMapException();
        }
        // Ensure there is one result for the XPath expression
        if (world.getLength() != 1) {
            throw new InvalidMapException();
        }
        world = world.item(0).getChildNodes();
        for(int i = 0; i < world.getLength(); i++) {
            Element element;
            // As outputted by 'Tiled' - even-entries are simply newlines
            if(i % 2 == 1) {
                element = (Element) world.item(i);
            } else {
                continue;
            }
            if(element.getNodeName() == "property" &&
                    element.hasAttribute("name") &&
                    element.hasAttribute("type") && element.getAttribute("type").equals("int") &&
                    element.hasAttribute("value")) {

                int value = Integer.parseInt(element.getAttribute("value").toString());

                if(element.getAttribute("name").equals("spawnX")) {
                    spawnX = value;
                }
                if(element.getAttribute("name").equals("spawnY")) {
                    spawnY = value;
                }
            } else {
                throw new InvalidMapException();
            }
        }


        // >> Parse the 'world_floor_XX' levels
        // This layer of the map is all underneath the character. Multiple layers are used to differentiate heights
        // (i.e. so the playerModel cannot walk off the edge of a ledge, etc)
        for(int i = 0; i < 5; i++) {
            NodeList worldLevelNode;
            try {
                worldLevelNode = (NodeList) xpath.compile("/map/group[@name='world']/layer[@name='world_floor_" + String.format("%02d", i) + "']").evaluate(mapXML, XPathConstants.NODESET);
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
        // This layer represents tiles that quickly inflicts damage to kill the playerModel
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

        // >> Parse the 'enemies' layer
        // Creates an array that has the coordinates of the center of the enemy as a double (as the user can create
        // the enemy anywhere on the world in the tile editor)
        enemiesNormal = new ArrayList<Point2D.Double>();
        enemiesBoss = new ArrayList<Point2D.Double>();
        NodeList enemiesNode;
        try {
            enemiesNode = (NodeList) xpath.compile("/map/objectgroup[@name='enemies']").evaluate(mapXML, XPathConstants.NODESET);
        } catch(XPathExpressionException ex) {
            // Compulsory to handle invalid XPath expression exception
            throw new InvalidMapException();
        }
        // Ensure there is one result for the XPath expression
        if (enemiesNode.getLength() != 1) {
            throw new InvalidMapException();
        }
        enemiesNode = enemiesNode.item(0).getChildNodes();
        for(int i = 0; i < enemiesNode.getLength(); i++) {
            Element element;
            // As outputted by 'Tiled' - even-entries are simply newlines
            if(i % 2 == 1) {
                element = (Element) enemiesNode.item(i);
            } else {
                continue;
            }
            System.out.println(enemiesNode.item(i).getNodeName());
            if(element.getNodeName() == "object" &&
               element.hasAttribute("type") &&
               element.hasAttribute("x") &&
               element.hasAttribute("y") &&
               element.hasAttribute("height") &&
               element.hasAttribute("width")) {

                double x = Integer.parseInt(element.getAttribute("x").toString());
                double y = Integer.parseInt(element.getAttribute("y").toString());
                double height = Integer.parseInt(element.getAttribute("height").toString());
                double width = Integer.parseInt(element.getAttribute("width").toString());
                Point2D.Double enemyPoint = new Point2D.Double((x+(width/2))/mapTileSize, (y+(height/2))/mapTileSize);

                if(element.getAttribute("type").equals("normal")) {
                    enemiesNormal.add(enemyPoint);
                }
                if(element.getAttribute("type").equals("boss")) {
                    enemiesBoss.add(enemyPoint);
                }
            }
        }


        // >> Parse the 'items' layer
        // Creates an array that has the coordinates of the center of each item as a double (as the user can create
        // items anywhere on the world in the tile editor)
        itemsHealthPotBig = new ArrayList<Point2D.Double>();
        itemsHealthPotSmall = new ArrayList<Point2D.Double>();
        itemsCoin = new ArrayList<Point2D.Double>();
        itemsFinalChest = new ArrayList<Point2D.Double>();
        itemsBow = new ArrayList<Point2D.Double>();
        itemsDagger = new ArrayList<Point2D.Double>();
        NodeList itemsNode;
        try {
            itemsNode = (NodeList) xpath.compile("/map/objectgroup[@name='items']").evaluate(mapXML, XPathConstants.NODESET);
        } catch(XPathExpressionException ex) {
            // Compulsory to handle invalid XPath expression exception
            throw new InvalidMapException();
        }
        // Ensure there is one result for the XPath expression
        if (itemsNode.getLength() != 1) {
            throw new InvalidMapException();
        }
        itemsNode = itemsNode.item(0).getChildNodes();
        for(int i = 0; i < itemsNode.getLength(); i++) {
            Element element;
            // As outputted by 'Tiled' - even-entries are simply newlines
            if(i % 2 == 1) {
                element = (Element) itemsNode.item(i);
            } else {
                continue;
            }
            System.out.println(itemsNode.item(i).getNodeName());
            if(element.getNodeName() == "object" &&
                    element.hasAttribute("type") &&
                    element.hasAttribute("x") &&
                    element.hasAttribute("y") &&
                    element.hasAttribute("height") &&
                    element.hasAttribute("width")) {

                double x = Integer.parseInt(element.getAttribute("x").toString());
                double y = Integer.parseInt(element.getAttribute("y").toString());
                double height = Integer.parseInt(element.getAttribute("height").toString());
                double width = Integer.parseInt(element.getAttribute("width").toString());
                Point2D.Double itemPoint = new Point2D.Double((x+(width/2))/mapTileSize, (y+(height/2))/mapTileSize);

                if(element.getAttribute("type").equals("health_pot_big")) {
                    itemsHealthPotBig.add(itemPoint);
                }
                if(element.getAttribute("type").equals("health_pot_small")) {
                    itemsHealthPotSmall.add(itemPoint);
                }
                if(element.getAttribute("type").equals("coin")) {
                    itemsCoin.add(itemPoint);
                }
                if(element.getAttribute("type").equals("final_chest")) {
                    itemsFinalChest.add(itemPoint);
                }
                if(element.getAttribute("type").equals("bow")) {
                    itemsBow.add(itemPoint);
                }
                if(element.getAttribute("type").equals("dagger")) {
                    itemsDagger.add(itemPoint);
                }
            }
        }


//        // Print out all parsed maps
//        System.out.println(Arrays.deepToString(mapFloor));
//        System.out.println(Arrays.deepToString(mapCosmetic));
//        System.out.println(Arrays.deepToString(mapFloated));
//        System.out.println(Arrays.deepToString(collisions));
//        System.out.println(Arrays.deepToString(death));
//        System.out.println(Arrays.deepToString(heightCollisions[0]));
//        System.out.println(Arrays.deepToString(heightCollisions[1]));
//        System.out.println(Arrays.deepToString(heightCollisions[2]));
//        System.out.println(Arrays.deepToString(heightCollisions[3]));
//        System.out.println(Arrays.deepToString(heightCollisions[4]));
//        System.out.println(Arrays.deepToString(heightMap));
//
//        System.out.println(enemiesNormal);
//        System.out.println(enemiesBoss);
//
//        System.out.println(itemsHealthPotBig);
//        System.out.println(itemsHealthPotSmall);
//        System.out.println(itemsCoin);
//        System.out.println(itemsFinalChest);
//        System.out.println(itemsBow);
//        System.out.println(itemsDagger);

    }

    // This takes care of extracting and cleaning all values before outputting back to the passed in 2D array
    // The NodeList passed in is of a <layer name="world_XXXXXXXX"> node in the map XML
    //
    // TODO Be aware that this will throw an exception if any world layer has properties - this is not ideal and would
    // be a priority to fix in future versions of this codebase
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
        world.spawnX = spawnX;
        world.spawnY = spawnY;

        world.collisions = collisions;
        world.death = death;
        world.mapFloor = mapFloor;
        world.mapCosmetic = mapCosmetic;

        world.heightCollisions = heightCollisions;
        world.heightMap = heightMap;

        world.enemiesNormal = enemiesNormal;
        world.enemiesBoss = enemiesBoss;

        world.itemsHealthPotBig = itemsHealthPotBig;
        world.itemsHealthPotSmall = itemsHealthPotSmall;
        world.itemsCoin = itemsCoin;
        world.itemsFinalChest = itemsFinalChest;
        world.itemsBow = itemsBow;
        world.itemsDagger = itemsDagger;

        return world;
    }
}
