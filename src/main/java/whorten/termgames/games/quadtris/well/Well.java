package whorten.termgames.games.quadtris.well;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.events.EventBus;
import whorten.termgames.games.quadtris.cell.Cell;
import whorten.termgames.games.quadtris.events.FullRowsEvent;
import whorten.termgames.games.quadtris.piece.Piece;
import whorten.termgames.utils.Coord;

public class Well {

	//can generalize this later if I ever want a nonstandard field
	//2d array is for spatial relationship
	//Coord and Cell sets are for membership checks and easy rendering
	private Cell[][] grid = new Cell[24][10];
	//private Set<Coord> occupiedLocations = new HashSet<>();
	//private Set<Cell> cells = new HashSet<>();
	private EventBus eventBus;
	private final static Logger logger = LogManager.getLogger(Well.class);
	
	private int cellCount = 0;
	
	public Well(EventBus eventBus){
		this.eventBus = eventBus;
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[0].length; col++){
				grid[row][col] = Cell.EMPTY;
			}
		}
	}	
	
	public synchronized Set<Cell> getCells(){
		Set<Cell> set = new HashSet<>();
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[0].length; col++){
				if(grid[row][col] != Cell.EMPTY){
					set.add(grid[row][col]);
				}
			}
		}
		return set;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int index = 0; index < 20; index++){
			Cell[] row = gridRow(index);
			sb.append("|");
			for(Cell cell : row){
				sb.append(cell == Cell.EMPTY ? " " : "X");
			}
			sb.append("|");
			sb.append("\n");
		}
		sb.append("+----------+");
		return sb.toString();
	}

	public synchronized void addCell(Cell cell){
		checkCellInvariants(cell);
		Coord loc = cell.getCoord();
		setGrid(loc.getRow(), loc.getCol(), cell);
		//cells.add(cell);
		//occupiedLocations.add(loc);
		cellCount += 1;
	}



	public synchronized boolean isOccupied(Coord coord){
		if(coord.getCol() < 0 || coord.getCol() >= 10 || coord.getRow() >= 20){
			return true;
		}
		return grid(coord.getRow(), coord.getCol()) != Cell.EMPTY;
	}

	public synchronized void addPiece(Piece piece) {
		if(isLegal(piece)){
			logger.debug("Adding piece, current cell count: " + cellCount);
			//logger.debug("... current Set<> sizes: " + occupiedLocations.size() + " " + cells.size());
			Piece finalPiece = applyGravity(piece);
			List<Integer> fullRows = new ArrayList<>();		
			List<Cell> pcells = Cell.fromPiece(finalPiece);
			for(Cell cell : pcells){
				Coord loc = cell.getCoord();
				setGrid(loc.getRow(), loc.getCol(), cell);
				//occupiedLocations.add(loc);
				//cells.add(cell);
				if(isFullRow(loc.getRow())){
					fullRows.add(loc.getRow());
				}
			}
			cellCount += 4;
			removeRows(fullRows);
		}	
	}

	private void removeRows(List<Integer> rows) {
		if(rows == null || rows.size() == 0){
			return;
		}
		
		Collections.sort(rows);
		for(Integer index : rows){
			logger.debug("Removing row, current cell count: " + cellCount);
			//logger.debug("... current Set<> sizes: " + occupiedLocations.size() + " " + cells.size());
			cellCount -= 10;
			Cell[] row = gridRow(index);
			for(int i = index; i >= 0; i--){
				for(int j = 0; j < row.length; j++){					
					//forgetCell(grid(i,j));	
					if(i > 0){
						setGrid(i, j, grid(i-1, j).moveDown(1));
						//recordCell(grid(i,j));						
					} else { // i == 0
						setGrid(i, j, Cell.EMPTY);
					}
				}

			}
		}
		//fire the event, renderer will pick it up and animate
		eventBus.fire(new FullRowsEvent(new ArrayList<>(rows)));
	}

	private void setGrid(int row, int col, Cell cell){
		grid[row+4][col] = cell;
	}
	
	private Cell[] gridRow(int row){
		return grid[row+4];
	}
	
	private Cell grid(int row, int col){
		return grid[row+4][col];
	}
	
	/*
	private void recordCell(Cell cell) {
		if(cell != Cell.EMPTY){
			//cells.add(cell);
			//occupiedLocations.add(cell.getCoord());
		}
	}
	
	private void forgetCell(Cell cell) {
		if(cell != Cell.EMPTY){
		//	cells.remove(cell);
	//		occupiedLocations.remove(cell.getCoord());
		}
	}*/

	private boolean isFullRow(int index) {
		Cell[] row = gridRow(index);
		for(Cell cell : row){
			if(cell == Cell.EMPTY){
				return false;
			}
		}
		return true;
	}

	public synchronized boolean isLegal(Piece piece){
		for(Coord coord : piece.getCoords()){
			// check bounds
			if(coord.getCol() < 0 || coord.getCol() >= 10 ||
			   coord.getRow() < 0 || coord.getRow() >= 20){
				return false;
			}
			
			// check if empty space
			if(isOccupied(coord)){
				return false;
			}
		}
		return true;
	}
	
	public synchronized boolean isOccupied(Piece piece){
		for(Coord coord : piece.getCoords()){
			// check if empty space
			if(isOccupied(coord)){
				return true;
			}
		}
		return false;
	}

	public synchronized Piece applyGravity(Piece piece) {
		Piece finalPiece = piece;
		while(isLegal(finalPiece.moveDown(1))){
			finalPiece = finalPiece.moveDown(1);
		}
		return finalPiece;
	}


	public synchronized boolean isInUprights(Piece piece) {
		for(Coord coord : piece.getCoords()){
			// check if in vertical bounds
			if(coord.getCol() < 0 || coord.getCol() >= 10){
				return false;
			}
		}
		return true;
	}
	
	public synchronized boolean isTouchingBottom(Piece piece) {
		for(Coord coord : piece.getCoords()){
			// check if in vertical bounds
			if(coord.getRow() >= 19){
				return true;
			}
		}
		return false;
	}
	
	private void checkCellInvariants(Cell cell) {
		if(cell == null){
			throw new IllegalArgumentException("Cell cannot be null");
		}
		Coord loc = cell.getCoord();
		if(loc.getRow() < 0 || loc.getRow() >= 20){
			throw new IllegalArgumentException("Cell row is out of bounds");
		}
		if(loc.getCol() < 0 || loc.getCol() >= 10){
			throw new IllegalArgumentException("Cell col is out of bounds");
		}
	}

	
}
