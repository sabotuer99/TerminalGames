package whorten.termgames.sounds;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

	private Sequencer sequencer = null;
	private boolean midiEnabled = true;
	private boolean soundEnabled = true;
	private SoundPlayer(){}
	
	public void resetMidi(){
		if(sequencer != null && sequencer.isOpen()){
			sequencer.close();
		}
	}
	
	public void stopMidi(){
		if(sequencer != null && sequencer.isOpen() && sequencer.isRunning()){
			sequencer.stop();
		}
	}
	
	public void restartMidi(){
		if(sequencer != null && sequencer.isOpen() && !sequencer.isRunning()){
			sequencer.setTickPosition(0);
			sequencer.start();
		}
	}
	
	public void playMidi(InputStream midiFile, boolean loop){
		
		if(sequencer == null){
			throw new IllegalStateException("cannot play midi, no sequencer set.");
		}		
		
		int loopCount = loop ? Sequencer.LOOP_CONTINUOUSLY : 0;
		try{		
			sequencer.open();
			sequencer.setSequence(midiFile);
			sequencer.setLoopCount(loopCount);
			sequencer.start();			
		} catch (Exception ex){
			// gulp
		}
	}
		
	public void play(InputStream soundFile){
		if(soundFile == null || !soundEnabled){
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
	
	public static class Builder{
		Sequencer sequencer = null;

		public Builder withSequencer(Sequencer sequencer) {
			this.sequencer = sequencer;
			return this;
		}

		public SoundPlayer build() {
			SoundPlayer sp = new SoundPlayer();
			sp.sequencer = this.sequencer;
			return sp;
		}
	}
	
	public void close(){
		if(sequencer != null && sequencer.isOpen()){
			sequencer.close();
		}
	}

	public void setMidiEnabled(boolean enabled){
		midiEnabled = enabled;
	}
	
	public void setSoundEnabled(boolean enabled){
		soundEnabled = enabled;
	}
	
	public boolean isMidiEnabled() {
		return midiEnabled;
	}

	public boolean isSoundEnabled() {
		return soundEnabled;
	}
	
	public boolean toggleMidi(){
		midiEnabled = !midiEnabled;
		if(!midiEnabled){
			stopMidi();
		} else {
			restartMidi();
		}
		return midiEnabled;
	}
	
	public boolean toggleSound(){
		soundEnabled = !soundEnabled;
		return soundEnabled;
	}
}
