package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import constraints.CheckYear;

/**
 * Defines the base state for a publication. Is inherited by all specialized types of publications.
 */
public class Publication extends DomainObject {
	@NotNull(message = "Title cannot be null")
	@NotBlank(message = "Title cannot be empty")
	String title;
	
	@NotNull(message = "Authors cannot be null")
	@NotEmpty(message = "Authors cannot be empty")
	List<Person> authors = new LinkedList<Person>();
	
	@Min(value = 1901, message = "Minimum value for year is 1901")
	@CheckYear(message = "Year cannot be larger than currentYear+1")
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