import java.awt.*;

import static java.lang.Math.abs;

public class CameraController {
    private float x = 0;
    private float y = 0;

    private CharacterController characterController;
    private World world;
    private WorldController worldController;

    public double deltaTime = 0;

    private Dimension screenSize;

    private Point charOffset;

    CameraController(World world, WorldController worldController, CharacterController characterController, Dimension screenSize) {
        this.characterController = characterController;
        this.screenSize = screenSize;
        this.world = world;
        this.worldController = worldController;

        x = worldController.getPos().x;
        y = worldController.getPos().y;
    }

    // Linear Interpolation
    // Used for smooth camera movement along a path
    private float lerp(int beginPos, int endPos, double percentStep) {
        return (float) ((beginPos * (1 - percentStep) + endPos * percentStep));
    }

    public void update() {
        Point charPos = characterController.getPos();
        Point worldPos = worldController.getPos();

        charOffset = new Point(
                charPos.x - (abs(worldPos.x) + (screenSize.width/2)) + characterController.getView().getWidth(),
                charPos.y - (abs(worldPos.y) + (screenSize.height/2) - characterController.getView().getHeight())
        );

        // Check the world allows camera movement in the x direction
        // (typically disabled if the world is smaller than the screen)
        if(worldController.isCameraEnabledX()) {
            x += lerp(-charOffset.x, 0, 0.25f) * deltaTime * 0.005;
            // Ensure camera can't move past the left-edge of the world (x-plane)
            if (!(x <= 0)) {
                x = 0;
            }
            // Ensure the camera can't move past the right-edge of the world (x-plane)
            int maxCameraX = (world.mapSize.width * world.tileSize) - screenSize.width;
            if (!(x >= -maxCameraX)) {
                x = -maxCameraX;
            }
            // If camera moving the world in only in one direction, use single-axis movement function
            if(!(worldController.isCameraEnabledY())) {
                worldController.moveWorldABSX((int)x);
            }
        }

        // Check the world allows camera movement in the y direction
        // (typically disabled if the world is smaller than the screen)
        if(worldController.isCameraEnabledY()) {
            y += lerp(-charOffset.y, 0, 0.25f) * deltaTime * 0.005;
            // Ensure camera can't move past the top-edge of the world (y-plane)
            if (!(y <= 0)) {
                y = 0;
            }
            // Ensure camera can't move past the bottom-edge of the world (y-plane)
            int maxCameraY = (world.mapSize.height * world.tileSize) - screenSize.height;
            if (!(y > -maxCameraY)) {
                y = -maxCameraY;
            }
            // If camera moving the world in only in one direction, use single-axis movement function
            if(!(worldController.isCameraEnabledX())) {
                worldController.moveWorldABSY((int)y);
            }
        }

        // Otherwise if camera moving the world in both directions, use dual-axis movement function
        if(worldController.isCameraEnabledX() && worldController.isCameraEnabledY()) {
            worldController.moveWorldABS((int)x, (int)y);
        }
    }
}
