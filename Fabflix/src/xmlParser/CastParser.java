package xmlParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import dao.MovieDAO;
import dao.StarDAO;
import data.Movie;
import data.Star;
import data.StarInMovie;

public class CastParser extends DefaultHandler {
	
	List<StarInMovie> starInMovie;
	private String tempVal;
	private StarInMovie tempSIM;
	
	public CastParser() {
		starInMovie = new ArrayList<StarInMovie>();
	}
	
	public void buildStarInMovie(HashMap<String, Integer> movieIdMap, HashMap<String, Integer> starIdMap) {
		for (StarInMovie sim : starInMovie) {
			if (movieIdMap.containsKey(sim.getMovie())) {
				sim.setMovieId(movieIdMap.get(sim.getMovie()));
			} else {
				// Find movie ID from database, -1 if not found
				MovieDAO movieDao = new MovieDAO();
				int id = movieDao.movieTitleToId(sim.getMovie());
				sim.setMovieId(id);
				movieIdMap.put(sim.getMovie(), id);
			}
			
			if (starIdMap.containsKey(sim.getStar())) {
				sim.setStarId(starIdMap.get(sim.getStar()));
			} else {
				// Find star ID from database, -1 if not found
				StarDAO starDao = new StarDAO();
				String fullName = sim.getStar();
	    		String firstName = "";
	    		String lastName = "";
	    		int spaceIndex = fullName.indexOf(" ");
	    		if (spaceIndex > -1) {
	    			firstName = fullName.substring(0, spaceIndex);
	    			lastName = fullName.substring(spaceIndex + 1);
	    		} else {
	    			lastName = fullName;
	    		}
	    		int id = starDao.starNameToId(firstName, lastName);
				sim.setStarId(id);
				starIdMap.put(sim.getStar(), id);
			}
		}
	}
	
	public void runParse() {
		parseDocument();
	}
	
	private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("xml_data/casts124.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    
    public void insertData() {
    	MovieDAO movieDao = new MovieDAO();
    	movieDao.batchInsertStarsInMovies(starInMovie);
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("m")) {
            //create a new instance of employee
            tempSIM = new StarInMovie();
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	if (qName.equalsIgnoreCase("m")) {
    		if (!tempSIM.getStar().equals("")) {
    			starInMovie.add(tempSIM);
    		} else {
    			System.out.println("Discard stars_in_movies entry where star name is empty.");
    		}
    	} else if (qName.equalsIgnoreCase("t")) {
    		tempSIM.setMovie(tempVal.trim());
    	} else if (qName.equalsIgnoreCase("a")) {
    		tempSIM.setStar(tempVal.trim());
    	}
    }

}
