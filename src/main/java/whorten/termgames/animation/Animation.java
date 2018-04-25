package whorten.termgames.animation;

import java.util.ArrayList;
import java.util.List;

import whorten.termgames.render.Renderer;

public class Animation {

	private List<Frame> frames;
	private Renderer renderer;
	private int loopCount;
	private boolean isInfinite;
	private Frame lastFrame;
	private volatile boolean halt;
	
	private Animation(){};
	
	public void run(){
		run(0);
	}
	
	public void run(int startFrame){
		for(int count = 0; canContinue(count); count++){
			for(int i = 0; i < frames.size(); i++){
				if(halt) break;
				Frame frame = frames.get((i + startFrame) % frames.size());
				
				//I only want one animation drawing a frame at a time
				synchronized (renderer){
					frame.drawFrame(renderer);
				}				
				lastFrame = frame;
				pause(frame.getFrameLength());
			}
		}
	}
	
	public void stop(){
		halt = true;
		Frame mask = lastFrame.getMaskingFrame();
		mask.drawFrame(renderer);
	}

	private boolean canContinue(int count) {
		return (count < loopCount || isInfinite) && !halt;
	}
	
	private void pause(long frameLength) {
		try{
			Thread.sleep(frameLength);
		} catch (Exception ex){
			//gulp
		}
	}

	public static class Builder{
		
		private List<Frame> frames = new ArrayList<>();
		private Renderer renderer;
		private boolean isInfinite = false;
		private int loopCount = 1;

		public Builder withFrames(List<Frame> frames){
			this.frames = new ArrayList<>(frames);
			return this;
		}
		
		public Builder addFrame(Frame frame){
			frames.add(frame);
			return this;
		}
		
		public Builder isInfinite(boolean isInfinite){
			this.isInfinite = isInfinite;
			return this;
		}
		
		public Builder withLoopCount(int loopCount){
			this.loopCount = loopCount;
			return this;
		}
		
		public Builder withRenderer(Renderer renderer){
			this.renderer = renderer;
			return this;
		}
		
		public Animation build(){
			Animation a = new Animation();
			a.frames = new ArrayList<>(this.frames);
			a.renderer = this.renderer;
			a.loopCount = this.loopCount;
			a.isInfinite = this.isInfinite;
			return a;
		}
	}
}
