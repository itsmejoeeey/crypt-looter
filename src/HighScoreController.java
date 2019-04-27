import java.util.ArrayList;

public class HighScoreController {
    public ArrayList<String> highScores = new ArrayList<>();

    public HighScoreController() {
        for(int i = 0; i < 10; i++) {
            highScores.add("XX");
        }


    }
    public void addHighScore(String newHighScore) {
        highScores.add(newHighScore);
        highScores.sort(String::compareTo);
        highScores.remove(10);
    }
    protected void saveHighScore() {

    }
}
