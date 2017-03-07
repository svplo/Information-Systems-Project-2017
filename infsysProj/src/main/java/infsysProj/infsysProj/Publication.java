package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

import java.util.List;

/**
 * Defines the base state for a publication. Is inherited by all specialized
 * types of publications.
 */
public class Publication extends DomainObject {

    public String getTitle(){
    	return "";
    }

    public void setTitle(String title){
    	
    }

    public List<Person> getAuthors(){
    	return null;
    }

    public void setAuthors(List<Person> authors){
    	
    }

    public int getYear(){
    	return 0;
    }

    public void setYear(int year){
    	
    }

    public String getElectronicEdition(){
    	return "";
    }

    public void setElectronicEdition(String electronicEdition){
    	
    }
    
}