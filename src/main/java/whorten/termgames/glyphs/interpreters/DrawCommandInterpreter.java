package whorten.termgames.glyphs.interpreters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import whorten.termgames.geometry.Coord;
import whorten.termgames.geometry.CoordLine;
import whorten.termgames.geometry.CoordRect;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.collate.GlyphStringCollater;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class DrawCommandInterpreter {

	private Glyph defaultGlyph = new Glyph.Builder(" ").build();
	private SpecInterpreter si = new SpecInterpreter();
	private Map<String, Transform> lambdas = new HashMap<>();
	private GlyphStringCollater gsc = new GlyphStringCollater();

	public DrawCommandInterpreter(){
		populateLambdas();
	}


	public Set<GlyphStringCoord> parse(String spec){
		Set<GlyphStringCoord> glyphs = new HashSet<>();
		
		//CHECK FOR GLYPH SPEC FIRST, EXTRACT IF PRESENT
		String gspec = getGlyphSpec(spec);
		String otherspec = spec.replaceAll(gspec, "");
		Glyph base = defaultGlyph;
		if(!"".equals(gspec)){
			String glyphSpec = gspec.substring(7, gspec.length() - 1);
			base = si.parse(glyphSpec);
		}

		String command = "";
		String params = "";	
		int breakPoint = otherspec.indexOf(" ");
		if(breakPoint == -1){
			command = otherspec;
		} else {
			command = otherspec.substring(0, breakPoint);
			params = otherspec.substring(breakPoint+1);
		}
		
		Transform t = lambdas.get(command);
		if (t != null) {
			glyphs.addAll(t.apply(params, base));
		}
		
		return glyphs;
	}

	private String getGlyphSpec(String spec) {
		String gpat = "GLYPH:\\[.*?[^\\\\]\\]";
		if(Pattern.matches(gpat, spec)){
			Pattern p = Pattern.compile(gpat);
			Matcher m = p.matcher(spec);
			return m.group();
		}
		return "";
	}
	
	private void populateLambdas() {
		lambdas.put("LINE", (String params, Glyph base) -> {return drawLine(params, base);});
		lambdas.put("RECT", (String params, Glyph base) -> {return drawRect(params, base);});
	}
	
	private Set<GlyphStringCoord> drawRect(String paramsRaw, Glyph base) {
		
		Map<String, Coord> endpoints = si.parseCoords(paramsRaw);
		Coord origin = endpoints.get("ORIGIN");
		Coord end = endpoints.get("END");
		CoordRect box = new CoordRect(origin, end);		

		//return gsc.collate(base, Sets.union(set1, set2));
		return gsc.collate(base, box);
	}


	private Set<GlyphStringCoord> drawLine(String paramsRaw, Glyph base) {

		Map<String, Coord> endpoints = si.parseCoords(paramsRaw);
		Coord origin = endpoints.get("ORIGIN");
		Coord end = endpoints.get("END");
		// create a line of coords
		Set<Coord> coords = new CoordLine(origin,end);
		return gsc.collate(base, coords);
	}

	public void setDefaultGlyphStringCollater(GlyphStringCollater gsc){
		this.gsc = gsc;
	}

	public void setDefaultGlyph(Glyph defaultGlyph) {
		this.defaultGlyph  = defaultGlyph;
	}
	
	private interface Transform extends BiFunction<String, Glyph, Set<GlyphStringCoord>>{}
}
