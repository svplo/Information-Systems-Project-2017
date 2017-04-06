package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a person. The person can have the role of an author or of an editor for some publications.
 */
public class Person extends DomainObject {

	private String name;
	private Set<Publication> authoredPublications = new HashSet<Publication>();
	private Set<Publication> editedPublications = new HashSet<Publication>();

	@SuppressWarnings("unused")
	public Person() {
		// All persistent classes need a no-args constructor.
		// The no-args constructor can be private.
	}
	
	public Person(String id, String name, Set<Publication> aPub, Set<Publication> ePub) {
		this.setId(id);
		this.setName(name);
		this.setAuthoredPublications(aPub);
		this.setEditedPublications(ePub);
	}

	public Person(String name, Set<Publication> aPub, Set<Publication> ePub) {
		this.setName(name);
		this.setAuthoredPublications(aPub);
		this.setEditedPublications(ePub);
	}
	
	public Person(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Publication> getAuthoredPublications() {
		return authoredPublications;
	}

	public void setAuthoredPublications(Set<Publication> authoredPublications) {
		this.authoredPublications = authoredPublications;
	}

	public Set<Publication> getEditedPublications() {
		return editedPublications;
	}

	public void setEditedPublications(Set<Publication> editedPublications) {
		this.editedPublications = editedPublications;
	}
}
