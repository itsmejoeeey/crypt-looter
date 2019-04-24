import java.awt.*;

//Simple class with a Character view
//TODO add BoxModel
public class BoxController {
    public Rectangle rect;
    private Character model;
    private CharacterView view;

    public BoxController(Rectangle rect, int height, boolean addView) {
        this.rect =  rect;
        model = new Character(rect, height);
        if(addView) {
            view = new CharacterView(rect);
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

    public CharacterView getView(){
        return view;
    }

    public int getHeight(){
        return model.height;
    }

    public void update() {
        if(view != null)
            view.moveWorld(0, 0);
    }
}
