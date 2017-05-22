package gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xml.sax.SAXException;

import infsysProj.infsysProj.*;


abstract class DatabaseHelper {

	
	static class QueryData {
		long execTime;
		long usedMem;
		String queryDetail;
		
		
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
		rowhead.createCell(3).setCellValue("Time NoSQL");
		rowhead.createCell(4).setCellValue("Time BaseX");
		rowhead.createCell(5).setCellValue("Memory ZooDB");
		rowhead.createCell(6).setCellValue("Memory NoSQL");
		rowhead.createCell(7).setCellValue("Memory BaseX");
		
		// Run all queries and measure start and endTime
		for (int i = 0; i < 15; i++) {
			//iterate over all three databases
			
			rowhead = sheet.createRow(i);
			rowhead.createCell(0).setCellValue("Query " + i);

			for (Database db : Database.values()) {
				database = db;
				
				QueryData queryData = runQuery(i);
				rowhead.createCell(1).setCellValue(queryData.queryDetail);
				switch(db){
				case ZOODB:
					rowhead.createCell(2).setCellValue(queryData.execTime + " ms");
					rowhead.createCell(5).setCellValue(queryData.usedMem + " KB");
					break;
					
				case NOSQL:
					rowhead.createCell(3).setCellValue(queryData.execTime + " ms");
					rowhead.createCell(6).setCellValue(queryData.usedMem + " KB");
					break;

				case BASEX:
					rowhead.createCell(4).setCellValue(queryData.execTime + " ms");
					rowhead.createCell(7).setCellValue(queryData.usedMem + " KB");
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
		Runtime rt = Runtime.getRuntime();
		long totalMem = rt.totalMemory();
		long mstart1 = 0;
		long mstart2 = 0;
		long mstart3 = 0;
		long mend1 = 0;
		long mend2 = 0;
		long mend3 = 0;
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
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 700; j++) {
				get().query1("conf/acm/ColeMM87");
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 700; j++) {
				get().query1("conf/flfp/Bohm86");
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 700; j++) {
				get().query1("conf/dac/ShirakawaOHTO80");
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();

			break;
		case 2:
			result.queryDetail = "find publication with offset";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 120; j++) {
				get().query2("expert system", 0, 3);
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 120; j++) {
				get().query2("expert system", 0, 3);
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 120; j++) {
				get().query2("expert system", 0, 3);
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 3:
			result.queryDetail = "find publication ordered";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 100; j++) {
				get().query3("expert system", 0, 30);
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 100; j++) {
				get().query3("expert system", 0, 30);
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 100; j++) {
				get().query3("expert system", 0, 30);
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 4:
			result.queryDetail = "find co authors";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 100; j++) {
				get().query4("Ian Parberry");
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 100; j++) {
				get().query4("Adi Shamir");
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 100; j++) {
				get().query4("J. Thomas Haigh");
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 5:
			result.queryDetail = "distance of 2 authors";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 50; j++) {
				get().query5("J. Thomas Haigh", "William R. Bevier");
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 50; j++) {
				get().query5("William D. Young","J. Thomas Haigh");
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 50; j++) {
				get().query5("Guy Dupenloup","Hiroshi Fukuda");
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 6:
			result.queryDetail = "avg authors per inproceedings";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 2500; j++) {
				get().query6();
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 2500; j++) {
				get().query6();
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 2500; j++) {
				get().query6();
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 7:
			result.queryDetail = "number of publications per year";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 1200; j++) {
				get().query7(1980,1990);
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 1200; j++) {
				get().query7(1970,1980);
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 1200; j++) {
				get().query7(1960,1970);
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 8:
			result.queryDetail = "number of publications for a conf";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 1000; j++) {
				get().query8("ICML");
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 1000; j++) {
				get().query8("ECHT");
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 1000; j++) {
				get().query8("ICML");
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 9:
			result.queryDetail = "authors and editors of conf";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 900; j++) {
				get().query9("ICML");
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 900; j++) {
				get().query9("ECHT");
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 900; j++) {
				get().query9("ICML");
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 10:
			result.queryDetail = "authors of conf";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 800; j++) {
				get().query10("ICML");
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 800; j++) {
				get().query10("ECHT");
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 800; j++) {
				get().query10("ICML");
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 11:
			result.queryDetail = "all publications of conf";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 900; j++) {
				get().query11("ICML");
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 900; j++) {
				get().query11("ECHT");
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 900; j++) {
				get().query11("ECHT");
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 12:
			result.queryDetail = "people who are editors and authors";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 16; j++) {
				try {
					get().query12();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 16; j++) {
				try {
					get().query12();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 16; j++) {
				try {
					get().query12();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 13:
			result.queryDetail = "publications were author appears last";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 50; j++) {
				try {
					get().query13("Adi Shamir");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 50; j++) {
				try {
					get().query13("Adi Shamir");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 50; j++) {
				try {
					get().query13("Adi Shamir");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		case 14:
			result.queryDetail = "publishers";
			start1 = System.currentTimeMillis();
			mstart1 = totalMem - rt.freeMemory();
			for (int j = 0; j < 25; j++) {
				get().query14(1950,1960);
			}
			end1 = System.currentTimeMillis();
			mend1 = totalMem - rt.freeMemory();
			
			start2 = System.currentTimeMillis();
			mstart2 = totalMem - rt.freeMemory();
			for (int j = 0; j < 25; j++) {
				get().query14(1960,1970);
			}
			end2 = System.currentTimeMillis();
			mend2 = totalMem - rt.freeMemory();

			start3 = System.currentTimeMillis();
			mstart3 = totalMem - rt.freeMemory();
			for (int j = 0; j < 25; j++) {
				get().query14(1980,1990);
			}
			end3 = System.currentTimeMillis();
			mend3 = totalMem - rt.freeMemory();
			
			break;
		default:
			break;
		}

		result.execTime = (end1 -start1 + end2-start2 +end3-start3)/3;
		result.usedMem = 0;
		int valid = 0; // counts valid runs
		// if the measurement is negative, this means that the garbage collector ran
		if(mend1 - mstart1 > 0){
			result.usedMem += mend1 - mstart1;
			valid++;
		}
		if(mend2 - mstart2 > 0){
			result.usedMem += mend2 - mstart2;
			valid++;
		} 
		if(mend3 - mstart3 > 0){
			result.usedMem += mend3 - mstart3;
			valid++;
		} 
		if (valid == 0) {
		// if the gc ran in all 3 measurements
			result.usedMem = 0;
		} else {
// divided by 1000 in order to get the measurements in KB, instead of bytes
			result.usedMem /= (valid*1000);
		}

		return result;

	}
	
	

	
	
}
