package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

/**
 * Represents one edition of a conference. It contains a reference
 * to the proceedings published after the conference edition.
 */
public class ConferenceEdition extends DomainObject {
	Conference conf;
	int year;
	Proceedings proceedings;
	
    public Conference getConference(){
    	zooActivateRead();
    	return conf;
    }

    public void setConference(Conference conference){
    	zooActivateWrite();
    	this.conf = conference;
    }

    public int getYear(){
    	zooActivateRead();
    	return year;
    }

    public void setYear(int year){
    	zooActivateWrite();
    	this.year = year;
    }

    public Proceedings getProceedings(){
    	zooActivateRead();
    	return proceedings;
    }

    public void setProceedings(Proceedings proceedings){
    	zooActivateWrite();
    	this.proceedings = proceedings;
    }
    
}