import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CharacterView extends JPanel {
    public CharacterModel model;
    private Rectangle transform;

    public double deltaTime;
    public double deltaTimeElapsed;

    private int animationMovementFrame = 0;
    private int animationAttackFrame = 0;
    private int animationDeadFrame = 0;

    private boolean attackSlashAnimation = false;
    private boolean attackBowAnimation = false;
    private boolean deadAnimationCompleted = false; // To stop the dead animation from looping

    private int g2d_image;
    private int g2d_imageDirection;
    private int g2d_imageFrame;

    static int texRes; // texture resolution
    static int animationFrameSpeed; // animation frame speed in milliseconds
    static boolean fourDirectionsOnly;
    static int movementAnimationFrames;
    static int slashAnimationFrames;
    static int bowAnimationFrames;
    static int hurtAnimationFrames;

    static String charTexNormalFilepath;
    static String charTexSlashFilepath;
    static String charTexBowFilepath;
    static String charTexHurtFilepath;

    BufferedImage[][][] charTexFrames;
    public CharacterView(Rectangle transform, CharacterModel model) {
        this.model = model;
        this.transform = transform;

    }

    protected void initView() {
        this.setOpaque(false);
        this.setFocusable(true);
        this.addKeyListener(new KeyController());
        this.setPreferredSize(new Dimension(transform.width,transform.height));

        /*
          Load animation images
        */
        BufferedImage charTexNormal;
        BufferedImage charTexSlash;
        BufferedImage charTexBow;
        BufferedImage charTexHurt;
        charTexFrames = new BufferedImage[4][8][13]; // 4 different tex map, 8 directions, up to 13 frames of animation
        try {
            charTexNormal = ImageIO.read(new File(charTexNormalFilepath));
            charTexSlash = ImageIO.read(new File(charTexSlashFilepath));
            charTexBow = ImageIO.read(new File(charTexBowFilepath));
            charTexHurt = ImageIO.read(new File(charTexHurtFilepath));
        } catch (IOException ex) {
            return;
        }
        if(fourDirectionsOnly) {
            for(int d = 0; d < 8; d = d + 2) {
                for(int a = 0; a < movementAnimationFrames; a++) {
                    charTexFrames[0][d][a] = charTexNormal.getSubimage(a*texRes, (d/2)*texRes, texRes, texRes);
                }
                for(int a = 0; a < slashAnimationFrames; a++) {
                    charTexFrames[1][d][a] = charTexSlash.getSubimage(a*texRes, (d/2)*texRes, texRes, texRes);
                }
                for(int a = 0; a < bowAnimationFrames; a++) {
                    charTexFrames[2][d][a] = charTexBow.getSubimage(a*texRes, (d/2)*texRes, texRes, texRes);
                }
            }
        } else {
            // For each row (each row corresponds to a direction, hence 8 directions)
            for(int d = 0; d < 8; d++) {
                for(int a = 0; a < movementAnimationFrames; a++) {
                    charTexFrames[0][d][a] = charTexNormal.getSubimage(a*texRes, d*texRes, texRes, texRes);
                }
                for(int a = 0; a < slashAnimationFrames; a++) {
                    charTexFrames[1][d][a] = charTexSlash.getSubimage(a*texRes, d*texRes, texRes, texRes);
                }
                for(int a = 0; a < bowAnimationFrames; a++) {
                    charTexFrames[2][d][a] = charTexBow.getSubimage(a*texRes, d*texRes, texRes, texRes);
                }
            }
        }

        // Hurt animation only occurs in one direction
        for(int a = 0; a < hurtAnimationFrames; a++) {
            charTexFrames[3][0][a] = charTexHurt.getSubimage(a*texRes, 0, texRes, texRes);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Default rectangle
//        g2.setColor(Color.black);
//        g2.fillRect(model.baseTranform.x, model.baseTranform.y, model.baseTranform.width, model.baseTranform.height);

        g2.drawImage(charTexFrames[g2d_image][g2d_imageDirection][g2d_imageFrame], -7, -7, 64, 64, null);

    }

    public void update() {
        // ANIMATIONS
        // Work out the image sprite to show above when 'paintComponent()' is evaluated
        // This sets g2d_image, g2d_imageDirection, and g2d_imageFrame which corresponds to an image in a 3D array
        if(!(model.walking ||
             (model.attackDagger || attackSlashAnimation) ||
             (model.attackBow || attackBowAnimation) ||
             (model.dead && !(deadAnimationCompleted)) )) {
            // If standing still - disable animations
            deltaTimeElapsed = 0;
            animationMovementFrame = 0;
            animationAttackFrame = 0;
            animationDeadFrame = 0;
        } else {
            // Set flag when attack is detected
            if(model.attackDagger) {
                this.attackSlashAnimation = true;
            }
            else if(model.attackBow) {
                this.attackBowAnimation = true;
            }

            // Increment frames every 100ms
            if(deltaTimeElapsed > animationFrameSpeed) {
                if(model.walking) {
                    animationMovementFrame++;
                }
                if(model.dead) {
                    animationDeadFrame++;
                }
                if(attackSlashAnimation || attackBowAnimation) {
                    animationAttackFrame++;
                }
                deltaTimeElapsed = 0;
            }
            deltaTimeElapsed += deltaTime;
        }

        if(model.dead && !(deadAnimationCompleted)) {
            g2d_image = 3;
            g2d_imageDirection = 0;
            g2d_imageFrame = animationDeadFrame & (hurtAnimationFrames - 1);
        }
        else if(attackSlashAnimation) {
            g2d_image = 1;
            g2d_imageDirection = model.direction;
            g2d_imageFrame = animationAttackFrame % (slashAnimationFrames - 1);
        }
        else if(attackBowAnimation) {
            g2d_image = 2;
            g2d_imageDirection = model.direction;
            g2d_imageFrame = animationAttackFrame % (bowAnimationFrames - 1);
        }
        else if(model.walking) {
            g2d_image = 0;
            g2d_imageDirection = model.direction;
            g2d_imageFrame = animationMovementFrame % (movementAnimationFrames - 1);
        }

        // Disable flag and reset attackAnimationFrame when animation completes
        if(attackSlashAnimation && (animationAttackFrame % (slashAnimationFrames - 1) == 0) && animationAttackFrame > 0) {
            this.attackSlashAnimation = false;
            animationAttackFrame = 0;
        }
        if(attackBowAnimation && (animationAttackFrame % (bowAnimationFrames - 1) == 0) && animationAttackFrame > 0) {
            this.attackBowAnimation = false;
            animationAttackFrame = 0;
        }
        if(model.dead && (animationDeadFrame % (hurtAnimationFrames - 1) == 0) && animationDeadFrame > 0) {
            this.deadAnimationCompleted = true;
            animationDeadFrame = 0;
        }

        repaint();
    }

    public void moveWorld(int newX, int newY) {
        if(model != null)
            this.setLocation(model.baseTranform.x + newX, model.baseTranform.y + newY);
    }

    public Point getPos() {
        return new Point(this.getX(), this.getY());
    }
}
