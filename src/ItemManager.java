import java.awt.*;
import java.util.ArrayList;

public class ItemManager {
    public ArrayList<ItemController> items = new ArrayList<>();
    public ItemManager (WorldController worldController, World world, BoxManager boxManager, CharacterModel model){
        boxManager.itemManager = this;
        int totalItems = 0;
        for (int i = 0; i < world.itemsHealthPotSmall.size(); i++){
            items.add(
                    new SmallHealthPotion(
                            (new Rectangle((int) world.itemsHealthPotSmall.get(i).x * world.tileSize, (int) (world.itemsHealthPotSmall.get(i).y * world.tileSize), 50, 50)),
                                model)
                    );
            worldController.getView().add(items.get(i).getView());
            totalItems++;
        }

        for (int i = 0; i < world.itemsHealthPotBig.size(); i++){
            items.add(
                    new LargeHealthPotion(
                            (new Rectangle((int) world.itemsHealthPotBig.get(i).x * world.tileSize, (int) (world.itemsHealthPotBig.get(i).y * world.tileSize), 50, 50)),
                            model)
            );
            worldController.getView().add(items.get(totalItems).getView());
            totalItems++;
        }

        for (int i = 0; i < world.itemsFinalChest.size(); i++){
            items.add(
                    new FinalChest(
                            (new Rectangle((int) world.itemsFinalChest.get(i).x * world.tileSize, (int) (world.itemsFinalChest.get(i).y * world.tileSize), 50, 50)),
                            model)
            );
            worldController.getView().add(items.get(totalItems).getView());
            totalItems++;
        }
    }

    public void update(){
        for(int i =0; i < items.size(); i++){
            items.get(i).update();
        }
    }
}
