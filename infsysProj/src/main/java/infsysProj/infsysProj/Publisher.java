package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.Set;

/**
 *  Represents a publisher. Besides the name it also contains references to all
 *  of the publications published by the publisher.
 */
public class Publisher extends DomainObject {

    public String getName(){
    	return "";
    }

    public void setName(String name){
    	
    }

    public Set<Publication> getPublications(){
    	return null;
    }

    public void setPublications(Set<Publication> publications){
    	
    }
    
}