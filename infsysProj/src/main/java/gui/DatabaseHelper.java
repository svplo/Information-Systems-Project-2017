package gui;

import java.io.IOException;
import java.io.PrintWriter;
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

	public static void UpdateProceedings(String proceedingsID, String title, int year, String elect, String note, int number, String publisher, String volume, String isbn, String series, String confEdition) {
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class, "id == '" + proceedingsID.replaceAll("'", "&#39") + "'");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		Proceedings proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingsID);

		} else {
			proc = proceedings.iterator().next();
			proc.setTitle(title);
			proc.setYear(year);
			// authors
			proc.setElectronicEdition(elect);
			proc.setNote(note);
			proc.setNumber(number);
			proc.getPublisher().setName(publisher);
			proc.setVolume(volume);
			proc.setIsbn(isbn);
			proc.getSeries().setName(series);
			proc.getConferenceEdition().getConference().setName(confEdition);
			// InProceedings

			pm.makePersistent(proc);

		}
		pm.currentTransaction().commit();

		DatabaseHelper.closeDB();

	}

	public static void DeleteProceeding(String proceedingsID) {
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class, "id == '" + proceedingsID.replaceAll("'", "&#39") + "'");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		Proceedings proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingsID);

		} else {
			proc = proceedings.iterator().next();
			pm.deletePersistent(proc);
		}
		pm.currentTransaction().commit();

		DatabaseHelper.closeDB();
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

	public static Collection<Proceedings> getAllProceedings() {
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

<<<<<<< HEAD
		} else {
			//result = inProceedings.iterator().next().getProceedings().getTitle();
		}

		DatabaseHelper.closeDB();
		return result;
	}
	
	public static String getPublisherName(String proceedingsID){
=======
	public static String getPublisherName(String proceedingsID) {
>>>>>>> 4ae025a8ac88d482bf76381b5d8298fa13d76839
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class, "id == '" + proceedingsID.replaceAll("'", "&#39") + "'");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		String result = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingsID);

		} else {
			result = proceedings.iterator().next().getPublisher().getName();
		}

		DatabaseHelper.closeDB();
		return result;
	}

	public static String getSeriesName(String proceedingsID) {
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class, "id == '" + proceedingsID.replaceAll("'", "&#39") + "'");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		String result = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingsID);

		} else {
			result = proceedings.iterator().next().getSeries().getName();
		}

		DatabaseHelper.closeDB();
		return result;
	}

	public static String getConferenceName(String proceedingsID) {
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class, "id == '" + proceedingsID.replaceAll("'", "&#39") + "'");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		String result = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingsID);

		} else {
			result = proceedings.iterator().next().getConferenceEdition().getConference().getName();
		}

		DatabaseHelper.closeDB();
		return result;
	}

	public static List<String> getInProceedingsOfProceedings(String proceedingsID) {
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class, "id == '" + proceedingsID.replaceAll("'", "&#39") + "'");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		List<String> result = new ArrayList<String>();
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingsID);

		} else {
			for (InProceedings i : proceedings.iterator().next().getInProceedings()) {
				result.add(i.getTitle());
			}
		}

		DatabaseHelper.closeDB();
		return result;
	}

	public static List<String> getAuthoredPublicationsForPerson(String personName) {
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name == '" + personName.replaceAll("'", "&#39") + "'");
		Collection<Person> persons = (Collection<Person>) query.execute();
		List<String> result = new ArrayList<String>();
		if (persons.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);

		} else {
			for (Publication i : persons.iterator().next().getAuthoredPublications()) {
				result.add(i.getTitle());
			}
		}

		DatabaseHelper.closeDB();
		return result;
	}

	public static List<String> getEditedPublicationsForPerson(String personName) {
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name == '" + personName.replaceAll("'", "&#39") + "'");
		Collection<Person> persons = (Collection<Person>) query.execute();
		List<String> result = new ArrayList<String>();
		if (persons.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);

		} else {
			for (Publication i : persons.iterator().next().getEditedPublications()) {
				result.add(i.getTitle());
			}
		}

		DatabaseHelper.closeDB();
		return result;
	}

	public static Collection<InProceedings> getAllInProceedings() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<InProceedings> ext = pm.getExtent(InProceedings.class);
		List<InProceedings> allPublications = new ArrayList<InProceedings>();
		for (InProceedings p : ext) {
			allPublications.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPublications;
	}

	public static List<Publication> searchForPublication(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Publication> proceedings = (Collection<Publication>) query.execute();
		List<Publication> allPublications = new ArrayList<Publication>(proceedings);
		Publication proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public static List<Person> searchForPeople(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Person> proceedings = (Collection<Person>) query.execute();
		List<Person> allPublications = new ArrayList<Person>(proceedings);
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public static List<Publication> searchForProceedings(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class, "title.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		List<Publication> allPublications = new ArrayList<Publication>(proceedings);
		Proceedings proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public static List<Publication> searchForInProceedings(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(InProceedings.class, "title.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<InProceedings> proceedings = (Collection<InProceedings>) query.execute();
		List<Publication> allPublications = new ArrayList<Publication>(proceedings);
		InProceedings proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public static void deletePerson(String personName) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name == '" + personName.replaceAll("'", "&#39") + "'");
		Collection<Person> proceedings = (Collection<Person>) query.execute();
		List<Person> allPublications = new ArrayList<Person>(proceedings);
		Person p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);
		} else {
			p = allPublications.iterator().next();
			pm.deletePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public static void updatePerson(String personName, String newPersonName, List<String> newAuthoredPublications, List<String> newEditedPublications) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);
		
		Query query = pm.newQuery(Person.class);
		query.setFilter("name == a");
		query.declareParameters("String a");

		Collection<Person> proceedings = (Collection<Person>) query.execute(personName);
		List<Person> allPublications = new ArrayList<Person>(proceedings);
		Person p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);
		} else {
			p = allPublications.iterator().next();
			p.setName(newPersonName);
			Set<Publication> autPubs = new HashSet<Publication>();
			for (String s : newAuthoredPublications) {
				Query query1 = pm.newQuery(InProceedings.class);
				query1.setFilter("title == a");
				query1.declareParameters("String a");

				Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query1.execute(s);
				if (proceedings1.isEmpty()) {
					System.out.println("Error: Did not Find InProceedings: " + s);
				} else {
					InProceedings inProc = proceedings1.iterator().next();
					autPubs.add(inProc);
					List<Person> authors = inProc.getAuthors();
					authors.add(p);
					inProc.setAuthors(authors);
				}
			}
			Set<Publication> editPubs = new HashSet<Publication>();

			for (String s : newEditedPublications) {
				
				Query query1 = pm.newQuery(Proceedings.class);
				query1.setFilter("title == a");
				query1.declareParameters("String a");

				Collection<Proceedings> proceedings1 = (Collection<Proceedings>) query1.execute(s);
				if (proceedings1.isEmpty()) {
					System.out.println("Error: Did not Find Proceedings: " + s);
				} else {
					Proceedings proc = proceedings1.iterator().next();
					editPubs.add(proc);
					List<Person> authors = proc.getAuthors();
					authors.add(p);
					proc.setAuthors(authors);

				}
			}

			p.setEditedPublications(editPubs);
			p.setAuthoredPublications(autPubs);
			pm.makePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public static void addPerson(String newPersonName, List<String> newAuthoredPublications, List<String> newEditedPublications) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);
		Person p = new Person(newPersonName);
		Set<Publication> autPubs = new HashSet<Publication>();
		for (String s : newAuthoredPublications) {
			Query query1 = pm.newQuery(InProceedings.class);
			query1.setFilter("title == a");
			query1.declareParameters("String a");

			Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query1.execute(s);
			if (proceedings1.isEmpty()) {
				System.out.println("Error: Did not Find InProceedings: " + s);
			} else {
				InProceedings inProc = proceedings1.iterator().next();
				autPubs.add(inProc);
				List<Person> authors = inProc.getAuthors();
				authors.add(p);
				inProc.setAuthors(authors);
			}
		}
		Set<Publication> editPubs = new HashSet<Publication>();

		for (String s : newEditedPublications) {
			
			Query query1 = pm.newQuery(Proceedings.class);
			query1.setFilter("title == a");
			query1.declareParameters("String a");

			Collection<Proceedings> proceedings1 = (Collection<Proceedings>) query1.execute(s);
			if (proceedings1.isEmpty()) {
				System.out.println("Error: Did not Find Proceedings: " + s);
			} else {
				Proceedings proc = proceedings1.iterator().next();
				editPubs.add(proc);
				List<Person> authors = proc.getAuthors();
				authors.add(p);
				proc.setAuthors(authors);
			}
		}

		p.setEditedPublications(editPubs);
		p.setAuthoredPublications(autPubs);
		pm.makePersistent(p);

		pm.currentTransaction().commit();
		closeDB(pm);

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

	public static Collection<Person> getAllPeople() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Person> ext = pm.getExtent(Person.class);
		List<Person> allPeople = new ArrayList<Person>();
		for (Person p : ext) {
			allPeople.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPeople;
	}

	// find publication by key (according to xml file)
	public static void query1(String id) {
		String thisQuery = "Query 1";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "id == '" + id.replaceAll("'", "&#39") + "'");
		Collection<Publication> publication = (Collection<Publication>) query.execute();
		Publication pub;
		try {
			PrintWriter writer = new PrintWriter(thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (publication.isEmpty()) {
				System.out.println("Error: Did not find a publication with ID: " + id);
			} else {
				pub = publication.iterator().next();
				writer.println("The title of the publication with id " + id + " is " + pub.getTitle() + ".");
				writer.println(pm.getObjectId(pub));
				writer.close();
			}
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// find publication by object id (database)
	// try 4197 (note: solution changes every time you reload database)
	public static void query1Alter(Integer oid) {
		String thisQuery = "Query 1 (alternative)";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Publication pub = (Publication) pm.getObjectById(oid.longValue());
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (pub == null) {
				writer.println("Error: Did not find a publication with ID: " + oid);
			} else {
				writer.println("The title of the publication with id " + oid + " is " + pub.getTitle() + ".");
				writer.println("The key of the publication is " + pub.getId() + ".");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// Find publications by title, returning only a subset of all found publications
	public static void query2(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 2";

		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains(t)");
		query.declareParameters("String t");
		query.setRange(":start, :end"); // Parameters should not be declared above

		Collection<Publication> proceedings = (Collection<Publication>) query.execute(title, startOffset, endOffset);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (proceedings.isEmpty()) {
				writer.println("Error: Did not find a publication with title: " + title + " in range: " + startOffset + "-" + endOffset);
			} else {
				Iterator<Publication> itr = proceedings.iterator();
				while (itr.hasNext()) {
					writer.println(itr.next().getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// Find publications by title, returning only a subset of all found publications ORDERED by title
	public static void query3(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 3";

		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains(t)");
		query.declareParameters("String t");
		query.setRange(":start, :end"); // Parameters should not be declared above
		// interpretation: order titles before or after setting the offset?
		query.setOrdering("title ascending");
		Collection<Publication> proceedings = (Collection<Publication>) query.execute(title, startOffset, endOffset);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (proceedings.isEmpty()) {
				writer.println("Error: Did not find a publication with title: " + title + " in range: " + startOffset + "-" + endOffset);
			} else {
				Iterator<Publication> itr = proceedings.iterator();
				while (itr.hasNext()) {
					writer.println(itr.next().getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// our own sorting algorithm, for sorting afterwards
	/*
	 * Iterator<Publication> itr = proceedings.iterator(); List<Publication> listOfPublications = new ArrayList<Publication>(); for (int i = 0; itr.hasNext(); i++) { listOfPublications.add(itr.next()); }
	 *
	 * Collections.sort(listOfPublications, new Comparator<Publication>() {
	 *
	 * public int compare(Publication s1, Publication s2) { return s1.getTitle().compareToIgnoreCase(s2.getTitle()); } }); for (Publication p : listOfPublications) { System.out.println(p.getTitle()); } }
	 */

	// returns name of the co-authors of a given author
	public static void query4(String author) {
		String thisQuery = "Query 4";

		DatabaseHelper.openDB();
		pm.currentTransaction().begin();
		Collection<Person> result = new HashSet<Person>();

		Query query = pm.newQuery(Person.class);
		query.setFilter("name == a");
		query.declareParameters("String a");
		Collection<Person> auth = (Collection<Person>) query.execute(author);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (auth.size() == 1) {
				for (Person p : auth) {
					Collection<Publication> publications = p.getAuthoredPublications();
					publications.addAll(p.getEditedPublications());
					result.add(p);
					if (publications.isEmpty()) {
						writer.println("Error: Did not find any publication with an author named: " + author);
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
									writer.println(currentPers.getName());
								} else {
									// don't print author's name or any duplicates
								}
							}
						}
					}
				}
			} else if (auth.size() > 1) {
				writer.println("Error: There are several persons whose name equals: " + author);
			} else {
				writer.println("Error: Did not find any person named: " + author);
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// distance between 2 authors
	public static void query5(String author1, String author2) {
		String thisQuery = "Query 5";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		String resultStr = "";
		Query q1 = pm.newQuery(Person.class);
		q1.declareParameters("String authorName");
		q1.setFilter("name == authorName");
		Collection<Person> authors1 = (Collection<Person>) q1.execute(author1);
		Query q2 = pm.newQuery(Person.class);
		q2.declareParameters("String authorName");
		q2.setFilter("name == authorName");
		Collection<Person> authors2 = (Collection<Person>) q2.execute(author2);
		// map to store distances
		HashMap<Person, Integer> distancesMap = new HashMap<Person, Integer>();
		// indicates if we found a path
		Boolean found = false;
		// stores object of author2
		Person authorTwo = null;
		if (authors1.isEmpty()) {
			resultStr = ("Error: No author with name " + author1 + "found.");
		} else if (authors1.size() > 1) {
			resultStr = ("Error: Several authors with name " + author1 + "found.");
		} else if (authors2.isEmpty()) {
			resultStr = ("Error: No author found with name " + author2 + ".");
		} else if (authors2.size() > 1) {
			resultStr = ("Error: Several authors with name " + author2 + "found.");
		} else {
			Iterator<Person> itr1 = authors1.iterator();
			Person authorOne = itr1.next(); // get corresponding object of author1
			Iterator<Person> itr2 = authors2.iterator();
			authorTwo = itr2.next(); // get author2

			Set<Publication> pubOfAuthor1 = authorOne.getAuthoredPublications();
			HashSet<Publication> visitedPublications = new HashSet<Publication>();
			HashSet<Publication> toVisitPublications = new HashSet<Publication>();
			HashSet<Publication> tempPublications = new HashSet<Publication>();
			toVisitPublications.addAll(pubOfAuthor1);
			Iterator<Publication> itrPub = toVisitPublications.iterator();
			int distance = 1;
			List<Person> coAuthors = new ArrayList<Person>();
			Publication currentPub = null;
			// hint: this loop can take very long
			// int no = 0;
			while ((!toVisitPublications.isEmpty())) {
				// no++;
				// if (no == 10){
				// break;
				// }
				// reset itrPub, because toVisitPublications has changed
				itrPub = toVisitPublications.iterator();
				while (itrPub.hasNext()) {
					currentPub = itrPub.next();
					coAuthors.addAll(currentPub.getAuthors());
					for (Person a : coAuthors) {
						distancesMap.putIfAbsent(a, distance);
						if (a == authorTwo) {
							found = true;
							break;
						}
						for (Publication p : a.getAuthoredPublications()) {
							if ((!visitedPublications.contains(p)) || (!toVisitPublications.contains(p))) {
								tempPublications.add(p);
							}
						}
					}
					if (found == true) {
						break;
					}
					visitedPublications.add(currentPub);
				}
				if (found == true) {
					break;
				}
				// add publications of co-authors
				toVisitPublications.addAll(tempPublications);
				tempPublications.clear();
				// remove the publications we have already visited (--> avoid loops)
				for (Publication p : visitedPublications) {
					toVisitPublications.remove(p);
				}
				distance++;
			}
		}
		if (found == true) {
			resultStr = ("The distance between the authors " + author1 + " and " + author2 + " is " + distancesMap.get(authorTwo) + ".");
		} else {
			resultStr = ("The authors " + author1 + " and " + author2 + " are not connected.");
		}
		DatabaseHelper.closeDB();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println(resultStr);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
	}

	// global average of authors / publication (InProceedings + Proceedings)
	public static void query6() {
		String thisQuery = "Query 6";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Extent<Publication> ext = pm.getExtent(Publication.class);
		double sum = 0.;
		double total = 0.;
		Iterator<Publication> itr = ext.iterator();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (itr.hasNext() == false) {
				writer.println("Error: No publications available.");
			} else {
				while (itr.hasNext()) {
					total += 1;
					sum += itr.next().getAuthors().size();
				}
				writer.println("Average authors " + (double) (sum / total) + ".");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// Returns the number of publications per year between the interval year1 and year 2
	public static void query7(int year1, int year2) {
		String thisQuery = "Query 7";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Publication.class);
		q1.setFilter("(this.year >= year1) && (this.year <= year2)");
		// TODO: is setGrouping not supported?
		// q1.setGrouping("this.year");
		q1.declareParameters("int year1, int year2");
		Collection<Publication> publications = (Collection<Publication>) q1.execute(year1, year2);
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (publications.isEmpty()) {
				writer.println("Error: No publications available.");
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
				writer.printf("%-12s%-17s\n", "Year", "No. Publications");
				for (int i = year1; i <= year2; i++) {
					writer.printf("%-12d%-12d\n", i, map.get(i));
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// No of all publications of a conference, except proceedings
	public static void query8(String conferenceName) {
		String thisQuery = "Query 8";
		String resultStr = "";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		int result = 0;
		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		if (conferences.isEmpty()) {
			resultStr = ("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1) {
			resultStr = ("Multiple conferences found with name " + conferenceName);
		} else {
			// only interested in 1 conference
			Iterator<Conference> itr = conferences.iterator();
			Collection<ConferenceEdition> editions = itr.next().getEditions();
			Iterator<ConferenceEdition> itrEditions = editions.iterator();
			// System.out.println(editions.size()); // I think it should be two, 1979 and 1983
			while (itrEditions.hasNext()) {
				ConferenceEdition e = itrEditions.next();
				// System.out.println(e.getYear()); // only finds 1979, solution prints all 20 proceedings from this edition, but non of the 1983 publications
				// System.out.println(e.getConference().getId());
				Proceedings proceedings = e.getProceedings();
				result += proceedings.getInProceedings().size();
			}
			resultStr = ("The number of in proceedings presented at the conference " + conferenceName + " was " + result + ".");
		}
		DatabaseHelper.closeDB();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println(resultStr);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

	}

	// count all people involved in a given conference
	public static void query9(String conferenceName) {
		String thisQuery = "Query 9";
		String resultStr = "";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		if (conferences.isEmpty()) {
			resultStr = ("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1) {
			resultStr = ("Multiple conferences found with name " + conferenceName);
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
			resultStr = ("Number of authors and editors at the conference " + conferenceName + " was " + result + ".");
		}
		DatabaseHelper.closeDB();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println(resultStr);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
	}

	// all authors of conferenceName
	public static void query10(String conferenceName) {
		String thisQuery = "Query 10";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (conferences.isEmpty()) {
				writer.println("No conference found with name " + conferenceName);
			} else if (conferences.size() > 1) {
				writer.println("Multiple conferences found with name " + conferenceName);
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
				writer.println("List of all authors and editors at the conference " + conferenceName + ":");
				writer.println("\nAuthors:");
				for (Person p : authors) {
					writer.println(p.getName());
				}
				writer.println("\nEditors:");
				for (Person p : editors) {
					writer.println(p.getName());
				}

			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();

	}

	// all publications of conferenceName
	public static void query11(String conferenceName) {
		String thisQuery = "Query 11";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (conferences.isEmpty()) {
				writer.println("No conference found with name " + conferenceName);
			} else if (conferences.size() > 1) {
				writer.println("Multiple conferences found with name " + conferenceName);
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
				writer.println("List of all Publications at the conference " + conferenceName + ":");
				for (InProceedings p : allPublications) {
					writer.println(p.getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// list of persons which were authors of an in proceeding and editor of the corresponding proceeding.
	public static void query12() {
		String thisQuery = "Query 12";
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
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of " + editorAndAuthor.size() + " authors that are also editor in the corresponding proceeding.");
			for (Person p : editorAndAuthor) {
				writer.println(p.getName());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// list of in proceedings where the given author appears as the last author
	public static void query13(String authorName) {
		String thisQuery = "Query 13";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Person.class, "name == authName");
		q1.declareParameters("String authName");
		Collection<Person> authors = (Collection<Person>) q1.execute(authorName);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (authors.isEmpty()) {
				writer.println("No author found with name " + authorName);
			} else if (authors.size() > 1) {
				writer.println("Multiple authors found with name " + authorName);
			} else {
				Iterator<Person> itrPers = authors.iterator();
				Query q2 = pm.newQuery(InProceedings.class, "this.authors.contains(authPerson)");
				q2.declareParameters("infsysProj.infsysProj.Person authPerson");
				Person aPerson = itrPers.next();
				Collection<InProceedings> pub = (Collection<InProceedings>) q2.execute(aPerson);
				List<InProceedings> result = new ArrayList<InProceedings>();
				Iterator<InProceedings> pubItr = pub.iterator();
				while (pubItr.hasNext()) {
					InProceedings i = pubItr.next();
					Person lastAuthor = i.getAuthors().get(i.getAuthors().size() - 1);
					if (lastAuthor.equals(aPerson)) {
						result.add(i);
					}
				}
				writer.println("List of " + result.size() + " in proceeding(s) where " + authorName + " was listed as last author.");
				for (InProceedings p : result) {
					writer.println(p.getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

	// list of publishers (Proceedings) whose authors appear in any in proceedings between year1 and year2
	public static void query14(int year1, int year2) {
		String thisQuery = "Query 14";
		DatabaseHelper.openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(InProceedings.class);
		q1.setFilter("(this.year >= year1) && (this.year <= year2)");
		q1.declareParameters("int year1, int year2");
		Collection<InProceedings> inProcs = (Collection<InProceedings>) q1.execute(year1, year2);
		HashSet<Person> setOfAuthors = new HashSet<Person>();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (inProcs.isEmpty()) {
				writer.println("Error: No in proceedings available.");
			} else {
				// create list of authors
				Iterator<InProceedings> itr = inProcs.iterator();
				InProceedings inP;
				while (itr.hasNext()) {
					inP = itr.next();
					setOfAuthors.addAll(inP.getAuthors());
				}
				// result set
				HashSet<Publisher> result = new HashSet<Publisher>();
				Extent<Proceedings> proceedings = pm.getExtent(Proceedings.class);
				for (Proceedings proc : proceedings) {
					if (setOfAuthors.containsAll(proc.getAuthors())) {
						result.add(proc.getPublisher());
					}
				}
				// print list of publishers
				writer.println("List of publishers of proceedings whose authors appear in any in proceedings which were published between " + year1 + " and " + year2 + ".");
				for (Publisher p : result) {
					writer.println(p.getName());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		DatabaseHelper.closeDB();
	}

}
