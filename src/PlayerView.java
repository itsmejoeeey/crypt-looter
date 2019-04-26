import java.awt.*;

public class PlayerView extends CharacterView {
    public PlayerView(Rectangle transform, CharacterModel model) {
        super(transform, model);

        texRes = 64; // texture resolution
        animationFrameSpeed = 100; // animation frame speed in milliseconds
        fourDirectionsOnly = false;
        movementAnimationFrames = 9;
        slashAnimationFrames = 6;
        bowAnimationFrames = 13;
        hurtAnimationFrames = 6;

        charTexNormalFilepath = "src/res/textures/man_walkcycle.png";
        charTexSlashFilepath = "src/res/textures/man_slash.png";
        charTexBowFilepath = "src/res/textures/man_bow.png";
        charTexHurtFilepath = "src/res/textures/man_hurt.png";

        initView();
    }
}
