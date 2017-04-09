package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * A specialized type of publications, represents the proceedings released at a certain conference edition. The proceedings contains all the articles published at that conference edition.
 */
public class Proceedings extends Publication {
	public Proceedings(Integer num) {
		super(num);
		// TODO Auto-generated constructor stub
	}

	public Proceedings() {
		super(1);
		// TODO Auto-generated constructor stub
	}

	String note;
	int number;
	Publisher publisher;
	String volume;
	String isbn;
	Series series;
	ConferenceEdition confEdition;
	Set<InProceedings> inProceedings = new HashSet<InProceedings>();

	public String getNote() {
		return note;
	}
	
	public String getID(){
		return super.getId();
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public ConferenceEdition getConferenceEdition() {
		return confEdition;
	}

	public void setConferenceEdition(ConferenceEdition conferenceEdition) {
		this.confEdition = conferenceEdition;
	}

	public Set<InProceedings> getInProceedings() {
		return inProceedings;
	}

	public void setInProceedings(Set<InProceedings> inProceedings) {
		this.inProceedings = inProceedings;
	}

	public void addInProceedings(InProceedings i){
		this.inProceedings.add(i);
	}
}