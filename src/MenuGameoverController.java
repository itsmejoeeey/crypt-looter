import javax.swing.*;

public class MenuGameoverController {

    MainController parent;
    MenuGameoverView view;

    public MenuGameoverController(MainController parent, CharacterModel character, HighScoreController highScoreController) {
        this.parent = parent;
        view = new MenuGameoverView(parent, character, highScoreController);
    }

    public JPanel getView() {
        return view;
    }
}
