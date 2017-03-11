package gui;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;

public class XMLParser {
		
   public void parse(){
	    try {

	    	SAXParserFactory factory = SAXParserFactory.newInstance();
	    	SAXParser saxParser = factory.newSAXParser();

	    	DefaultHandler handler = new DefaultHandler() {

	    	InProceedings inProcTemp = null;
	    	Proceedings	procTemp = null;
			ArrayList<Person> authors = new ArrayList<Person>();
			String title = "";
			int year;
			String pages = "";
			boolean inAuthor = false;
			boolean inTitle = false;
			boolean inPages = false;
			boolean inYear = false;

	    	public void startElement(String uri, String localName,String qName,
	                    Attributes attributes) throws SAXException {

	    		

	    		if (qName.equalsIgnoreCase("inproceedings")) {
	    			
	    			title = "";
	    			year = 0;
	    			pages = "";
	    			authors.clear();

	    			inProcTemp = new InProceedings(3);
	    			inProcTemp.setId(attributes.getValue(1));
	    			inProcTemp.setElectronicEdition(attributes.getValue(0));
	    		}
	    		
	    		else if (qName.equalsIgnoreCase("proceedings")) {
	    			
	    			title = "";
	    			year = 0;
	    			pages = "";
	    			authors.clear();

	    			procTemp = new Proceedings(3);
	    			procTemp.setId(attributes.getValue(1));
	    			procTemp.setElectronicEdition(attributes.getValue(0));

	    			
	    		}
	    		
	    		else if(qName.equalsIgnoreCase("author")){
	    			inAuthor = true;
	    		}
	    		
	    		else if(qName.equalsIgnoreCase("title")){
	    			inTitle = true;
	    		}
	    		else if(qName.equalsIgnoreCase("pages")){
	    			inPages = true;
	    		}
	    		else if(qName.equalsIgnoreCase("inYear")){
	    			inYear = true;
	    		}

	    	}

	    	public void endElement(String uri, String localName,
	    		String qName) throws SAXException {
	    		
	    		if (qName.equalsIgnoreCase("inproceedings")) {
	    			inProcTemp.setAuthors(authors);
	    			inProcTemp.setYear(year);
	    			inProcTemp.setPages(pages);
	    			inProcTemp.setTitle(title);
	    			
	    			DatabaseHelper.addInProceeding(inProcTemp);
	    			
	    			inProcTemp = null;
	    		}
	    		else if (qName.equalsIgnoreCase("proceedings")) {
	    			procTemp.setAuthors(authors);
	    			procTemp.setYear(year);
	    			procTemp.setTitle(title);
	    			
	    			DatabaseHelper.addProceeding(procTemp);
	    			
	    			procTemp = null;
	    		}

	    		else if(qName.equalsIgnoreCase("author")){
	    			inAuthor = false;
	    		}
	    		else if(qName.equalsIgnoreCase("title")){
	    			inTitle = false;
	    		}
	    		else if(qName.equalsIgnoreCase("pages")){
	    			inPages = false;
	    		}
	    		else if(qName.equalsIgnoreCase("year")){
	    			inYear = false;
	    		}




	    		//System.out.println("End Element :" + qName);

	    	}

	    	public void characters(char ch[], int start, int length) throws SAXException {

	            if (inAuthor && (inProcTemp != null || procTemp != null)) {
	                authors.add(new Person( new String(ch, start, length)));
	            }
	            else if (inTitle && (inProcTemp != null || procTemp != null)) {
	    			title = new String(ch,start,length);
	            }
	            else if (inPages && (inProcTemp != null || procTemp != null)) {
	    			pages = new String(ch,start,length);
	            }
	            else if (inYear && (inProcTemp != null || procTemp != null)) {
	    			year = Integer.parseInt(new String(ch,start,length));
	            }

	       

	    	}

	         };
	        
	        DatabaseHelper.createDB();
 			DatabaseHelper.openDB();

	        saxParser.parse("dblp_filtered_small.xml", handler);
	        
	        DatabaseHelper.closeDB();

	         } catch (Exception e) {
	           e.printStackTrace();
	         }

	       }

	   
   
}
