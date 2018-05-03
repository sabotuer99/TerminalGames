package whorten.termgames.glyphs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import whorten.termgames.glyphs.GlyphString.Appender;
import whorten.termgames.render.GlyphStringCoord;

public class GlyphLoader {
	
	public Set<GlyphStringCoord> parse(InputStream bais) {
		Set<GlyphStringCoord> glyphs = new HashSet<GlyphStringCoord>();
		Scanner in = new Scanner(bais);
		Glyph.Builder b = new Glyph.Builder(" ");
			
		Map<String, Glyph> map = getMap(in);
			
		for(int row = 0; in.hasNext(); row++){
			String line = in.nextLine();
			Appender current = new GlyphString.Appender();
			for(int i = 0; i < line.length(); i++){
				String c = Character.toString(line.charAt(i));
				if(" ".equals(c)){
					if(current.length() > 0){
						GlyphStringCoord gsc = new GlyphStringCoord(row, i - current.length(), current.build());
						glyphs.add(gsc);
						current = new GlyphString.Appender();
					}				
					continue;
				} else {
					Glyph mapped = map.get(c);
					if(mapped == null){
						current.append(b.withBase(c).build());
					} else {
						current.append(mapped);
					}					
				}
			}
			if(current.length() > 0){
				GlyphStringCoord gsc = new GlyphStringCoord(row, 
						                                    line.length() - current.length(), 
						                                    current.build());
				glyphs.add(gsc);
			}
		}
		in.close();
		return glyphs;
	}

	private Map<String, Glyph> getMap(Scanner in) {
		
		try{	
			Map<String, Glyph> map = new HashMap<>();
			String sentinel = in.nextLine();
			for(String line = in.nextLine(); !line.equals(sentinel) && in.hasNext(); line = in.nextLine()){
				map.put(line.substring(0, 1), specToGlyph(line.substring(1)));
			}		
			return map;
		} catch (Exception ex){
			return new HashMap<>();
		}
		
	}

	public Glyph specToGlyph(String spec) {
		return new Glyph.Builder("O").build();
	}

}
