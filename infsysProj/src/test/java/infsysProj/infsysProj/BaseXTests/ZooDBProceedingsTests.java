package infsysProj.infsysProj.BaseXTests;

import gui.*;
import infsysProj.infsysProj.Proceedings;

import java.util.ArrayList;

import junit.framework.TestCase;

public class ZooDBProceedingsTests extends TestCase {

    public DatabaseHelperZooDB dbHelper = new DatabaseHelperZooDB();
	public Proceedings p;
	public ArrayList<String> authors = new ArrayList<String>();
	public int year;

	// assigning the values
	protected void setUp() {
		p = new Proceedings();
		p.setIsbn("77");
		p.setNote("");
		p.setElectronicEdition("");
		p.setVolume("");
		authors.add("Per Baacke");
		
		p.setYear(1999);
	}

	// no title
	public void testErrNoTitle() {
		
		p.setTitle("");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Per Baacke");

        try { 
        	
            dbHelper.addProceeding(p, authors, new ArrayList<String>(), "", "", "", year , false);
            fail("Should raise an Error");
       } 
       catch (Error success) {
       } 
	}
	
	//year below 1901
	public void testErrYearBelow1901() {
		p.setTitle("Title");
		year = 1899;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Per Baacke");
		
        try { 
            dbHelper.addProceeding(p, authors, new ArrayList<String>(), "", "", "", year , false);
            fail("Should raise an Error");
       } 
       catch (Error success) {
       } 
	}

	//year above 2017
	public void testErrYearAbove2017() {
		p.setTitle("Title");
		year = 2020;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Per Baacke");

        try { 
            dbHelper.addProceeding(p, authors, new ArrayList<String>(), "", "", "", year , false);
            fail("Should raise an Error");
       } 
       catch (Error success) {
       } 
	}

	//authors list empty
	public void testErrNoAuthors() {
		p.setTitle("Title");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();

        try { 
            dbHelper.addProceeding(p, authors, new ArrayList<String>(), "", "", "", year , false);
            fail("Should raise an Error");
       } 
       catch (Error success) {
       } 
	}
	
	public void testOk() {
		p.setTitle("Title");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Per Baacke");

        try { 
            dbHelper.addProceeding(p, authors, new ArrayList<String>(), "bla", "bla", "bla", year , false);
       } 
       catch (Error success) {
           fail("Should not raise an Error");

       } 
	}


}
