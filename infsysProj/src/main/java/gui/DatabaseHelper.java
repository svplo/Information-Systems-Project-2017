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

	// Find publications by title, returning only a subset of all found publications
	public static void query2(String title, int startOffset, int endOffset) {
		System.out.println("Query 2:");

		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains(t)");
		query.declareParameters("String t");
		query.setRange(":start, :end"); // Parameters should not be declared above

		Collection<Publication> proceedings = (Collection<Publication>) query.execute(title, startOffset, endOffset);
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with title: " + title + " in range: " + startOffset + "-" + endOffset);
		} else {
			Iterator<Publication> itr = proceedings.iterator();
			while (itr.hasNext()) {
				System.out.println(itr.next().getTitle());
			}
		}
		DatabaseHelper.closeDB();
	}

	// Find publications by title, returning only a subset of all found publications ORDERED by title
	public static void query3(String title, int startOffset, int endOffset) {
		System.out.println("Query 3:");

		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains(t)");
		query.declareParameters("String t");
		query.setRange(":start, :end"); // Parameters should not be declared above
		// interpretation: order titles before or after setting the offset?
		query.setOrdering("title ascending");
		Collection<Publication> proceedings = (Collection<Publication>) query.execute(title, startOffset, endOffset);
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with title: " + title + " in range: " + startOffset + "-" + endOffset);
		} else {
			Iterator<Publication> itr = proceedings.iterator();
			while (itr.hasNext()) {
				System.out.println(itr.next().getTitle());
			}
		}
		DatabaseHelper.closeDB();
		}

	//our own sorting algorithm
/*		Iterator<Publication> itr = proceedings.iterator();
*		List<Publication> listOfPublications = new ArrayList<Publication>();
*		for (int i = 0; itr.hasNext(); i++) {
*				listOfPublications.add(itr.next());
*		}
*
*		Collections.sort(listOfPublications, new Comparator<Publication>() {
*
*			public int compare(Publication s1, Publication s2) {
*				return s1.getTitle().compareToIgnoreCase(s2.getTitle());
*			}
*		});
*		for (Publication p : listOfPublications) {
*			System.out.println(p.getTitle());
*		}
*	}
*/

	// returns name of the co-authors of a given author
	public static void query4(String author) {
		System.out.println("Query 4:");

		DatabaseHelper.openDB();
		pm.currentTransaction().begin();
		Collection<Person> result = new HashSet<Person>();

		Query query = pm.newQuery(Person.class);
		query.setFilter("name == a");
		query.declareParameters("String a");
		Collection<Person> auth = (Collection<Person>) query.execute(author);
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

	public static void query5(String author1, String author2) {
		System.out.println("Query 5:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Person.class);
		q1.setFilter("name == ('" + author1.replaceAll("'", "&#39") + "')");
		Collection<Person> authors = (Collection<Person>) q1.execute();
		HashMap<String, Integer> distances = new HashMap<String, Integer>();
		if (authors.isEmpty()) {
			System.out.println("Error: No author with name " + author1 + "found.");
		} else if (authors.size() > 1) {
			System.out.println("Error: Several authors with name " + author1 + "found.");
		} else {
			Iterator<Person> itr = authors.iterator();
			Person author = itr.next(); // get author
			Set<Publication> publications = author.getAuthoredPublications();
			Iterator<Publication> itrPub = publications.iterator();

			Set<Person> coAuthors = new HashSet<Person>();
			while (itrPub.hasNext()) {
				// if (!distances.contains(itrPub.))
				// distance.add()
			}
		}
		System.out.println("The distance between the authors " + author1 + " and " + author2 + " is " + distances.get(author2));
		DatabaseHelper.closeDB();
	}

	// global average of authors / publication (InProceedings + Proceedings)
	public static void query6() {
		System.out.println("Query 6:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Extent <Publication> ext = pm.getExtent(Publication.class);
		double sum = 0.;
		double total = 0.;
		Iterator<Publication> itr = ext.iterator();
		if (itr.hasNext() == false) {
			System.out.println("Error: No publications available.");
		} else {
			while (itr.hasNext()) {
				total += 1;
				sum += itr.next().getAuthors().size();
			}
			System.out.println("Average authors " + (double) (sum / total) + ".");
		}
		DatabaseHelper.closeDB();
	}

	// Returns the number of publications per year between the interval year1 and year 2
	public static void query7(int year1, int year2) {
		System.out.println("Query 7:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Publication.class);
		q1.setFilter("(this.year >= year1) && (this.year <= year2)");
		// TODO: is setGrouping not supported?
		// q1.setGrouping("this.year");
		q1.declareParameters("int year1, int year2");
		Collection<Publication> publications = (Collection<Publication>) q1.execute(year1, year2);
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (publications.isEmpty()) {
			System.out.println("Error: No publications available.");
		} else {
			// group by year
			Iterator<Publication> itr = publications.iterator();
			Publication p;
			int currYear;
			while (itr.hasNext()) {
				p = itr.next();
				currYear = p.getYear();
				if (map.containsKey(currYear)) {
					int current = map.get(currYear);
					map.put(currYear, current + 1);
				} else {
					map.put(currYear, 1);
				}
			}

			// print Hashtable
			System.out.printf("%-12s%-17s\n", "Year", "No. Publications");
			for (int i = year1; i <= year2; i++) {
				System.out.printf("%-12d%-12d\n", i, map.get(i));
			}

		}
		DatabaseHelper.closeDB();
	}

	// No of all publications of a conference, except proceedings
	// That is counting only the inproceedings?
	public static void query8(String conferenceName) {
		System.out.println("Query 8:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		int result = 0;
		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		if (conferences.isEmpty()) {
			System.out.println("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1) {
			System.out.println("Multiple conferences found with name " + conferenceName);
		} else {
			// only interested in 1 conference
			Iterator<Conference> itr = conferences.iterator();
			Collection<ConferenceEdition> editions = itr.next().getEditions();
			Iterator<ConferenceEdition> itrEditions = editions.iterator();
			while (itrEditions.hasNext()) {
				ConferenceEdition e = itrEditions.next();
				Proceedings proceedings = e.getProceedings();
				result += proceedings.getInProceedings().size();
			}
		}
		DatabaseHelper.closeDB();
		System.out.println("The number of in proceedings presented at the conference " + conferenceName + " was " + result + ".");
	}

	// count all people involved in a given conference
	public static void query9(String conferenceName) {
		System.out.println("Query 9:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		if (conferences.isEmpty()) {
			System.out.println("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1) {
			System.out.println("Multiple conferences found with name " + conferenceName);
		} else {
			// only interested in 1 conference
			HashSet<Person> authors = new HashSet<Person>();
			HashSet<Person> editors = new HashSet<Person>();
			Iterator<Conference> itr = conferences.iterator();
			Collection<ConferenceEdition> editions = itr.next().getEditions();
			Iterator<ConferenceEdition> itrEditions = editions.iterator();
			while (itrEditions.hasNext()) {
				ConferenceEdition e = itrEditions.next();
				Proceedings proc = e.getProceedings();
				editors.addAll(proc.getAuthors());
				Set<InProceedings> inproceedings = proc.getInProceedings();
				Iterator<InProceedings> inProcItr = inproceedings.iterator();
				while (inProcItr.hasNext()) {
					InProceedings inProc = inProcItr.next();
					Iterator<Person> inProcAuthorsItr = inProc.getAuthors().iterator();
					while (inProcAuthorsItr.hasNext()) {
						Person auth = inProcAuthorsItr.next();
							authors.add(auth);
					}
				}
			}
			int result = authors.size() + editors.size();
			System.out.println("Number of authors and editors at the conference " + conferenceName + " was " + result + ".");
		}
		DatabaseHelper.closeDB();
	}

	public static void query10(String conferenceName) {
		System.out.println("Query 10:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		if (conferences.isEmpty()) {
			System.out.println("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1) {
			System.out.println("Multiple conferences found with name " + conferenceName);
		} else {
			// only interested in 1 conference
			HashSet<Person> authors = new HashSet<Person>();
			HashSet<Person> editors = new HashSet<Person>();
			Iterator<Conference> itr = conferences.iterator();
			Collection<ConferenceEdition> editions = itr.next().getEditions();
			Iterator<ConferenceEdition> itrEditions = editions.iterator();
			while (itrEditions.hasNext()) {
				ConferenceEdition e = itrEditions.next();
				Proceedings proc = e.getProceedings();
				editors.addAll(proc.getAuthors());
				Set<InProceedings> inproceedings = proc.getInProceedings();
				Iterator<InProceedings> inProcItr = inproceedings.iterator();
				while (inProcItr.hasNext()) {
					InProceedings inProc = inProcItr.next();
					Iterator<Person> inProcAuthorsItr = inProc.getAuthors().iterator();
					while (inProcAuthorsItr.hasNext()) {
						Person auth = inProcAuthorsItr.next();
							authors.add(auth);
					}

				}
			}
			System.out.println("List of all authors and editors at the conference " + conferenceName + ":");
			System.out.println("\nAuthors:");
			for (Person p : authors) {
				System.out.println(p.getName());
			}
			System.out.println("\nEditors:");
			for (Person p : editors) {
				System.out.println(p.getName());
			}

		}
		DatabaseHelper.closeDB();

	}

	public static void query11(String conferenceName) {
		System.out.println("Query 11:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		if (conferences.isEmpty()) {
			System.out.println("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1) {
			System.out.println("Multiple conferences found with name " + conferenceName);
		} else {
			// only interested in 1 conference
			Iterator<Conference> itr = conferences.iterator();
			Collection<ConferenceEdition> editions = itr.next().getEditions();
			Iterator<ConferenceEdition> itrEditions = editions.iterator();
			List<InProceedings> allPublications = new ArrayList<InProceedings>();
			while (itrEditions.hasNext()) {
				ConferenceEdition e = itrEditions.next();
				Proceedings proc = e.getProceedings();
				Set<InProceedings> inproceedings = proc.getInProceedings();
				allPublications.addAll(inproceedings);
			}
			System.out.println("List of all Publications at the conference " + conferenceName + ":");
			for (InProceedings p : allPublications) {
				System.out.println(p.getTitle());
			}
		}
		DatabaseHelper.closeDB();
	}

	public static void query12() {

		System.out.println("Query 12:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
		List<Person> editorAndAuthor = new ArrayList<Person>();
		for (Proceedings p : ext) {
			List<Person> authors = p.getAuthors();
			for (InProceedings inP : p.getInProceedings()) {
				for (Person editor : inP.getAuthors()) {
					if (authors.contains(editor) && !editorAndAuthor.contains(editor)) {
						editorAndAuthor.add(editor);
					}
				}
			}

		}
		ext.closeAll();
		System.out.println("List of " + editorAndAuthor.size() + " authors that are also editor in the corresponding proceeding.");
		for (Person p : editorAndAuthor) {
			System.out.println(p.getName());
		}
		DatabaseHelper.closeDB();
	}

	public static void query13(String authorName) {
		System.out.println("Query 13:");
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Person.class, "name == authName");
		q1.declareParameters("String authName");
		Collection<Person> authors = (Collection<Person>) q1.execute(authorName);
		if (authors.isEmpty()) {
			System.out.println("No author found with name " + authorName);
		} else if (authors.size() > 1) {
			System.out.println("Multiple authors found with name " + authorName);
		} else {
			Iterator<Person> itrPers = authors.iterator();
			Query q2 = pm.newQuery(InProceedings.class, "this.authors.contains(authPerson)");
			q2.declareParameters("Person authPerson");
			Person aPerson = itrPers.next();
			Collection<Publication> pub = (Collection<Publication>) q2.execute(aPerson);
			List<Publication> result = new ArrayList<Publication>();
			Iterator<Publication> pubItr = pub.iterator();
			while (pubItr.hasNext()) {
				Publication i = pubItr.next();
				Person lastAuthor = i.getAuthors().get(i.getAuthors().size() - 1);
				if (lastAuthor.equals(aPerson)) {
					result.add(i);
				}
			}
			System.out.println("List of " + result.size() + " publications where " + authorName + " was listed as last author.");
			for (Publication p : result) {
				System.out.println(p.getTitle());
			}

		}

		DatabaseHelper.closeDB();

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
