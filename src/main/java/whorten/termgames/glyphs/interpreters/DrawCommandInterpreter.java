package whorten.termgames.glyphs.interpreters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.render.GlyphStringCoord;

public class DrawCommandInterpreter {

	private Glyph defaultGlyph = new Glyph.Builder(" ").build();
	private SpecInterpreter si = new SpecInterpreter();
	private Map<String, Transform> lambdas = new HashMap<>();

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
	}
	
	private Set<GlyphStringCoord> drawLine(String paramsRaw, Glyph base) {
		Set<GlyphStringCoord> glyphs = new HashSet<>();
		String[] params = paramsRaw.split(" ");
		int origin_x = 0;
		int origin_y = 0;
		int end_x = 0;
		int end_y = 0;
		
		for(String param : params){
			String[] p = param.split(":");
			if("ORIGIN".equals(p[0])){
				String[] xy = p[1].split(",");
				origin_x = Integer.valueOf(xy[0]);
				origin_y = Integer.valueOf(xy[1]);
			} else if ("END".equals(p[0])){
				String[] xy = p[1].split(",");
				end_x = Integer.valueOf(xy[0]);
				end_y = Integer.valueOf(xy[1]);
			}
		}
		
		int run = end_x - origin_x;
		int rise = end_y - origin_y;
		//if run == 0, line is vertical
		double slope = Double.POSITIVE_INFINITY;
		if(run != 0){
			slope = (rise * 1.0) / run;
		}
		
	
		GlyphString.Appender b = new GlyphString.Appender();
		double x = origin_x;
		double y = origin_y;
		for(int i = 0; i < Math.max(run, rise); i++){
			double next_x = x;
			if(slope != Double.POSITIVE_INFINITY){
				next_x = x + i * (1.0 / run); 
			}
			double next_y = y;
			if(slope != 0){
				next_y = y + slope * i;
			}
			
			if(roundToSame(y, next_y)){
				b.append(base);	
			} else {
				//build current, reinit appender
				if(b.length() > 0){
					GlyphString gs = b.build();
					int gscx = (int)Math.round(x) - b.length();
					int gscy = (int)Math.round(y);
					GlyphStringCoord gsc = new GlyphStringCoord(gscy, gscx, gs);
					System.out.println(gsc);
					glyphs.add(gsc);
				}
				b = new GlyphString.Appender();
				b.append(base);	
			} 
				
			x = next_x;
			y = next_y;
		}
		//flush last bits
		if(b.length() > 0){
			GlyphString gs = b.build();
			glyphs.add(new GlyphStringCoord((int)y, (int)x - b.length(), gs));
		}
			
		return glyphs;
	}


	private boolean roundToSame(double a, double b) {
		return Math.round(a) == Math.round(b);
	}


	public void setDefaultGlyph(Glyph defaultGlyph) {
		this.defaultGlyph  = defaultGlyph;
	}
	
	private interface Transform extends BiFunction<String, Glyph, Set<GlyphStringCoord>>{}
}
