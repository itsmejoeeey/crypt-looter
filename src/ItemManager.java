import java.awt.*;
import java.util.ArrayList;

public class ItemManager {
    public ArrayList<ItemController> items = new ArrayList<>();
    private WorldController worldController;
    public ItemManager (WorldController worldController, World world, BoxManager boxManager, CharacterModel playerModel, CharacterModel[] bossModels){
        boxManager.itemManager = this;
        this.worldController = worldController;
        int totalItems = 0;
        for (int i = 0; i < world.itemsHealthPotSmall.size(); i++){
            items.add(
                    new SmallHealthPotion(
                            (new Rectangle((int) world.itemsHealthPotSmall.get(i).x * world.tileSize, (int) (world.itemsHealthPotSmall.get(i).y * world.tileSize), 50, 50)),
                            playerModel)
            );
            worldController.getView().add(items.get(i).getView());
            totalItems++;
        }

        for (int i = 0; i < world.itemsHealthPotBig.size(); i++){
            items.add(
                    new LargeHealthPotion(
                            (new Rectangle((int) world.itemsHealthPotBig.get(i).x * world.tileSize, (int) (world.itemsHealthPotBig.get(i).y * world.tileSize), 50, 50)),
                            playerModel)
            );
            worldController.getView().add(items.get(totalItems).getView());
            totalItems++;
        }

        for (int i = 0; i < world.itemsFinalChest.size(); i++){
            items.add(
                    new FinalChest(
                            (new Rectangle((int) world.itemsFinalChest.get(i).x * world.tileSize, (int) (world.itemsFinalChest.get(i).y * world.tileSize), 50, 50)),
                            bossModels)
            );
            worldController.getView().add(items.get(totalItems).getView());
            totalItems++;
        }

        for (int i = 0; i < world.itemsCoin.size(); i++){
            items.add(
                    new Coin(
                            (new Rectangle((int) world.itemsCoin.get(i).x * world.tileSize, (int) (world.itemsCoin.get(i).y * world.tileSize), 50, 50)),
                            playerModel)
            );
            worldController.getView().add(items.get(totalItems).getView());
            totalItems++;
        }

        for (int i = 0; i < world.itemsDagger.size(); i++){
            items.add(
                    new Dagger(
                            (new Rectangle((int) world.itemsDagger.get(i).x * world.tileSize, (int) (world.itemsDagger.get(i).y * world.tileSize), 50, 50)),
                            playerModel)
            );
            worldController.getView().add(items.get(totalItems).getView());
            totalItems++;
        }

        for (int i = 0; i < world.itemsBow.size(); i++){
            items.add(
                    new Bow(
                            (new Rectangle((int) world.itemsBow.get(i).x * world.tileSize, (int) (world.itemsBow.get(i).y * world.tileSize), 50, 50)),
                            playerModel)
            );
            worldController.getView().add(items.get(totalItems).getView());
            totalItems++;
        }
    }


    public void useItem(ItemController item){
        items.remove(item);
        worldController.getView().remove(item.getView());
        worldController.getView().repaint();
    }
}
