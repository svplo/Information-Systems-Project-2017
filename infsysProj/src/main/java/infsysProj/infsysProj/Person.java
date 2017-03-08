package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Represents a person. The person can have the role of an author
 * or of an editor for some publications.
 */
@PersistenceCapable
public class Person extends DomainObject {
	
	private String name;
	private Set<Publication> authoredPublications = new HashSet<>();
	private Set<Publication> editedPublications = new HashSet<>();
	
    @SuppressWarnings("unused")
	private Person() {
        // All persistent classes need a no-args constructor. 
        // The no-args constructor can be private.
    }
    
    public Person(String name) {
        // no activation required
        this.name = name;
    }
    
    public String getName(){
    	//prepare objects for reading
    	zooActivateRead();
    	return name;
    }

    public void setName(String name){
    	//prepare objects for writing
    	zooActivateWrite();
    	this.name = name;
    }

    public Set<Publication> getAuthoredPublications(){
    	return authoredPublications;
    }

    public void setAuthoredPublications(Set<Publication> authoredPublications){
    	this.authoredPublications = authoredPublications;
    }

    public Set<Publication> getEditedPublications(){
    	return editedPublications;
    }
    public void setEditedPublications(Set<Publication> editedPublications){
    	this.editedPublications = editedPublications;
    }
}
