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
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
import whorten.termgames.glyphs.gradient.GradientSpec;
import whorten.termgames.glyphs.interpreters.SpecInterpreter;

public class GradientProcessor implements Processor {

	SpecInterpreter si = new SpecInterpreter();
	Map<Range,Map<String,GradientSpec>> ranges = new HashMap<>();
	Set<GlyphStringCoord> gscs = new HashSet<>();
	
	@Override
	public void withInstruction(String instruction) {
		Range range = parseRange(instruction);
		Map<String,GradientSpec> gs = parseGradients(instruction, range);
		ranges.put(range, gs);
	}

	private Map<String,GradientSpec> parseGradients(String instruction, Range range) {
		Map<String,GradientSpec> map = new HashMap<>();
		Map<String,String> specs = si.getAllSpecs(instruction);
		
		GradientSpec fg = parseGradientSpec("FG", specs, range);
		GradientSpec bg = parseGradientSpec("BG", specs, range);

		if(fg != null){
			map.put("FG", fg);
		}
		if(bg != null){
			map.put("BG", bg);
		}
		
		return map;
	}
	
	private GradientSpec parseGradientSpec(String prefix, Map<String,String> specs, Range range){
		if(specs.containsKey(prefix + "END") && specs.containsKey(prefix + "START")){
			int[] startRGB = parseRGB(specs.get(prefix + "START"));
			int[] endRGB = parseRGB(specs.get(prefix + "END"));
			if(startRGB != null && endRGB != null){
				return new GradientSpec.Builder()
						.withStartRGB(startRGB[0], startRGB[1], startRGB[2])
						.withEndRGB(endRGB[0], endRGB[1], endRGB[2])
						.withCols(range.maxCol - range.minCol + 1)
						.withRows(range.maxRow - range.minRow + 1)
						.setHorizontal(specs.containsKey(prefix + "HORZ"))
						.build();			
			}
		}
		return null;
	}

	private int[] parseRGB(String string) {
		String[] rawVals = string.split(",");
		if(rawVals.length == 3){
			try{
				int[] rgb = new int[3];
				rgb[0] = Integer.parseInt(rawVals[0]);
				rgb[1] = Integer.parseInt(rawVals[1]);
				rgb[2] = Integer.parseInt(rawVals[2]);
				return rgb;
			} catch (Exception ex){
				//gulp
			}
		}
		return null;
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
		Entry<Range,Map<String,GradientSpec>> g = getFirstRange(coord);
		if(g != null && g.getValue() != null){
			Map<String,GradientSpec> gradients = g.getValue();
			GlyphString.Builder gsb = new GlyphString.Builder(key);
			int row = coord.getRow() - g.getKey().minRow;
			int col = coord.getCol() - g.getKey().minCol;
			if(gradients.containsKey("BG")){
				int[] rbg = gradients.get("BG").rgbFor(row, col);
				gsb.withBgColor(rbg[0], rbg[1], rbg[2]);
			}
			if(gradients.containsKey("FG")){
				int[] rbg = gradients.get("FG").rgbFor(row, col);
				gsb.withFgColor(rbg[0], rbg[1], rbg[2]);
			}
			gscs.add(new GlyphStringCoord(coord, gsb.build()));
		}
	}

	private Entry<Range,Map<String,GradientSpec>> getFirstRange(Coord coord) {
		try{
			Entry<Range,Map<String,GradientSpec>> g = ranges.entrySet()
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
