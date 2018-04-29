package whorten.termgames.games.quadtris.board;

import java.util.ArrayList;
import java.util.List;

import whorten.termgames.events.EventBus;
import whorten.termgames.games.quadtris.events.DropPieceEvent;
import whorten.termgames.games.quadtris.events.RedrawPieceEvent;
import whorten.termgames.games.quadtris.events.RedrawPieceEvent.PieceTransformType;
import whorten.termgames.games.quadtris.events.SpawnPieceEvent;
import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.games.quadtris.piece.PieceFactory;
import whorten.termgames.games.quadtris.well.Well;
import whorten.termgames.utils.Coord;
import whorten.termgames.utils.Counter;

public class QuadtrisBoard {

	private QuadtrisBoard(){};
	private Well well;
	private Piece currentPiece;
	private Piece nextPiece;
	private Coord baseOrigin;
	private EventBus eventBus;
	private Counter<String> stats = new Counter<>();
	private volatile boolean isAlive;
	
	public void tick(){
		
		if(well.isLegal(currentPiece)){
			Piece down = currentPiece.moveDown(1);
			if(well.isLegal(down)){
				eventBus.fire(new RedrawPieceEvent(currentPiece, down, 
						PieceTransformType.TICK));
				currentPiece = down;
			} else {
				dropPiece();
			}
		} else {
			//piece overlaps, game over
			isAlive = false;
		}
	}
	
	public void movePieceLeft(){
		Piece candidate = currentPiece.moveLeft(1);
		if(well.isLegal(candidate)){
			eventBus.fire(new RedrawPieceEvent(currentPiece, candidate, 
					PieceTransformType.TRANSLATION));
			currentPiece = candidate;
			return;
		}
	}
	
	public void movePieceRight(){
		Piece candidate = currentPiece.moveRight(1);
		if(well.isLegal(candidate)){
			eventBus.fire(new RedrawPieceEvent(currentPiece, candidate, 
					PieceTransformType.TRANSLATION));
			currentPiece = candidate;
			return;
		}
	}
	
	public void dropPiece(){
		if(!well.isLegal(currentPiece)){ //player initiated drop just after an illegal spawn (endgame)
			return;
		}
		eventBus.fire(new RedrawPieceEvent(currentPiece, 
				well.applyGravity(currentPiece), 
				PieceTransformType.DROP));
	    well.addPiece(currentPiece);
	    eventBus.fire(new DropPieceEvent());
	    spawnNextPiece();
	}
	
	public void rotatePieceClockwise(){
		for(Piece candidate : fuzzPieceCol(currentPiece.rotateClockwise())){
			if(well.isLegal(candidate)){
				eventBus.fire(new RedrawPieceEvent(currentPiece, candidate, 
						PieceTransformType.ROTATION));
				currentPiece = candidate;
				return;
			}
		}
	}
	
	public void rotatePieceCounterClockwise(){
		for(Piece candidate : fuzzPieceCol(currentPiece.rotateCounterClockwise())){
			if(well.isLegal(candidate)){
				eventBus.fire(new RedrawPieceEvent(currentPiece, candidate, PieceTransformType.ROTATION));
				currentPiece = candidate;
				return;
			}
		}
	}
	
	public boolean isAlive(){
		return isAlive;
	}
	
	public Piece getCurrentPiece(){
		return currentPiece;
	}
	
	public Piece getNextPiece(){
		return nextPiece;
	}
	
	public Well getWell() {
		return well;
	}
	
	public Integer getCount(String name){
		return stats.getCount(name);
	}
	
	private List<Piece> fuzzPieceCol(Piece base){
		List<Piece> pieces = new ArrayList<>();
		for(int i = 0; i <= 2; i++){
			pieces.add(base.moveLeft(i));
			if(i > 0){
				pieces.add(base.moveRight(i));				
			}
		}
		return pieces;
	}
	
	private void spawnNextPiece() {
		currentPiece = nextPiece;
		stats.increment(currentPiece.getName());
		nextPiece = PieceFactory.getRandomPiece(baseOrigin);
		eventBus.fire(new SpawnPieceEvent());
	}
	
	public static class Builder{	
			
		private Coord baseOrigin = new Coord(5,-1);
		private Piece currentPiece = PieceFactory.getRandomPiece(baseOrigin);
		private Piece nextPiece = PieceFactory.getRandomPiece(baseOrigin);
		private Well well;
		private EventBus eventBus;
		
		public Builder(EventBus eventBus){
			this.eventBus = eventBus;
			this.well = new Well(eventBus);
		}
		
		private boolean isAlive = true;
		
		public QuadtrisBoard build(){
			QuadtrisBoard qb = new QuadtrisBoard();
			qb.well = this.well;
			qb.eventBus = this.eventBus;
			qb.currentPiece = this.currentPiece;
			qb.nextPiece = this.nextPiece;
			qb.baseOrigin = this.baseOrigin;
			qb.isAlive = this.isAlive;
			qb.stats.increment(currentPiece.getName());
			return qb;
		}
		
		public Builder withWell(Well well){
			this.well = well;
			return this;
		}
		
		public Builder withCurrentPiece(Piece currentPiece){
			this.currentPiece = currentPiece;
			return this;
		}
		
		public Builder withNextPiece(Piece nextPiece){
			this.nextPiece = nextPiece;
			return this;
		}
		
	}

	
	
}
