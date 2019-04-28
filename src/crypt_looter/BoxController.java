package crypt_looter;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;

//Simple class with a CharacterModel view
//TODO add BoxModel
public class BoxController {
    private Rectangle rect;
    private CharacterModel model;
    private CharacterView view;
    private BoxView boxView;
    public boolean useView = true;

    public BoxController(CharacterModel model, CharacterView view){
        this.model = model;
        this.view = view;
        useView = false;
    }

    public BoxController(Rectangle rect, WorldController worldController, int height, boolean addView) {
        this.rect =  rect;
        model = new CharacterModel(rect, height);
        useView = addView;
        if(addView) {
            boxView = new BoxView(rect);
            switch (height){
                case 0:
                    boxView.setBackground(Color.BLACK);
                    break;
                case 1:
                    boxView.setBackground(Color.RED);
                    break;
                case 2:
                    boxView.setBackground(Color.WHITE);
                    break;
                default:
                    boxView.setBackground(Color.GREEN);
                    break;
            }
            //view.model = model;
            worldController.getView().add(boxView);
        }
    }

    public Rectangle getRect(){
        if(view != null){
            return model.getTransform();
        }
        return rect;
    }

    public void setDeath(boolean death){
        model.dead = death;
    }

    public Rectangle getCenter(){
        Rectangle center = model.getTransform();
        center.setLocation((int) center.getCenterX(), (int) center.getCenterY());
        return center;
    }
    public void setHeight(int height){
        model.height = height;
    }

    public CharacterView getView(){
        return view;
    }

    public int getHeight(){
        return model.height;
    }

    public void update() {
        if(model != null && useView)
            model.moveWorld(0, 0);
    }
}
