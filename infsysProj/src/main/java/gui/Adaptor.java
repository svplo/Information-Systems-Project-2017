package gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.BasicDBObject;

import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class Adaptor {

	public static final Document toDBDocument(Conference conf) {
		Document doc = new Document("_id", conf.getId())
				.append("name", conf.getName());
		
		List<BasicDBObject> conferenceEditions = new ArrayList<>();

		for(ConferenceEdition e : conf.getEditions()){
			conferenceEditions.add(new BasicDBObject("_id",e.getId() ));
		}
		doc.append("conferenceEditions", conferenceEditions);

		return doc;
	}
	
	public static final Conference toConference(Document doc) {
		Conference conf = new Conference();
		conf.setId((String)doc.get("_id"));
		conf.setName((String)doc.get("name"));

		Set<ConferenceEdition> conferenceEditions = new HashSet<ConferenceEdition>();
		List<Document> conferenceEditionsIDDoc = ((List<Document>)doc.get("conferenceEditions"));
		for(Document d: conferenceEditionsIDDoc){
			ConferenceEdition proc = new ConferenceEdition();
			proc.setId((String)d.get("_id"));
			conferenceEditions.add(proc);
		}
		
		conf.setEditions(conferenceEditions);

		return conf;
	}

	public static final Document toDBDocument(ConferenceEdition confEdition) {
		Document doc = new Document("_id", confEdition.getId())
				.append("conference", confEdition.getConference().getId())
				.append("proceedings", confEdition.getProceedings().getId())
				.append("year", confEdition.getYear());
		
		
		return doc;
	}
	
	public static final ConferenceEdition toConferenceEdition(Document doc) {
		ConferenceEdition confEdition = new ConferenceEdition();
		confEdition.setId((String)doc.get("_id"));
		confEdition.setYear((int)doc.getInteger("year"));

		Conference conference = new Conference();
		conference.setId(doc.getString("conference"));
		confEdition.setConference(conference);
		Proceedings proc = new Proceedings();
		proc.setId(doc.getString("proceedings"));
		confEdition.setProceedings(proc);
		return confEdition;
	}

	public static final Document toDBDocument(InProceedings inProceeding) {
		Document doc = new Document("_id", inProceeding.getId())
				.append("note", inProceeding.getNote())
				.append("title", inProceeding.getTitle())
				.append("electronicEdition", inProceeding.getElectronicEdition())
				.append("pages", inProceeding.getPages())
				.append("year", inProceeding.getYear());
		
		if(inProceeding.getProceedings() != null){
			doc.append("proceedings", inProceeding.getProceedings().getId());
		}
		else{
			doc.append("proceedings", "");
		}
		List<BasicDBObject> authors = new ArrayList<>();

		for(Person p : inProceeding.getAuthors()){
			authors.add(new BasicDBObject("_id",p.getId() ));		
		}
		doc.append("authors", authors);

		return doc;
	}

	public static final InProceedings toInProceedings(Document doc) {
		InProceedings proc = new InProceedings();
		proc.setId((String)doc.get("_id"));
		proc.setNote(doc.getString("note"));
		proc.setElectronicEdition((String)doc.get("electronicEdition"));
		proc.setTitle((String) doc.get("title"));
		proc.setYear((int)doc.getInteger("year"));
		
		List<Person> authors = new ArrayList<Person>();
		List<Document> authorsIDsDoc = ((List<Document>)doc.get("authors"));
		for(Document d: authorsIDsDoc){
			Person aut = new Person();
			aut.setId((String)d.get("_id"));
			authors.add(aut);
		}
		
		proc.setAuthors(authors);

		
		Proceedings pr = new Proceedings();
		pr.setId((String) doc.get("proceedings"));
		proc.setProceedings(pr);
		return proc;
	}

	public static final Document toDBDocument(Person person) {
		Document doc = new Document("_id", person.getId())
				.append("name", person.getName());
		
		List<BasicDBObject> authoredPublications = new ArrayList<>();
		List<BasicDBObject> editedPublications = new ArrayList<>();

		for(Publication p : person.getAuthoredPublications()){
			authoredPublications.add(new BasicDBObject("_id", p.getId()));
		}
		for(Publication p : person.getEditedPublications()){
			editedPublications.add(new BasicDBObject("_id", p.getId()));
		}

		doc.append("authoredPublications", authoredPublications);
		doc.append("editedPublications", editedPublications);

		return doc;
	}
	
	public static final Person toPerson(Document doc) {
		Person person = new Person();
		person.setId((String)doc.get("_id"));
		person.setName(doc.getString("name"));	
		
		Set<Publication> IDs = new HashSet<Publication>();
		List<Document> inProceedingsIDsDoc = ((List<Document>)doc.get("authoredPublications"));
		for(Document d: inProceedingsIDsDoc){
			InProceedings inProc = new InProceedings();
			inProc.setId((String)d.get("_id"));
			IDs.add(inProc);
		}
		
		Set<Publication> ProcIDs = new HashSet<Publication>();
		List<Document> proceedingsIDsDoc = ((List<Document>)doc.get("editedPublications"));
		for(Document d: proceedingsIDsDoc){
			Proceedings proc = new Proceedings();
			proc.setId((String)d.get("_id"));
			ProcIDs.add(proc);
		}
		
		person.setAuthoredPublications(IDs);
		person.setEditedPublications(ProcIDs);
		

		return person;
	}
	
	public static final Document toDBDocument(Publication pub) {
		Document doc = new Document("_id", pub.getId())
				.append("electronicEdition", pub.getElectronicEdition())
				.append("title", pub.getTitle())
				.append("year", pub.getYear());
		List<BasicDBObject> authors = new ArrayList<>();

		for(Person p : pub.getAuthors()){
			authors.add(new BasicDBObject("_id",p.getId() ));		
		}
		doc.append("authors", authors);		
		
		return doc;
	}
	
	public static final Publication toPublication(Document doc) {
		Publication pub = new Publication(0);
		pub.setId((String)doc.get("_id"));
		pub.setTitle(doc.getString("title"));		
		pub.setElectronicEdition((String)doc.get("electronicEdition"));
		pub.setYear((int)doc.getInteger("year"));

		return pub;
	}

	public static final Document toDBDocument(Publisher publisher) {
		Document doc = new Document("_id", publisher.getId())
				.append("name", publisher.getName());
		
		List<BasicDBObject> publications = new ArrayList<>();

		for(Publication p : publisher.getPublications()){
			publications.add(new BasicDBObject("_id", p.getId()));
		}

		doc.append("publications", publications);
		
		return doc;
	}
	
	public static final Publisher toPublisher(Document doc) {
		Publisher publisher = new Publisher();
		publisher.setId((String)doc.get("_id"));
		publisher.setName(doc.getString("name"));		

		Set<Publication> publications = new HashSet<Publication>();
		List<Document> conferenceEditionsIDDoc = ((List<Document>)doc.get("publications"));
		for(Document d: conferenceEditionsIDDoc){
			Publication proc = new Publication(0);
			proc.setId((String)d.get("_id"));
			publications.add(proc);
		}
		
		publisher.setPublications(publications);

		return publisher;
	}

	public static final Document toDBDocument(Series series) {
		Document doc = new Document("_id", series.getId())
				.append("name", series.getName());
		
		List<BasicDBObject> publications = new ArrayList<>();

		for(Publication p : series.getPublications()){
			publications.add(new BasicDBObject("_id", p.getId()));
		}

		doc.append("publications", publications);

		
		
		return doc;
	}
	
	public static final Series toSeries(Document doc) {
		Series series = new Series();
		series.setId((String)doc.get("_id"));
		series.setName(doc.getString("name"));		

		Set<Publication> publications = new HashSet<Publication>();
		List<Document> conferenceEditionsIDDoc = ((List<Document>)doc.get("publications"));
		for(Document d: conferenceEditionsIDDoc){
			Publication proc = new Publication(0);
			proc.setId((String)d.get("_id"));
			publications.add(proc);
		}
		
		series.setPublications(publications);
		
		return series;
	}

	public static final Document toDBDocument(Proceedings proceeding) {

    	Document doc = new Document("_id", proceeding.getId())
				.append("note", proceeding.getNote())
				.append("publisher", proceeding.getPublisher().getId())
				.append("volume", proceeding.getVolume())
				.append("isbn", proceeding.getIsbn())
				.append("number", proceeding.getNumber())				
				.append("series", proceeding.getSeries().getId())
				.append("conferenceEdition", proceeding.getConferenceEdition().getId())
				.append("year", proceeding.getYear())
				.append("electronicEdition", proceeding.getElectronicEdition())
    			.append("title", proceeding.getTitle());
    			
    	
		List<BasicDBObject> inProceedings = new ArrayList<>();

		for(InProceedings i : proceeding.getInProceedings()){
			inProceedings.add(new BasicDBObject("_id", i.getId()));
		}

		doc.append("inProceedings", inProceedings);
    	
		
		List<BasicDBObject> authors = new ArrayList<>();

		for(Person p : proceeding.getAuthors()){
			authors.add(new BasicDBObject("_id",p.getId() ));		
		}
		doc.append("authors", authors);

    	return doc;
    			
    	    			
	}

	public static final Proceedings toProceeding(Document doc) {
		Proceedings proc = new Proceedings();
		proc.setId((String)doc.get("_id"));
		proc.setNote(doc.getString("note"));
		proc.setVolume(doc.getString("volume"));
		proc.setIsbn((String)doc.get("isbn"));
		proc.setElectronicEdition((String)doc.get("electronicEdition"));
		proc.setTitle((String) doc.get("title"));
		proc.setYear((int)doc.getInteger("year"));
		
		List<Person> authors = new ArrayList<Person>();
		List<Document> authorsIDsDoc = ((List<Document>)doc.get("authors"));
		for(Document d: authorsIDsDoc){
			Person aut = new Person();
			aut.setId((String)d.get("_id"));
			authors.add(aut);
		}
		proc.setAuthors(authors);

		Series series = new Series();
		series.setId(doc.getString("series"));
		proc.setSeries(series);
		
		Publisher publisher = new Publisher();
		publisher.setId(doc.getString("publisher"));
		proc.setPublisher(publisher);

		ConferenceEdition confE = new ConferenceEdition();
		confE.setId(doc.getString("conferenceEdition"));
		proc.setConferenceEdition(confE);

		return proc;
	}



}
