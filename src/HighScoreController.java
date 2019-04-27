import java.util.ArrayList;
import java.util.Collections;

public class HighScoreController {
    public ArrayList<String> highScores = new ArrayList<>();

    public HighScoreController() {
        for(int i = 0; i < 10; i++) {
            highScores.add("~");
        }


    }
    public void addHighScore(String newHighScore) {
        highScores.add(newHighScore);
        highScores.sort(String::compareTo);
        highScores.remove(10);
        Collections.reverse(highScores);
    }
    protected void saveHighScore() {

    }
}
