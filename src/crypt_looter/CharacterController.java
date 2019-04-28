package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class CharacterController {
    public double deltaTime = 0;
    double deltaX = 0, deltaY = 0;

    protected CharacterView view;
    protected CharacterModel model;
    public BoxManager boxManager;
    public BoxController boxController;
    protected SoundController soundController;

    public CharacterController(Point spawnPos, SoundController soundController, BoxManager boxManager){
        model = new CharacterModel(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), 2);
        this.soundController = soundController;
        this.boxManager = boxManager;
    }

    //Sets model direction based on the attackX and attackY
    protected void setDirection(int attackX, int attackY){
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

    }

    public JPanel getView() {
        return view;
    }
}
