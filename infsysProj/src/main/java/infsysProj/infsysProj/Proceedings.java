package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;


import java.util.Set;

/**
 * A specialized type of publications, represents the proceedings released at
 * a certain conference edition. The proceedings contains all the articles published
 * at that conference edition.
 */
public class Proceedings extends Publication {

    public String getNote(){
    	return "";
    }

    public void setNote(String note){
    	
    }

    public int getNumber(){
    	return 0;
    }

    public void setNumber(int number){
    	
    }

    public Publisher getPublisher(){
    	return null;
    }

    public void setPublisher(Publisher publisher){
    	
    }

    public String getVolume(){
    	return "";
    }

    public void setVolume(String volume){
    	
    }
    
    public String getIsbn(){
    	return "";
    }
    
    public void setIsbn(String isbn){
    	
    }

    public Series getSeries(){
    	return null;
    }

    public void setSeries(Series series){
    	
    }

    public ConferenceEdition getConferenceEdition(){
    	return null;
    }

    public void setConferenceEdition(ConferenceEdition conferenceEdition){
    	
    }

    public Set<InProceedings> getPublications(){
    	return null;
    }

    public void setPublications(Set<InProceedings> publications){
    	
    }

}