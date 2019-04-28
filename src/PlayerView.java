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

        charTexNormalFilepath = "./res/textures/man_walkcycle.png";
        charTexSlashFilepath = "./res/textures/man_slash.png";
        charTexBowFilepath = "./res/textures/man_bow.png";
        charTexHurtFilepath = "./res/textures/man_hurt.png";

        initView();
    }
}
