import java.awt.*;

public class BoxController {
    public Rectangle player;
    public Rectangle box;
    public Vector2 Collisions(Vector2 velocity, float speed){
        Rectangle projPlayer = player;

        //System.out.println(box.getBounds());
        projPlayer.x += velocity.x;
        projPlayer.y += velocity.y;
        //System.out.println(projPlayer.getBounds());
        if(projPlayer.intersects(box)){
            System.out.println("HIT!");
            return new Vector2(velocity.x - speed,velocity.y - speed);
        }
        return velocity;
    }
}
