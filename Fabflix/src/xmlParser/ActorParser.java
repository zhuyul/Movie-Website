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

import dao.StarDAO;
import data.Star;

public class ActorParser extends DefaultHandler {

	List<Star> myStars;
	HashMap<String, Integer> starIdMap;
	
	private String tempVal;
	private Star tempStar;
	
	public ActorParser() {
		myStars = new ArrayList<Star>();
		starIdMap = new HashMap<String, Integer>();
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
            sp.parse("xml_data/actors63.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    
    private void insertData() {
    	StarDAO starDao = new StarDAO();
    	List<Integer> starIds = starDao.batchInsertStar(myStars);
    	if (starIds.size() != myStars.size()) {
    		System.out.println("Star Id size don't match");
    		return;
    	}
    	Iterator<Star> starIter = myStars.iterator();
    	Iterator<Integer> idIter = starIds.iterator();
    	while (starIter.hasNext()) {
    		starIter.next().setId(idIter.next());
    	}
    	for (Star star : myStars) {
    		String fullName = "";
    		if (!star.getFirstName().equals("")) {
    			fullName += star.getFirstName() + " ";
    		}
    		fullName += star.getLastName();
    		starIdMap.put(fullName, star.getId());
    	}
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("actor")) {
            //create a new instance of employee
            tempStar = new Star();
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	if (qName.equalsIgnoreCase("actor")) {
    		myStars.add(tempStar);
    	} else if (qName.equalsIgnoreCase("familyname")) {
    		if (!tempVal.trim().equals("")) {
    			if (tempStar.getLastName() != null) {
    				tempStar.setLastName(tempVal.trim());
    			}
    		}
    	} else if (qName.equalsIgnoreCase("firstname")) {
    		if (!tempVal.trim().equals("")) {
    			if (tempStar.getFirstName() != null) {
    				tempStar.setFirstName(tempVal.trim());
    			}
    		}
    	} else if (qName.equalsIgnoreCase("stagename")) {
    		String fullName = tempVal.trim();
    		String firstName = "";
    		String lastName = "";
    		int spaceIndex = fullName.indexOf(" ");
    		if (spaceIndex > -1) {
    			firstName = fullName.substring(0, spaceIndex);
    			lastName = fullName.substring(spaceIndex + 1);
    		} else {
    			lastName = fullName;
    		}
    		tempStar.setFirstName(firstName);
    		tempStar.setLastName(lastName);
    	}
    }
    
    public HashMap<String, Integer> getStarIdMap() {
    	return starIdMap;
    }

}
