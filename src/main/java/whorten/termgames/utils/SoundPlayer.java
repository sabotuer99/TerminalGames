package whorten.termgames.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

	private Sequencer sequencer = null;
	private Synthesizer synthesizer = null;
	private SoundPlayer(){
		try{
		     MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		     for (int i = 0; i < infos.length; i++){
		    	 Object mysteryDevice = MidiSystem.getMidiDevice(infos[i]);
		    	 if(mysteryDevice instanceof Sequencer) {
		    			 sequencer = (Sequencer) mysteryDevice;
		    	 }
		    	 if(mysteryDevice instanceof Synthesizer) {
		    			 synthesizer = (Synthesizer) mysteryDevice;
		    	 }
		     }
		     sequencer.open();
		     synthesizer.open();
		     Receiver synthReceiver = synthesizer.getReceiver();
		     Transmitter seqTransmitter = sequencer.getTransmitter();
		     seqTransmitter.setReceiver(synthReceiver);
		} catch (Exception ex){
			//gulp
		}
	}
	
	public void stopMidi(){
		if(sequencer != null && sequencer.isOpen() && sequencer.isRunning()){
			sequencer.stop();
		}
	}
	
	public void playMidi(InputStream midiFile, boolean loop){

//		try{
//	     sequencer.stop();     	     
//	     sequencer.setSequence(MidiSystem.getSequence(midiFile));
//	     sequencer.start();
//	     MidiChannel[] channels = synthesizer.getChannels();
//	     for (int i = 0; i < channels.length; i++)
//	          channels[i].controlChange(7, 30);
//		
//		} catch (Exception ex) {}
		
		//		if(sequencer == null){
//			throw new IllegalStateException("cannot play midi, no sequencer set.");
//		}		
//		
//		int loopCount = loop ? Sequencer.LOOP_CONTINUOUSLY : 0;
//		try{		
//			sequencer.open();
//			sequencer.setSequence(midiFile);
//			sequencer.setLoopCount(loopCount);
//			
//			for(Track track : sequencer.getSequence().getTracks()){	
//				track.add(new MidiEvent(
//					    new ShortMessage(ShortMessage.CONTROL_CHANGE, 7, 30),
//					    0));
//				track.add(new MidiEvent(
//					    new ShortMessage(ShortMessage.CONTROL_CHANGE, 39, 0),
//					    0));
//			}
//			
//			sequencer.start();
//			
//		} catch (Exception ex){
//			// gulp
//		}
	}
	
	
	
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
}
