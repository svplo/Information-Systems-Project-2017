package infsysProj.infsysProj;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a publisher. Besides the name it also contains references to all of the publications published by the publisher.
 */
public class Publisher extends DomainObject {
	String name;
	Set<Publication> publications = new HashSet<Publication>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Publication> getPublications() {
		return publications;
	}

	public void setPublications(Set<Publication> publications) {
		this.publications = publications;
	}

}
