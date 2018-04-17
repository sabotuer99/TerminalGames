package whorten.termgames.utils;

public class StringUtils {

	public static String repeat(String body, int iterations){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < iterations; i++){
			sb.append(body);
		}
		return sb.toString();
	}
}
