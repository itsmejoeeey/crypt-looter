import java.awt.*;

//Simple class with a CharacterModel view
//TODO add BoxModel
public class BoxController {
    private Rectangle rect;
    private CharacterModel model;
    private CharacterView view;
    public boolean useView = true;

    public BoxController(CharacterModel model, CharacterView view){
        this.model = model;
        this.view = view;
        useView = false;
    }

    public BoxController(Rectangle rect, int height, boolean addView) {
        this.rect =  rect;
        model = new CharacterModel(rect, height);
        useView = addView;
        if(addView) {
            view = new CharacterView(rect, model);
            switch (height){
                case 0:
                    view.setBackground(Color.BLACK);
                    break;
                case 1:
                    view.setBackground(Color.RED);
                    break;
                case 2:
                    view.setBackground(Color.WHITE);
                    break;
                default:
                    view.setBackground(Color.GREEN);
                    break;
            }

            view.model = model;
        }
    }

    public Rectangle getRect(){
        if(view != null){
            Rectangle transform = new Rectangle(model.baseTranform);
            transform.setLocation((int) (model.baseTranform.x + model.x), (int)(model.baseTranform.y + model.y));
            return transform;
        }
        return rect;
    }

    public void setDeath(boolean death){
        model.dead = death;
    }

    public Rectangle getCenter(){
        Rectangle center = view.getBounds();
        center.setLocation((int) center.getCenterX(), (int) center.getCenterY());
        return center;
    }
    public void setHeight(int height){
        model.height = height;
    }

    public CharacterView getView(){
        return view;
    }

    public void setViewEnable(boolean enable){
        view.setEnabled(enable);
    }

    public int getHeight(){
        return model.height;
    }

    public void update() {
        if(view != null && useView)
            view.moveWorld(0, 0);
    }
}
