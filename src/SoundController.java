import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SoundController {
    Clip inGameBackground;
    AudioInputStream footstepAudioStream;
    Clip playerFootstep;
    double deltaTime;
    double footstepTimer = 0;
    CharacterModel playerModel;
    public SoundController(CharacterModel playerModel){
        this.playerModel = playerModel;
        try {
            String backgroundPath = "src/res/sounds/ingame_background.wav";
            AudioInputStream backgroundAudioStream = AudioSystem.getAudioInputStream(new File(backgroundPath).getAbsoluteFile());
            System.out.println(backgroundAudioStream.getFormat());
            inGameBackground = AudioSystem.getClip();
            inGameBackground.open(backgroundAudioStream);


            String footstepPath = "src/res/sounds/footstep01.wav";
            footstepAudioStream = AudioSystem.getAudioInputStream(new File(footstepPath).getAbsoluteFile());
            playerFootstep = AudioSystem.getClip();
            playerFootstep.open(footstepAudioStream);

        } catch (UnsupportedAudioFileException ue){
            System.out.println("Unsupported Audio File " + ue.getMessage());
        }catch (IOException ue){
            System.out.println("Audio File Not Found" + ue.getMessage());
        } catch (LineUnavailableException lue){
            System.out.println("Line Unavailable Audio File " + lue.getMessage());
        }
    }

    public void update(){
        if(playerModel.walking && footstepTimer < 0){
            playerFootstep.start();
            footstepTimer = 0.5;
        }
        if(footstepTimer >= 0){
            footstepTimer -= deltaTime / 1000;
        }
    }

    public void playBackgroundMusic(){
        inGameBackground.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopBackgroundMusic(){
        inGameBackground.stop();
    }
}
