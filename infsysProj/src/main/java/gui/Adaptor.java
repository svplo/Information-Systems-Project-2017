package gui;

import org.bson.Document;


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
		return new Document("_id", conf.getId())
				.append("name", conf.getName())
				.append("editions", conf.getEditions());
	}

	public static final Document toDBDocument(ConferenceEdition confEdition) {
		return new Document("_id", confEdition.getId())
				.append("conference", confEdition.getConference())
				.append("proceedings", confEdition.getProceedings())
				.append("year", confEdition.getYear());
	}
	
	public static final Document toDBDocument(InProceedings inProceeding) {
		return new Document("_id", inProceeding.getId())
				.append("note", inProceeding.getNote())
				.append("title", inProceeding.getTitle())
				.append("authors", inProceeding.getAuthors())
				.append("electronicEdition", inProceeding.getElectronicEdition())
				.append("pages", inProceeding.getPages())
				.append("proceedings", inProceeding.getProceedings())
				.append("year", inProceeding.getYear());
	}

	public static final Document toDBDocument(Person person) {
		return new Document("_id", person.getId())
				.append("name", person.getName())
				.append("authoredPublications", person.getAuthoredPublications())
				.append("editedPublications", person.getEditedPublications());
	}
	
	public static final Document toDBDocument(Proceedings proceeding) {
		return new Document("_id", proceeding.getId())
				.append("note", proceeding.getNote())
				.append("publisher", proceeding.getPublisher())
				.append("volume", proceeding.getVolume())
				.append("isbn", proceeding.getIsbn())
				.append("series", proceeding.getSeries())
				.append("conferenceEdition", proceeding.getConferenceEdition())
				.append("inProceeding", proceeding.getInProceedings());
	}
	
	public static final Document toDBDocument(Publication pub) {
		return new Document("_id", pub.getId())
				.append("authors", pub.getAuthors())
				.append("electronicEdition", pub.getElectronicEdition())
				.append("title", pub.getTitle())
				.append("year", pub.getYear());
	}
	
	public static final Document toDBDocument(Publisher publisher) {
		return new Document("_id", publisher.getId())
				.append("name", publisher.getName())
				.append("publications", publisher.getPublications());
	}

	public static final Document toDBDocument(Series series) {
		return new Document("_id", series.getId())
				.append("name", series.getName())
				.append("publications", series.getPublications());
	}

}
