package whorten.termgames.glyphs.interpreters;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.BgColor;
import whorten.termgames.glyphs.FgColor;
import whorten.termgames.glyphs.Glyph;
import whorten.termgames.glyphs.Glyph.Builder;

public class SpecInterpreter {
	private final static Logger logger = LogManager.getLogger(SpecInterpreter.class);
	private Map<String, Transform> lambdas = new HashMap<>();

	public SpecInterpreter(){
		populateLambdas();
	}
	
	public Glyph parse(String spec) {
		String[] settings = spec.split(" ");
		String base = " ";
		if (settings.length > 0 && settings[0].length() == 1) {
			base = settings[0];
		}
		Glyph.Builder builder = new Glyph.Builder(base);

		for (int i = 1; i < settings.length; i++) {
			String setting = settings[i];
			String command = "";
			String params = "";			
			int breakPoint = setting.indexOf(":");
			if(breakPoint == -1){
				command = setting;
			} else {
				command = setting.substring(0, breakPoint);
				params = setting.substring(breakPoint+1);
			}
			Transform t = lambdas.get(command);
			if (t != null) {
				t.accept(params, builder);
			}
		}

		return builder.build();
	}
	
	public Map<String,String> getAllSpecs(String rawSpec){
		Map<String,String> map = new HashMap<>();
		if(rawSpec == null){ return map;}
		
		// remove subspec groups
		rawSpec.replaceAll(getGlyphSpec(rawSpec), "");
		rawSpec.replaceAll(getCoordsSpec(rawSpec), "");
		
		// get all the key/value pairs
		String[] specs = rawSpec.split(" ");
		for(String spec : specs){
			String[] parts = spec.split(":");
			String key = parts[0];
			String value = "";
			if(parts.length > 1){
				value = parts[1];
			}
			map.put(key, value);
		}
		return map;
	}

	public String getGlyphSpec(String spec) {
		String gpat = "GLYPH:\\[.*?[^\\\\]\\]";
		if(Pattern.matches(gpat, spec)){
			Pattern p = Pattern.compile(gpat);
			Matcher m = p.matcher(spec);
			m.find();
			return m.group();
		}
		return "";
	}
	
	public Glyph parseGlyphSpec(String spec, Glyph defaultGlyph) {
		String gspec = getGlyphSpec(spec);
		Glyph base = defaultGlyph;
		if(!"".equals(gspec)){
			String glyphSpec = gspec.substring(7, gspec.length() - 1);
			base = parse(glyphSpec);
		}
		return base;
	}
	
	public String getCoordsSpec(String spec) {
		String gpat = "COORDS:\\[.*?\\]";
		if(Pattern.matches(gpat, spec)){
			Pattern p = Pattern.compile(gpat);
			Matcher m = p.matcher(spec);
			m.find();
			return m.group();
		}
		return "";
	}

	/**
	 * Expects a string of coordinates in the form
	 * FOO:x,y BAR:x,y 
	 * 
	 * @param rawCoords
	 * @return
	 */
	public Map<String, Coord> parseCoords(String spec){
		Map<String, Coord> coords = new HashMap<>();
		String rawCoordSpec = getCoordsSpec(spec);
		if("".equals(rawCoordSpec)){
			return coords;
		}
		String rawCoords = rawCoordSpec.substring(8, rawCoordSpec.length() - 1); 
		
		String[] params = rawCoords.split(" ");
		for(String param : params){
			String[] p = param.split(":");
			String key = p[0];
			String[] xy = p[1].split(",");
			int x = Integer.valueOf(xy[0]);
			int y = Integer.valueOf(xy[1]);
			coords.put(key, new Coord(x,y));
		}
		return coords;
	}

	private void populateLambdas() {
		lambdas.put("BASE", this::setBase);
		lambdas.put("FG", this::setFg);
		lambdas.put("BG", this::setBg);
		lambdas.put("FGA", this::setFgAfter);
		lambdas.put("BGA", this::setBgAfter);
		lambdas.put("BOLD", this::setBold);
		lambdas.put("UNDERLINE", this::setUnderline);
	}

	private void setUnderline(String setting, Builder builder) {
		builder.isUnderline(true);
	}

	private void setBold(String setting, Builder builder) {
		builder.isBold(true);
	}

	private void setBgAfter(String setting, Builder builder) {
		if(setting == null || setting.length() == 0) { return; }
		int[] rgb = getRbg(setting);
		if(rgb.length == 3){
			builder.withBgAfter(rgb[0], rgb[1], rgb[2]);
		}
	}

	private void setFgAfter(String setting, Builder builder) {
		if(setting == null || setting.length() == 0) { return; }
		int[] rgb = getRbg(setting);
		if(rgb.length == 3){
			builder.withFgAfter(rgb[0], rgb[1], rgb[2]);
		}
	}

	private void setFg(String setting, Builder builder) {
		if(setting == null || setting.length() == 0) { return; }
		int[] rgb = getRbg(setting);
		if(rgb.length == 3){
			builder.withForegroundColor(rgb[0], rgb[1], rgb[2]);
		} else {
			try{
				builder.withForegroundColor(FgColor.valueOf(setting));
			}
			catch(IllegalArgumentException ex){
				logger.warn("Tried to set invalid foreground color: %s", setting);
			}
		}
	}
	
	private void setBg(String setting, Builder builder) {
		if(setting == null || setting.length() == 0) { return; }
		int[] rgb = getRbg(setting);
		if(rgb.length == 3){
			builder.withBackgroundColor(rgb[0], rgb[1], rgb[2]);
		} else {
			try{
				builder.withBackgroundColor(BgColor.valueOf(setting));
			}
			catch(IllegalArgumentException ex){
				logger.warn("Tried to set invalid background color: %s", setting);
			}
		}
	}
	
	private int[] getRbg(String setting){
		String[] rgb = setting.split(",");
		int[] parsed = new int[0];
		if(rgb.length == 3){
			int r = Integer.valueOf(rgb[0]);
			int g = Integer.valueOf(rgb[1]);
			int b = Integer.valueOf(rgb[2]);
			parsed = new int[]{r, g, b};
		}
		return parsed;
	}

	private void setBase(String setting, Builder builder) {
		if (setting.length() == 1) {
			builder.withBase(setting);
		}
	}
	
	private interface Transform extends BiConsumer<String, Glyph.Builder> {}
}
