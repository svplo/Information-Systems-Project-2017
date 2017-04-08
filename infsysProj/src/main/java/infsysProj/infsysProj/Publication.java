package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Defines the base state for a publication. Is inherited by all specialized types of publications.
 */
public class Publication extends DomainObject {
	String title;
	List<Person> authors = new LinkedList<Person>();
	int year;
	String electronicEdition;

	public Publication(Integer num) {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Person> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Person> authors) {
		this.authors = authors;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getElectronicEdition() {
		return electronicEdition;
	}

	public void setElectronicEdition(String electronicEdition) {
		this.electronicEdition = electronicEdition;
	}

	public void addAuthor(Person p){
		this.authors.add(p);
	}

}