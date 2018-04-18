package whorten.termgames.render;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import whorten.termgames.glyphs.Glyph;

public class ScreenBuffer {

	private int maxrow;
	private int maxcol;
	private Map<Integer, Queue<Glyph>> buffer;
	private Glyph nullGlyph;
	
	public ScreenBuffer(int row, int col){
		this(row, col, new Glyph.Builder(" ").build());
	}
	
	public ScreenBuffer(int row, int col, Glyph nullGlyph){
		buffer = new HashMap<>();
		this.maxrow = row;
		this.maxcol = col;
		this.nullGlyph = nullGlyph;
	}
	
	public void bufferAt(int row, int col, Glyph glyph){
		checkBounds(row, col);		
		Integer index = getIndex(row, col);		
		Queue<Glyph> q = getQueue(index);
		
		q.offer(glyph);
		buffer.put(index, q);
	}
	
	public Glyph revertAt(int row, int col){
		checkBounds(row, col);		
		Integer index = getIndex(row, col);		
		Queue<Glyph> q = getQueue(index);
		Glyph glyph = nullGlyph;
		
		if(!q.isEmpty()){
			glyph = q.poll();
		} 
		buffer.put(index, q);
		return glyph;
	}
	
	public void clearAt(int row, int col){
		checkBounds(row, col);		
		Integer index = getIndex(row, col);	
		buffer.put(index, new LinkedList<>());
	}
	
	public void clearAll(){
		buffer = new HashMap<>();
	}

	private Queue<Glyph> getQueue(Integer index) {
		Queue<Glyph> q = buffer.get(index);
		if(q == null){
			q = new LinkedList<>();
		}
		return q;
	}

	private void checkBounds(int row, int col) {
		if(row > maxrow || col > maxcol || row < 1 || col < 1){
			throw new IllegalArgumentException(
					String.format("location out of bounds! row=%d, col=%d", row, col));
		}
	}

	private Integer getIndex(int row, int col) {
		int index = row + col << 16;
		return Integer.valueOf(index);
	}

}
