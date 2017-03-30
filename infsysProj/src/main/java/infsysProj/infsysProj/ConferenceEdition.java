package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

/**
 * Represents one edition of a conference. It contains a reference to the proceedings published after the conference edition.
 */
public class ConferenceEdition extends DomainObject {
	Conference conf;
	int year;
	Proceedings proceedings;

	public Conference getConference() {
		return conf;
	}

	public void setConference(Conference conference) {
		this.conf = conference;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Proceedings getProceedings() {
		return proceedings;
	}

	public void setProceedings(Proceedings proceedings) {
		this.proceedings = proceedings;
	}

}