package gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

	
	static class QueryData {
		long execTime;
		String queryDetail;
		long memoryUsage;
		
		/*
	    public QueryData(long execTime,String queryDetail,long memoryUsage) {
	        this.execTime = execTime;
	        this.queryDetail = queryDetail;
	        this.memoryUsage = memoryUsage;

	    } 
	    */
	}

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

	public static DatabaseHelper get() {
		switch (database) {
		case ZOODB:
			return new DatabaseHelperZooDB();
		case BASEX:
			return new DatabaseHelperBaseX();
		case NOSQL:
			return new DatabaseHelperNoSQL();
		default:
			return null;
		}
	}

	abstract List<Publication> getAllInProceedings();

	abstract void createDB();

	abstract List<Publication> getAllPublications();

	abstract List<Publication> getAllProceedings();

	abstract List<Person> getAllPeople();

	abstract List<Conference> getAllConference();

	abstract List<Series> getAllSeries();

	abstract List<Publisher> getAllPublisher();

	abstract List<ConferenceEdition> getAllConferenceEdition();

	abstract String getNumberOfPublicationsForPublisher(Publisher name);

	abstract String getConferenceName(String proceedingName);

	abstract String getConferenceEditionName(ConferenceEdition edition);

	abstract String getConferenceEditionProceeding(ConferenceEdition edition);

	abstract String getConferenceYear(String proceedingName);

	abstract List<String> getInProceedingsOfProceedings(String proceedingId);

	abstract List<String> getAuthoredPublicationsForPerson(String personName);

	abstract List<String> getEditedPublicationsForPerson(String personName);

	abstract Proceedings getProceedingOfInproceeding(InProceedings inProceedings);

	abstract List<String> getAuthorsOfInProceeding(InProceedings inProceeding);

	abstract List<String> getAuthorsOfProceeding(Proceedings proceeding);

	abstract String getPublisherName(Proceedings proceeding);

	abstract String getSeriesName(Proceedings proceeding);

	abstract List<Publication> searchForPublication(String search);

	abstract List<Person> searchForPeople(String search);

	abstract List<Conference> searchForConference(String search);

	abstract List<Series> searchForSeries(String search);

	abstract List<ConferenceEdition> searchForConferenceEdition(String search);

	abstract List<Publisher> searchForPublisher(String search);

	abstract List<Publication> searchForProceedings(String search);

	abstract List<Publication> searchForInProceedings(String search);

	abstract void updatePerson(String oldName, String name, List<String> authoredPublications, List<String> editedPublications);

	abstract void deletePerson(String name);

	abstract void updateProceeding(String title, Proceedings newProc, List<String> authors, List<String> inProcNames, String publisherName, String seriesName, String conferenceName, int confYear);

	abstract void deleteProceeding(String title);

	abstract void addPerson(String newName, List<String> authoredPublications, List<String> editedPublications);

	abstract void deleteInProceeding(String id);

	abstract void updateInProceeding(String id, InProceedings newInProceeding, String procTitle, List<String> authors);

	abstract void addInProceeding(InProceedings newInProceeding, String procTitle, List<String> authors);

	abstract void addProceeding(Proceedings newProceeding, List<String> authors, List<String> inProceedings, String pubName, String seriesName, String confName, int confYear);

	/**
	 * 
	 * Queries
	 * 
	 **/

	// find title of abstractation by key (according to xml file)
	abstract void query1(String id);

	// Find abstractations by title, returning only a subset of all found abstractations
	abstract void query2(String title, int startOffset, int endOffset);

	// Find abstractations by title, returning only a subset of all found abstractations ORDERED by title
	abstract void query3(String title, int startOffset, int endOffset);

	// returns name of the co-authors of a given author
	abstract void query4(String author);

	abstract void query5(String name1, String name2);

	// global average of authors / abstractation (only InProceedings)
	abstract void query6();

	// Returns the number of abstractations per year between the interval year1 and year 2
	abstract void query7(int year1, int year2);

	// No of all abstractations of a conference, for every edition except proceedings
	abstract void query8(String conferenceName);

	abstract void query9(String confName);

	abstract void query10(String confName);

	abstract void query11(String confName);

	abstract void query12() throws ParserConfigurationException, SAXException, IOException;

	// all abstractations, where given author is mentioned last
	abstract void query13(String author) throws Exception;

	abstract void query14(int year1, int year2);

	public static void queryStatistics() {

		// Create .xls file
		String filename = "QueryStatistics.xls";
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");

		HSSFRow rowhead = sheet.createRow(0);
		rowhead.createCell(0).setCellValue("Query No.");
		rowhead.createCell(1).setCellValue("Details");
		rowhead.createCell(2).setCellValue("Time ZooDB");
		rowhead.createCell(4).setCellValue("Time NoSQL");
		rowhead.createCell(3).setCellValue("Time BaseX");

		// Run all queries and measure start and endTime
		for (int i = 1; i < 15; i++) {
			//iterate over all three databases
			
			rowhead = sheet.createRow(i);
			rowhead.createCell(0).setCellValue("Query " + i);

			for (Database db : Database.values()) {
				database = db;
				
				QueryData queryData = runQuery(i);
				rowhead.createCell(1).setCellValue(queryData.queryDetail);
				switch(db){
				case ZOODB:
					rowhead.createCell(2).setCellValue(queryData.execTime + "ms");
					break;
				case BASEX:
					rowhead.createCell(3).setCellValue(queryData.execTime + "ms");
					break;
				case NOSQL:
					rowhead.createCell(4).setCellValue(queryData.execTime + "ms");
					break;
				}

				
				
				
				FileOutputStream fileOut;
				try {
					fileOut = new FileOutputStream(filename);
					workbook.write(fileOut);
					fileOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Your excel file has been generated!");

	}
	
	
	private static QueryData runQuery(int i){
		
		QueryData result = new QueryData();
		long start1 = 0;
		long start2 = 0;
		long start3 = 0;
		long end1 = 0;
		long end2 = 0;
		long end3 = 0;

		switch (i) {
		case 1:
			result.queryDetail = "find publication by key";
			start1 = System.currentTimeMillis();
			for (int j = 0; j < 100; j++) {
				get().query1("conf/acm/ColeMM87");
			}
			end1 = System.currentTimeMillis();
			
			start2 = System.currentTimeMillis();
			for (int j = 0; j < 100; j++) {
				get().query1("conf/flfp/Bohm86");
			}
			end2 = System.currentTimeMillis();

			start3 = System.currentTimeMillis();
			for (int j = 0; j < 100; j++) {
				get().query1("conf/dac/ShirakawaOHTO80");
			}
			end3 = System.currentTimeMillis();

			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			break;
		case 11:
			break;
		case 12:
			break;
		case 13:
			break;
		case 14:
			break;
		default:
			break;
		}

		result.execTime = (end1 -start1 + end2-start2 +end3-start3)/3;
		return result;

	}
	
	

	
	
}
