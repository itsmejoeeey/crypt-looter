import java.awt.*;

public class BossController extends EnemyController {
    private ProjectileManager projectileManager;
    public BossController(Point spawnPos, BoxController player, BoxManager _boxManager, SoundController _soundController, CharacterModel playerModel, ProjectileManager projectileManager){
        super(spawnPos, player, _boxManager, _soundController, playerModel);
        this.projectileManager = projectileManager;
        System.out.println(boxController.getView() == view);
        model.maxHealth = 5;
        model.health = 5;
        speed = 0.03;
    }

    @Override
    public void initView(Point spawnPos){
        view = new BossView(new Rectangle(spawnPos.x, spawnPos.y, 80, 80), model);
    }

    public void update(){
        super.update();
    }

    public void attackDetection(){
        Point aiVector = aiController.attackDir();

        int attackX = aiVector.x;
        int attackY = aiVector.y;

        setDirection(attackX, -attackY);
        if(model.attackTimer <= 0 && aiController.canAttack(150, model.height)) {
            double segmentAngle = 2 * Math.PI / 8;
            for(int i =0; i < 8; i++){
                double x = Math.cos(segmentAngle * i) * 2;
                double y = Math.sin(segmentAngle * i) * 2;
                projectileManager.spawnProjectile(new Point((int) (view.getWidth() * x + view.getX()),(int) (view.getHeight() * y + view.getY())), new Vector2(Math.signum((int)x), Math.signum((int)y)), boxController);
            }
            model.attackBow = true;
            model.attackTimer = attackTime;
        }else {
            model.attackBow = false;
            attackController.active = false;
            model.attackTimer -= deltaTime / 1000;
        }
    }
}
