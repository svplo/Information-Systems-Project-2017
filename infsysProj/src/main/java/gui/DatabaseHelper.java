package gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
	private static String dbStandardName = "TheNoSQLDatabase9";
	private static MongoClient mongoClient;

	// source: http://mongodb.github.io/mongo-java-driver/3.0/driver/getting-started/quick-tour/
	public static void connectToDB() {
		// if using the default port
		// mongoClient = new MongoClient();
		// MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		Builder builder = MongoClientOptions.builder().connectTimeout(3000);
		mongoClient = new MongoClient("localhost", 27017);
		try {
			mongoClient.getAddress();
		} catch (Exception e) {
			System.out.println("Database unavailable!");
			mongoClient.close();
			return;
		}

		// DB db = mongoClient.getDB( "mydb" );
	}

	public static void closeConnectionDB() {
		mongoClient.close();
	}

	public static void createDB() {
		// database will be created automatically if it does not exist
		database = mongoClient.getDatabase(dbStandardName);
	}

	public static List<Publication> getAllPublications() {
		List<Publication> result = getAllInProceedings();
		result.addAll(getAllProceedings());
		return result;
	}

	public static List<Publication> getAllProceedings() {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Proceedings");
		List<Publication> result = new ArrayList<Publication>();
		FindIterable<Document> iterable = collection.find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(Adaptor.toProceeding(document));
			}
		});

		closeConnectionDB();
		return result;
	}

	public static List<Person> getAllPeople() {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Person");
		List<Person> result = new ArrayList<Person>();
		FindIterable<Document> iterable = collection.find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(Adaptor.toPerson(document));
			}
		});

		closeConnectionDB();
		return result;
	}

	public static List<Conference> getAllConference() {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Conference");
		List<Conference> result = new ArrayList<Conference>();
		FindIterable<Document> iterable = collection.find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(Adaptor.toConference(document));
			}
		});

		closeConnectionDB();
		return result;
	}

	public static List<Series> getAllSeries() {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Series");
		List<Series> result = new ArrayList<Series>();
		FindIterable<Document> iterable = collection.find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(Adaptor.toSeries(document));
			}
		});

		closeConnectionDB();
		return result;
	}

	public static List<Publisher> getAllPublisher() {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Publisher");
		List<Publisher> result = new ArrayList<Publisher>();
		FindIterable<Document> iterable = collection.find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(Adaptor.toPublisher(document));
			}
		});

		closeConnectionDB();
		return result;
	}

	public static List<ConferenceEdition> getAllConferenceEdition() {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("ConferenceEdition");
		List<ConferenceEdition> result = new ArrayList<ConferenceEdition>();
		FindIterable<Document> iterable = collection.find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(Adaptor.toConferenceEdition(document));
			}
		});

		closeConnectionDB();
		return result;
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

	public static List<String> getIDCollection(Document doc, String key) {
		List<String> IDs = new ArrayList<String>();

		List<Document> inProceedingsIDsDoc = ((List<Document>) doc.get(key));
		for (Document d : inProceedingsIDsDoc) {
			IDs.add((String) d.get("_id"));
		}
		return IDs;
	}

	public static List<String> getInProceedingsOfProceedings(String proceedingId) {

		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Proceedings");
		BasicDBObject query = new BasicDBObject("_id", proceedingId);
		FindIterable<Document> iterable = collection.find(query);
		List<String> idList = getIDCollection(iterable.first(), "inProceedings");
		List<String> result = new ArrayList<String>();
		collection = database.getCollection("InProceedings");
		for (String inProceedingID : idList) {
			query = new BasicDBObject("_id", inProceedingID);
			iterable = collection.find(query);
			result.add((Adaptor.toInProceedings(iterable.first())).getTitle());
		}

		closeConnectionDB();
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

	public static List<Publication> getAllInProceedings() {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("InProceedings");
		List<Publication> result = new ArrayList<Publication>();
		FindIterable<Document> iterable = collection.find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				result.add(Adaptor.toInProceedings(document));
			}
		});

		closeConnectionDB();
		return result;
	}

	public static List<Publication> searchForPublication(String search) {
		List<Publication> result = searchForProceedings(search);
		result.addAll(searchForInProceedings(search));
		return result;

	}

	public static List<Person> searchForPeople(String search) {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Person");
		BasicDBObject query = new BasicDBObject();
		query.put("name", java.util.regex.Pattern.compile(search));

		FindIterable<Document> iterable = collection.find(query);
		List<Person> result = new ArrayList<Person>();
		Iterator it = iterable.iterator();
		while (it.hasNext()) {
			result.add(Adaptor.toPerson((Document) it.next()));
		}
		closeConnectionDB();

		return result;

	}

	public static List<Conference> searchForConference(String search) {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Conference");
		BasicDBObject query = new BasicDBObject();
		query.put("name", java.util.regex.Pattern.compile(search));

		FindIterable<Document> iterable = collection.find(query);
		List<Conference> result = new ArrayList<Conference>();
		Iterator it = iterable.iterator();
		while (it.hasNext()) {
			result.add(Adaptor.toConference((Document) it.next()));
		}
		closeConnectionDB();

		return result;
	}

	public static List<Series> searchForSeries(String search) {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Series");
		BasicDBObject query = new BasicDBObject();
		query.put("name", java.util.regex.Pattern.compile(search));

		FindIterable<Document> iterable = collection.find(query);
		List<Series> result = new ArrayList<Series>();
		Iterator it = iterable.iterator();
		while (it.hasNext()) {
			result.add(Adaptor.toSeries((Document) it.next()));
		}
		closeConnectionDB();

		return result;
	}

	public static List<ConferenceEdition> searchForConferenceEdition(String search) {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("ConferenceEdition");
		BasicDBObject query = new BasicDBObject();
		query.put("title", java.util.regex.Pattern.compile(search));

		FindIterable<Document> iterable = collection.find(query);
		List<ConferenceEdition> result = new ArrayList<ConferenceEdition>();
		Iterator it = iterable.iterator();
		while (it.hasNext()) {
			result.add(Adaptor.toConferenceEdition((Document) it.next()));
		}
		closeConnectionDB();

		return result;
	}

	public static List<Publisher> searchForPublisher(String search) {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Publisher");
		BasicDBObject query = new BasicDBObject();
		query.put("name", java.util.regex.Pattern.compile(search));

		FindIterable<Document> iterable = collection.find(query);
		List<Publisher> result = new ArrayList<Publisher>();
		Iterator it = iterable.iterator();
		while (it.hasNext()) {
			result.add(Adaptor.toPublisher((Document) it.next()));
		}
		closeConnectionDB();

		return result;
	}

	public static List<Publication> searchForProceedings(String search) {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("Proceedings");
		BasicDBObject query = new BasicDBObject();
		query.put("title", java.util.regex.Pattern.compile(search));

		FindIterable<Document> iterable = collection.find(query);
		List<Publication> result = new ArrayList<Publication>();
		Iterator it = iterable.iterator();
		while (it.hasNext()) {
			result.add(Adaptor.toProceeding((Document) it.next()));
		}
		closeConnectionDB();

		return result;

	}

	public static List<Publication> searchForInProceedings(String search) {
		connectToDB();
		createDB();

		MongoCollection<Document> collection = database.getCollection("InProceedings");
		BasicDBObject query = new BasicDBObject();
		query.put("title", java.util.regex.Pattern.compile(search));

		FindIterable<Document> iterable = collection.find(query);
		List<Publication> result = new ArrayList<Publication>();
		Iterator it = iterable.iterator();
		while (it.hasNext()) {
			result.add(Adaptor.toProceeding((Document) it.next()));
		}
		closeConnectionDB();

		return result;
	}

	public static void addInProceedings(List<InProceedings> list) {
		MongoCollection<Document> collection = database.getCollection("InProceedings");
		int length = list.size();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < length; i++) {
			documents.add(Adaptor.toDBDocument(list.get(i)));

			if (i % 500 == 0) {
				System.out.println(i + " / " + length + " InProceedings added to Database.");
			}
		}
		if (!documents.isEmpty()) {
			collection.insertMany(documents);
		} else {
			System.out.println("No documents (InProceedings) created.");
		}
		System.out.println("All InProceedings added to Database");
	}

	public static void addPersons(List<Person> list) {
		// Add Persons
		MongoCollection<Document> collection = database.getCollection("Person");
		int length = list.size();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < length; i++) {
			documents.add(Adaptor.toDBDocument(list.get(i)));
		}
		if (!documents.isEmpty()) {
			collection.insertMany(documents);
		} else {
			System.out.println("No documents (Persons) created.");
		}

	}

	public static void addSeries(List<Series> list) {
		// Add Series
		MongoCollection<Document> collection = database.getCollection("Series");
		int length = list.size();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < length; i++) {
			documents.add(Adaptor.toDBDocument(list.get(i)));
		}
		if (!documents.isEmpty()) {
			collection.insertMany(documents);
		} else {
			System.out.println("No documents (Series) created.");
		}

	}

	public static void addPublishers(List<Publisher> list) {
		// Add Publisher
		MongoCollection<Document> collection = database.getCollection("Publisher");
		int length = list.size();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < length; i++) {
			documents.add(Adaptor.toDBDocument(list.get(i)));
		}
		if (!documents.isEmpty()) {
			collection.insertMany(documents);
		} else {
			System.out.println("No documents (Publisher) created.");
		}

	}

	public static void addConferences(List<Conference> list) {
		// Add Conference
		MongoCollection<Document> collection = database.getCollection("Conference");
		int length = list.size();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < length; i++) {
			documents.add(Adaptor.toDBDocument(list.get(i)));
		}
		if (!documents.isEmpty()) {
			collection.insertMany(documents);
		} else {
			System.out.println("No documents (Conference) created.");
		}

	}

	public static void addConferenceEditions(List<ConferenceEdition> list) {
		// Add Conference Editions
		MongoCollection<Document> collection = database.getCollection("ConferenceEdition");
		int length = list.size();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < length; i++) {
			documents.add(Adaptor.toDBDocument(list.get(i)));
		}
		if (!documents.isEmpty()) {
			collection.insertMany(documents);
		} else {
			System.out.println("No documents (ConferenceEdition) created.");
		}

	}

	public static void addProceedings(List<Proceedings> list) {

		// Add Proceedings
		MongoCollection<Document> collection = database.getCollection("Proceedings");
		int length = list.size();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < length; i++) {
			documents.add(Adaptor.toDBDocument(list.get(i)));

			if (i % 500 == 0) {
				System.out.println(i + " / " + length + " Proceedings added to Database.");
			}
		}
		if (!documents.isEmpty()) {
			collection.insertMany(documents);
		} else {
			System.out.println("No documents (Proceedings) created.");
		}
		System.out.println("All Proceedings added to Database");

	}

	public static void updatePerson(String id, String name, List<String> authoredPublications, List<String> editedPublications) {
		connectToDB();
		createDB();
		
		MongoCollection<Document> collection = database.getCollection("Person");
		BasicDBObject whereQuery = new BasicDBObject();
		Iterator<String> aPubIter = authoredPublications.iterator();
		Iterator<String> ePubIter = authoredPublications.iterator();
		Set<Publication> aPublications = new HashSet<Publication>();
		Set<Publication> ePublications = new HashSet<Publication>();
		while (aPubIter.hasNext()) {
			whereQuery.put("title", aPubIter.next());
			Iterator<Document> cursor = collection.find(whereQuery).iterator();
			while (cursor.hasNext()) {
				aPublications.add(Adaptor.toPublication(cursor.next()));
			}
		}
		while (ePubIter.hasNext()) {
			whereQuery.put("title", ePubIter.next());
			Iterator<Document> cursor = collection.find(whereQuery).iterator();
			while (cursor.hasNext()) {
				ePublications.add(Adaptor.toPublication(cursor.next()));
			}
		}
		Person p = new Person(id, name, aPublications, ePublications);
		BasicDBObject findPerson = new BasicDBObject();
		findPerson.put("_id", id);
		//instead of updating only specific fields, replace them all
		collection.replaceOne(findPerson, Adaptor.toDBDocument(p));
		closeConnectionDB();
	}

	public static void deletePerson(String id) {
		connectToDB();
		createDB();
		MongoCollection<Document> collection = database.getCollection("Person");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", id);
		collection.deleteOne(whereQuery);
		closeConnectionDB();
	}

	public static void addPerson(String newName, List<String> authoredPublications, List<String> editedPublications) {
			connectToDB();
			createDB();
			
			MongoCollection<Document> collection = database.getCollection("Person");
			BasicDBObject whereQuery = new BasicDBObject();
			Iterator<String> aPubIter = authoredPublications.iterator();
			Iterator<String> ePubIter = authoredPublications.iterator();
			Set<Publication> aPublications = new HashSet<Publication>();
			Set<Publication> ePublications = new HashSet<Publication>();
			while (aPubIter.hasNext()) {
				whereQuery.put("title", aPubIter.next());
				Iterator<Document> cursor = collection.find(whereQuery).iterator();
				while (cursor.hasNext()) {
					aPublications.add(Adaptor.toPublication(cursor.next()));
				}
			}
			while (ePubIter.hasNext()) {
				whereQuery.put("title", ePubIter.next());
				Iterator<Document> cursor = collection.find(whereQuery).iterator();
				while (cursor.hasNext()) {
					ePublications.add(Adaptor.toPublication(cursor.next()));
				}
			}
			Person p = new Person(newName, aPublications, ePublications);
			//instead of updating only specific fields, replace them all
			collection.insertOne(Adaptor.toDBDocument(p));
			closeConnectionDB();
		}

}