package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.Set;

/**
 * Represents a certain conference. A conference has more conference editions,
 * usually one edition per year.
 */
public class Conference extends DomainObject {

    public String getName(){
    	return "";
    }

    public void setName(String name){
    	
    }
    public Set<ConferenceEdition> getEditions(){
    	return null;
    }
    public void setEditions(Set<ConferenceEdition> editions){
    	
    }
    
}