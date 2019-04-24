import javax.swing.*;

public class HUDController {

    HUDView view;

    public HUDController(MainController parent, CharacterModel character) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view = new HUDView(parent, character);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public JPanel getView() {
        return view;
    }
}
