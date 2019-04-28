package crypt_looter;

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

        charTexNormalFilepath = "textures/man_walkcycle.png";
        charTexSlashFilepath = "textures/man_slash.png";
        charTexBowFilepath = "textures/man_bow.png";
        charTexHurtFilepath = "textures/man_hurt.png";

        initView();
    }
}
