package gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.zoodb.jdo.ZooJdoHelper;
import org.zoodb.tools.ZooHelper;

import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class DatabaseHelper {
	private static PersistenceManager pm;
	private static String dbStandardName = "TheDatabase";

	public static void openDB() {
		pm = ZooJdoHelper.openDB(dbStandardName);
	}

	public static void closeDB() {
		closeDB(pm);
	}

	public static void createDB() {
		// remove database if it exists
		if (ZooHelper.dbExists(dbStandardName)) {

			ZooHelper.removeDb(dbStandardName);
		}

		// create database
		// By default, all database files will be created in %USER_HOME%/zoodb
		ZooHelper.createDb(dbStandardName);
	}

	private static void closeDB(PersistenceManager pm) {
		if (pm.currentTransaction().isActive()) {
			pm.currentTransaction().rollback();
		}
		pm.close();
		pm.getPersistenceManagerFactory().close();
	}

	public static Collection<Publication> getAllPublications() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Publication> ext = pm.getExtent(Publication.class);
		List<Publication> allPublications = new ArrayList<Publication>();
		for (Publication p : ext) {
			allPublications.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPublications;
	}

	public static void addInProceedings(List<InProceedings> list) {

		pm.setMultithreaded(true);

		int length = list.size();
		for (int i = 0; i < length; i++) {
			pm.currentTransaction().begin();
			pm.currentTransaction().setRetainValues(true);
			pm.makePersistent(list.get(i));
			pm.currentTransaction().commit();

			if (i % 500 == 0) {

				System.out.println(i + " / " + length + " InProceedings added to Database.");
			} else {
			}
		}
		System.out.println("All InProceedings added to Database");
	}

	public static void addProceedings(List<Proceedings> list) {

		pm.setMultithreaded(true);

		int length = list.size();
		for (int i = 0; i < length; i++) {
			pm.currentTransaction().begin();
			pm.currentTransaction().setRetainValues(true);
			pm.makePersistent(list.get(i));
			pm.currentTransaction().commit();

			if (i % 500 == 0) {

				System.out.println(i + " / " + length + " Proceedings added to Database.");
			}
		}
		System.out.println("All Proceedings added to Database");
	}

	public static Collection<Proceedings> getAllPeople() {

		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
		List<Proceedings> allPublications = new ArrayList<Proceedings>();
		for (Proceedings p : ext) {
			allPublications.add(p);
		}
		ext.closeAll();

		closeDB(pm);

		return allPublications;

	}

	// find publication by id
	public static void query1(String id) {
		System.out.println("Query 1:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "id == '" + id.replaceAll("'", "&#39") + "'");
		Collection<Publication> proceedings = (Collection<Publication>) query.execute();
		Publication proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + id);

		} else {
			proc = proceedings.iterator().next();
			System.out.println(proc.getTitle());
		}

		DatabaseHelper.closeDB();

	}

	//Find publications by title, returning only a subset of all found publications
	public static void query2(String title, int startOffset, int endOffset) {
		System.out.println("Query 2:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains('" + title.replaceAll("'", "&#39") + "')");
		Collection<Publication> proceedings = (Collection<Publication>) query.execute();
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + title + " in range: " + startOffset + "-" + endOffset);

		} else {
			Iterator<Publication> itr = proceedings.iterator();

			for (int i = 0; itr.hasNext(); i++) {
				if (i >= startOffset && i < endOffset) {
					System.out.println(itr.next().getTitle());
				} else {
					itr.next();
				}
			}
		}

		DatabaseHelper.closeDB();

	}

	//Find publications by title, returning only a subset of all found publications ORDERED by title
	public static void query3(String title, int startOffset, int endOffset) {
		System.out.println("Query 3:");

		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains('" + title.replaceAll("'", "&#39") + "')");
		Collection<Publication> proceedings = (Collection<Publication>) query.execute();
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + title + " in range: " + startOffset + "-" + endOffset);

		} else {

			Iterator<Publication> itr = proceedings.iterator();
			List<Publication> listOfPublications = new ArrayList<Publication>();
			for (int i = 0; itr.hasNext(); i++) {
				if (i >= startOffset && i < endOffset) {
					listOfPublications.add(itr.next());
				} else {
					itr.next();
				}
			}

			Collections.sort(listOfPublications, new Comparator<Publication>() {

				public int compare(Publication s1, Publication s2) {
					return s1.getTitle().compareToIgnoreCase(s2.getTitle());
				}
			});
			for (Publication p : listOfPublications) {
				System.out.println(p.getTitle());
			}
		}

		DatabaseHelper.closeDB();

	}

	//TODO: use == or contains for query?
	//returns name of the co-authors of a given author
	public static void query4(String author) {
		System.out.println("Query 4:");

		DatabaseHelper.openDB();
		pm.currentTransaction().begin();
		Collection<Person> result = new HashSet<Person>();

		Query query = pm.newQuery(Person.class);
		query.setFilter("name == ('" + author.replaceAll("'", "&#39") + "')");
		Collection<Person> auth = (Collection<Person>) query.execute();
		if (auth.size() == 1) {
			for (Person p : auth) {
				Collection<Publication> publications = p.getAuthoredPublications();
				publications.addAll(p.getEditedPublications());
				result.add(p);
				if (publications.isEmpty()) {
					System.out.println("Error: Did not find any publication with an author named: " + author);
				} else {
					Iterator<Publication> itr1 = publications.iterator();
					Publication currentPub;
					while (itr1.hasNext()) {
						currentPub = itr1.next();
						Iterator<Person> itr2 = currentPub.getAuthors().iterator();
						while (itr2.hasNext()) {
							Person currentPers = itr2.next();
							if (!result.contains(currentPers)) {
								// no duplicate values
								result.add(currentPers);
								System.out.println(currentPers.getName());
							} else {
								// don't print author's name or any duplicates
							}
						}
					}
				}
			}
		} else if (auth.size() > 1) {
			System.out.println("Error: There are several persons whose name contain: " + author);
		} else {
			System.out.println("Error: Did not find any person named: " + author);
		}
		DatabaseHelper.closeDB();
	}

	public static void query5() {

	}

	//global average of authors / publication (InProceedings + Proceedings)
	//TODO: In our case Publications = InProceedings + Proceedings?
	public static void query6() {
		System.out.println("Query 6:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Publication.class);
		Collection<Publication> publications = (Collection<Publication>) q1.execute();
		double sum = 0.;
		double total = publications.size();
		if (publications.isEmpty()) {
			System.out.println("Error: No publications available.");

		} else {
			Iterator<Publication> itr = publications.iterator();
			while (itr.hasNext()){
				sum += itr.next().getAuthors().size();
			}
			System.out.println("Average authors "+ (double)(sum/total) +".");
		}
		DatabaseHelper.closeDB();
	}

	// Returns the number of publications per year between the interval year1 and year 2
	public static void query7(int year1, int year2) {
		System.out.println("Query 7:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Publication.class);
		Collection<Publication> publications = (Collection<Publication>) q1.execute();
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (publications.isEmpty()) {
			System.out.println("Error: No publications available.");

		} else {
			Iterator<Publication> itr = publications.iterator();
			Publication p;
			int currYear;
			while (itr.hasNext()){
				p = itr.next();
				currYear = p.getYear();
				if(currYear < year1 || currYear > year2){
					continue;
				}
				if (map.containsKey(currYear)){
					int current = map.get(currYear);
					map.put(currYear, current+1);
				} else {
					map.put(currYear, 1);
				}
			}
		}
		DatabaseHelper.closeDB();
		//print Hashtable
		System.out.printf("%-12s%-17s\n","Year","No. Publications");
		for (int i = year1; i <= year2; i++){
			if (map.containsKey(i)){
		        System.out.printf("%-12d%-12d\n",i,map.get(i));
			}
		}
	}

	//No of all publications of a conference, except proceedings
	//That is counting only the inproceedings?
	public static void query8(String conferenceName) {
		System.out.println("Query 8:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		int result = 0;
		Query q1 = pm.newQuery(Conference.class, "name == '" + conferenceName.replaceAll("'", "&#39") + "'");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute();
		if(conferences.isEmpty()){
			System.out.println("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1){
			System.out.println("Multiple conferences found with name " + conferenceName);
		} else {
			//only interested in 1 conference
			Iterator<Conference> itr = conferences.iterator();
			Collection<ConferenceEdition> editions = itr.next().getEditions();
			Iterator<ConferenceEdition> itrEditions = editions.iterator();
			while (itrEditions.hasNext()){
				ConferenceEdition e = itrEditions.next();
				Proceedings proceedings = e.getProceedings();
				result += proceedings.getInProceedings().size();
			}	
		}
		DatabaseHelper.closeDB();
		System.out.println("The number of in proceedings presented at the conference "+ conferenceName + " was " + result +".");
	}

	//count all people involved in a given conference
	public static void query9() {

	}
	
	public static void query10() {

	}
	
	public static void query11() {

	}

	public static void query12() {

	}
	
	public static void query13() {

	}
	
	public static void query14() {

	}

	@Deprecated
	public static void addInProceeding(InProceedings in, String crossref) {
		pm.currentTransaction().begin();
		if (crossref != "") {
			Query query = pm.newQuery(Proceedings.class, "id == '" + crossref.replaceAll("'", "&#39") + "'");
			Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
			Proceedings proc;
			if (proceedings.isEmpty()) {
				System.out.println("Error: Did not find corresponding Proceeding while attempting to add InProceeding to Database. Crossref: " + crossref);

			} else {
				proc = proceedings.iterator().next();
				in.setProceedings(proc);

			}
		}

		// Handle authors

		List<Person> authors = new ArrayList<Person>();
		for (Person p : in.getAuthors()) {
			Query query4 = pm.newQuery(Person.class, "name == '" + p.getName().replaceAll("'", "&#39") + "'");
			Collection<Person> editorObjects = (Collection<Person>) query4.execute();
			Person author;
			if (editorObjects.isEmpty()) {
				author = p;
				Set<Publication> publications = new HashSet<Publication>();
				publications.add(in);
				author.setAuthoredPublications(publications);

			} else {
				author = editorObjects.iterator().next();
				Set<Publication> publications = author.getAuthoredPublications();
				publications.add(in);
				author.setAuthoredPublications(publications);
			}
			authors.add(author);

		}
		System.out.println("Added InProceeding: " + in.getTitle());

		in.setAuthors(authors);

		pm.makePersistent(in);
		pm.currentTransaction().commit();
	}

	@Deprecated
	public static void addProceeding(Proceedings in, String booktitle, String publisher, String series) {
		pm.setMultithreaded(true);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);
		Query query = pm.newQuery(Conference.class, "name == '" + booktitle.replaceAll("'", "&#39") + "'");
		Collection<Conference> conferences = (Collection<Conference>) query.execute();
		Conference conf;
		if (conferences.isEmpty()) {
			conf = new Conference();
			conf.setName(booktitle);
		} else {
			conf = conferences.iterator().next();
		}
		ConferenceEdition edition = new ConferenceEdition();
		edition.setConference(conf);
		edition.setYear(in.getYear());
		edition.setProceedings(in);
		Set<ConferenceEdition> conferenceEditions = new HashSet<ConferenceEdition>();
		conferenceEditions.add(edition);
		conf.setEditions(conferenceEditions);

		Query query2 = pm.newQuery(Publisher.class, "name == '" + publisher.replaceAll("'", "&#39") + "'");
		Collection<Publisher> publishers = (Collection<Publisher>) query2.execute();
		Publisher pub;
		if (publishers.isEmpty()) {
			pub = new Publisher();
			pub.setName(publisher);
			Set<Publication> publications = new HashSet<Publication>();
			publications.add(in);
			pub.setPublications(publications);

		} else {
			pub = publishers.iterator().next();
			Set<Publication> publications = pub.getPublications();
			publications.add(in);
			pub.setPublications(publications);
		}

		Query query3 = pm.newQuery(Series.class, "name == '" + series.replaceAll("'", "&#39") + "'");
		Collection<Series> seriesObjects = (Collection<Series>) query3.execute();
		Series seriesResult;
		if (seriesObjects.isEmpty()) {
			seriesResult = new Series();
			seriesResult.setName(series);
			Set<Publication> publications = new HashSet<Publication>();
			publications.add(in);
			seriesResult.setPublications(publications);

		} else {
			seriesResult = seriesObjects.iterator().next();
			Set<Publication> publications = seriesResult.getPublications();
			publications.add(in);
			seriesResult.setPublications(publications);
		}

		// Handle authors
		List<Person> editors = new ArrayList<Person>();

		for (Person p : in.getAuthors()) {
			Query query4 = pm.newQuery(Person.class, "name == '" + p.getName().replaceAll("'", "&#39") + "'");
			Collection<Person> editorObjects = (Collection<Person>) query4.execute();
			Person editorResult;
			if (editorObjects.isEmpty()) {
				editorResult = p;
				Set<Publication> publications = new HashSet<Publication>();
				publications.add(in);
				editorResult.setEditedPublications(publications);

			} else {
				editorResult = editorObjects.iterator().next();
				Set<Publication> publications = editorResult.getEditedPublications();
				publications.add(in);
				editorResult.setEditedPublications(publications);
			}
			editors.add(editorResult);

		}

		in.setAuthors(editors);
		in.setSeries(seriesResult);
		in.setPublisher(pub);
		in.setConferenceEdition(edition);
		pm.makePersistent(in);
		pm.currentTransaction().commit();
	}

	@Deprecated
	public static Proceedings addProceedingwithCrossRef(String crossref, InProceedings inproc) {
		pm.currentTransaction().begin();
		Query query = pm.newQuery(Proceedings.class, "id == 'crossref'");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		Proceedings proc;
		if (proceedings.isEmpty()) {
			proc = new Proceedings();
			proc.setId(crossref);
			Set<InProceedings> inprocs = new HashSet<InProceedings>();
			inprocs.add(inproc);
			proc.setInProceedings(inprocs);
		} else {
			proc = proceedings.iterator().next();
			Set<InProceedings> publications = proc.getInProceedings();
			publications.add(inproc);
			proc.setInProceedings(publications);
		}
		pm.makePersistent(proc);
		pm.currentTransaction().commit();
		return proc;
	}

}
