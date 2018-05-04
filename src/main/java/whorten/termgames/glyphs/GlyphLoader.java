package whorten.termgames.glyphs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import whorten.termgames.glyphs.GlyphString.Appender;
import whorten.termgames.glyphs.interpreters.SpecInterpreter;
import whorten.termgames.render.GlyphStringCoord;

public class GlyphLoader {
	
	private SpecInterpreter si = new SpecInterpreter();

	public Set<GlyphStringCoord> parse(InputStream bais) {
		Set<GlyphStringCoord> glyphs = new HashSet<GlyphStringCoord>();
		Scanner in = new Scanner(bais);
		Map<String, Glyph> map = new HashMap<>();
		processMetadata(in, map, glyphs);	
		
		for(int row = 0; in.hasNext(); row++){
			String line = in.nextLine();
			Appender current = new GlyphString.Appender();
			for(int i = 0; i < line.length(); i++){
				String c = charAt(line, i);
				if(" ".equals(c)){
					if(current.length() > 0){
						GlyphStringCoord gsc = new GlyphStringCoord(row, i - current.length(), current.build());
						glyphs.add(gsc);
						current = new GlyphString.Appender();
					}				
					continue;
				} else {
					appendGlyph(map, current, c);					
				}
			}
			if(current.length() > 0){
				int col = line.length() - current.length();
				glyphs.add(finishGlyphString(row, col, current));
			}
		}
		in.close();
		return glyphs;
	}

	private GlyphStringCoord finishGlyphString(int row, int col, Appender current) {
		return new GlyphStringCoord(row, col, current.build());
	}

	private String charAt(String line, int i) {
		return Character.toString(line.charAt(i));
	}

	private void appendGlyph(Map<String, Glyph> map, Appender current, String c) {
		Glyph mapped = map.get(c);
		Glyph defaultGlyph = map.get("DEFAULT");
		Glyph.Builder b = new Glyph.Builder(" ");
		if(mapped == null){
			if(defaultGlyph != null){
				current.append(new Glyph.Builder(defaultGlyph).withBase(c).build());
			} else {
				current.append(b.withBase(c).build());							
			}
		} else {
			current.append(mapped);
		}
	}

	private void processMetadata(Scanner in, Map<String, Glyph> map, Set<GlyphStringCoord> glyphs) {
		try{	
			String sentinel = in.nextLine();
			for(String line = in.nextLine(); !line.equals(sentinel) && in.hasNext(); line = in.nextLine()){
				int firstSpace = line.indexOf(" ");
				String key = line.substring(0, firstSpace);
				map.put(key, specToGlyph(line));				
			}		
		} catch (Exception ex){
		}
	}

	private Glyph specToGlyph(String spec) {
		return si.parse(spec);
	}

	public void setSpecInterpreter(SpecInterpreter si){
		this.si  = si;
	}
}
