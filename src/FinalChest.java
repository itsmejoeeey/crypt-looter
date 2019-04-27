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
        mainController.updateState(MainController.GameState_t.GAME_OVER);
    }

    @Override
    public boolean canTrigger(){
        return bossModel.dead;
    }
}
