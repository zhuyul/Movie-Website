package xmlParser;

import java.util.HashMap;

public class ParseMain {

	public static void main(String[] args) {
		
		MovieParser mp = new MovieParser();
		ActorParser ap = new ActorParser();
		CastParser cp = new CastParser();
		
		mp.runParse();
		ap.runParse();
		cp.runParse();
		
		HashMap<String, Integer> movieIdMap = mp.getMovieIdMap();
		HashMap<String, Integer> starIdMap = ap.getStarIdMap();
				
		cp.buildStarInMovie(movieIdMap, starIdMap);
		cp.insertData();
		
		System.out.println("Data Ingestion Complete");
	}
}
