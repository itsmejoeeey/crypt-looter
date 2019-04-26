import java.awt.*;

public class EnemyAIController {
    public BoxController enemy;
    public BoxController player;
    public World world;
    public int attackDistance;

    public EnemyAIController(BoxController enemy, BoxController player){
        this.enemy = enemy;
        this.player = player;
    }

    public Vector2 move (){
        float x = (player.getView().getBounds().x - enemy.getView().getBounds().x);
        float y = (player.getView().getBounds().y - enemy.getView().getBounds().y);
        x = x != 0?Math.signum(x):0f;
        y = y != 0?Math.signum(y):0f;
        return new Vector2(x, y);
    }

    public Point attackDir(){
        float x = (player.getView().getBounds().x - enemy.getView().getBounds().x);
        float y = (player.getView().getBounds().y - enemy.getView().getBounds().y);
        
        if(Math.abs(x) > Math.abs(y)){
            return new Point((int) Math.signum(x), 0);
        } else {
            return new Point(0,(int) Math.signum(y));
        }
    }

    public boolean attack(){
        float x = (player.getView().getBounds().x - enemy.getView().getBounds().x);
        float y = (player.getView().getBounds().y - enemy.getView().getBounds().y);
        if(Math.sqrt(x*x + y*y) < attackDistance){
            return true;
        }
        return false;
    }
}
