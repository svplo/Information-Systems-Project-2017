package gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.zoodb.jdo.ZooJdoHelper;
import org.zoodb.tools.ZooHelper;

import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class DatabaseHelper {
	private static PersistenceManager pm;
	private static String dbStandardName = "TheDatabase";
	
	public static void openDB(){
        pm = ZooJdoHelper.openDB(dbStandardName);
	}
	
	public static void closeDB(){
        closeDB(pm);
	}

	    
    public static void addInProceeding(InProceedings in, String crossref){
        pm.currentTransaction().begin();
        if(crossref != ""){
            Query query = pm.newQuery(Proceedings.class, "id == '" + crossref.replaceAll("'","&#39") + "'");
            Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
            Proceedings proc;
            if(proceedings.isEmpty()){
            	System.out.println("Error: Did not find corresponding Proceeding while attempting to add InProceeding to Database. Crossref: " + crossref);
            	
            }
            else{
            	proc = proceedings.iterator().next();
                in.setProceedings(proc);

            }
        }
        
        
        //Handle authors
        
        List<Person> authors = new ArrayList<Person>();
        for(Person p: in.getAuthors()){
            Query query4 = pm.newQuery(Person.class, "name == '" + p.getName().replaceAll("'","&#39") + "'");
            Collection<Person> editorObjects = (Collection<Person>) query4.execute();
            Person author;
            if(editorObjects.isEmpty()){
            	author = p;
                Set<Publication> publications = new HashSet<Publication>();
                publications.add(in);
                author.setAuthoredPublications(publications);

            }
            else{
            	author = editorObjects.iterator().next();
                Set<Publication> publications = author.getAuthoredPublications();
                publications.add(in);
                author.setAuthoredPublications(publications);
            }
            authors.add(author);

        }
    	System.out.println("Added InProceeding: " + in.getTitle());

        in.setAuthors(authors);

    	pm.makePersistent(in);
        pm.currentTransaction().commit();
    }
    
    public static void addProceeding(Proceedings in, String booktitle, String publisher, String series){
        pm.currentTransaction().begin();
        Query query = pm.newQuery(Conference.class, "name == '" + booktitle.replaceAll("'","&#39") + "'");
        Collection<Conference> conferences = (Collection<Conference>) query.execute();
        Conference conf;
        if(conferences.isEmpty()){
        	conf = new Conference();
        	conf.setName(booktitle);
        }
        else{
        	conf = conferences.iterator().next();
        }
    	ConferenceEdition edition = new ConferenceEdition();
    	edition.setConference(conf);
    	edition.setYear(in.getYear());
    	edition.setProceedings(in);
    	Set<ConferenceEdition> conferenceEditions = new HashSet<ConferenceEdition>();
    	conferenceEditions.add(edition);
    	conf.setEditions(conferenceEditions);
    	
        Query query2 = pm.newQuery(Publisher.class, "name == '" + publisher.replaceAll("'","&#39") + "'");
        Collection<Publisher> publishers = (Collection<Publisher>) query2.execute();
        Publisher pub;
        if(publishers.isEmpty()){
        	pub = new Publisher();
        	pub.setName(publisher);
            Set<Publication> publications = new HashSet<Publication>();
            publications.add(in);
            pub.setPublications(publications);

        }
        else{
        	pub = publishers.iterator().next();
            Set<Publication> publications = pub.getPublications();
            publications.add(in);
            pub.setPublications(publications);
        }
        
        Query query3 = pm.newQuery(Series.class, "name == '" + series.replaceAll("'","&#39") + "'");
        Collection<Series> seriesObjects = (Collection<Series>) query3.execute();
        Series seriesResult;
        if(seriesObjects.isEmpty()){
        	seriesResult = new Series();
        	seriesResult.setName(series);
            Set<Publication> publications = new HashSet<Publication>();
            publications.add(in);
            seriesResult.setPublications(publications);

        }
        else{
        	seriesResult = seriesObjects.iterator().next();
            Set<Publication> publications = seriesResult.getPublications();
            publications.add(in);
            seriesResult.setPublications(publications);
        }
        
        
        //Handle authors
        List<Person> editors = new ArrayList<Person>();

        for(Person p: in.getAuthors()){
            Query query4 = pm.newQuery(Person.class, "name == '" + p.getName().replaceAll("'","&#39") + "'");
            Collection<Person> editorObjects = (Collection<Person>) query4.execute();
            Person editorResult;
            if(editorObjects.isEmpty()){
            	editorResult = p;
                Set<Publication> publications = new HashSet<Publication>();
                publications.add(in);
                editorResult.setEditedPublications(publications);

            }
            else{
            	editorResult = editorObjects.iterator().next();
                Set<Publication> publications = editorResult.getEditedPublications();
                publications.add(in);
                editorResult.setEditedPublications(publications);
            }
            editors.add(editorResult);

        }
        
        in.setAuthors(editors);
        in.setSeries(seriesResult);
        in.setPublisher(pub);
    	in.setConferenceEdition(edition);
    	pm.makePersistent(in);
        pm.currentTransaction().commit();
    }

    public static Proceedings addProceedingwithCrossRef(String crossref, InProceedings inproc){
        pm.currentTransaction().begin();
        Query query = pm.newQuery(Proceedings.class, "id == 'crossref'");
        Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
        Proceedings proc;
        if(proceedings.isEmpty()){
        	proc = new Proceedings();
        	proc.setId(crossref);
        	Set<InProceedings> inprocs = new HashSet<InProceedings>();
        	inprocs.add(inproc);
        	proc.setPublications(inprocs);
        }
        else{
        	proc = proceedings.iterator().next();
        	Set<InProceedings> publications = proc.getPublications();
        	publications.add(inproc);
        	proc.setPublications(publications);
        }
    	pm.makePersistent(proc);
        pm.currentTransaction().commit();
        return proc;
    }

	
	
    public static void createDB() {
        // remove database if it exists
        if (ZooHelper.dbExists(dbStandardName)) {

            ZooHelper.removeDb(dbStandardName);
        }

        // create database
        // By default, all database files will be created in %USER_HOME%/zoodb
        ZooHelper.createDb(dbStandardName);
    }

    private static void closeDB(PersistenceManager pm) {
        if (pm.currentTransaction().isActive()) {
            pm.currentTransaction().rollback();
        }
        pm.close();
        pm.getPersistenceManagerFactory().close();
    }

	public static Collection<Publication> getAllPublications() {
        PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
        pm.currentTransaction().begin();
        
        Extent<Publication> ext = pm.getExtent(Publication.class);
        List<Publication> allPublications = new ArrayList<Publication>();
        for (Publication p: ext) {
        	allPublications.add(p);
        }
        ext.closeAll();

        closeDB(pm);
        return allPublications;
    }


}