import javax.swing.*;

public class MenuGameoverController {

    MainController parent;
    MenuGameoverView view;

    public MenuGameoverController(MainController parent, CharacterModel character) {
        this.parent = parent;
        view = new MenuGameoverView(parent, character);
    }

    public JPanel getView() {
        return view;
    }
}
