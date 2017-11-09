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

import dao.GenreDAO;
import dao.MovieDAO;
import data.Genre;
import data.Movie;

public class MovieParser extends DefaultHandler {

	List<Movie> myMovies;
	List<Genre> genreList;
	HashMap<String, Integer> movieIdMap;
	
	private String tempVal;
	private String tempDirname;
	private Movie tempMovie;
	
	public MovieParser() {
		myMovies = new ArrayList<Movie>();
		GenreDAO dao = new GenreDAO();
		genreList = dao.getAllGenre();
		movieIdMap = new HashMap<String, Integer>();
	}
	
	public void runParse() {
		parseDocument();
		insertData();
	}
	
	private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("xml_data/mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    
    private void insertData() {
    	MovieDAO movieDao = new MovieDAO();
    	List<Integer> movieIds = movieDao.batchInsertMovie(myMovies);
    	if (movieIds.size() != myMovies.size()) {
    		System.out.println("ID and Movie Size don't match.");
    		return;
    	}
    	Iterator<Movie> movieIter = myMovies.iterator();
    	Iterator<Integer> idIter = movieIds.iterator();
    	while (movieIter.hasNext() && idIter.hasNext()) {
    		movieIter.next().setId(idIter.next());
    	}
    	for (Movie movie : myMovies) {
    		movieIdMap.put(movie.getTitle(), movie.getId());
    	}
    	movieDao.batchInsertGenresInMovies(myMovies);
    }
	
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("film")) {
            //create a new instance of employee
            tempMovie = new Movie();
            tempMovie.setDirector(tempDirname);
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	if (qName.equalsIgnoreCase("film")) {
    		if (!tempMovie.getTitle().equals("")) {
	            //add movie to the list
	            myMovies.add(tempMovie);
    		}
    	} else if (qName.equalsIgnoreCase("dirname")) {
    		tempDirname = tempVal.trim();
    	} else if (qName.equalsIgnoreCase("t")) {
    		tempMovie.setTitle(tempVal.trim());
    	} else if (qName.equalsIgnoreCase("year")) {
    		// Use 1900 if year data is corrupt 
    		int year = 0;
    		try {
    			year = Integer.parseInt(tempVal.trim());
    		} catch (Exception e) {
    			System.out.println("Movie: " + tempMovie.getTitle() + " is set to year 0 due to a incompatible year format which is: " + tempVal);
    		}
    		tempMovie.setYear(year);
    	} else if (qName.equalsIgnoreCase("cat")) {
    		for (Genre genre : genreList) {
    			if (genre.getName().equals(tempVal.trim())) {
    				tempMovie.addToGenreList(genre);
    				return;
    			}
    		}
    		
    		Genre tempGenre = new Genre();
    		GenreDAO genreDao = new GenreDAO();
    		int id = genreDao.getIdFromName(tempVal.trim());
    		if (id == -1) {
    			id = genreDao.insertGenre(tempVal.trim());
    		}
    		tempGenre.setName(tempVal.trim());
    		tempGenre.setId(id);
    		genreList.add(tempGenre);
    		tempMovie.addToGenreList(tempGenre);
    	}
    }
    
    public HashMap<String, Integer> getMovieIdMap() {
    	return movieIdMap;
    }

}
