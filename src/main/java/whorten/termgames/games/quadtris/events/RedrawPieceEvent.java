package whorten.termgames.games.quadtris.events;

import whorten.termgames.events.Event;
import whorten.termgames.games.quadtris.piece.Piece;

public class RedrawPieceEvent implements Event {

	public enum PieceTransformType {
		ROTATION,
		TRANSLATION,
		DROP,
		TICK
	}

	private Piece oldPiece;
	private Piece newPiece;
	private PieceTransformType transform;

	public RedrawPieceEvent(Piece oldPiece, Piece newPiece, PieceTransformType transform) {
		this.oldPiece = oldPiece;
		this.newPiece = newPiece;
		this.transform = transform;
	}
	
	public Piece getOldPiece(){
		return oldPiece;
	}
	
	public Piece getNewPiece(){
		return newPiece;
	}
	
	public PieceTransformType getTransform(){
		return transform;
	}

}
