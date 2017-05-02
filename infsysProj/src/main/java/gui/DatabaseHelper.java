package gui;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

abstract class DatabaseHelper {
	
	public static enum Database {
		ZOODB("ZooDB"), BASEX("BaseX"), NOSQL("NoSQL");

		private final String string;

		private Database(String string) {
			this.string = string;
		}

		public String getString() {
			return string;
		}

	}
	
	public static Database database = Database.ZOODB;
		
	public static DatabaseHelper get(){
		switch(database){
		case ZOODB:
			return new DatabaseHelperZooDB();
		case BASEX:
			return new DatabaseHelperBaseX();
		case NOSQL:
			return new DatabaseHelperBaseX();
		default:
			return null;
		}
	}
	
		abstract List<Publication> getAllInProceedings();
	
		abstract  void createDB();

		abstract List<Publication> getAllPublications();

		abstract  List<Publication> getAllProceedings();

		abstract  List<Person> getAllPeople();

		abstract  List<Conference> getAllConference();

		abstract  List<Series> getAllSeries();

		abstract  List<Publisher> getAllPublisher();

		abstract  List<ConferenceEdition> getAllConferenceEdition();

		abstract  String getNumberOfPublicationsForPublisher(Publisher name);

		abstract  String getConferenceName(String proceedingName);

		abstract  String getConferenceEditionName(ConferenceEdition edition);

		abstract  String getConferenceEditionProceeding(ConferenceEdition edition);
		
		abstract  String getConferenceYear(String proceedingName);

		abstract  List<String> getInProceedingsOfProceedings(String proceedingId);

		abstract  List<String> getAuthoredPublicationsForPerson(String personName);
		
		abstract  List<String> getEditedPublicationsForPerson(String personName);
		
		abstract  Proceedings getProceedingOfInproceeding(InProceedings inProceedings);

		abstract  List<String> getAuthorsOfInProceeding(InProceedings inProceeding);

		abstract  List<Publication> searchForPublication(String search);

		abstract  List<Person> searchForPeople(String search);

		abstract  List<Conference> searchForConference(String search);
		
		abstract  List<Series> searchForSeries(String search);
		
		abstract  List<ConferenceEdition> searchForConferenceEdition(String search);

		abstract  List<Publisher> searchForPublisher(String search);
		
		abstract  List<Publication> searchForProceedings(String search);

		abstract  List<Publication> searchForInProceedings(String search);

		abstract  void updatePerson(String oldName, String name, List<String> authoredPublications, List<String> editedPublications);

		abstract  void deletePerson(String name);

		abstract  void updateProceeding(String title, Proceedings newProc, List<String> authors, List<String> inProcNames, String publisherName, String seriesName, String conferenceName, int confYear);

		abstract  void deleteProceeding(String title);

		abstract  void addPerson(String newName, List<String> authoredPublications, List<String> editedPublications);

		abstract  void deleteInProceeding(String id);

		abstract  void updateInProceeding(String id, InProceedings newInProceeding, String procTitle, List<String> authors);
		
		abstract  void addInProceeding(InProceedings newInProceeding, String procTitle, List<String> authors);

		abstract  void addProceeding(Proceedings newProceeding, List<String> authors, List<String> inProceedings, String pubName, String seriesName, String confName, int confYear);
			
		
		abstract void openDB();
		abstract void addProceedings(List<Proceedings> readProceedings);
		abstract void addInProceedings(List<InProceedings> readInProceedings);
		abstract void closeDB();

		/**
		 * 
		 * Queries
		 * 
		 **/

		// find title of abstractation by key (according to xml file)
		abstract  void query1(String id);
		
		// Find abstractations by title, returning only a subset of all found abstractations
		abstract  void query2(String title, int startOffset, int endOffset);

		// Find abstractations by title, returning only a subset of all found abstractations ORDERED by title
		abstract  void query3(String title, int startOffset, int endOffset);

		// returns name of the co-authors of a given author
		abstract  void query4(String author);

		abstract  void query5(String name1, String name2);


		// global average of authors / abstractation (only InProceedings)
		abstract  void query6();
		// Returns the number of abstractations per year between the interval year1 and year 2
		abstract  void query7(int year1, int year2);
		// No of all abstractations of a conference, for every edition except proceedings
		abstract  void query8(String conferenceName);

		abstract  void query9(String confName);

		abstract  void query10(String confName);

		abstract  void query11(String confName);

		abstract  void query12() throws ParserConfigurationException, SAXException, IOException;

		// all abstractations, where given author is mentioned last
		abstract  void query13(String author) throws Exception;

		abstract void query14(int year1, int year2);
}
