import javax.lang.model.util.Elements;
import javax.swing.*;
import java.awt.*;

public class CharacterController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;

    // Used to keep track of when a second has elapsed
    private double deltaTimeElapsed = 0;

    public float speed = 0.2f;

    CharacterView view;
    CharacterModel model;
    public BoxManager boxManager;
    public BoxController boxController;
    public AttackController[] attackController = new AttackController[3];

    public CharacterController(Point spawnPos, BoxManager _boxManager) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    model = new CharacterModel(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), 2);
                    view = new CharacterView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
                    attackController[0] = new AttackController(new Rectangle(spawnPos.x, spawnPos.y, 20,20));
                    attackController[1] = new AttackController(new Rectangle(spawnPos.x, spawnPos.y, 20,20));
                    attackController[2] = new AttackController(new Rectangle(spawnPos.x, spawnPos.y, 20,20));
                    model.direction = 4;
                    boxController = new BoxController(model, view);
                    boxManager = _boxManager;
                    boxManager.entities.add(boxController);
                    boxManager.playerAttacks = attackController;
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    //Moves player based on key inputs
    //delta is equal to newPosition - oldPosition so if 0 the player will stand still
    public void update() {
        view.deltaTime = deltaTime;
        groundMovement();
        attackDetection();
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
            deltaTimeElapsed-= 1000;
            model.secondsElapsed++;
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

        if(attackX == 0 && attackY == 1){
            model.direction = 0;
        } else if(attackX == 1 && attackY == 1){
            model.direction = 7;
        } else if(attackX == 1 && attackY == 0){
            model.direction = 6;
        } else if(attackX == 1 && attackY == -1){
            model.direction = 5;
        } else if(attackX == 0 && attackY == -1){
            model.direction = 4;
        }else if(attackX == -1 && attackY == -1){
            model.direction = 3;
        }else if(attackX == -1 && attackY == 0){
            model.direction = 2;
        }else if(attackX == -1 && attackY == 1){
            model.direction = 1;
        }

        int[] centreAttackPosition = getMoveDirection(Math.floorMod((model.direction), 8));
        int[] leftAttackPosition = getMoveDirection(Math.floorMod((model.direction + 1), 8));
        int[] rightAttackPosition = getMoveDirection(Math.floorMod((model.direction - 1), 8));

        System.out.println(model.walking);

        //System.out.println(attackX +"," + getMoveDirection(model.direction)[0] +"," + attackY +"," + getMoveDirection(model.direction)[1]);
        //System.out.println(attackController[0].getWidth() + ":" + attackController[0].getHeight());
        //System.out.println(leftAttackPosition[0] + ":" + leftAttackPosition[1] + "_" + attackX + ":" + attackY + "_" + rightAttackPosition[0] + ":" + rightAttackPosition[1]);

        Rectangle centreRectangle = new Rectangle(view.getBounds().x + centreAttackPosition[0] * view.getBounds().height + (view.getBounds().height - attackController[0].getHeight())/2, view.getBounds().y - centreAttackPosition[1] * view.getBounds().width + (view.getBounds().width - attackController[0].getWidth())/2, attackController[0].getWidth(), attackController[0].getHeight());
        Rectangle leftRectangle = new Rectangle(view.getBounds().x + leftAttackPosition[0] * view.getBounds().height + (view.getBounds().height - attackController[1].getHeight())/2, view.getBounds().y - leftAttackPosition[1] * view.getBounds().height + (view.getBounds().width - attackController[1].getWidth())/2, attackController[1].getWidth(), attackController[1].getHeight());
        Rectangle rightRectangle = new Rectangle(view.getBounds().x + rightAttackPosition[0] * view.getBounds().height + (view.getBounds().height - attackController[2].getHeight())/2, view.getBounds().y - rightAttackPosition[1] * view.getBounds().height  + (view.getBounds().width - attackController[2].getWidth())/2, attackController[2].getWidth(), attackController[2].getHeight());

        attackController[0].updateHitBox(centreRectangle, 0);
        attackController[1].updateHitBox(leftRectangle, 0);
        attackController[2].updateHitBox(rightRectangle, 0);

        //Origins playerOrigins = new Origins(view.getBounds(), 0, 0);
        attackController[0].active = (KeyStates.attackKey.changedSinceLastChecked() && KeyStates.attackKey.keyState());
        model.attackDagger = attackController[0].active;
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

    public JPanel getView() {
        return view;
    }

    public Point getPos() {
        return view.getPos();
    };
}
