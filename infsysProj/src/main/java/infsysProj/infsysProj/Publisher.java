package main.java.infsysProj.infsysProj;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a publisher. Besides the name it also contains references to all of the publications published by the publisher.
 */
public class Publisher extends DomainObject {
	String name;
	Set<Publication> publications = new HashSet<Publication>();

	public String getName() {
		zooActivateRead();
		return name;
	}

	public void setName(String name) {
		zooActivateWrite();
		this.name = name;
	}

	public Set<Publication> getPublications() {
		zooActivateRead();
		return publications;
	}

	public void setPublications(Set<Publication> publications) {
		zooActivateWrite();
		this.publications = publications;
	}
	
	public void addPublication(Publication p){
		zooActivateWrite();
		this.publications.add(p);
	}

}
