package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a series of publications. Besides the name it also contains references to all of the publications published by in the same series.
 */
public class Series extends DomainObject {
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

}