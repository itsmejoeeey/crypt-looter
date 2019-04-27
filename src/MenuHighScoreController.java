import javax.swing.*;

public class MenuHighScoreController {

    MainController parent;
    MenuHighScoreView view;

    int[] highScores = new int[10];

    public MenuHighScoreController(MainController parent) {
        this.parent = parent;

        for(int i = 0; i < 10; i++) {
            highScores[i] = 0;
        }

        view = new MenuHighScoreView(parent, highScores);
    }

    public JPanel getView() {
        return view;
    }
}
