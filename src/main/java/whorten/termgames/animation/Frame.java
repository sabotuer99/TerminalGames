package whorten.termgames.animation;

import whorten.termgames.render.Renderer;

public interface Frame {

	void drawFrame(Renderer renderer);
	long getFrameLength();
	Frame getMaskingFrame();
}
