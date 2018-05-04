package whorten.termgames.glyphs.interpreters;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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


	private void populateLambdas() {
		lambdas.put("BASE", (String s, Glyph.Builder b) -> {setBase(s,b);});
		lambdas.put("FG", (String s, Glyph.Builder b) -> {setFg(s,b);});
		lambdas.put("BG", (String s, Glyph.Builder b) -> {setBg(s,b);});
		lambdas.put("FGA", (String s, Glyph.Builder b) -> {setFgAfter(s,b);});
		lambdas.put("BGA", (String s, Glyph.Builder b) -> {setBgAfter(s,b);});
		lambdas.put("BOLD", (String s, Glyph.Builder b) -> {setBold(s,b);});
		lambdas.put("UNDERLINE", (String s, Glyph.Builder b) -> {setUnderline(s,b);});
	}

	private void setUnderline(String setting, Builder builder) {
		builder.isUnderline(true);
	}

	private void setBold(String setting, Builder builder) {
		builder.isBold(true);
	}

	private void setBgAfter(String setting, Builder builder) {
		int[] rgb = getRbg(setting);
		if(rgb.length == 3){
			builder.withBgAfter(rgb[0], rgb[1], rgb[2]);
		}
	}

	private void setFgAfter(String setting, Builder builder) {
		int[] rgb = getRbg(setting);
		if(rgb.length == 3){
			builder.withFgAfter(rgb[0], rgb[1], rgb[2]);
		}
	}

	private void setFg(String setting, Builder builder) {
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
