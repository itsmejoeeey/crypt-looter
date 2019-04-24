import java.awt.*;

//Simple class with a Character view
//TODO add BoxModel
public class BoxController {
    Rectangle rect;
    private Character model;
    private CharacterView view;
    private boolean useView = true;

    public BoxController(Character model, CharacterView view){
        this.model = model;
        this.view = view;
        useView = false;
    }

    public BoxController(Rectangle rect, int height, boolean addView) {
        this.rect = rect;
        model = new Character(rect, height);
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

    public CharacterView getView(){
        return view;
    }

    public Rectangle getRect (){
        if(rect != null){
            return  rect;
        }
        return view.getBounds();
    }

    public void setViewEnable(boolean enable){
        view.setEnabled(enable);
    }

    public int getHeight(){
        return model.height;
    }

    public void update() {
        if(view != null && useView) {
            view.moveWorld(0, 0);
        }
    }
}
