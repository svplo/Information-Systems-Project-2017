package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.text.Document;

import org.basex.core.*;
import org.basex.core.cmd.*;
import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.iter.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.node.FElem;

import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.DomainObject;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class DatabaseHelper {
	private static Context context;
	private static String dbStandardName = "TheBaseXDatabase";

	public static void test(){
		
	}

	static List<DomainObject> myQuery(final String query, Class<? extends DomainObject> c) {

		List<DomainObject> result = new ArrayList<DomainObject>();
		// Create a query processor
		try (QueryProcessor proc = new QueryProcessor(query, context)) {
			// Store the pointer to the result in an iterator:
			Iter iter = proc.iter();
			// Iterate through all items and serialize
			for (Item item; (item = iter.next()) != null;) {
				DomainObject obj = Adaptor.toJava(item, c);
				result.add(obj);

			}
		}
		catch(QueryException e){
			e.printStackTrace();
		}

		return result;
	}
	
	static List<String> myQuery(final String query) {

		List<String> result = new ArrayList<String>();
		// Create a query processor
		try (QueryProcessor proc = new QueryProcessor(query, context)) {
			// Store the pointer to the result in an iterator:
			Iter iter = proc.iter();
			// Iterate through all items and serialize
			for (Item item; (item = iter.next()) != null;) {
				
				result.add(item.serialize().toString());

			}
		}
		catch(QueryException | QueryIOException e){
			e.printStackTrace();
		}
	    return result;
	}

	public static void connectToDB() {
		context = new Context();
		try {
			new Open(dbStandardName).execute(context);
		} catch (BaseXException e) {
			System.out.println("There was an Error closing the Database");

		}

	}

	public static void closeConnectionDB() {
		try {
			new Close().execute(context);
		} catch (BaseXException e) {
			System.out.println("There was an Error closing the Database");
		}
	}

	public static void createDB() {

		context = new Context();

		System.out.println("Creating Database");

		// Create a database from a local or remote XML document or XML String
		try {
			new CreateDB(dbStandardName, "dblp_filtered.xml").execute(context);

			System.out.print(new InfoDB().execute(context));

		} catch (BaseXException e) {
			System.out.println("Something went wrong while creating the database");
			e.printStackTrace();
		}

		// Close the database context
		context.close();

	}

	public static String escape(String s){
		return(s.replaceAll("\"", "&#34;"));
	}
	
	public static List<Publication> getAllPublications() {
		List<Publication> result = getAllInProceedings();
		result.addAll(getAllProceedings());
		return result;
	}

	public static List<Publication> getAllInProceedings() {
		connectToDB();

		String query = "/root/inproceedings";
		List<Publication> result =(List<Publication>)(List<?>) myQuery(query, InProceedings.class);

		closeConnectionDB();
		return result;
	}

	public static List<Publication> getAllProceedings() {
		connectToDB();

		String query = "/root/proceedings";
		List<Publication> result =(List<Publication>)(List<?>) myQuery(query, Proceedings.class);
		
		closeConnectionDB();
		return result;
	}

	public static List<Person> getAllPeople() {
		connectToDB();
		
		String query = "for $x in distinct-values((root/proceedings/editor|root/inproceedings/author)) return <name>{$x}</name>";
		List<Person> result =(List<Person>)(List<?>) myQuery(query, Person.class);

		for(Person p : result){
			query = "count(for $x in distinct-values((root/proceedings[editor = \"" +p.getName()+"\"]/title)) return $x)";
			int size = Integer.valueOf(myQuery(query).get(0));
			for(int i = 0; i<size;i++){
				p.addEditedPublication(new Proceedings());
			}			
			
			query = "count(for $x in distinct-values((root/inproceedings[author = \"" +p.getName()+"\"]/title)) return $x)";
			size = Integer.valueOf(myQuery(query).get(0));
			for(int i = 0; i<size;i++){
				p.addAuthoredPublication(new InProceedings());
			}			

		}
		
		closeConnectionDB();
		return result;
	}

	public static List<Conference> getAllConference() {
		connectToDB();

		
		String query = "for $x in distinct-values(root/proceedings/booktitle) return <name>{$x}</name>";
		List<Conference> result =(List<Conference>)(List<?>) myQuery(query, Conference.class);
		for(Conference conf : result){
			query = "count(/root/proceedings[booktitle = \"" + conf.getName().replaceAll("\"", "'") + "\"])";
			int size = Integer.valueOf(myQuery(query).get(0));
			for(int i = 0; i<size;i++){
				conf.addEdition(new ConferenceEdition());
			}
		}
		
		closeConnectionDB();
		return result;
	}

	public static List<Series> getAllSeries() {
		connectToDB();

		String query = "for $x in distinct-values(root/proceedings/series)    order by $x    return <name>{$x}</name>";
		List<Series> result =(List<Series>)(List<?>) myQuery(query, Series.class);

		for(Series s : result){
			query = "count(for $x in (root/proceedings[series = \""+s.getName()+"\"]/series)  return <series>{$x}</series>)";
			int size = Integer.valueOf(myQuery(query).get(0));
			for(int i = 0; i<size; i++){
				s.addPublication(new Proceedings());
			}
		}
		
		closeConnectionDB();
		return result;
	}

	public static List<Publisher> getAllPublisher() {
		connectToDB();

		String query = "for $x in distinct-values(root/proceedings/publisher)    order by $x    return <publisher>{$x}</publisher>";
		List<Publisher> result =(List<Publisher>)(List<?>) myQuery(query,Publisher.class);

		
		closeConnectionDB();
		return result;
	}

	public static List<ConferenceEdition> getAllConferenceEdition() {
		List<Publication> proceedings = getAllProceedings();
		List<ConferenceEdition> result = new ArrayList<ConferenceEdition>();
		for(Publication p : proceedings){
			ConferenceEdition e = new ConferenceEdition();
			e.setYear(p.getYear());
			e.setProceedings((Proceedings)p);
			Conference conf = new Conference();
			conf.setName(getConferenceEditionName(e));
			e.setConference(conf);
			result.add(e);
		}
		return result;
	}
	
	public static String getNumberOfPublicationsForPublisher(String name){
		connectToDB();
		String query = "count(root/proceedings[publisher = \""+name+"\"])";
		String result = myQuery(query).get(0);

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

		String query = "/root/proceedings[title = \"" + escape(proceedingName) + "\"]/booktitle";
		String result = myQuery(query).get(0);
		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static String getConferenceEditionName(ConferenceEdition edition) {
		DatabaseHelper.connectToDB();
		String query = "for $x in distinct-values(root/proceedings[title = \""+escape(edition.getProceedings().getTitle())+"\"]/booktitle) return <name>{$x}</name>";
		List<Conference> result =(List<Conference>)(List<?>) myQuery(query, Conference.class);

		DatabaseHelper.closeConnectionDB();
		if(result.size() != 0){
			return result.get(0).getName();
		}
		else{
			return "";
		}
	}

	public static String getConferenceEditionProceeding(ConferenceEdition edition) {
		return edition.getProceedings().getTitle();
	}

	public static String getConferenceYear(String proceedingName) {
		DatabaseHelper.connectToDB();

		String query = "/root/proceedings[title = \"" + escape(proceedingName) + "\"]/year";
		String result = myQuery(query).get(0);

		
		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static List<String> getInProceedingsOfProceedings(String proceedingId) {

		connectToDB();
		
		String query = "for $x in (/root/inproceedings) where $x/crossref = \""+ proceedingId.replace("key=\"", "").replaceAll("\"", "")+"\" return $x/title/text()";
		List<String> result = myQuery(query);

		
		closeConnectionDB();
		return result;

	}

	public static List<String> getAuthoredPublicationsForPerson(String personName) {
		DatabaseHelper.connectToDB();
		String query = "for $x in distinct-values((root/inproceedings[author = \"" +personName+"\"]/title)) return $x";
		List<String> result = myQuery(query);
		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static List<String> getEditedPublicationsForPerson(String personName) {
		DatabaseHelper.connectToDB();
		String query = "for $x in distinct-values((root/proceedings[editor = \"" +personName+"\"]/title)) return $x";
		List<String> result = myQuery(query);
		DatabaseHelper.closeConnectionDB();
		return result;
	}

	public static Proceedings getProceedingOfInproceeding(String proceedingsID) {
		connectToDB();
		
		String query = "/root/proceedings[@key = \"" + proceedingsID + "\"]";
		List<Publication> result =(List<Publication>)(List<?>) myQuery(query, Proceedings.class);

		System.out.println(result.get(0).getTitle());
		closeConnectionDB();
		if(result.get(0) == null){
			return new Proceedings();
		}
		else{
			return (Proceedings) result.get(0);
		}
	}

	public static List<String> getAuthorsOfInProceeding(InProceedings inProceeding) {
		
		List<String> result = new ArrayList<String>();
		for(Person p: inProceeding.getAuthors()){
			result.add(p.getName());
		}
		return result;
	}

	public static List<Publication> searchForPublication(String search) {
		List<Publication> result = searchForProceedings(search);
		result.addAll(searchForInProceedings(search));
		return result;

	}

	public static List<Person> searchForPeople(String search) {
		connectToDB();
		String query = "for $x in distinct-values((root/proceedings/editor|root/inproceedings/author)) where contains($x,\"" + escape(search) + "\" ) return <name>{$x}</name>";
		List<Person> result =(List<Person>)(List<?>) myQuery(query, Person.class);

		for(Person p : result){
			query = "count(for $x in distinct-values((root/proceedings[editor = \"" +p.getName()+"\"]/title)) return $x)";
			int size = Integer.valueOf(myQuery(query).get(0));
			for(int i = 0; i<size;i++){
				p.addEditedPublication(new Proceedings());
			}			
			
			query = "count(for $x in distinct-values((root/inproceedings[author = \"" +p.getName()+"\"]/title)) return $x)";
			size = Integer.valueOf(myQuery(query).get(0));
			for(int i = 0; i<size;i++){
				p.addAuthoredPublication(new InProceedings());
			}			

		}

		closeConnectionDB();

		return result;

	}

	public static List<Conference> searchForConference(String search) {
		connectToDB();
		String query = "for $x in distinct-values(root/proceedings/booktitle) where contains($x,\"" + escape(search) + "\" ) return <publisher>{$x}</publisher>";
		List<Conference> result =(List<Conference>)(List<?>) myQuery(query, Conference.class);
		for(Conference conf : result){
			query = "count(/root/proceedings[booktitle = \"" + conf.getName().replaceAll("\"", "'") + "\"])";
			int size = Integer.valueOf(myQuery(query).get(0));
			for(int i = 0; i<size;i++){
				conf.addEdition(new ConferenceEdition());
			}
		}

		closeConnectionDB();

		return result;
	}

	public static List<Series> searchForSeries(String search) {
		connectToDB();
		String query = "for $x in distinct-values(root/proceedings/series) where contains($x,\"" + escape(search) + "\" ) return <name>{$x}</name>";
		List<Series> result =(List<Series>)(List<?>) myQuery(query, Series.class);

		closeConnectionDB();

		return result;
	}

	public static List<ConferenceEdition> searchForConferenceEdition(String search) {
		
		connectToDB();
		String query = "for $x in /root/proceedings where contains($x/year,\""+escape(search)+"\") return $x";
		List<Publication> proceedings =(List<Publication>)(List<?>) myQuery(query, Proceedings.class);
		closeConnectionDB();

		List<ConferenceEdition> result = new ArrayList<ConferenceEdition>();
		for(Publication p : proceedings){
			ConferenceEdition e = new ConferenceEdition();
			e.setYear(p.getYear());
			e.setProceedings((Proceedings)p);
			Conference conf = new Conference();
			conf.setName(getConferenceEditionName(e));
			e.setConference(conf);
			result.add(e);
		}
		return result;

	}

	public static List<Publisher> searchForPublisher(String search) {
		connectToDB();

		String query = "for $x in distinct-values(root/proceedings/publisher) where contains($x,\"" + escape(search) + "\" ) return <publisher>{$x}</publisher>";
		List<Publisher> result =(List<Publisher>)(List<?>) myQuery(query, Publisher.class);

		closeConnectionDB();

		return result;
	}

	public static List<Publication> searchForProceedings(String search) {
		connectToDB();
		String query = "for $x in /root/proceedings where contains($x/title,\""+escape(search)+"\") return $x";
		List<Publication> result =(List<Publication>)(List<?>) myQuery(query, Proceedings.class);
		closeConnectionDB();

		return result;

	}

	public static List<Publication> searchForInProceedings(String search) {
		connectToDB();
		String query = "for $x in /root/inproceedings where contains($x/title,\""+escape(search)+"\") return $x";
		List<Publication> result =(List<Publication>)(List<?>) myQuery(query, InProceedings.class);
		closeConnectionDB();

		return result;
	}

	public static void updatePerson(String id, String name, List<String> authoredPublications, List<String> editedPublications) {
		connectToDB();
		createDB();

		// remove Person from authorList of authored Publications
		Iterator<Document> cursor0 = myQuery("Person", "_id", id);
		Person oldPerson = Adaptor.toPerson(cursor0.next());
		for (Publication inProc : oldPerson.getAuthoredPublications()) {
			Iterator<Document> cursor1 = myQuery("InProceedings", "_id", inProc.getId());
			InProceedings inProceeding = Adaptor.toInProceedings(cursor1.next());
			List<Person> newAuthors = new ArrayList<Person>();
			for (Person aut : inProceeding.getAuthors()) {
				if (!aut.getId().equals(id)) {
					newAuthors.add(aut);
				}
			}
			inProceeding.setAuthors(newAuthors);
			myReplacement("InProceedings", "_id", inProc.getId(), Adaptor.toDBDocument(inProceeding));
		}

		// remove Person from authorList of edited Publications
		for (Publication proc : oldPerson.getEditedPublications()) {
			Iterator<Document> cursor1 = myQuery("Proceedings", "_id", proc.getId());
			Proceedings proceeding = Adaptor.toProceeding(cursor1.next());
			List<Person> newAuthors = new ArrayList<Person>();
			for (Person aut : proceeding.getAuthors()) {
				if (!aut.getId().equals(id)) {
					newAuthors.add(aut);
				}
			}
			proceeding.setAuthors(newAuthors);
			myReplacement("Proceedings", "_id", proc.getId(), Adaptor.toDBDocument(proceeding));
		}

		Iterator<String> aPubIter = authoredPublications.iterator();
		Iterator<String> ePubIter = editedPublications.iterator();
		Set<Publication> aPublications = new HashSet<Publication>();
		Set<Publication> ePublications = new HashSet<Publication>();
		while (aPubIter.hasNext()) {

			Iterator<Document> cursor = myQuery("InProceedings", "title", aPubIter.next());
			while (cursor.hasNext()) {
				InProceedings inProceeding = Adaptor.toInProceedings(cursor.next());
				inProceeding.addAuthor(oldPerson);
				myReplacement("InProceedings", "_id", inProceeding.getId(), Adaptor.toDBDocument(inProceeding));
				aPublications.add(inProceeding);

			}
		}
		while (ePubIter.hasNext()) {
			Iterator<Document> cursor = myQuery("Proceedings", "title", ePubIter.next());
			while (cursor.hasNext()) {
				Proceedings proceeding = Adaptor.toProceeding(cursor.next());
				proceeding.addAuthor(oldPerson);
				myReplacement("Proceedings", "_id", proceeding.getId(), Adaptor.toDBDocument(proceeding));
				ePublications.add(proceeding);
			}
		}
		Person p = new Person(id, name, aPublications, ePublications);
		myReplacement("Person", "_id", id, Adaptor.toDBDocument(p));
		closeConnectionDB();
	}

	public static void deletePerson(String id) {
		connectToDB();
		createDB();

		// remove Person from authorList of authored Publications
		Iterator<Document> cursor0 = myQuery("Person", "_id", id);
		Person oldPerson = Adaptor.toPerson(cursor0.next());
		for (Publication inProc : oldPerson.getAuthoredPublications()) {
			Iterator<Document> cursor1 = myQuery("InProceedings", "_id", inProc.getId());
			InProceedings inProceeding = Adaptor.toInProceedings(cursor1.next());
			List<Person> newAuthors = new ArrayList<Person>();
			for (Person aut : inProceeding.getAuthors()) {
				if (!aut.getId().equals(id)) {
					newAuthors.add(aut);
				}
			}
			inProceeding.setAuthors(newAuthors);
			myReplacement("InProceedings", "_id", inProc.getId(), Adaptor.toDBDocument(inProceeding));
		}

		// remove Person from authorList of edited Publications
		for (Publication inProc : oldPerson.getEditedPublications()) {
			Iterator<Document> cursor1 = myQuery("Proceedings", "_id", inProc.getId());
			Proceedings inProceeding = Adaptor.toProceeding(cursor1.next());
			List<Person> newAuthors = new ArrayList<Person>();
			for (Person aut : inProceeding.getAuthors()) {
				if (!aut.getId().equals(id)) {
					newAuthors.add(aut);
				}
			}
			inProceeding.setAuthors(newAuthors);
			myReplacement("Proceedings", "_id", inProc.getId(), Adaptor.toDBDocument(inProceeding));
		}

		myDelete("Person", "_id", id);
		closeConnectionDB();
	}

	public static void updateProceeding(String title, Proceedings newProc, List<String> authors, List<String> inProcNames, String publisherName, String seriesName, String conferenceName, int confYear) {

		deleteProceeding(title);
		addProceeding(newProc, authors, inProcNames, publisherName, seriesName, conferenceName, confYear);
	}

	public static void deleteProceeding(String title) {
		connectToDB();
		createDB();

		// remove Proceeding from Author's edited Publication list
		Iterator<Document> cursor0 = myQuery("Proceedings", "title", title);
		Proceedings oldProceeding = Adaptor.toProceeding(cursor0.next());
		for (Person aut : oldProceeding.getAuthors()) {
			Iterator<Document> cursor1 = myQuery("Person", "_id", aut.getId());
			Person author = Adaptor.toPerson(cursor1.next());
			Set<Publication> newEditedPublications = new HashSet<Publication>();
			for (Publication pub : author.getEditedPublications()) {
				if (!pub.getId().equals(oldProceeding.getID())) {
					newEditedPublications.add(pub);
				}
			}
			author.setEditedPublications(newEditedPublications);
			myReplacement("Person", "_id", author.getId(), Adaptor.toDBDocument(author));
		}

		// remove Proceeding from InProceeding
		for (InProceedings inProc : oldProceeding.getInProceedings()) {
			Iterator<Document> cursor1 = myQuery("InProceedings", "_id", inProc.getId());
			InProceedings inProceeding = Adaptor.toInProceedings(cursor1.next());
			inProceeding.setProceedings(null);
			myReplacement("InProceedings", "_id", inProceeding.getId(), Adaptor.toDBDocument(inProceeding));
		}

		// remove Proceeding from Series
		Iterator<Document> cursor1 = myQuery("Series", "_id", oldProceeding.getSeries().getId());
		Series series = Adaptor.toSeries(cursor1.next());
		Set<Publication> newPublications = new HashSet<Publication>();
		for (Publication pub : series.getPublications()) {
			if (!pub.getId().equals(oldProceeding.getID())) {
				newPublications.add(pub);
			}
		}
		series.setPublications(newPublications);

		myReplacement("Series", "_id", series.getId(), Adaptor.toDBDocument(series));

		// remove Proceeding from Publisher
		cursor1 = myQuery("Publisher", "_id", oldProceeding.getPublisher().getId());
		Publisher publisher = Adaptor.toPublisher(cursor1.next());
		newPublications = new HashSet<Publication>();
		for (Publication pub : publisher.getPublications()) {
			if (!pub.getId().equals(oldProceeding.getID())) {
				newPublications.add(pub);
			}
		}
		publisher.setPublications(newPublications);

		myReplacement("Publisher", "_id", publisher.getId(), Adaptor.toDBDocument(publisher));

		// delete ConferenceEdition and remove confID from Conference
		cursor1 = myQuery("ConferenceEdition", "_id", oldProceeding.getConferenceEdition().getId());
		ConferenceEdition conferenceEdition = Adaptor.toConferenceEdition(cursor1.next());

		Iterator<Document> cursor2 = myQuery("Conference", "_id", conferenceEdition.getConference().getId());
		Conference conference = Adaptor.toConference(cursor2.next());

		Set<ConferenceEdition> newConfEds = new HashSet<ConferenceEdition>();
		for (ConferenceEdition confEd : conference.getEditions()) {
			if (!conferenceEdition.getId().equals(confEd.getId())) {
				newConfEds.add(confEd);
			}
		}
		conference.setEditions(newConfEds);

		myReplacement("Conference", "_id", conference.getId(), Adaptor.toDBDocument(conference));
		myDelete("ConferenceEdition", "_id", conferenceEdition.getId());

		myDelete("Proceedings", "_id", oldProceeding.getId());
		closeConnectionDB();
	}

	public static void addPerson(String newName, List<String> authoredPublications, List<String> editedPublications) {
		connectToDB();
		createDB();
		String id = (new ObjectId()).toString();
		Person p = new Person();
		p.setId(id);
		p.setName(newName);
		Iterator<String> aPubIter = authoredPublications.iterator();
		Iterator<String> ePubIter = editedPublications.iterator();
		Set<Publication> aPublications = new HashSet<Publication>();
		Set<Publication> ePublications = new HashSet<Publication>();
		while (aPubIter.hasNext()) {

			Iterator<Document> cursor = myQuery("InProceedings", "title", aPubIter.next());
			while (cursor.hasNext()) {
				InProceedings inProceeding = Adaptor.toInProceedings(cursor.next());
				inProceeding.addAuthor(p);
				myReplacement("InProceedings", "_id", inProceeding.getId(), Adaptor.toDBDocument(inProceeding));
				aPublications.add(inProceeding);

			}
		}
		while (ePubIter.hasNext()) {
			Iterator<Document> cursor = myQuery("Proceedings", "title", ePubIter.next());
			while (cursor.hasNext()) {
				Proceedings inProceeding = Adaptor.toProceeding(cursor.next());
				inProceeding.addAuthor(p);
				myReplacement("Proceedings", "_id", inProceeding.getId(), Adaptor.toDBDocument(inProceeding));
				aPublications.add(inProceeding);
			}
		}

		p.setAuthoredPublications(aPublications);
		p.setEditedPublications(ePublications);

		// instead of updating only specific fields, replace them all
		MongoCollection<Document> collection = database.getCollection("Person");
		collection.insertOne(Adaptor.toDBDocument(p));

		closeConnectionDB();
	}


	public static void deleteInProceeding(String id) {
		connectToDB();
		createDB();

		// remove InProceeding from Author's authored Publication list
		Iterator<Document> cursor0 = myQuery("InProceedings", "_id", id);
		InProceedings oldInProceeding = Adaptor.toInProceedings(cursor0.next());
		for (Person aut : oldInProceeding.getAuthors()) {
			Iterator<Document> cursor1 = myQuery("Person", "_id", aut.getId());
			Person author = Adaptor.toPerson(cursor1.next());
			Set<Publication> newAuthoredPublications = new HashSet<Publication>();
			for (Publication pub : author.getAuthoredPublications()) {
				if (!pub.getId().equals(oldInProceeding.getId())) {
					newAuthoredPublications.add(pub);
				}
			}
			author.setAuthoredPublications(newAuthoredPublications);
			myReplacement("Person", "_id", author.getId(), Adaptor.toDBDocument(author));
		}

		// remove InProceeding from Proceeding
		if (!oldInProceeding.getProceedings().getId().equals("")) {
			Iterator<Document> cursor1 = myQuery("Proceedings", "_id", oldInProceeding.getProceedings().getId());
			Proceedings proceeding = Adaptor.toProceeding(cursor1.next());

			Set<InProceedings> newInProceedings = new HashSet<InProceedings>();
			for (InProceedings pub : proceeding.getInProceedings()) {
				if (!pub.getId().equals(oldInProceeding.getId())) {
					newInProceedings.add(pub);
				}
			}
			proceeding.setInProceedings(newInProceedings);

			myReplacement("Proceedings", "_id", proceeding.getId(), Adaptor.toDBDocument(proceeding));
		}

		myDelete("InProceedings", "_id", oldInProceeding.getId());
		closeConnectionDB();
	}

	public static void updateInProceeding(String id, InProceedings newInProceeding, String procTitle, List<String> authors) {
		connectToDB();
		createDB();

		deleteInProceeding(id);
		addInProceeding(newInProceeding, procTitle, authors);

		closeConnectionDB();
	}

	public static void p(Object o) {
		System.out.println(o);
	}

	public static void addInProceeding(InProceedings newInProceeding, String procTitle, List<String> authors) {
		connectToDB();
		createDB();

		// create new ID for new InProceedings
		String id = (new ObjectId()).toString();
		newInProceeding.setId(id);

		// update all authors
		Iterator<String> authorIter = authors.iterator();
		List<Person> authorsList = new ArrayList<Person>();
		while (authorIter.hasNext()) {

			Iterator<Document> cursor = myQuery("Person", "name", authorIter.next());
			while (cursor.hasNext()) {
				Person p = Adaptor.toPerson(cursor.next());
				p.addAuthoredPublication(newInProceeding);
				myReplacement("Person", "_id", p.getId(), Adaptor.toDBDocument(p));
				authorsList.add(p);
			}
		}
		newInProceeding.setAuthors(authorsList);

		if (!procTitle.equals("")) {
			// update Proceeding
			Iterator<Document> cursor = myQuery("Proceedings", "title", procTitle);
			Proceedings proceedings = Adaptor.toProceeding(cursor.next());
			proceedings.addInProceedings(newInProceeding);
			myReplacement("Proceedings", "_id", proceedings.getId(), Adaptor.toDBDocument(proceedings));
			newInProceeding.setProceedings(proceedings);
		}

		MongoCollection<Document> collection = database.getCollection("InProceedings");
		collection.insertOne(Adaptor.toDBDocument(newInProceeding));

		closeConnectionDB();

	}

	public static void addProceeding(Proceedings newProceeding, List<String> authors, List<String> inProceedings, String pubName, String seriesName, String confName, int confYear) {
		connectToDB();
		createDB();

		// create new ID for new InProceedings
		String id = (new ObjectId()).toString();
		newProceeding.setId(id);

		p(authors);
		// update all authors
		Iterator<String> authorIter = authors.iterator();
		List<Person> authorsList = new ArrayList<Person>();
		while (authorIter.hasNext()) {

			Iterator<Document> cursor = myQuery("Person", "name", authorIter.next());
			while (cursor.hasNext()) {
				Person p = Adaptor.toPerson(cursor.next());
				p.addEditedPublication(newProceeding);
				myReplacement("Person", "_id", p.getId(), Adaptor.toDBDocument(p));
				authorsList.add(p);
			}
		}
		newProceeding.setAuthors(authorsList);

		// update all authors
		Iterator<String> inProcIter = inProceedings.iterator();
		Set<InProceedings> inProcList = new HashSet<InProceedings>();
		while (inProcIter.hasNext()) {

			Iterator<Document> cursor = myQuery("InProceedings", "title", inProcIter.next());
			while (cursor.hasNext()) {
				InProceedings p = Adaptor.toInProceedings(cursor.next());
				p.setProceedings(newProceeding);
				myReplacement("InProceedings", "_id", p.getId(), Adaptor.toDBDocument(p));
				inProcList.add(p);
			}
		}
		newProceeding.setInProceedings(inProcList);

		// handle Publisher
		Iterator<Document> cursor = myQuery("Publisher", "name", pubName);
		Publisher pub = new Publisher();
		if (!cursor.hasNext()) {
			pub.setName(pubName);
			Set<Publication> set = new HashSet<Publication>();
			set.add(newProceeding);
			pub.setPublications(set);
			pub.setId((new ObjectId()).toString());
			myInsert("Publisher", Adaptor.toDBDocument(pub));
		} else {
			pub = Adaptor.toPublisher(cursor.next());
			pub.addPublication(newProceeding);
			myReplacement("Publisher", "_id", pub.getId(), Adaptor.toDBDocument(pub));
		}

		newProceeding.setPublisher(pub);

		// handle Series
		cursor = myQuery("Series", "name", seriesName);
		Series series = new Series();
		if (!cursor.hasNext()) {
			series.setName(seriesName);
			Set<Publication> set = new HashSet<Publication>();
			set.add(newProceeding);
			series.setPublications(set);
			series.setId((new ObjectId()).toString());
			myInsert("Series", Adaptor.toDBDocument(series));
		} else {
			series = Adaptor.toSeries(cursor.next());
			series.addPublication(newProceeding);
			myReplacement("Series", "_id", series.getId(), Adaptor.toDBDocument(series));
		}

		newProceeding.setSeries(series);

		// handle Conference/ConferenceEdition
		cursor = myQuery("Conference", "name", confName);
		Conference conference = new Conference();

		ConferenceEdition confE = new ConferenceEdition();
		confE.setYear(confYear);
		confE.setId((new ObjectId()).toString());
		confE.setProceedings(newProceeding);

		if (!cursor.hasNext()) {
			conference.setName(confName);
			Set<Publication> set = new HashSet<Publication>();
			set.add(newProceeding);
			Set<ConferenceEdition> confEList = new HashSet<ConferenceEdition>();
			confEList.add(confE);
			conference.setEditions(confEList);
			conference.setId((new ObjectId()).toString());
			confE.setConference(conference);
			myInsert("Conference", Adaptor.toDBDocument(conference));
		} else {
			conference = Adaptor.toConference(cursor.next());
			conference.addEdition(confE);
			confE.setConference(conference);
			myReplacement("Conference", "_id", conference.getId(), Adaptor.toDBDocument(conference));
		}

		newProceeding.setConferenceEdition(confE);
		myInsert("ConferenceEdition", Adaptor.toDBDocument(confE));

		myInsert("Proceedings", Adaptor.toDBDocument(newProceeding));

		closeConnectionDB();

	}

	
	
	
	
	
	
	/**
	 * 
	 * Queries
	 * 
	 **/

	// TODO: add Publication table
	// find publication by key (according to xml file)
	public static void query1(String id) {
		String thisQuery = "Query 1";
		connectToDB();
		createDB();

		Document pub;
		MongoCollection<Document> proc = database.getCollection("Proceedings");
		FindIterable<Document> resultProc = proc.find(Filters.eq("_id", id));
		MongoCollection<Document> inProc = database.getCollection("InProceedings");
		FindIterable<Document> resultInProc = inProc.find(Filters.eq("_id", id));

		Iterator<Document> itrProc = resultProc.iterator();
		Iterator<Document> itrInProc = resultInProc.iterator();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if ((!itrProc.hasNext()) && (!itrInProc.hasNext())) {
				System.out.println("Error: Did not find a publication with ID: " + id);
			} else if ((itrProc.hasNext()) && (itrInProc.hasNext())) {
				System.out.println("Error: Query is ambigous.");
			} else {
				if (itrProc.hasNext()) {
					pub = itrProc.next();
				} else {
					pub = itrInProc.next();
				}
				writer.println("The title of the publication with id " + id + " is " + Adaptor.toPublication(pub).getTitle() + ".");
				writer.close();
			}
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	// Find publications by title, returning only a subset of all found publications
	public static void query2(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 2";
		connectToDB();
		createDB();

		MongoCollection<Document> proc = database.getCollection("Proceedings");
		FindIterable<Document> resultProc = proc.find(Filters.regex("title", title));
		MongoCollection<Document> inProc = database.getCollection("InProceedings");
		FindIterable<Document> resultInProc = inProc.find(Filters.regex("title", title));

		int i = 0;
		Iterator<Document> itrProc = resultProc.iterator();
		Iterator<Document> itrInProc = resultInProc.iterator();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if ((!itrProc.hasNext()) && (!itrInProc.hasNext())) {
				System.out.println("Error: Did not find a publication with title: " + title);
			} else {
				while ((itrProc.hasNext() || itrInProc.hasNext()) && i < endOffset) {
					if (i < startOffset) {
						if (itrInProc.hasNext()) {
							itrInProc.next();
						} else {
							itrProc.next();
						}
						i++;
						continue;
					} else if (itrProc.hasNext()) {
						writer.println(Adaptor.toPublication(itrProc.next()).getTitle());
						i++;

					} else if (itrInProc.hasNext()) {
						writer.println(Adaptor.toPublication(itrInProc.next()).getTitle());
						i++;
					}
				}
			}

			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	// Find publications by title, returning only a subset of all found publications ORDERED by title
	public static void query3(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 3";
		connectToDB();
		createDB();

		MongoCollection<Document> proc = database.getCollection("Proceedings");
		FindIterable<Document> resultProc = proc.find(Filters.regex("title", title));
		MongoCollection<Document> inProc = database.getCollection("InProceedings");
		FindIterable<Document> resultInProc = inProc.find(Filters.regex("title", title));
		ArrayList<Publication> all = new ArrayList<Publication>();
		Iterator<Document> itrProc = resultProc.iterator();
		Iterator<Document> itrInProc = resultInProc.iterator();
		boolean non = true;
		while (itrProc.hasNext()) {
			all.add(Adaptor.toPublication(itrProc.next()));
			non = false;
		}
		while (itrInProc.hasNext()) {
			non = false;
			all.add(Adaptor.toPublication(itrInProc.next()));
		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (non) {
				System.out.println("Error: Did not find a publication with title: " + title);
			} else {
				Collections.sort(all, new Comparator<Publication>() {
					public int compare(Publication s1, Publication s2) {
						return s1.getTitle().compareToIgnoreCase(s2.getTitle());
					}
				});
				for (int i = startOffset; i < endOffset; i++) {
					writer.println(all.get(i).getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	// returns name of the co-authors of a given author
	public static void query4(String author) {
		String thisQuery = "Query 4";
		connectToDB();
		createDB();
		Iterator<Document> cursor = myQuery("Person", "name", author);

		HashSet<String> result = new HashSet<String>();
		if (!cursor.hasNext()) {
			System.out.println("Author not found");
			return;
		}
		Person author1 = Adaptor.toPerson(cursor.next());
		for (Publication pub : author1.getAuthoredPublications()) {
			Iterator<Document> cursor1 = myQuery("InProceedings", "_id", pub.getId());
			InProceedings inProc = Adaptor.toInProceedings(cursor1.next());
			for (Person coAuthor : inProc.getAuthors()) {
				Iterator<Document> cursor2 = myQuery("Person", "_id", coAuthor.getId());
				Person per = Adaptor.toPerson(cursor2.next());
				result.add(per.getName());
			}
		}

		for (Publication pub : author1.getEditedPublications()) {
			Iterator<Document> cursor1 = myQuery("Proceedings", "_id", pub.getId());
			Proceedings proc = Adaptor.toProceeding(cursor1.next());
			for (Person coAuthor : proc.getAuthors()) {
				Iterator<Document> cursor2 = myQuery("Person", "_id", coAuthor.getId());
				Person per = Adaptor.toPerson(cursor2.next());
				result.add(per.getName());
			}
		}
		// remove author's own name
		result.remove(author);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			for (String str : result) {
				writer.println(str);
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public static void query5(String name1, String name2) {

		String thisQuery = "Query 5";
		connectToDB();
		createDB();

		Person author1 = new Person();
		Person author2 = new Person();

		Iterator<Document> cursor = myQuery("Person", "name", name1);

		if (!cursor.hasNext()) {
			System.out.println("author 1 not found");
			return;
		} else {
			author1 = Adaptor.toPerson(cursor.next());
		}

		cursor = myQuery("Person", "name", name2);

		if (!cursor.hasNext()) {
			System.out.println("author 2 not found");
			return;
		} else {
			author2 = Adaptor.toPerson(cursor.next());
		}

		int maxDepth = 15;
		int minDistance = Integer.MAX_VALUE;

		for (int i = 2; i < maxDepth; i++) {
			minDistance = query5rec(author1.getId(), author2.getId(), 0, i);
			if (!(minDistance == Integer.MAX_VALUE + 1)) {
				break;
			}

		}

		System.out.println(minDistance);

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (minDistance == Integer.MAX_VALUE + 1) {
				writer.println("No path between the two authors has been found");
			} else {
				writer.println("The shortest path between " + name1 + " and " + name2 + " has length:" + minDistance);
			}
			writer.close();

		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

		DatabaseHelper.closeConnectionDB();
	}

	public static int query5rec(String id1, String id2, int currDepth, int maxDepth) {

		Person author1 = new Person();

		Iterator<Document> cursor = myQuery("Person", "_id", id1);

		if (!cursor.hasNext()) {
			System.out.println("author 1 not found");
			return 100;
		} else {
			author1 = Adaptor.toPerson(cursor.next());
		}
		for (Publication pub : author1.getAuthoredPublications()) {
			cursor = myQuery("InProceedings", "_id", pub.getId());
			InProceedings inProc = Adaptor.toInProceedings(cursor.next());
			for (Person coAuthor : inProc.getAuthors()) {
				if (coAuthor.getId().equals(id2)) {
					return 1;
				}
			}
		}
		if (currDepth > maxDepth) {
			return Integer.MAX_VALUE;
		}
		int currentMinDistance = Integer.MAX_VALUE;

		for (Publication pub : author1.getAuthoredPublications()) {
			cursor = myQuery("InProceedings", "_id", pub.getId());
			InProceedings inProc = Adaptor.toInProceedings(cursor.next());
			for (Person coAuthor : inProc.getAuthors()) {
				if (!coAuthor.getId().equals(id1)) {
					int distance = query5rec(coAuthor.getId(), id2, currDepth + 1, maxDepth);
					if (distance == 1) {
						return distance + 1;
					}
					if (currentMinDistance > distance) {
						currentMinDistance = distance;
					}
				}
			}
		}

		return currentMinDistance + 1;

	}

	// global average of authors / publication (InProceedings + Proceedings)
	public static void query6() {
		String thisQuery = "Query 6";
		connectToDB();
		createDB();
		double authors = 0.;
		MongoCollection<Document> inProc = database.getCollection("InProceedings");
		MongoCollection<Document> proc = database.getCollection("Proceedings");
		double publications = inProc.count() + proc.count();
		Bson field1 = new BasicDBObject("authors", 1);
		Iterator<Document> itr = inProc.find(new Document()).projection(Projections.fields(Projections.include("authors"), Projections.excludeId())).iterator();
		while (itr.hasNext()) {
			String str = itr.next().toJson();
			// System.out.println(str);
			// System.out.println(str.replaceAll("_id", ""));
			// plus 1, because there is one superfluous _id
			authors += (str.length() - str.replaceAll("_id", "").length()) / 3;
			// System.out.println((str.length() - str.replaceAll("_id", "").length()) / 3);
		}
		itr = proc.find(new Document()).projection(field1).iterator();
		while (itr.hasNext()) {
			String str = itr.next().toJson();
			authors += (str.length() - str.replaceAll("_id", "").length()) / 3;
		}
		closeConnectionDB();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (publications != 0) {
				writer.println("Average authors " + (double) (authors / publications) + ".");
			} else {
				writer.println("There are no publications available.");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
	}

	// Returns the number of publications per year between the interval year1 and year 2
	public static void query7(int year1, int year2) {
		String thisQuery = "Query 7";
		connectToDB();
		createDB();

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.printf("%-20s%-12s%-17s%n%n", "Publication Type", "Year", "No. Publications");
			MongoCollection<Document> pubs = database.getCollection("Proceedings");
			AggregateIterable<Document> procs = pubs.aggregate(Arrays.asList(Aggregates.match(Filters.lte("year", year2)), Aggregates.match(Filters.gte("year", year1)), Aggregates.group("$year", Accumulators.sum("year", 1)), Aggregates.sort(Sorts.ascending("year"))));
			Iterator<Document> itrProc = procs.iterator();

			MongoCollection<Document> inProc = database.getCollection("InProceedings");
			AggregateIterable<Document> inProcs = inProc.aggregate(Arrays.asList(Aggregates.match(Filters.lte("year", year2)), Aggregates.match(Filters.gte("year", year1)), Aggregates.group("$year", Accumulators.sum("year", 1)), Aggregates.sort(Sorts.ascending("year"))));
			Iterator<Document> itrInProc = inProcs.iterator();
			int no;
			int year;
			int all;
			Document temp;
			while (itrInProc.hasNext() || itrProc.hasNext()) {
				temp = itrInProc.next();
				no = temp.getInteger("_id");
				year = temp.getInteger("year");
				all = year;
				writer.printf("%-20s%-12s%-17s%n", "InProceedings ", no, year);
				temp = itrProc.next();
				no = temp.getInteger("_id");
				year = temp.getInteger("year");
				writer.printf("%-20s%-12s%-17s%n", "Proceedings ", no, year);
				all = all + year;
				writer.printf("%-20s%-12s%-17s%n%n", "All Publications", no, all);

			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	// No of all publications of a conference, except proceedings
	public static void query8(String conferenceName) {
		connectToDB();
		createDB();

		String thisQuery = "Query 8";

		List<String> resultList = new ArrayList<String>();
		Iterator<Document> cursor = myQuery("Conference", "name", conferenceName);
		if (!cursor.hasNext()) {
			System.out.println("Did not find a Conference with name: " + conferenceName);
			return;
		}
		Conference conference = Adaptor.toConference(cursor.next());
		for (ConferenceEdition e : conference.getEditions()) {
			cursor = myQuery("ConferenceEdition", "_id", e.getId());
			ConferenceEdition edition = Adaptor.toConferenceEdition(cursor.next());

			cursor = myQuery("Proceedings", "_id", edition.getProceedings().getId());
			Proceedings proc = Adaptor.toProceeding(cursor.next());

			for (InProceedings i : proc.getInProceedings()) {
				if (!resultList.contains(i.getId())) {
					resultList.add(i.getId());
				}
			}
		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("The number of all InProceedings of Conference " + conferenceName + "is  " + resultList.size() + ".");
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public static void query9(String confName) {
		connectToDB();
		createDB();

		String thisQuery = "Query 9";

		List<String> resultList = new ArrayList<String>();
		Iterator<Document> cursor = myQuery("Conference", "name", confName);
		if (!cursor.hasNext()) {
			System.out.println("Did not find a Conference with name:" + confName);
			return;
		}
		Conference conference = Adaptor.toConference(cursor.next());
		for (ConferenceEdition e : conference.getEditions()) {
			cursor = myQuery("ConferenceEdition", "_id", e.getId());
			ConferenceEdition edition = Adaptor.toConferenceEdition(cursor.next());

			cursor = myQuery("Proceedings", "_id", edition.getProceedings().getId());
			Proceedings proc = Adaptor.toProceeding(cursor.next());
			for (Person p : proc.getAuthors()) {
				if (!resultList.contains(p.getId())) {
					resultList.add(p.getId());
				}
			}

			for (InProceedings i : proc.getInProceedings()) {
				cursor = myQuery("InProceedings", "_id", i.getId());
				InProceedings inProc = Adaptor.toInProceedings(cursor.next());
				for (Person p : inProc.getAuthors()) {
					if (!resultList.contains(p.getId())) {
						resultList.add(p.getId());
					}
				}
			}
		}

		closeConnectionDB();

		p(resultList.size());
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("Number of authors and editors:" + resultList.size());
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

	}

	public static void query11(String confName) {
		connectToDB();
		createDB();

		String thisQuery = "Query 11";

		List<String> resultList = new ArrayList<String>();
		Iterator<Document> cursor = myQuery("Conference", "name", confName);
		if (!cursor.hasNext()) {
			System.out.println("Did not find a Conference with name:" + confName);
			return;
		}
		Conference conference = Adaptor.toConference(cursor.next());
		for (ConferenceEdition e : conference.getEditions()) {
			cursor = myQuery("ConferenceEdition", "_id", e.getId());
			ConferenceEdition edition = Adaptor.toConferenceEdition(cursor.next());

			cursor = myQuery("Proceedings", "_id", edition.getProceedings().getId());
			Proceedings proc = Adaptor.toProceeding(cursor.next());

			for (InProceedings i : proc.getInProceedings()) {
				if (!resultList.contains(i.getId())) {
					resultList.add(i.getId());
				}
			}
		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of all " + resultList.size() + " InProceedings of Conference " + confName);
			for (String id : resultList) {
				cursor = myQuery("InProceedings", "_id", id);
				InProceedings inProc = Adaptor.toInProceedings(cursor.next());

				writer.println(inProc.getTitle());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

		closeConnectionDB();
	}

	public static void query10(String confName) {
		connectToDB();
		createDB();

		String thisQuery = "Query 10";

		List<String> resultList = new ArrayList<String>();
		Iterator<Document> cursor = myQuery("Conference", "name", confName);
		if (!cursor.hasNext()) {
			System.out.println("Did not find a Conference with name:" + confName);
			return;
		}
		Conference conference = Adaptor.toConference(cursor.next());
		for (ConferenceEdition e : conference.getEditions()) {
			cursor = myQuery("ConferenceEdition", "_id", e.getId());
			ConferenceEdition edition = Adaptor.toConferenceEdition(cursor.next());

			cursor = myQuery("Proceedings", "_id", edition.getProceedings().getId());
			Proceedings proc = Adaptor.toProceeding(cursor.next());
			for (Person p : proc.getAuthors()) {
				if (!resultList.contains(p.getId())) {

					resultList.add(p.getId());
				}
			}

			for (InProceedings i : proc.getInProceedings()) {
				cursor = myQuery("InProceedings", "_id", i.getId());
				InProceedings inProc = Adaptor.toInProceedings(cursor.next());
				for (Person p : inProc.getAuthors()) {
					if (!resultList.contains(p.getId())) {
						resultList.add(p.getId());
					}
				}
			}
		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of all " + resultList.size() + " authors and editors");
			for (String id : resultList) {
				cursor = myQuery("Person", "_id", id);
				Person person = Adaptor.toPerson(cursor.next());

				writer.println(person.getName());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

		closeConnectionDB();

	}

	public static void query12() {

		String thisQuery = "Query 12";
		List<String> resultList = new ArrayList<String>();
		List<Publication> allProceedings = getAllProceedings();

		connectToDB();
		createDB();

		for (Publication p : allProceedings) {
			List<String> authorsOfProc = new ArrayList<String>();
			for (Person pers : p.getAuthors()) {
				authorsOfProc.add(pers.getId());
			}

			for (InProceedings i : ((Proceedings) p).getInProceedings()) {
				Iterator<Document> cursor = myQuery("InProceedings", "_id", i.getId());
				InProceedings inProceedings = Adaptor.toInProceedings(cursor.next());
				for (Person pers : inProceedings.getAuthors()) {
					if (authorsOfProc.contains(pers.getId()) && !resultList.contains(pers.getId())) {
						resultList.add(pers.getId());
					}
				}
			}

		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of all " + resultList.size() + " authors that occur as editor in Proceeding as well as in InProceeding as and author:");
			for (String id : resultList) {
				Iterator<Document> cursor = myQuery("Person", "_id", id);
				Person person = Adaptor.toPerson(cursor.next());

				writer.println(person.getName());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

		closeConnectionDB();

	}

	// all publications, where given author is mentioned last
	public static void query13(String author) {
		int count = 0;
		String thisQuery = "Query 13";
		connectToDB();
		createDB();

		MongoCollection<Document> pers = database.getCollection("Person");
		FindIterable<Document> person = pers.find(Filters.eq("name", author)).projection(Projections.include("_id"));
		String authorId = person.first().toJson().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("_id", "").replaceAll(":", "").replaceAll("\"", "").trim();
		// System.out.println("author id :" + authorId);
		int idLength = authorId.length();
		MongoCollection<Document> inProcs = database.getCollection("InProceedings");
		AggregateIterable<Document> conferences = inProcs.aggregate(Arrays.asList(Aggregates.match(Filters.elemMatch("authors", Filters.eq(authorId))), Aggregates.project(Projections.include("authors")), Aggregates.project(Projections.excludeId())));
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			Iterator<Document> itr = conferences.iterator();
			while (itr.hasNext()) {
				String res = itr.next().toJson().replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("_id", "").replaceAll("authors", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(":", "").replaceAll(",", "").trim();
				// System.out.println(res);
				if (res.substring(res.length() - idLength, res.length()).equals(authorId)) {
					// System.out.println(res.substring(res.length()-idLength, res.length()));
					count++;
				}
			}
			writer.println("The number of publications where the author named " + author + " is mentioned last is " + count + ".");
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public static void query14(int year1, int year2) {

		String thisQuery = "Query 14";
		List<String> resultList = new ArrayList<String>();
		List<Publication> allProceedings = getAllProceedings();
		List<Publication> allInProceedings = getAllInProceedings();
		connectToDB();
		createDB();

		for (Publication p : allProceedings) {
			List<String> authorsOfProc = new ArrayList<String>();
			for (Person pers : p.getAuthors()) {
				authorsOfProc.add(pers.getId());
			}
			boolean found = false;
			for (Publication i : allInProceedings) {
				for (Person pers : i.getAuthors()) {
					if (authorsOfProc.contains(pers.getId()) && i.getYear() >= year1 && i.getYear() <= year2) {
						String pubID = ((Proceedings) p).getPublisher().getId();
						if (!resultList.contains(pubID)) {
							resultList.add(pubID);
						}
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}

		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of all " + resultList.size() + " Publishers.....:");
			for (String id : resultList) {
				Iterator<Document> cursor = myQuery("Publisher", "_id", id);
				Publisher publisher = Adaptor.toPublisher(cursor.next());

				writer.println(publisher.getName());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

		closeConnectionDB();

	}
}
