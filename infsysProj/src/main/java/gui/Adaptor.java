package gui;

import java.util.ArrayList;
import java.util.List;

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
		for(Publication p : person.getAuthoredPublications()){
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
		pub.setTitle((String) doc.get("title"));
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
		

		return proc;
	}



}
