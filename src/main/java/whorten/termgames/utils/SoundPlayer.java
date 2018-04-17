package whorten.termgames.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

	public void play(InputStream soundFile){
		if(soundFile == null){
			return;
		}
		
		Clip clip = null;
	      try {         
	          AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile); 
	          clip = AudioSystem.getClip();
	          clip.open(audioIn);
	          new ClipHandler(clip).play();
	       } catch (UnsupportedAudioFileException e) {
	       } catch (IOException e) {
	       } catch (LineUnavailableException e) {
	       } 
	}
	
	private class ClipHandler implements LineListener, Runnable {
	    private Clip clip;

	    public ClipHandler(Clip clip) {
	        this.clip = clip;
	        clip.addLineListener(this);
	    }

	    public void play() {
	        clip.start();
	    }

	    public void update(LineEvent e) {
	        if (e.getType() == LineEvent.Type.STOP) {
	            new Thread(this).start();
	        }
	    }

	    public void run() {
	        clip.close();
	    }
	}
}
