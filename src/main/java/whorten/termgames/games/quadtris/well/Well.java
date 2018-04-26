package whorten.termgames.games.quadtris.well;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import whorten.termgames.events.EventBus;
import whorten.termgames.games.quadtris.cell.Cell;
import whorten.termgames.games.quadtris.events.FullRowsEvent;
import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.utils.Coord;

public class Well {

	//can generalize this later if I ever want a nonstandard field
	//2d array is for spacial relationship
	//Coord and Cell serts are for membership checks and easy rendering
	private Cell[][] grid = new Cell[20][10];
	private Set<Coord> occupiedLocations = new HashSet<>();
	private Set<Cell> cells = new HashSet<>();
	private EventBus eventBus;
	
	public Well(EventBus eventBus){
		this.eventBus = eventBus;
	}
	
	
	public Set<Cell> getCells(){
		return new HashSet<>(cells);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Cell[] row : grid){
			sb.append("|");
			for(Cell cell : row){
				sb.append(cell == null ? " " : "X");
			}
			sb.append("|\n");
		}
		sb.append("+----------+");
		return sb.toString();
	}


	public boolean isOccupied(Coord coord){
		return occupiedLocations.contains(coord);
	}

	public void addPiece(Piece piece) {
		if(isLegal(piece)){
			Piece finalPiece = applyGravity(piece);
			List<Integer> fullRows = new ArrayList<>();		
			List<Cell> pcells = Cell.fromPiece(finalPiece);
			for(Cell cell : pcells){
				Coord loc = cell.getCoord();
				grid[loc.getRow()][loc.getCol()] = cell;
				occupiedLocations.add(loc);
				cells.add(cell);
				if(isFullRow(loc.getRow())){
					fullRows.add(loc.getRow());
				}
			}
			removeRows(fullRows);
		}	
	}

	private void removeRows(List<Integer> rows) {
		if(rows == null || rows.size() == 0){
			return;
		}
		
		Collections.sort(rows);
		for(Integer index : rows){
			Cell[] row = grid[index];
			for(int i = index; i >= 0; i--){
				for(int j = 0; j < row.length; j++){
					
					if(i == index){
						cells.remove(grid[i][j]);
						occupiedLocations.remove(grid[i][j].getCoord());
					}

					if(i > 0){
						grid[i][j] = grid[i-1][j];
					} else { // i == 0
						grid[i][j] = null;
					}
				}

			}
		}
		//fire the event, renderer will pick it up and animate
		eventBus.fire(new FullRowsEvent(new ArrayList<>(rows)));
	}

	private boolean isFullRow(int index) {
		Cell[] row = grid[index];
		for(Cell cell : row){
			if(cell == null){
				return false;
			}
		}
		return true;
	}

	public boolean isLegal(Piece piece){
		for(Coord coord : piece.getCoords()){
			// check if empty space
			if(occupiedLocations.contains(coord)){
				return false;
			}
			// check bounds
			if(coord.getCol() < 0 || coord.getCol() >= 10 ||
			   coord.getRow() < 0 || coord.getRow() >= 20){
				return false;
			}
		}
		return true;
	}
	
	public boolean isOccupied(Piece piece){
		for(Coord coord : piece.getCoords()){
			// check if empty space
			if(occupiedLocations.contains(coord)){
				return true;
			}
		}
		return false;
	}

	private Piece applyGravity(Piece piece) {
		Piece finalPiece = piece;
		while(isLegal(finalPiece.moveDown(1))){
			finalPiece = finalPiece.moveDown(1);
		}
		return finalPiece;
	}


	public boolean isInUprights(Piece piece) {
		for(Coord coord : piece.getCoords()){
			// check if in vertical bounds
			if(coord.getCol() < 0 || coord.getCol() >= 10){
				return false;
			}
		}
		return true;
	}
	
	public boolean isTouchingBottom(Piece piece) {
		for(Coord coord : piece.getCoords()){
			// check if in vertical bounds
			if(coord.getRow() >= 19){
				return true;
			}
		}
		return false;
	}
	
}
