import javax.swing.*;
import java.awt.*;

public class MenuPauseController {

    MainController parent;
    MenuPauseView view;

    public MenuPauseController(MainController parent) {
        this.parent = parent;
        view = new MenuPauseView(parent);
    }

    public void update() {
        if(KeyStates.pauseKey.changedSinceLastChecked() && KeyStates.pauseKey.keyState()){
            parent.updateState(MainController.GameState_t.NORMAL_GAME);
            return;
        }
    }

    public JPanel getView() {
        return view;
    }
}
