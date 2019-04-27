import javax.lang.model.util.Elements;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerController extends CharacterController {

    // Used to keep track of when a second has elapsed
    private double deltaTimeElapsed = 0;

    public float speed = 0.2f;

    private MainController parent;

    public AttackController[] attackController = new AttackController[3];
    private ProjectileManager projectileManager;

    public PlayerController(MainController parent, Point spawnPos, BoxManager _boxManager, SoundController soundController, ProjectileManager projectileManager) {
        super(spawnPos, soundController, _boxManager);

        this.parent = parent;

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view = new PlayerView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }

        boxController = new BoxController(model, view);
        attackController[0] = new AttackController(new Rectangle(spawnPos.x, spawnPos.y, 20,20));
        attackController[1] = new AttackController(new Rectangle(spawnPos.x, spawnPos.y, 20,20));
        attackController[2] = new AttackController(new Rectangle(spawnPos.x, spawnPos.y, 20,20));
        model.direction = 4;
        boxManager.entities.add(boxController);
        boxManager.playerAttacks = attackController;
        boxManager.player = boxController;
        this.projectileManager = projectileManager;
    }

    //Moves player based on key inputs
    //delta is equal to newPosition - oldPosition so if 0 the player will stand still
    public void update() {
        if(!model.dead) {
            groundMovement();
            attackDetection();
            fireBow();
            if(boxManager.detectEnemyAttackCollision(boxController)){
                model.decreaseHealth(1);
            }
            if(boxManager.detectEnemyProjectileCollision(boxController)){
                model.decreaseHealth(1);
            }
            boxManager.detectItemCollision(boxController);
            if(model.dead){
                model.walking = false;
                model.attackDagger = false;
                model.attackBow = false;
                model.health = 0;

                Timer timer = new Timer(1250, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parent.updateState(MainController.GameState_t.GAME_OVER);
                        // Stop the timer before it loops
                        ((Timer)e.getSource()).stop();
                    }
                });
                timer.start();
            }
        }
        //System.out.println(model.dead);

        // Update the character animations
        view.deltaTime = deltaTime;
        view.update();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view.moveWorld((int) x, (int) y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }

        deltaTimeElapsed += deltaTime;
        if(deltaTimeElapsed > 1000) {
            deltaTimeElapsed -= 1000;
            model.secondsElapsed++;
        }
    }

    public void fireBow(){
        if(model.projectileTimer <= 0 && (KeyStates.projKey.keyState()) && model.bowEquipped){
            projectileManager.spawnProjectile(new Point(boxController.getCenter().x - 15, boxController.getCenter().y - 5), new Vector2(getMoveDirection(model.direction)[0], -getMoveDirection(model.direction)[1]), boxController, true);
            model.projectileTimer = 2;
            model.attackBow = true;
        }
        if(model.projectileTimer > 0){
            model.attackBow = false;
            model.projectileTimer -= deltaTime / 1000;
        }
    }

    public void groundMovement(){
        double deltaX = 0, deltaY = 0;
        if (KeyStates.moveRightKey.keyState())
            deltaX = deltaTime * speed;
        if (KeyStates.moveLeftKey.keyState())
            deltaX = -deltaTime * speed;
        if (KeyStates.moveForwardKey.keyState())
            deltaY = -deltaTime * speed;
        if (KeyStates.moveBackwardsKey.keyState())
            deltaY = deltaTime * speed;
        //Checks with the box manager if it will hit a box and returns movement vector based on collisions
        Vector2 v = boxManager.move(new Vector2((float) deltaX, (float) deltaY), view.getBounds(), boxController);
        x += v.x;
        y += v.y;
    }

    int attackX;
    int attackY;
    public void attackDetection(){
        model.walking = false;
        if(KeyStates.moveRightKey.keyState() ||
                KeyStates.moveLeftKey.keyState() ||
                KeyStates.moveForwardKey.keyState() ||
                KeyStates.moveBackwardsKey.keyState()){
            attackX = 0;
            attackY = 0;
            model.walking = true;
        }

        if (KeyStates.moveRightKey.keyState()) {
            attackX += 1;
            model.walking = true;
        }
        if (KeyStates.moveLeftKey.keyState()){
            attackX += -1;
            model.walking = true;
        }
        if (KeyStates.moveForwardKey.keyState()){
            attackY += 1;
            model.walking = true;
        }
        if (KeyStates.moveBackwardsKey.keyState()){
            attackY += -1;
            model.walking = true;
        }

        setDirection(attackX, attackY);

        int[] centreAttackPosition = getMoveDirection(Math.floorMod((model.direction), 8));
        int[] leftAttackPosition = getMoveDirection(Math.floorMod((model.direction + 1), 8));
        int[] rightAttackPosition = getMoveDirection(Math.floorMod((model.direction - 1), 8));


        double distanceFactor = 0.8;

        //System.out.println(attackX +"," + getMoveDirection(model.direction)[0] +"," + attackY +"," + getMoveDirection(model.direction)[1]);
        //System.out.println(attackController[0].getWidth() + ":" + attackController[0].getHeight());
        //System.out.println(leftAttackPosition[0] + ":" + leftAttackPosition[1] + "_" + attackX + ":" + attackY + "_" + rightAttackPosition[0] + ":" + rightAttackPosition[1]);
        Rectangle centreRectangle = new Rectangle((int)(view.getBounds().x + centreAttackPosition[0] * view.getBounds().height * distanceFactor + (view.getBounds().height - attackController[0].getHeight())/2),
                (int) (view.getBounds().y - centreAttackPosition[1] * view.getBounds().width * distanceFactor + (view.getBounds().width - attackController[0].getWidth())/2), attackController[0].getWidth(), attackController[0].getHeight());
        Rectangle leftRectangle = new Rectangle((int)(view.getBounds().x + leftAttackPosition[0] * view.getBounds().height * distanceFactor + (view.getBounds().height - attackController[1].getHeight())/2),
                (int) (view.getBounds().y - leftAttackPosition[1] * view.getBounds().height * distanceFactor + (view.getBounds().width - attackController[1].getWidth())/2), attackController[1].getWidth(), attackController[1].getHeight());
        Rectangle rightRectangle = new Rectangle((int) (view.getBounds().x + rightAttackPosition[0] * view.getBounds().height * distanceFactor + (view.getBounds().height - attackController[2].getHeight())/2),
                (int) (view.getBounds().y - rightAttackPosition[1] * view.getBounds().height * distanceFactor + (view.getBounds().width - attackController[2].getWidth())/2), attackController[2].getWidth(), attackController[2].getHeight());

        leftRectangle.setLocation((centreRectangle.x - leftRectangle.x)/2 + leftRectangle.x, (centreRectangle.y - leftRectangle.y)/2 + leftRectangle.y);
        rightRectangle.setLocation((centreRectangle.x - rightRectangle.x)/2 + rightRectangle.x, (centreRectangle.y - rightRectangle.y)/2 + rightRectangle.y);
        attackController[0].updateHitBox(centreRectangle, 0);
        attackController[1].updateHitBox(leftRectangle, 0);
        attackController[2].updateHitBox(rightRectangle, 0);

        attackController[0].attackHeight = model.height;

        //System.out.println(model.attackDagger);
        if(model.attackTimer <= 0 && model.daggerEquipped) {
            attackController[0].active = (KeyStates.attackKey.changedSinceLastChecked() && KeyStates.attackKey.keyState());
            model.attackDagger = attackController[0].active;
            if(model.attackDagger){
                model.attackTimer = 0.4;
            }
        } else {
            model.attackDagger = false;
            attackController[0].active = false;
            model.attackTimer -= deltaTime / 1000;
        }
    }

    public int[] getMoveDirection(int direction){
        int [] directions = new int[2];
        switch (direction){
            case 7:
                directions[0] = 1;
                directions[1] = 1;
                return directions;
            case 6:
                directions[0] = 1;
                directions[1] = 0;
                return directions;
            case 5:
                directions[0] = 1;
                directions[1] = -1;
                return directions;
            case 4:
                directions[0] = 0;
                directions[1] = -1;
                return directions;
            case 3:
                directions[0] = -1;
                directions[1] = -1;
                return directions;
            case 2:
                directions[0] = -1;
                directions[1] = 0;
                return directions;
            case 1:
                directions[0] = -1;
                directions[1] = 1;
                return directions;
            case 0:
                directions[0] = 0;
                directions[1] = 1;
                return directions;
        }
        return directions;
    }

    public Point getPos() {
        return view.getPos();
    };
}
