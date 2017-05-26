package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Defines the base state for a publication. Is inherited by all specialized types of publications.
 */
public class Publication extends DomainObject {
	@NotNull
	String title;
	List<Person> authors = new LinkedList<Person>();
	
	@Min(value = 1901)
	@Max(value = 1)
	int year;
	String electronicEdition;

	public Publication(Integer num) {
		
	}

	public String getTitle() {
		zooActivateRead();
		return title;
	}

	public void setTitle(String title) {
		zooActivateWrite();
		this.title = title;
	}

	public List<Person> getAuthors() {
		zooActivateRead();
		return authors;
	}

	public void setAuthors(List<Person> authors) {
		zooActivateWrite();
		this.authors = authors;
	}

	public Integer getYear() {
		zooActivateRead();
		return year;
	}

	public void setYear(int year) {
		zooActivateWrite();
		this.year = year;
	}

	public String getElectronicEdition() {
		zooActivateRead();
		return electronicEdition;
	}

	public void setElectronicEdition(String electronicEdition) {
		zooActivateWrite();
		this.electronicEdition = electronicEdition;
	}

	public void addAuthor(Person p){
		zooActivateWrite();
		this.authors.add(p);
	}

}