import java.awt.*;

public class LargeHealthPotion extends ItemController {
    CharacterModel playerModel;
    public LargeHealthPotion(Rectangle bounds, CharacterModel playerModel){
        super(bounds);
        this.playerModel = playerModel;
    }

    @Override
    public void triggerItem(){
         playerModel.increaseHealth(3);
    }

    @Override
    public boolean canTrigger(){
        if(playerModel.health != playerModel.maxHealth){
            return true;
        }
        return false;
    }
}
