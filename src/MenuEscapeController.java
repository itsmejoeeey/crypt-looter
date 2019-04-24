import javax.swing.*;

public class MenuEscapeController {

    MainController parent;
    MenuEscapeView view;

    public MenuEscapeController(MainController parent, CharacterModel character) {
        this.parent = parent;
        view = new MenuEscapeView(parent, character);
    }

    public void update() {
        if(KeyStates.escapeKey.changedSinceLastChecked() && KeyStates.escapeKey.keyState()){
            parent.updateState(MainController.GameState_t.NORMAL_GAME);
            return;
        }
    }

    public JPanel getView() {
        return view;
    }
}
