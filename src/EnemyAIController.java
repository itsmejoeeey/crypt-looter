import java.awt.*;

public class EnemyAIController {
    public BoxController enemy;
    public BoxController player;

    public EnemyAIController(BoxController enemy, BoxController player){
        this.enemy = enemy;
        this.player = player;
    }

    public Vector2 move (){
        System.out.println();
        float x = (player.getView().getBounds().x - enemy.getView().getBounds().x);
        float y = (player.getView().getBounds().y - enemy.getView().getBounds().y);
        x = x != 0?Math.signum(x) * 1f:0f;
        y = y != 0?Math.signum(y) * 1f:0f;
        return new Vector2(x, y);
    }
}
