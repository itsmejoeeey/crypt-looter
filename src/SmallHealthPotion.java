import java.awt.*;

public class SmallHealthPotion extends ItemController {
    CharacterModel playerModel;
    public SmallHealthPotion(Rectangle bounds, CharacterModel playerModel){
        super(bounds);
        this.playerModel = playerModel;
    }

    @Override
    public void triggerItem(){
        playerModel.increaseHealth(1);
    }
}
