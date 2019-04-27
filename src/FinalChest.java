import java.awt.*;

public class FinalChest extends ItemController {
    public MainController mainController;
    private CharacterModel bossModel;
    public FinalChest(Rectangle bounds, CharacterModel bossModel){
        super(bounds);
        this.bossModel = bossModel;
    }

    @Override
    public void triggerItem(){
        if(bossModel.dead) {
            mainController.updateState(MainController.GameState_t.GAME_OVER);
        }
    }
}
