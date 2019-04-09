import java.awt.*;

import static java.lang.Math.abs;

public class CameraController {
    private WorldController world;
    private CharacterController character;

    public double deltaTime = 0;

    private Dimension screenSize;

    private Point charOffset;

    CameraController(WorldController world, CharacterController character, Dimension screenSize) {
        this.character = character;
        this.screenSize = screenSize;
        this.world = world;
    }

    // Linear Interpolation
    // Used for smooth camera movement along a path
    private int lerp(int beginPos, int endPos, double percentStep) {
        double step = percentStep * deltaTime;
        return (int)((beginPos * (1 - step) + endPos * step));
    }

    public void update() {
        Point charPos = character.getPos();
        Point worldPos = world.getPos();

        charOffset = new Point(
                charPos.x - (abs(worldPos.x) + (screenSize.width/2)),
                charPos.y - (abs(worldPos.y) + (screenSize.height/2))
        );

        move(
                lerp(-charOffset.x, 0, 0.5f),
                lerp(-charOffset.y, 0, 0.5f)
        );
    }

    public void move(int deltaX, int deltaY) {
        world.moveWorld(deltaX, deltaY);
    }
}
