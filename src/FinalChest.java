import java.awt.*;

public class FinalChest extends ItemController {
    public MainController mainController;
    public FinalChest(Rectangle bounds){
        super(bounds);
    }

    @Override
    public void triggerItem(){
        mainController.updateState(MainController.GameState_t.GAME_OVER);
    }
}
