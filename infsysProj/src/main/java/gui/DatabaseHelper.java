package gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class DatabaseHelper {
	private static MongoDatabase database;
	private static String dbStandardName = "TheNoSQLDatabase";
	private static MongoClient mongoClient;

	// source: http://mongodb.github.io/mongo-java-driver/3.0/driver/getting-started/quick-tour/
	public static void connectToDB() {
		// if using the default port
		//mongoClient = new MongoClient();
		// MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		Builder builder = MongoClientOptions.builder().connectTimeout(3000);  
		mongoClient = new MongoClient( "localhost" , 27017 );
		try {
			mongoClient.getAddress();
		} catch (Exception e) {
		    System.out.println("Database unavailable!");
		    mongoClient.close();
		    return;
		}

		//DB db = mongoClient.getDB( "mydb" );
	}

	public static void closeConnectionDB() {
		mongoClient.close();
	}

	public static void createDB() {
		// database will be created automatically if it does not exist
			database = mongoClient.getDatabase(dbStandardName);
	}

/*	private static void closeDB(DB databaseManager) {
		if (databaseManager.currentTransaction().isActive()) {
			databaseManager.currentTransaction().rollback();
		}
		databaseManager.close();
		databaseManager.getPersistenceManagerFactory().close();
	}

	// TODO

	public static void DeleteProceeding(String proceedingName) {
		DatabaseHelper.connectToDB();
		database.currentTransaction().begin();
		database.currentTransaction().setRetainValues(true);

		Query query = database.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(proceedingName);
		Proceedings proc = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingName);

		} else {
			proc = proceedings.iterator().next();
			for (InProceedings in : proc.getInProceedings()) {
				in.setProceedings(null);
			}
			for (Person p : proc.getAuthors()) {

				Set<Publication> oldAut = p.getEditedPublications();
				Set<Publication> newAut = new HashSet<Publication>();
				for (Publication ufhe : oldAut) {
					if (!ufhe.getTitle().equals(proc.getTitle())) {
						System.out.println("added" + ufhe.getTitle());
						newAut.add(ufhe);
					}
				}
				p.setEditedPublications(newAut);
				database.makePersistent(p);

			}

			database.deletePersistent(proc);
		}
		database.currentTransaction().commit();

		DatabaseHelper.closeConnectionDB();
	}
*/
	public static MongoCollection<Document> getAllPublications() {
		connectToDB();
		MongoCollection<Document> collection = database.getCollection("Publications");
		closeConnectionDB();
		return collection;
	}

	public static MongoCollection<Document> getAllProceedings() {
		connectToDB();
		MongoCollection<Document> collection = database.getCollection("Proceedings");
		closeConnectionDB();
		return collection;
	}

	public static String getPublisherName(String proceedingName) {
		DatabaseHelper.connectToDB();

		DatabaseHelper.closeConnectionDB();
		return "";
	}

	public static String getSeriesName(String proceedingName) {
		DatabaseHelper.connectToDB();

		DatabaseHelper.closeConnectionDB();
		return "";
	}

	public static String getConferenceName(String proceedingName) {
		DatabaseHelper.connectToDB();

		DatabaseHelper.closeConnectionDB();
		return "";
	}

	public static String getConferenceEditionName(ConferenceEdition proceedingName) {
		DatabaseHelper.connectToDB();

		DatabaseHelper.closeConnectionDB();
		return "";
	}

	public static String getConferenceEditionProceeding(ConferenceEdition proceedingName) {
		DatabaseHelper.connectToDB();
		
		DatabaseHelper.closeConnectionDB();
		return "";
	}

	public static String getConferenceYear(String proceedingName) {
		DatabaseHelper.connectToDB();


		Collection<Proceedings> proceedings = new HashSet<Proceedings>();

		String result = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with name: " + proceedingName);

		} else {
			result = String.valueOf(proceedings.iterator().next().getConferenceEdition().getYear());
		}

		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static List<String> getAuthorsOfProceedings(String proceedingsName) {
		DatabaseHelper.connectToDB();

		Collection<Proceedings> proceedings = new HashSet<Proceedings>();
		List<String> result = new ArrayList<String>();
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingsName);

		} else {
			for (Person i : proceedings.iterator().next().getAuthors()) {
				result.add(i.getName());
			}
		}
		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static List<String> getInProceedingsOfProceedings(String proceedingName) {
		DatabaseHelper.connectToDB();

		MongoCollection<Document> proceedings = database.getCollection("Proceedings");
		List<String> result = new ArrayList<String>();
		if (proceedings.count() == 0l) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingName);

		} else {
			FindIterable<Document> cursor = proceedings.find();
			for (InProceedings i : ((Proceedings) ((Iterator<DBObject>) cursor).next()).getInProceedings()) {
				result.add(i.getTitle());
			}
		}

		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static List<String> getAuthoredPublicationsForPerson(String personName) {
		DatabaseHelper.connectToDB();

		Collection<Person> persons = new HashSet<Person>();
		List<String> result = new ArrayList<String>();
		if (persons.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);

		} else {
			for (Publication i : persons.iterator().next().getAuthoredPublications()) {
				result.add(i.getTitle());
			}
		}

		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static List<String> getEditedPublicationsForPerson(String personName) {
		DatabaseHelper.connectToDB();
		
		Collection<Person> persons = new HashSet<Person>();
		List<String> result = new ArrayList<String>();
		if (persons.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);

		} else {
			for (Publication i : persons.iterator().next().getEditedPublications()) {
				result.add(i.getTitle());
			}
		}

		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static MongoCollection<Document> getAllInProceedings() {
		connectToDB();
		MongoCollection<Document> collection = database.getCollection("InProceedings");
		closeConnectionDB();
		return collection;
	}

	public static List<Publication> searchForPublication(String search) {
		connectToDB();

		Collection<Publication> publication = new HashSet<Publication>();
		List<Publication> allPublications = new ArrayList<Publication>(publication);
		Publication proc;
		if (publication.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		closeConnectionDB();

		return allPublications;

	}

	public static List<Person> searchForPeople(String search) {
		connectToDB();

		List<Person> people = new ArrayList<Person>();
		if (people.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		closeConnectionDB();;

		return people;

	}

	public static List<Conference> searchForConference(String search) {
		connectToDB();

		List<Conference> conference = new ArrayList<Conference>();
		closeConnectionDB();

		return conference;
	}

	public static List<Series> searchForSeries(String search) {
		connectToDB();

		List<Series> series = new ArrayList<Series>();
		
		closeConnectionDB();

		return series;
	}

	public static List<ConferenceEdition> searchForConferenceEdition(String search) {
		connectToDB();

		List<ConferenceEdition> confEdition = new ArrayList<ConferenceEdition>();
		
		closeConnectionDB();

		return confEdition;
	}

	public static List<Publisher> searchForPublisher(String search) {
		connectToDB();

		List<Publisher> publisher = new ArrayList<Publisher>();
		
		closeConnectionDB();

		return publisher;
	}

	public static MongoCollection<Document> searchForProceedings(String search) {
		connectToDB();

		MongoCollection<Document> publication = database.getCollection("Proceedings");
		
		closeConnectionDB();

		return publication;
	}

	public static List<Publication> searchForInProceedings(String search) {
		connectToDB();

		List<Publication> publication = new ArrayList<Publication>();
		
		closeConnectionDB();

		return publication;
	}

/*	public static void deletePerson(String personName) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name == '" + personName.replaceAll("'", "&#39") + "'");
		Collection<Person> proceedings = (Collection<Person>) query.execute();
		Person p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with name: " + personName);
		} else {
			p = proceedings.iterator().next();
			for (Publication publ : p.getAuthoredPublications()) {

				List<Person> oldAut = publ.getAuthors();
				List<Person> newAut = new ArrayList<Person>();
				for (Person ufhe : oldAut) {
					if (!ufhe.getName().equals(p.getName())) {
						newAut.add(ufhe);
					}
				}
				publ.setAuthors(newAut);
				pm.makePersistent(p);

			}

			for (Publication publ : p.getEditedPublications()) {

				List<Person> oldAut = publ.getAuthors();
				List<Person> newAut = new ArrayList<Person>();
				for (Person ufhe : oldAut) {
					if (!ufhe.getName().equals(p.getName())) {
						newAut.add(ufhe);
					}
				}
				publ.setAuthors(newAut);
				pm.makePersistent(p);

			}

			pm.deletePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public static void updatePerson(String personName, String newPersonName, List<String> newAuthoredPublications, List<String> newEditedPublications) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		/*
		 * Query query0 = pm.newQuery(Person.class); query0.setFilter("name == a"); query0.declareParameters("String a");
		 * 
		 * Collection<Person> proceedings10 = (Collection<Person>) query0.execute(newPersonName); if (!proceedings10.isEmpty()) { System.out.println("There already exists a Person with name: " + newPersonName); pm.currentTransaction().commit(); closeDB(pm); return; }
		 */
/*		Query query = pm.newQuery(Person.class);
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

			for (Publication oldPub : p.getAuthoredPublications()) {
				List<Person> oldSet = oldPub.getAuthors();
				List<Person> newSet = new ArrayList<Person>();
				for (Person ufhe : oldSet) {
					if (!ufhe.getName().equals(p.getName())) {
						newSet.add(ufhe);
					}
				}
				oldPub.setAuthors(newSet);
				pm.makePersistent(oldPub);
			}

			for (Publication oldPub : p.getEditedPublications()) {
				List<Person> oldSet = oldPub.getAuthors();
				List<Person> newSet = new ArrayList<Person>();
				for (Person ufhe : oldSet) {
					if (!ufhe.getName().equals(p.getName())) {
						newSet.add(ufhe);
						System.out.println("f");
					} else {
						System.out.println("t");
					}
				}
				oldPub.setAuthors(newSet);
				pm.makePersistent(oldPub);
			}

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

		Query query0 = pm.newQuery(Person.class);
		query0.setFilter("name == a");
		query0.declareParameters("String a");

		Collection<Person> proceedings10 = (Collection<Person>) query0.execute(newPersonName);
		if (!proceedings10.isEmpty()) {
			System.out.println("There already exists a Person with name: " + newPersonName);
			pm.currentTransaction().commit();
			closeDB(pm);
			return;
		}

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

	public static String getProceedingsName(String inProceedingTitle) {

		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query1 = pm.newQuery(InProceedings.class);
		query1.setFilter("title == a");
		query1.declareParameters("String a");

		Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query1.execute(inProceedingTitle);
		String result = "";
		if (proceedings1.isEmpty()) {
			System.out.println("Error: Did not Find InProceedings: " + inProceedingTitle);
		} else {
			InProceedings inProc = proceedings1.iterator().next();
			if (inProc.getProceedings() != null) {
				result = inProc.getProceedings().getTitle();
			}
		}

		pm.currentTransaction().commit();
		closeDB(pm);
		return result;
	}

	public static List<String> getAuthorsOfInProceeding(String inProceedingTitle) {

		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query1 = pm.newQuery(InProceedings.class);
		query1.setFilter("title == a");
		query1.declareParameters("String a");

		Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query1.execute(inProceedingTitle);
		List<String> result = new ArrayList<String>();
		if (proceedings1.isEmpty()) {
			System.out.println("Error: Did not Find InProceedings: " + inProceedingTitle);
		} else {
			for (Person p : proceedings1.iterator().next().getAuthors()) {
				result.add(p.getName());
			}
		}

		pm.currentTransaction().commit();
		closeDB(pm);
		return result;
	}

	public static void deleteInProceeding(String title) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query = pm.newQuery(InProceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query.execute(title);

		InProceedings p;
		if (proceedings1.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + title);
		} else {
			p = proceedings1.iterator().next();
			for (Person pers : p.getAuthors()) {

				Set<Publication> oldAut = pers.getAuthoredPublications();
				Set<Publication> newAut = new HashSet<Publication>();
				for (Publication ufhe : oldAut) {
					if (!ufhe.getTitle().equals(p.getTitle())) {
						newAut.add(ufhe);
					}
				}
				pers.setAuthoredPublications(newAut);
				pm.makePersistent(pers);

			}
			if (p.getProceedings() != null) {
				Proceedings proceed = p.getProceedings();
				Set<InProceedings> oldInProcs = proceed.getInProceedings();
				Set<InProceedings> newInProcs = new HashSet<InProceedings>();
				for (InProceedings ufhe : oldInProcs) {
					if (!ufhe.getTitle().equals(p.getTitle())) {
						newInProcs.add(ufhe);
					}
				}
				proceed.setInProceedings(newInProcs);
				pm.makePersistent(proceed);
			}

			pm.deletePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}
*/
	public static void addInProceedings(List<InProceedings> list) {

		//database.setMultithreaded(true);
		MongoCollection<Document> collection = database.getCollection("InProceedings");
		int length = list.size();
		for (int i = 0; i < length; i++) {
			collection.insertOne(Adaptor.toDBDocument(list.get(i)));

			if (i % 500 == 0) {

				System.out.println(i + " / " + length + " InProceedings added to Database.");
			} else {
			}
		}
		System.out.println("All InProceedings added to Database");
	}
/*
	public static void updateInProceeding(String name, InProceedings newInProc, String proceedingsName, List<String> authors) {
		DBCollection collection = database.getCollection("InProceedings");

		Query query = pm.newQuery(InProceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<InProceedings> proceedings = (Collection<InProceedings>) query.execute(name);
		InProceedings p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + name);
		} else {
			p = proceedings.iterator().next();
			p.setTitle(newInProc.getTitle());
			p.setNote(newInProc.getNote());
			p.setElectronicEdition(newInProc.getElectronicEdition());
			p.setPages(newInProc.getPages());
			p.setYear(newInProc.getYear());

			if (p.getProceedings() != null) {
				Proceedings theOldP = p.getProceedings();
				Set<InProceedings> oldinProcs = theOldP.getInProceedings();
				oldinProcs.remove(p);
				theOldP.setInProceedings(oldinProcs);
			}
			Query query1 = pm.newQuery(Proceedings.class);
			query1.setFilter("title == a");
			query1.declareParameters("String a");

			Collection<Proceedings> theProceeding = (Collection<Proceedings>) query1.execute(proceedingsName);
			Proceedings theP;
			if (theProceeding.isEmpty()) {
				System.out.println("Error: Did not find a publication with ID: " + proceedingsName);
			} else {
				theP = theProceeding.iterator().next();
				p.setProceedings(theP);
				Set<InProceedings> inProcs = theP.getInProceedings();
				inProcs.add(p);
				theP.setInProceedings(inProcs);
			}

			for (Person oldAuthor : p.getAuthors()) {
				Set<Publication> oldSet = oldAuthor.getAuthoredPublications();
				Set<Publication> newSet = new HashSet<Publication>();
				for (Publication ufhe : oldSet) {
					if (!ufhe.getTitle().equals(newInProc.getTitle())) {
						newSet.add(ufhe);
					}
				}
				oldAuthor.setAuthoredPublications(newSet);
				pm.makePersistent(oldAuthor);
			}
			List<Person> theNewAuthors = new ArrayList<Person>();
			for (String newAuthor : authors) {
				Query query2 = pm.newQuery(Person.class);
				query2.setFilter("name == a");
				query2.declareParameters("String a");

				Collection<Person> listOfAuthors = (Collection<Person>) query2.execute(newAuthor);
				Person theA;
				if (listOfAuthors.isEmpty()) {
					System.out.println("Error: Did not find a author with ID: " + newAuthor);
				} else {

					theA = listOfAuthors.iterator().next();

					Set<Publication> authoredPubs = theA.getAuthoredPublications();
					authoredPubs.add((Publication) p);
					theA.setAuthoredPublications(authoredPubs);

					theNewAuthors.add(theA);
					pm.makePersistent(theA);
				}
			}
			p.setAuthors(theNewAuthors);

			pm.makePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public static void updateProceeding(String name, Proceedings newProc, List<String> authors, List<String> inProceedings, String publisherName, String seriesName, String conf, int confEdition) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(name);
		Proceedings p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with name: " + name);
		} else {
			p = proceedings.iterator().next();
			p.setTitle(newProc.getTitle());
			p.setNote(newProc.getNote());
			p.setElectronicEdition(newProc.getElectronicEdition());
			p.setYear(newProc.getYear());
			p.setNumber(newProc.getNumber());
			p.setIsbn(newProc.getIsbn());
			p.setVolume(newProc.getVolume());

			// Remove Proceedings from old authors
			for (Person oldAuthor : p.getAuthors()) {
				Set<Publication> oldSet = oldAuthor.getEditedPublications();
				Set<Publication> newSet = new HashSet<Publication>();
				for (Publication ufhe : oldSet) {
					if (!ufhe.getTitle().equals(newProc.getTitle())) {
						newSet.add(ufhe);
					}
				}
				oldAuthor.setEditedPublications(newSet);
				pm.makePersistent(oldAuthor);
			}

			// add new authors to Proceeding and add Proceeding to authors
			List<Person> newAuthors = new ArrayList<Person>();
			for (String s : authors) {
				Query query1 = pm.newQuery(Person.class);
				query1.setFilter("name == a");
				query1.declareParameters("String a");

				Collection<Person> author = (Collection<Person>) query1.execute(s);
				Person person;
				if (author.isEmpty()) {
					System.out.println("Error: Did not find a Person with name: " + s);
				} else {
					person = author.iterator().next();
					newAuthors.add(person);
					Set<Publication> editedPubs = person.getEditedPublications();
					editedPubs.add(p);
					person.setEditedPublications(editedPubs);
				}

			}
			p.setAuthors(newAuthors);

			// remove old Proceeding from old InProceedings
			for (InProceedings i : p.getInProceedings()) {
				i.setProceedings(null);
				pm.makePersistent(i);
			}

			// add new InProceedings to Proceeding and set Proceeding of InProceedings
			Set<InProceedings> newInProceedings = new HashSet<InProceedings>();
			for (String s : inProceedings) {
				Query query1 = pm.newQuery(InProceedings.class);
				query1.setFilter("title == a");
				query1.declareParameters("String a");

				Collection<InProceedings> inProceed = (Collection<InProceedings>) query1.execute(s);
				InProceedings inProc;
				if (inProceed.isEmpty()) {
					System.out.println("Error: Did not find a InProceeding with name: " + s);
				} else {
					inProc = inProceed.iterator().next();
					newInProceedings.add(inProc);
					inProc.setProceedings(p);
				}

			}
			p.setInProceedings(newInProceedings);

			// remove Proceeding from old Publisher
			Query query1 = pm.newQuery(Publisher.class);
			query1.setFilter("name == a");
			query1.declareParameters("String a");

			Collection<Publisher> inProceed = (Collection<Publisher>) query1.execute(p.getPublisher().getName());
			Publisher inProc;
			if (inProceed.isEmpty()) {
				System.out.println("Error: Did not find a Publisher with name: " + p.getPublisher().getName());
			} else {
				inProc = inProceed.iterator().next();
				Set<Publication> oldSet = inProc.getPublications();
				Set<Publication> newSet = new HashSet<Publication>();
				for (Publication ufhe : oldSet) {
					if (!ufhe.getTitle().equals(newProc.getTitle())) {
						newSet.add(ufhe);
					}
				}
				inProc.setPublications(newSet);
				pm.makePersistent(inProc);
			}

			// add Proceeding to new Publisher and add Publisher to Proceeding (possibly creat new Publisher)
			Query query2 = pm.newQuery(Publisher.class);
			query2.setFilter("name == a");
			query2.declareParameters("String a");

			Collection<Publisher> inProceed1 = (Collection<Publisher>) query2.execute(publisherName);
			Publisher inProc1 = new Publisher();
			if (inProceed1.isEmpty()) {
				System.out.println("Did not find a Publisher with name: " + publisherName + " created a new Publisher with that name");
				inProc1.setName(publisherName);
				Set<Publication> newSet = new HashSet<Publication>();
				newSet.add(p);
				inProc1.setPublications(newSet);
			} else {
				inProc1 = inProceed1.iterator().next();
				Set<Publication> oldSet = inProc1.getPublications();
				oldSet.add(p);
				inProc1.setPublications(oldSet);
			}

			p.setPublisher(inProc1);

			// remove Proceedings from old Series
			Query query3 = pm.newQuery(Series.class);
			query3.setFilter("name == a");
			query3.declareParameters("String a");

			Collection<Series> inProceed3 = (Collection<Series>) query3.execute(p.getSeries().getName());
			Series inProc3;
			if (inProceed3.isEmpty()) {
				System.out.println("Error: Did not find a Series with name: " + p.getSeries().getName());
			} else {
				inProc3 = inProceed3.iterator().next();
				Set<Publication> oldSet = inProc3.getPublications();
				Set<Publication> newSet = new HashSet<Publication>();
				for (Publication ufhe : oldSet) {
					if (!ufhe.getTitle().equals(newProc.getTitle())) {
						newSet.add(ufhe);
					}
				}
				inProc3.setPublications(newSet);
				pm.makePersistent(inProc3);
			}

			// add Proceeding to new Series and add Series to Proceeding (possibly create new Series)
			Query query4 = pm.newQuery(Series.class);
			query4.setFilter("name == a");
			query4.declareParameters("String a");

			Collection<Series> inProceed4 = (Collection<Series>) query4.execute(seriesName);
			Series inProc4 = new Series();
			if (inProceed4.isEmpty()) {
				System.out.println("Did not find a Series with name: " + seriesName + " created a new Series with that name");
				inProc4.setName(seriesName);
				Set<Publication> newSet = new HashSet<Publication>();
				newSet.add(p);
				inProc4.setPublications(newSet);
			} else {
				inProc4 = inProceed4.iterator().next();
				Set<Publication> oldSet = inProc4.getPublications();
				oldSet.add(p);
				inProc4.setPublications(oldSet);
			}

			p.setSeries(inProc4);

			ConferenceEdition ed = p.getConferenceEdition();

			Conference oldConf = ed.getConference();
			Set<ConferenceEdition> oldSet = oldConf.getEditions();
			Set<ConferenceEdition> newSet = new HashSet<ConferenceEdition>();
			for (ConferenceEdition ufhe : oldSet) {
				if (!(ufhe.getYear() == newProc.getYear())) {
					newSet.add(ufhe);
				}
			}
			oldConf.setEditions(newSet);
			pm.makePersistent(oldConf);

			Query query5 = pm.newQuery(Conference.class);
			query5.setFilter("name == a");
			query5.declareParameters("String a");

			Collection<Conference> inProceed5 = (Collection<Conference>) query5.execute(conf);
			Conference inProc5;
			if (inProceed5.isEmpty()) {
				System.out.println("Did not find a Conference with name: " + conf + " created a new one.");
				inProc5 = new Conference();
				inProc5.setName(conf);
				Set<ConferenceEdition> newS = new HashSet<ConferenceEdition>();
				newS.add(ed);
				inProc5.setEditions(newS);
			} else {
				inProc5 = inProceed5.iterator().next();
				Set<ConferenceEdition> newS = inProc5.getEditions();
				newS.add(ed);
				inProc5.setEditions(newS);
			}

			ed.setYear(confEdition);
			ed.setConference(inProc5);
			pm.makePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public static void addProceeding(Proceedings newProc, List<String> authors, List<String> inProceedings, String publisherName, String seriesName, String conf, int confEdition) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(newProc.getTitle());
		if (!proceedings.isEmpty()) {
			System.out.println("Error: There exists already a Proceeding with the name: " + newProc.getTitle());
			pm.currentTransaction().rollback();
			closeDB(pm);
			return;
		}
		Proceedings p = newProc;

		// add new authors to Proceeding and add Proceeding to authors
		List<Person> newAuthors = new ArrayList<Person>();
		for (String s : authors) {
			Query query1 = pm.newQuery(Person.class);
			query1.setFilter("name == a");
			query1.declareParameters("String a");

			Collection<Person> author = (Collection<Person>) query1.execute(s);
			Person person;
			if (author.isEmpty()) {
				System.out.println("Error: Did not find a Person with name: " + s);
			} else {
				person = author.iterator().next();
				newAuthors.add(person);
				Set<Publication> editedPubs = person.getEditedPublications();
				editedPubs.add(p);
				person.setEditedPublications(editedPubs);
			}

		}
		p.setAuthors(newAuthors);

		// add new InProceedings to Proceeding and set Proceeding of InProceedings
		Set<InProceedings> newInProceedings = new HashSet<InProceedings>();
		for (String s : inProceedings) {
			Query query1 = pm.newQuery(InProceedings.class);
			query1.setFilter("title == a");
			query1.declareParameters("String a");

			Collection<InProceedings> inProceed = (Collection<InProceedings>) query1.execute(s);
			InProceedings inProc;
			if (inProceed.isEmpty()) {
				System.out.println("Error: Did not find a InProceeding with name: " + s);
			} else {
				inProc = inProceed.iterator().next();
				newInProceedings.add(inProc);
				inProc.setProceedings(p);
			}

		}
		p.setInProceedings(newInProceedings);

		// add Proceeding to new Publisher and add Publisher to Proceeding (possibly creat new Publisher)
		Query query2 = pm.newQuery(Publisher.class);
		query2.setFilter("name == a");
		query2.declareParameters("String a");

		Collection<Publisher> inProceed1 = (Collection<Publisher>) query2.execute(publisherName);
		Publisher inProc1 = new Publisher();
		if (inProceed1.isEmpty()) {
			System.out.println("Did not find a Publisher with name: " + publisherName + " created a new Publisher with that name");
			inProc1.setName(publisherName);
			Set<Publication> newSet = new HashSet<Publication>();
			newSet.add(p);
			inProc1.setPublications(newSet);
		} else {
			inProc1 = inProceed1.iterator().next();
			Set<Publication> oldSet = inProc1.getPublications();
			oldSet.add(p);
			inProc1.setPublications(oldSet);
		}

		p.setPublisher(inProc1);

		// add Proceeding to new Series and add Series to Proceeding (possibly create new Series)
		Query query4 = pm.newQuery(Series.class);
		query4.setFilter("name == a");
		query4.declareParameters("String a");

		Collection<Series> inProceed4 = (Collection<Series>) query4.execute(seriesName);
		Series inProc4 = new Series();
		if (inProceed4.isEmpty()) {
			System.out.println("Did not find a Series with name: " + seriesName + " created a new Series with that name");
			inProc4.setName(seriesName);
			Set<Publication> newSet = new HashSet<Publication>();
			newSet.add(p);
			inProc4.setPublications(newSet);
		} else {
			inProc4 = inProceed4.iterator().next();
			Set<Publication> oldSet = inProc4.getPublications();
			oldSet.add(p);
			inProc4.setPublications(oldSet);
		}

		p.setSeries(inProc4);

		Query query5 = pm.newQuery(Conference.class);
		query5.setFilter("name == a");
		query5.declareParameters("String a");
		ConferenceEdition ed = new ConferenceEdition();
		Collection<Conference> inProceed5 = (Collection<Conference>) query5.execute(conf);
		Conference inProc5;
		if (inProceed5.isEmpty()) {
			System.out.println("Did not find a Conference with name: " + conf + " created a new one.");
			inProc5 = new Conference();
			inProc5.setName(conf);
			Set<ConferenceEdition> newS = new HashSet<ConferenceEdition>();
			newS.add(ed);
			inProc5.setEditions(newS);
		} else {
			inProc5 = inProceed5.iterator().next();
			Set<ConferenceEdition> newS = inProc5.getEditions();
			newS.add(ed);
			inProc5.setEditions(newS);
		}

		ed.setYear(confEdition);
		ed.setConference(inProc5);
		p.setConferenceEdition(ed);
		pm.makePersistent(p);

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public static void addInProceeding(InProceedings newInProc, String proceedingsName, List<String> authors) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query0 = pm.newQuery(InProceedings.class);
		query0.setFilter("title == a");
		query0.declareParameters("String a");

		Collection<InProceedings> alreadyExists = (Collection<InProceedings>) query0.execute(newInProc.getTitle());
		if (!alreadyExists.isEmpty()) {
			System.out.println("There already exists an InProceeding with title " + newInProc.getTitle());
			pm.currentTransaction().rollback();
			closeDB(pm);

			return;

		}
		Query query2 = pm.newQuery(Proceedings.class);
		query2.setFilter("title == a");
		query2.declareParameters("String a");

		Collection<Proceedings> listOfAuthors = (Collection<Proceedings>) query2.execute(proceedingsName);
		Proceedings theA;
		if (listOfAuthors.isEmpty()) {
			System.out.println("Error: Did not find a Proceeding with name: " + proceedingsName);
		} else {
			theA = listOfAuthors.iterator().next();
			newInProc.setProceedings(theA);
			Set<InProceedings> oldInProcs = theA.getInProceedings();
			oldInProcs.add(newInProc);
			theA.setInProceedings(oldInProcs);
		}

		List<Person> newAuthors = new ArrayList<Person>();
		for (String s : authors) {
			Query query1 = pm.newQuery(Person.class);
			query1.setFilter("name == a");
			query1.declareParameters("String a");

			Collection<Person> theL = (Collection<Person>) query1.execute(s);
			Person author;
			if (theL.isEmpty()) {
				System.out.println("Error: Did not find a author with name: " + s);
			} else {
				author = theL.iterator().next();
				newAuthors.add(author);
				Set<Publication> inProcs = author.getAuthoredPublications();
				inProcs.add(newInProc);
				author.setAuthoredPublications(inProcs);
			}

		}

		newInProc.setAuthors(newAuthors);

		pm.makePersistent(newInProc);

		pm.currentTransaction().commit();
		closeDB(pm);
	}
*/
	public static void addProceedings(List<Proceedings> list) {

		//database.setMultithreaded(true);
		MongoCollection<Document> collection = database.getCollection("Proceedings");
		int length = list.size();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < length; i++) {
			    documents.add(new Document("i", i));
			if (i % 500 == 0) {

				System.out.println(i + " / " + length + " Proceedings added to Database.");
			}
		}
		collection.insertMany(documents);
		System.out.println("All Proceedings added to Database");
	}
/*
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

	public static Collection<Conference> getAllConference() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Conference> ext = pm.getExtent(Conference.class);
		List<Conference> allConference = new ArrayList<Conference>();
		for (Conference c : ext) {
			allConference.add(c);
		}
		ext.closeAll();

		closeDB(pm);
		return allConference;
	}

	public static Collection<Publisher> getAllPublisher() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Publisher> ext = pm.getExtent(Publisher.class);
		List<Publisher> allPublisher = new ArrayList<Publisher>();
		for (Publisher p : ext) {
			allPublisher.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPublisher;
	}
*/
	public static MongoCollection<Document> getAllSeries() {
		connectToDB();
		MongoCollection<Document> collection = database.getCollection("Series");
		closeConnectionDB();
		return collection;
	}

	public static MongoCollection<Document> getAllConferenceEdition() {
		connectToDB();
		MongoCollection<Document> collection = database.getCollection("ConferecenEdition");
		closeConnectionDB();
		return collection;
	}

/*	// find publication by key (according to xml file)
	public static void query1(String id) {
		String thisQuery = "Query 1";
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// find publication by object id (database)
	// try 4197 (note: solution changes every time you reload database)
	public static void query1Alter(Integer oid) {
		String thisQuery = "Query 1 (alternative)";
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// Find publications by title, returning only a subset of all found publications
	public static void query2(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 2";

		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// Find publications by title, returning only a subset of all found publications ORDERED by title
	public static void query3(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 3";

		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
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
/*	public static void query4(String author) {
		String thisQuery = "Query 4";

		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// distance between 2 authors
	public static void query5(String author1, String author2) {
		String thisQuery = "Query 5";
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
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
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// Returns the number of publications per year between the interval year1 and year 2
	public static void query7(int year1, int year2) {
		String thisQuery = "Query 7";
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// No of all publications of a conference, except proceedings
	public static void query8(String conferenceName) {
		String thisQuery = "Query 8";
		String resultStr = "";
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
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
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
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
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();

	}

	// all publications of conferenceName
	public static void query11(String conferenceName) {
		String thisQuery = "Query 11";
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// list of persons which were authors of an in proceeding and editor of the corresponding proceeding.
	public static void query12() {
		String thisQuery = "Query 12";
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// list of in proceedings where the given author appears as the last author
	public static void query13(String authorName) {
		String thisQuery = "Query 13";
		DatabaseHelper.connectToDB();
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
		DatabaseHelper.closeConnectionDB();
	}

	// list of publishers (Proceedings) whose authors appear in any in proceedings between year1 and year2
	public static void query14(int year1, int year2) {
		String thisQuery = "Query 14";
		DatabaseHelper.connectToDB();

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
		DatabaseHelper.closeConnectionDB();
	}
*/
}