package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a certain conference. A conference has more conference editions,
 * usually one edition per year.
 */
public class Conference extends DomainObject {
	String name;
	Set<ConferenceEdition> editions = new HashSet<ConferenceEdition>();
	
    public String getName(){
    	zooActivateRead();
    	return name;
    }

    public void setName(String name){
    	zooActivateWrite();
    	this.name = name;
    }
    public Set<ConferenceEdition> getEditions(){
    	zooActivateRead();
    	return editions;
    }
    public void setEditions(Set<ConferenceEdition> editions){
    	zooActivateWrite();
    	this.editions = editions;
    }
    
}