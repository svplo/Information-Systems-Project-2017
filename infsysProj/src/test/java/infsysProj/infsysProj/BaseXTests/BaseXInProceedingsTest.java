package infsysProj.infsysProj.BaseXTests;

import gui.*;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Proceedings;

import java.util.ArrayList;

import junit.framework.TestCase;

public class BaseXInProceedingsTest extends TestCase {

    public DatabaseHelperBaseX dbHelper = new DatabaseHelperBaseX();
	public InProceedings p;
	public ArrayList<String> authors = new ArrayList<String>();
	public int year;

	// assigning the values
	protected void setUp() {
		p = new InProceedings();
		p.setNote("");
		p.setElectronicEdition("");
		authors.add("Peter");
		
		p.setYear(1999);
	}

	// no title
	public void testErrNoTitle() {
		
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("");
		p.setNote("Draft");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);
            fail("Should raise an Error");

       } 
       catch (Error success) {

       } 
	}
	
	//year below 1901
	public void testErrYearBelow1901() {
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Draft");
		year = 1899;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);
            fail("Should raise an Error");

       } 
       catch (Error success) {

       } 

	}

	//year above 2017
	public void testErrYearAbove2017() {
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Draft");
		year = 2020;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);
            fail("Should raise an Error");

       } 
       catch (Error success) {

       } 

	}

	//authors list empty
	public void testErrNoAuthors() {
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Draft");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);
            fail("Should raise an Error");

       } 
       catch (Error success) {

       } 

	}
	
	public void testErrPagesFormat(){
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("44ll");
		p.setTitle("Title");
		p.setNote("Draft");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);
            fail("Should raise an Error");

       } 
       catch (Error success) {

       } 

	}
	
	public void testOkPagesFormat(){
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4-6");
		p.setTitle("Title");
		p.setNote("Draft");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);

       } 
       catch (Error success) {
           fail("Should not raise an Error");

       } 

	}

	public void testErrNote(){
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("MyNote");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);
            fail("Should raise an Error");

       } 
       catch (Error success) {

       } 

	}
	public void testErrProceedingIsNull(){
		Proceedings proc = new Proceedings();
		proc.setTitle("");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Draft");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);
            fail("Should raise an Error");

       } 
       catch (Error success) {

       } 

	}
	public void testOkNote1(){
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Draft");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);

       } 
       catch (Error success) {
           fail("Should not raise an Error");

       } 

	}
	public void testOkNote2(){
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Submitted");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);

       } 
       catch (Error success) {
           fail("Should not raise an Error");

       } 

	}
	
	public void testOkNote3(){
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Published");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);

       } 
       catch (Error success) {
           fail("Should not raise an Error");

       } 

	}
	public void testOkNote4(){
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Accepted");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);

       } 
       catch (Error success) {
           fail("Should not raise an Error");

       } 

	}


	
	public void testOk() {
		Proceedings proc = new Proceedings();
		proc.setTitle("Proceedings of the Interdisciplinary Workshop on the Synthesis and Simulation of Living Systems (ALIFE '87), Los Alamos, NM, USA, September 1987");
		p.setProceedings(proc);
		p.setPages("4");
		p.setTitle("Title");
		p.setNote("Draft");
		year = 1999;
		p.setYear(year);
		authors = new ArrayList<String>();
		authors.add("Peter");

        try { 
        	
            dbHelper.addInProceeding(p, proc.getTitle(), authors, false);

       } 
       catch (Error success) {
           fail("Should not raise an Error");

       } 

	}


}
