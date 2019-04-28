package crypt_looter;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.sound.sampled.*;

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
            String backgroundPath = "sounds/music_background.wav";
            AudioInputStream backgroundAudioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(backgroundPath)));
            System.out.println(backgroundAudioStream.getFormat());
            inGameBackground = AudioSystem.getClip();
            inGameBackground.open(backgroundAudioStream);


            String footstepPath = "sounds/footstep01.wav";
            AudioInputStream footstepAudioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(footstepPath)));
            playerFootstep = AudioSystem.getClip();
            playerFootstep.open(footstepAudioStream);

            String swordSlashPath = "sounds/sword.wav";
            AudioInputStream slashAudioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(swordSlashPath)));
            slashEffect = AudioSystem.getClip();
            slashEffect.open(slashAudioStream);

            String enemy1HitPath = "sounds/monster01.wav";
            AudioInputStream enemyHit1Stream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(enemy1HitPath)));
            enemyHit1 = AudioSystem.getClip();
            enemyHit1.open(enemyHit1Stream);

            String enemy2HitPath = "sounds/monster02.wav";
            AudioInputStream enemyHit2Stream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(enemy2HitPath)));
            enemyHit2 = AudioSystem.getClip();
            enemyHit2.open(enemyHit2Stream);

            String enemy3HitPath = "sounds/monster03.wav";
            AudioInputStream enemyHit3Stream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(enemy3HitPath)));
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
        inGameBackground.stop();
        inGameBackground.loop(Clip.LOOP_CONTINUOUSLY);
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

    public void stopAll() {
        inGameBackground.stop();
        playerFootstep.stop();
        slashEffect.stop();
        enemyHit1.stop();
        enemyHit2.stop();
        enemyHit3.stop();
    }
}
