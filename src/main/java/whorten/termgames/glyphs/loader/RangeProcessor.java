package whorten.termgames.glyphs.loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.Range;
import whorten.termgames.geometry.Range.Builder;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
import whorten.termgames.glyphs.interpreters.SpecInterpreter;

public class RangeProcessor implements Processor{

	SpecInterpreter si = new SpecInterpreter();
	Map<Range,Glyph> ranges = new HashMap<>();
	Set<GlyphStringCoord> gscs = new HashSet<>();
	
	@Override
	public void withInstruction(String instruction) {
		Glyph glyph = si.parseGlyphSpec(instruction, null);
		if(glyph == null){
			return;
		}
		Range range = parseRange(instruction);
		ranges.put(range, glyph);
	}

	private Range parseRange(String instruction) {
		Builder rb = new Range.Builder();
		Map<String,Coord> coords = si.parseCoords(instruction);
		if(coords.size() == 0){		
			Map<String,String> specs = si.getAllSpecs(instruction);
			Map<String,Consumer<String>> lambdas = getLambdas(rb);
			for(String key : specs.keySet()){
				String value = specs.get(key);
				if(lambdas.containsKey(key)){
					lambdas.get(key).accept(value);
				}
			}
		} else {
			Coord topLeft = coords.get("TOPLEFT");
			Coord bottomRight = coords.get("BOTTOMRIGHT");
			rb.betweenCoords(topLeft, bottomRight);
		}
			
		return rb.build();
	}

	@Override
	public void apply(Coord coord, String key) {
		if(coord == null || key == null){
			return;
		}
		Entry<Range, Glyph> g = getFirstRange(coord);
		if(g != null && g.getValue() != null){
			String base = " ".equals(g.getValue().getBase()) ? key : g.getValue().getBase();
			GlyphString gs = new GlyphString.Builder(g.getValue()).withBaseString(base).build();
			gscs.add(new GlyphStringCoord(coord, gs));
		}
	}

	private Entry<Range, Glyph> getFirstRange(Coord coord) {
		try{
			Entry<Range,Glyph> g = ranges.entrySet()
			      .stream()
			      .filter(entry -> entry.getKey().inRange(coord))
			      .findFirst()
			      .get();
			return g;
		} catch (NoSuchElementException e){
			return null;
		}
	}

	@Override
	public int applicability(Coord coord, String key) {
		return getFirstRange(coord) == null ? 0 : 9;
	}

	@Override
	public Set<GlyphStringCoord> process() {
		return new HashSet<>(gscs);
	}
	

	private Map<String, Consumer<String>> getLambdas(Builder rb) {
		Map<String, Consumer<String>> map = new HashMap<>();
		map.put("MAXROW", v -> {
			int maxRow = Integer.parseInt(v);
			rb.withMaxRow(maxRow);
		});
		map.put("MAXCOL", v -> {
			int maxCol = Integer.parseInt(v);
			rb.withMaxCol(maxCol);
		});
		map.put("MINROW", v -> {
			int minRow = Integer.parseInt(v);
			rb.withMinRow(minRow);
		});
		map.put("MINCOL", v -> {
			int minCol = Integer.parseInt(v);
			rb.withMinCol(minCol);
		});	
		return map;
	}
	
}
