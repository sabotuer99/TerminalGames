package whorten.termgames.games.quadtris.well;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import whorten.termgames.events.EventBus;
import whorten.termgames.games.quadtris.cell.Cell;
import whorten.termgames.games.quadtris.piece.PieceFactory;
import whorten.termgames.geometry.Coord;
import whorten.termgames.render.Renderer;
import whorten.termgames.testing.ArrayRenderer;


public class WellRendererTests {

	@Test
	public void renderBoard_rightLocation(){
		Renderer r = new ArrayRenderer(80, 26);
		WellRenderer sut = getSut(r); 
		
		sut.drawBlockWell();
		
		assertEquals(emptyBoard(), r.toString());
	}
	
	@Test
	public void renderBoard_drawPiece_RightLocation(){
		Renderer r = new ArrayRenderer(80, 26);
		WellRenderer sut = getSut(r); 
		
		sut.drawBlockWell();
		sut.drawPiece(PieceFactory.getO(new Coord(5,0)));
		
		assertEquals(pieceTest(), r.toString());
	}
	
	@Test
	public void renderBoard_drawWellInterior_ClearsOtherOutput(){
		Renderer r = new ArrayRenderer(80, 26);
		WellRenderer sut = getSut(r); 
		
		sut.drawBlockWell();
		sut.drawPiece(PieceFactory.getO(new Coord(5,0)));
		Well well = new Well(new EventBus());
		Cell.Builder builder = new Cell.Builder();
		well.addCell(builder.withLocation(new Coord(0,0)).build());
		well.addCell(builder.withLocation(new Coord(9,0)).build());
		well.addCell(builder.withLocation(new Coord(0,19)).build());
		well.addCell(builder.withLocation(new Coord(9,19)).build());
		
		sut.drawWellCells(well);
		
		assertEquals(cellTest(), r.toString());
	}

	private WellRenderer getSut(Renderer mockRenderer) {
		return new WellRenderer.Builder(mockRenderer)
				.withOriginOffset(new Coord(7,3)).build();
	}
	
	private String emptyBoard(){
		return  
		    "                                                                                \n"
		  + "                                                                                \n"
		  + "    XX                    XX   XXXXXXXXXXX                                      \n"
		  + "    XX                    XX   XXXXXXXXXXXX                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   XXXXXXXXXXXX                                     \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XXXXXXXXXXXXXXXXXXXXXXXX                                                    \n"
		  + "                                                                                \n"
		  + "                                                                                \n"
		  + "                                                                                \n";
	}
	
	private String pieceTest(){
		return  
		    "                                                                                \n"
		  + "                                                                                \n"
		  + "    XX          XXXX      XX   XXXXXXXXXXX                                      \n"
		  + "    XX          XXXX      XX   XXXXXXXXXXXX                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   XXXXXXXXXXXX                                     \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XXXXXXXXXXXXXXXXXXXXXXXX                                                    \n"
		  + "                                                                                \n"
		  + "                                                                                \n"
		  + "                                                                                \n";
	}
	
	private String cellTest(){
		return  
		    "                                                                                \n"
		  + "                                                                                \n"
		  + "    XXXX                XXXX   XXXXXXXXXXX                                      \n"
		  + "    XX                    XX   XXXXXXXXXXXX                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   X          X                                     \n"
		  + "    XX                    XX   XXXXXXXXXXXX                                     \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XX                    XX                                                    \n"
		  + "    XXXX                XXXX                                                    \n"
		  + "    XXXXXXXXXXXXXXXXXXXXXXXX                                                    \n"
		  + "                                                                                \n"
		  + "                                                                                \n"
		  + "                                                                                \n";
	}
}
