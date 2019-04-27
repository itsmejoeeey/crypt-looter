import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SoundController {
    private Clip inGameBackground;
    private Clip playerFootstep;
    private Clip slashEffect;
    private Clip enemyHit1;
    private Clip enemyHit2;
    private Clip enemyHit3;
    double deltaTime;
    double footstepTimer = 0;
    private CharacterModel playerModel;
    private MainController parent;
    public SoundController(MainController parent, CharacterModel playerModel){
        this.parent = parent;
        this.playerModel = playerModel;
        try {
            String backgroundPath = "src/res/sounds/ingame_background.wav";
            AudioInputStream backgroundAudioStream = AudioSystem.getAudioInputStream(new File(backgroundPath).getAbsoluteFile());
            System.out.println(backgroundAudioStream.getFormat());
            inGameBackground = AudioSystem.getClip();
            inGameBackground.open(backgroundAudioStream);


            String footstepPath = "src/res/sounds/footstep01.wav";
            AudioInputStream footstepAudioStream = AudioSystem.getAudioInputStream(new File(footstepPath).getAbsoluteFile());
            playerFootstep = AudioSystem.getClip();
            playerFootstep.open(footstepAudioStream);

            String swordSlashPath = "src/res/sounds/swordsound.wav";
            AudioInputStream slashAudioStream = AudioSystem.getAudioInputStream(new File(swordSlashPath).getAbsoluteFile());
            slashEffect = AudioSystem.getClip();
            slashEffect.open(slashAudioStream);

            String enemy1HitPath = "src/res/sounds/monster-2.wav";
            AudioInputStream enemyHit1Stream = AudioSystem.getAudioInputStream(new File(enemy1HitPath).getAbsoluteFile());
            enemyHit1 = AudioSystem.getClip();
            enemyHit1.open(enemyHit1Stream);

            String enemy2HitPath = "src/res/sounds/monster-7.wav";
            AudioInputStream enemyHit2Stream = AudioSystem.getAudioInputStream(new File(enemy2HitPath).getAbsoluteFile());
            enemyHit2 = AudioSystem.getClip();
            enemyHit2.open(enemyHit2Stream);

            String enemy3HitPath = "src/res/sounds/monster-10.wav";
            AudioInputStream enemyHit3Stream = AudioSystem.getAudioInputStream(new File(enemy3HitPath).getAbsoluteFile());
            enemyHit3 = AudioSystem.getClip();
            enemyHit3.open(enemyHit3Stream);

        } catch (UnsupportedAudioFileException ue){
            System.out.println("Unsupported Audio File " + ue.getMessage());
        }catch (IOException ue){
            System.out.println("Audio File Not Found" + ue.getMessage());
        } catch (LineUnavailableException lue){
            System.out.println("Line Unavailable Audio File " + lue.getMessage());
        }
    }

    public void update(){
        walkingEffects();
        playAttack();
    }

    public void walkingEffects(){
        if(playerModel.walking){
            playerFootstep.loop(-1);
        }
        if(!playerModel.walking || parent.state != MainController.GameState_t.NORMAL_GAME ){
            playerFootstep.stop();
        }
        if(footstepTimer >= 0){
            footstepTimer -= deltaTime / 1000;
        }
    }

    public void playAttack(){
        if(playerModel.attackDagger) {
            slashEffect.setFramePosition(0);
            slashEffect.loop(0);
        }
    }

    public void playBackgroundMusic(){
        inGameBackground.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopBackgroundMusic(){
        inGameBackground.stop();
    }

    public void playEnemyHit(){
        int hit = (int) (Math.random() * 3 + 1);
        switch (hit) {
            case 1:
                enemyHit1.setFramePosition(0);
                enemyHit1.loop(0);
            case 2:
                enemyHit2.setFramePosition(0);
                enemyHit2.loop(0);
            case 3:
                enemyHit3.setFramePosition(0);
                enemyHit3.loop(0);
        }
    }
}
