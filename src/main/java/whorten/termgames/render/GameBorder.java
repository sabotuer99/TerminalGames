package whorten.termgames.render;

import static whorten.termgames.utils.StringUtils.repeat;

import java.util.ArrayList;
import java.util.List;

import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.Glyph.Builder;
import whorten.termgames.utils.BoxDrawingGenerator;

public class GameBorder {

	private GameBorder(){};
	
	List<GlyphCoord> border = new ArrayList<>();
	
	public List<GlyphCoord> getGlyphCoords(){
		return new ArrayList<>(border);
	}
	
	public static class Builder{
		private BoxDrawingGenerator bdg = new BoxDrawingGenerator();
		private int rows;
		private int cols;
		private String[] blueprint;
		private Glyph.Builder baseGlyphBuilder = new Glyph.Builder(" ");
		
		public Builder(int rows, int cols){
			this.rows = rows;
			this.cols = cols;
			blueprint = new String[rows];
		}
		
		public GameBorder build(){
			String[] transformed = bdg.transform(blueprint);
			GameBorder gb = new GameBorder();
			gb.border = new ArrayList<>();
			
			for(int row = 0; row < rows; row ++){
				char[] rowChars = transformed[row].toCharArray();
				for(int col = 0; col < cols; col++){
					String baseChar = Character.toString(rowChars[col]);
					if(!" ".equals(baseChar)){
						Glyph glyph = baseGlyphBuilder.withBase(baseChar).build();
						GlyphCoord glyphCoord = new GlyphCoord(row+1, col+1, glyph);
						gb.border.add(glyphCoord);
					}
				}
			}
			
			return gb;
		}
		
		/*
		 * Creates a border around entire screen, with a 20 cell wide sidebar
		 */
		public Builder withDefaultLayout(){
			
			//first, last row
			String ends = repeat("#", cols);
			blueprint[0] = ends;
			blueprint[rows - 1] = ends;
			
			//middle rows
			StringBuilder sb = new StringBuilder();
			String middle = sb.append("#")
					          .append(repeat(" ", cols - 23))
					          .append("#")
					          .append(repeat(" ", 20))
					          .append("#")
					          .toString();
			
			for(int i = 1; i < rows - 1; i++){
				blueprint[i] = middle;
			}	
			
			return this;
		}
		
		public Builder withFgColor(FgColor color){
			baseGlyphBuilder.withForegroundColor(color);
			return this;
		}
		
		public Builder withBgColor(BgColor color){
			baseGlyphBuilder.withBackgroundColor(color);
			return this;
		}

		public Builder withBgColor(int r, int g, int b) {
			baseGlyphBuilder.withBackgroundColor(r, g, b);
			return this;
		}

		public Builder withNoSidebar() {
			//first, last row
			String ends = repeat("#", cols);
			blueprint[0] = ends;
			blueprint[rows - 1] = ends;
			
			//middle rows
			StringBuilder sb = new StringBuilder();
			String middle = sb.append("#")
					          .append(repeat(" ", cols - 2))
					          .append("#")
					          .toString();
			
			for(int i = 1; i < rows - 1; i++){
				blueprint[i] = middle;
			}	
			
			return this;
		}

		public Builder withFgColor(int r, int g, int b) {
			baseGlyphBuilder.withForegroundColor(r, g, b);
			return this;
		}
	}
}
