import java.awt.*;

import static java.lang.Math.abs;

public class CameraController {
    private float x = 0;
    private float y = 0;
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
    private float lerp(int beginPos, int endPos, double percentStep) {
        //return (float) ((endPos - beginPos) * percentStep * deltaTime);
        double step = percentStep;
        return (float) ((beginPos * (1 - step) + endPos * step));
    }

    public void update() {
        Point charPos = character.getPos();
        Point worldPos = world.getPos();

        charOffset = new Point(
                charPos.x - (abs(worldPos.x) + (screenSize.width/2)) + character.getView().getWidth(),
                charPos.y - (abs(worldPos.y) + (screenSize.height/2) - character.getView().getHeight())
        );

        if(!(worldPos.x <= 0)) {
            x = 0;
        } else {
            x += lerp(-charOffset.x, 0, 0.25f) * deltaTime * 0.005;
        }

        if(!(worldPos.y <= 0)) {
            y = 0;
        } else {
            y += lerp(-charOffset.y, 0, 0.25f) * deltaTime * 0.005;
        }

        move((int) x, (int) y);
    }

    public void move(int deltaX, int deltaY) {
        world.moveWorldABS(deltaX, deltaY);
    }
}
