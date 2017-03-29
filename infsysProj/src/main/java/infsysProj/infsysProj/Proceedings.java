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
		zooActivateRead();
		return note;
	}

	public String getID() {

		return super.getId();
	}

	public void setNote(String note) {
		zooActivateWrite();
		this.note = note;
	}

	public int getNumber() {
		zooActivateRead();
		return number;
	}

	public void setNumber(int number) {
		zooActivateWrite();
		this.number = number;
	}

	public Publisher getPublisher() {
		zooActivateRead();
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		zooActivateWrite();
		this.publisher = publisher;
	}

	public String getVolume() {
		zooActivateRead();
		return volume;
	}

	public void setVolume(String volume) {
		zooActivateWrite();
		this.volume = volume;
	}

	public String getIsbn() {
		zooActivateRead();
		return isbn;
	}

	public void setIsbn(String isbn) {
		zooActivateWrite();
		this.isbn = isbn;
	}

	public Series getSeries() {
		zooActivateRead();
		return series;
	}

	public void setSeries(Series series) {
		zooActivateWrite();
		this.series = series;
	}

	public ConferenceEdition getConferenceEdition() {
		zooActivateRead();
		return confEdition;
	}

	public void setConferenceEdition(ConferenceEdition conferenceEdition) {
		zooActivateWrite();
		this.confEdition = conferenceEdition;
	}

	public Set<InProceedings> getInProceedings() {
		zooActivateRead();
		return inProceedings;
	}

	public void setInProceedings(Set<InProceedings> inProceedings) {
		zooActivateWrite();
		this.inProceedings = inProceedings;
	}

}