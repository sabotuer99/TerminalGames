package whorten.termgames.glyphs.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.GlyphString;
import whorten.termgames.glyphs.collate.GlyphStringCollater;
import whorten.termgames.glyphs.collate.GlyphStringCoord;
import whorten.termgames.glyphs.interpreters.SpecInterpreter;

public class GlyphLoader {

	private Coord offset = null;
	
	public Set<GlyphStringCoord> parse(InputStream bais) {
		Set<GlyphStringCoord> glyphs = new HashSet<GlyphStringCoord>();
		BufferedReader in = new BufferedReader(new InputStreamReader(bais));
		offset = new Coord(0,0);

		Map<String, Processor> processors = new HashMap<>();
		processors.put("SPEC", new SpecProcessor());
		processors.put("BOX", new BoxDrawingProcessor());
		processors.put("RANGE", new RangeProcessor());

		Map<String, Processor> map = new HashMap<>();
		map.put(" ", NoOpProcessor.getInstance());

		Map<String, Consumer<String>> commands = new HashMap<>();
		commands.put("APPLY", (p) -> apply(p, processors, map, in));
		commands.put("SPEC", (p) -> process(processors, map, "SPEC", p));
		commands.put("BOX", (p) -> process(processors, map, "BOX", p));
		commands.put("RANGE", (p) -> process(processors, map, "RANGE", p));
		commands.put("OFFSET", (p) -> setOffset(p));
		commands.put("PROCESS_SPACE", (p) -> map.put(" ", map.get("SPEC")));
		commands.put("RANGE_SPACE", (p) -> map.put(" ", map.get("RANGE")));

		try {
				while (in.ready()) {
					String line = in.readLine();
					String cmd = getCommand(line);
					String params = getParams(line);
		
					if (commands.containsKey(cmd)) {
						commands.get(cmd).accept(params);
					}
				}
		
				for (Processor p : processors.values()) {
					glyphs.addAll(p.process());
				}
		} catch (IOException ex){
			//gulp
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				//gulp
			}
		}
		glyphs = GlyphString.offSetCollection(glyphs, offset);
		return new GlyphStringCollater().collate(glyphs);
	}

	private void setOffset(String p) {
		SpecInterpreter si = new SpecInterpreter();
		Map<String,Coord> points = si.parseCoords(p);
		offset = points.get("ORIGIN");
	}

	private void process(Map<String, Processor> processors, Map<String, Processor> map, String cmd, String params) {
		Processor p = processors.get(cmd);
		if (p != null) {
			String key = getKey(params);
			p.withInstruction(params);
			map.put(key, p);
		}
	}

	private void apply(String instructions, Map<String, Processor> processors, Map<String, Processor> map, BufferedReader in){
		// TODO process apply instructions for Coord offset or whatever
		try{
			for (int row = 0; in.ready(); row++) {
				String line = in.readLine();
				String[] charz = line.split("");
				for (int col = 0; col < charz.length; col++) {
					String key = charz[col];
					Processor p = map.get(key);
					Coord coord = new Coord(col, row);
					if (p == null) {
						p = mostApplicable(processors, coord, key);
					}
					p.apply(coord, key);
				}
			}
		} catch (IOException io){
			//gulp
		}
	}

	private Processor mostApplicable(Map<String, Processor> processors, Coord coord, String key) {
		return processors.values().stream().max(getComparator(coord, key)).get();
	}

	private Comparator<? super Processor> getComparator(final Coord coord, final String key) {
		return new Comparator<Processor>() {

			@Override
			public int compare(Processor o1, Processor o2) {
				return o1.applicability(coord, key) - o2.applicability(coord, key);
			}
		};
	}

	private String getKey(String line) {
		String key = "";
		int split = line.indexOf(" ");
		if (split != -1) {
			key = line.substring(0, split);
		}
		return key;
	}

	private String getParams(String line) {
		String params = "";
		int split = line.indexOf(":");
		if (split != -1 && split != line.length() - 1) {
			params = line.substring(split + 1);
		}
		return params;
	}

	private String getCommand(String line) {
		String cmd = "";
		int split = line.indexOf(":");
		if (split != -1) {
			cmd = line.substring(0, split);
		}
		return cmd;
	}
}
