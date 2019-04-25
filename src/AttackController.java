import javax.swing.*;
import java.awt.*;

public class AttackController extends JPanel {
    public double rotation;
    private Rectangle rect;
    public boolean active;

    public AttackController(Rectangle rect){
        this.rect = rect;
        setPreferredSize(new Dimension(rect.width, rect.height));
    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Point[] points = getPoints();
        int xpoints[] = {points[0].x, points[1].x, points[2].x, points[3].x};
        int ypoints[] = {points[0].y, points[1].y, points[2].y, points[3].y};

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.RED);

        g2.fillPolygon(xpoints, ypoints, 4);
    }


    public Point[] getPoints(){
        Point[] points = new Point[4];
        rect = getBounds();
        int x = rect.x;
        int y = rect.y;
        for (int i = 0; i < points.length; i++){
            points[i] = new Point();
            switch (i) {
                case 0:
                    x = rect.x;
                    y = rect.y;
                    break;
                case 1:
                    x = rect.x + rect.height;
                    y = rect.y;
                    break;
                case 2:
                    x = rect.x;
                    y = rect.y + rect.width;
                    break;
                case 3:
                    x = rect.x + rect.height;
                    y = rect.y + rect.width;
                    break;
            }
            points[i].x = (int) (x * Math.cos(rotation) - y * Math.sin(rotation));
            points[i].y = (int) (x * Math.sin(rotation) + y * Math.cos(rotation));
        }
        return points;
    }

    public void updateHitBox(Rectangle bounds, double rotation){
        this.rotation = rotation;
        setBounds(bounds);
    }
}
