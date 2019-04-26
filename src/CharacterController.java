import javax.swing.*;
import java.awt.*;

public class CharacterController {
    protected float x = 0;
    protected float y = 0;
    public double deltaTime = 0;

    protected CharacterView view;
    protected CharacterModel model;
    public BoxManager boxManager;
    public BoxController boxController;
    protected SoundController soundController;

    public CharacterController(Point spawnPos, SoundController soundController, BoxManager boxManager){
        model = new CharacterModel(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), 2);
        view = new CharacterView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
        boxController = new BoxController(model, view);
        this.soundController = soundController;
        this.boxManager = boxManager;
    }

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
