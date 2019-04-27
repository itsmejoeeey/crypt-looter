import javax.swing.*;
import java.util.ArrayList;

public class MenuHighScoreController {

    MainController parent;
    MenuHighScoreView view;

    ArrayList<String> highScores;

    public MenuHighScoreController(MainController parent, ArrayList<String> highScores) {
        this.parent = parent;
        this.highScores = highScores;

        view = new MenuHighScoreView(parent, highScores);
    }

    public JPanel getView() {
        return view;
    }
}
