package whorten.termgames.glyphs.interpreters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

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
		Glyph base = si.parseGlyphSpec(spec, defaultGlyph);
		String gspec = si.getGlyphSpec(spec);
		String rest = spec.replaceAll(gspec, "");

		String command = "";
		String params = "";	
		int breakPoint = rest.indexOf(" ");
		if(breakPoint == -1){
			command = rest;
		} else {
			command = rest.substring(0, breakPoint);
			params = rest.substring(breakPoint+1);
		}
		
		Transform t = lambdas.get(command);
		if (t != null) {
			glyphs.addAll(t.apply(params, base));
		}
		
		return glyphs;
	}
	
	private void populateLambdas() {
		lambdas.put("LINE", this::drawLine);
		lambdas.put("RECT", this::drawRect);
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
