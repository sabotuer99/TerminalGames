package whorten.termgames.glyphs.loader;

import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.collate.GlyphStringCollater;
import whorten.termgames.glyphs.collate.GlyphStringCoord;

public class GlyphLoader {
	
	private static final String APPLY = "APPLY";

	public Set<GlyphStringCoord> parse(InputStream bais) {
		Set<GlyphStringCoord> glyphs = new HashSet<GlyphStringCoord>();
		Scanner in = new Scanner(bais);
		Map<String, Processor> processors = new HashMap<>();
		processors.put("SPEC", new SpecProcessor());
		processors.put("BOX", new BoxDrawingProcessor());
		//processors.put("RANGE", new RangeSpecProcessor());
		
		Map<String, Processor> map = new HashMap<>();
		map.put(" ", NoOpProcessor.getInstance());
		
		while(in.hasNext()){
			String line = in.nextLine();
			String cmd = getCommand(line);
			String params = getParams(line);
			
			if(APPLY.equals(cmd)){
				apply(params, processors, map, in);
			} else {
				Processor p = processors.get(cmd);
				if(p != null){
					String key = getKey(params);
					p.withInstruction(params);
					map.put(key, p);
				}
			}
		}
		
		for(Processor p : processors.values()){
			glyphs.addAll(p.process());
		}
		
		in.close();
		return new GlyphStringCollater().collate(glyphs);	
	}


	private void apply(String instructions,
						Map<String, Processor> processors,
						Map<String, Processor> map,
						Scanner in){
		//TODO process apply instructions for Coord offset or whatever
		for(int row = 0; in.hasNext(); row++){
			String line = in.nextLine();
			String[] charz = line.split("");
			for(int col = 0; col < charz.length; col++){
				String key = charz[col];
				Processor p = map.get(key);
				Coord coord = new Coord(col, row);
				if(p == null){
					p = mostApplicable(processors, coord, key);					
				}
				p.apply(coord, key);
			}
		}
	}
	
	
	private Processor mostApplicable(Map<String, Processor> processors, Coord coord, String key) {
		return processors.values().stream()
				.max(getComparator(coord, key)).get();
	}

	private Comparator<? super Processor> getComparator(final Coord coord, final String key) {
		return new Comparator<Processor>(){

			@Override
			public int compare(Processor o1, Processor o2) {
				return o1.applicability(coord, key)
						- o2.applicability(coord, key);
			}};
	}

	private String getKey(String line) {
		String key = "";
		int split = line.indexOf(" ");
		if(split != -1){
			key = line.substring(0, split);
		}
		return key;
	}

	private String getParams(String line) {
		String params = "";
		int split = line.indexOf(":");
		if(split != -1 && split != line.length() - 1){
			params = line.substring(split + 1);
		}
		return params;
	}

	private String getCommand(String line) {
		String cmd = "";
		int split = line.indexOf(":");
		if(split != -1){
			cmd = line.substring(0,split);
		}
		return cmd;
	}
}
