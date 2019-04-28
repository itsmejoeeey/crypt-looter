package crypt_looter;

import java.awt.*;

public class BossView extends CharacterView{
    public BossView(Rectangle transform, CharacterModel model) {
        super(transform, model);

        texRes = 64; // texture resolution
        animationFrameSpeed = 200; // animation frame speed in milliseconds
        fourDirectionsOnly = true;
        movementAnimationFrames = 4;
        slashAnimationFrames = 4;
        bowAnimationFrames = 4;
        hurtAnimationFrames = 6;

        charTexNormalFilepath = "textures/boss_walkcycle.png";
        charTexSlashFilepath = "textures/boss_slash.png";
        // No enemy bow textures, but CharacterView requires one so we will reuse the boss slash texture
        charTexBowFilepath = "textures/boss_slash.png";
        charTexHurtFilepath = "textures/boss_hurt.png";

        initView();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Default rectangle
//        g2.setColor(Color.black);
//        g2.fillRect(playerModel.baseTranform.x, playerModel.baseTranform.y, playerModel.baseTranform.width, playerModel.baseTranform.height);

        g2.drawImage(charTexFrames[g2d_image][g2d_imageDirection][g2d_imageFrame], 0, -10, 64, 64, null);

    }
}
