import javax.swing.*;

public class HUDController {

    HUDView view;

    public HUDController(MainController parent, CharacterModel character) {
        view = new HUDView(parent, character);
    }

    public void update() {
    }

    public JPanel getView() {
        return view;
    }
}
