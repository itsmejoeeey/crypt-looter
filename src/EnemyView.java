import java.awt.*;

public class EnemyView extends CharacterView {
    public EnemyView(Rectangle transform, CharacterModel model) {
        super(transform, model);

        texRes = 64; // texture resolution
        animationFrameSpeed = 100; // animation frame speed in milliseconds
        fourDirectionsOnly = true;
        movementAnimationFrames = 9;
        slashAnimationFrames = 6;
        bowAnimationFrames = 13;
        hurtAnimationFrames = 6;

        charTexNormalFilepath = "./res/textures/enemy_walkcycle.png";
        charTexSlashFilepath = "./res/textures/enemy_slash.png";
        // No enemy bow textures, but CharacterView requires one so we will reuse the playerModel texture
        charTexBowFilepath = "./res/textures/man_bow.png";
        charTexHurtFilepath = "./res/textures/enemy_hurt.png";

        initView();
    }
}
