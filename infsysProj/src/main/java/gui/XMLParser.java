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
import infsysProj.infsysProj.Publisher;

public class XMLParser {
	
	private static String xmlFileName = "dblp_filtered.xml";
	
	public void parse(){
        DatabaseHelper.createDB();
        DatabaseHelper.openDB();

        parseProceedings(xmlFileName);
        parseInProceedings(xmlFileName);
        
        DatabaseHelper.closeDB();

	}
	private void parseProceedings(String xmlName){
	    try {

	    	SAXParserFactory factory = SAXParserFactory.newInstance();
	    	SAXParser saxParser = factory.newSAXParser();

	    	DefaultHandler handler = new DefaultHandler() {

	    	Proceedings	procTemp = null;
			ArrayList<Person> editors = new ArrayList<Person>();
			String title = "";
			int year;
			String booktitle = "";
			String isbn = "";
			String publisher = "";
			String series = "";
			String volume = "";
			String ee = "";
			boolean inEditor = false;
			boolean inTitle = false;
			boolean inYear = false;
			boolean inBooktitle = false;
			boolean inISBN = false;
			boolean inPublisher = false;
			boolean inSeries = false;
			boolean inVolume = false;
			boolean inEE = false;
			
	    	public void startElement(String uri, String localName,String qName,
	                    Attributes attributes) throws SAXException {

	    		
	    		
	    		if (qName.equalsIgnoreCase("proceedings")) {
	    			
	    			title = "";
	    			year = 0;
	    			booktitle = "";
	    			publisher = "";
	    			series = "";
	    			volume = "";
	    			ee = "";
	    			editors.clear();

	    			procTemp = new Proceedings(3);
	    			procTemp.setId(attributes.getValue(1));
	    			//procTemp.setElectronicEdition(attributes.getValue(0));

	    			
	    		}
	    		
	    		else if(qName.equalsIgnoreCase("editor")){
	    			inEditor = true;
	    		}
	    		
	    		else if(qName.equalsIgnoreCase("title")){
	    			inTitle = true;
	    		}
	    		else if(qName.equalsIgnoreCase("year")){
	    			inYear = true;
	    		}

	    		else if(qName.equalsIgnoreCase("booktitle")){
	    			inBooktitle = true;
	    		}
	    		else if(qName.equalsIgnoreCase("isbn")){
	    			inISBN = true;
	    		}
	    		else if(qName.equalsIgnoreCase("publisher")){
	    			inPublisher = true;
	    		}
	    		else if(qName.equalsIgnoreCase("series")){
	    			inSeries = true;
	    		}
	    		else if(qName.equalsIgnoreCase("volume")){
	    			inVolume = true;
	    		}
	    		else if(qName.equalsIgnoreCase("ee")){
	    			inEE = true;
	    		}





	    	}

	    	public void endElement(String uri, String localName,
	    		String qName) throws SAXException {
	    			    		
	    		if (qName.equalsIgnoreCase("proceedings")) {
	    			procTemp.setAuthors(editors);
	    			procTemp.setYear(year);
	    			procTemp.setTitle(title);
	    			procTemp.setIsbn(isbn);
	    			procTemp.setVolume(volume);
	    			procTemp.setElectronicEdition(ee);
	    			
	    			DatabaseHelper.addProceeding(procTemp,booktitle, publisher, series);
	    			
	    			procTemp = null;
	    		}

	    		else if(qName.equalsIgnoreCase("editor")){
	    			inEditor = false;
	    		}
	    		else if(qName.equalsIgnoreCase("title")){
	    			inTitle = false;
	    		}
	    		else if(qName.equalsIgnoreCase("year")){
	    			inYear = false;
	    		}
	    		else if(qName.equalsIgnoreCase("booktitle")){
	    			inBooktitle = false;
	    		}
	    		else if(qName.equalsIgnoreCase("isbn")){
	    			inISBN = false;
	    		}
	    		else if(qName.equalsIgnoreCase("publisher")){
	    			inPublisher = false;
	    		}
	    		else if(qName.equalsIgnoreCase("series")){
	    			inSeries = false;
	    		}
	    		else if(qName.equalsIgnoreCase("volume")){
	    			inVolume = false;
	    		}
	    		else if(qName.equalsIgnoreCase("ee")){
	    			inEE = false;
	    		}






	    		//System.out.println("End Element :" + qName);

	    	}

	    	public void characters(char ch[], int start, int length) throws SAXException {
	    		
	    		if(procTemp != null){
		            if (inEditor) {
		                editors.add(new Person( new String(ch, start, length)));
		            }
		            else if (inTitle) {
		    			title = new String(ch,start,length);
		            }
		            else if (inYear) {
		    			year = Integer.parseInt(new String(ch,start,length));
		            }
		            else if (inBooktitle) {
		    			booktitle = new String(ch,start,length);
		            }
		            else if (inISBN) {
		    			isbn = new String(ch,start,length);
		            }
		            else if (inPublisher) {
		    			publisher = new String(ch,start,length);
		            }
		            else if (inSeries) {
		    			series = new String(ch,start,length);
		            }
		            else if (inVolume) {
		    			volume = new String(ch,start,length);
		            }
		            else if (inEE) {
		    			ee = new String(ch,start,length);
		            }

	    		}



	       

	    	}

	         };
	        
	        saxParser.parse(xmlName, handler);
	        
	         } catch (Exception e) {
	           e.printStackTrace();
	         }

	       }

	private void parseInProceedings(String xmlName){
		    try {

		    	SAXParserFactory factory = SAXParserFactory.newInstance();
		    	SAXParser saxParser = factory.newSAXParser();

		    	DefaultHandler handler = new DefaultHandler() {

		    	InProceedings inProcTemp = null;
				ArrayList<Person> authors = new ArrayList<Person>();
				String title = "";
				int year;
				String pages = "";
				String booktitle = "";
				String crossref = "";
				String ee = "";
				boolean inAuthor = false;
				boolean inTitle = false;
				boolean inPages = false;
				boolean inYear = false;
				boolean inBooktitle = false;
				boolean inCrossRef = false;
				boolean inEE = false;

		    	public void startElement(String uri, String localName,String qName,
		                    Attributes attributes) throws SAXException {

		    		

		    		if (qName.equalsIgnoreCase("inproceedings")) {
		    			
		    			title = "";
		    			year = 0;
		    			pages = "";
		    			booktitle = "";
		    			crossref = "";
		    			ee = "";

		    			authors.clear();

		    			inProcTemp = new InProceedings(3);
		    			inProcTemp.setId(attributes.getValue(1));
		    			inProcTemp.setElectronicEdition(attributes.getValue(0));
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
		    		else if(qName.equalsIgnoreCase("year")){
		    			inYear = true;
		    		}

		    		else if(qName.equalsIgnoreCase("booktitle")){
		    			inBooktitle = true;
		    		}
		    		else if(qName.equalsIgnoreCase("crossref")){
		    			inCrossRef = true;
		    		}
		    		else if(qName.equalsIgnoreCase("ee")){
		    			inEE = true;
		    		}



		    	}

		    	public void endElement(String uri, String localName,
		    		String qName) throws SAXException {
		    		
		    		if (qName.equalsIgnoreCase("inproceedings")) {
		    			inProcTemp.setAuthors(authors);
		    			inProcTemp.setYear(year);
		    			inProcTemp.setPages(pages);
		    			inProcTemp.setTitle(title);
		    			inProcTemp.setElectronicEdition(ee);
		    					    			
		    			DatabaseHelper.addInProceeding(inProcTemp, crossref);
		    			
		    			inProcTemp = null;
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
		    		else if(qName.equalsIgnoreCase("booktitle")){
		    			inBooktitle = false;
		    		}
		    		else if(qName.equalsIgnoreCase("crossref")){
		    			inCrossRef = false;
		    		}
		    		else if(qName.equalsIgnoreCase("ee")){
		    			inEE = false;
		    		}






		    		//System.out.println("End Element :" + qName);

		    	}

		    	public void characters(char ch[], int start, int length) throws SAXException {
		    		
		    		if(inProcTemp != null){
		    			
			            if (inAuthor) {
			                authors.add(new Person( new String(ch, start, length)));
			            }
			            else if (inTitle) {
			    			title = new String(ch,start,length);
			            }
			            else if (inPages) {
			    			pages = new String(ch,start,length);
			            }
			            else if (inYear) {
			    			year = Integer.parseInt(new String(ch,start,length));
			            }
			            else if (inBooktitle) {
			    			booktitle = new String(ch,start,length);
			            }
			            else if (inCrossRef) {
			    			crossref = new String(ch,start,length);
			            }
			            else if (inEE) {
			    			ee = new String(ch,start,length);
			            }

			            

		    		}


		       

		    	}

		         };
		        
		        saxParser.parse(xmlName, handler);
		        

		         } catch (Exception e) {
		           e.printStackTrace();
		         }

		       }
   
   
}
