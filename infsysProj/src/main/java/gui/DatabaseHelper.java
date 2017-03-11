package gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.zoodb.jdo.ZooJdoHelper;
import org.zoodb.tools.ZooHelper;

import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;

public class DatabaseHelper {
	private static PersistenceManager pm;
	private static String dbStandardName = "TheDatabase";
	
	public static void openDB(){
        pm = ZooJdoHelper.openDB(dbStandardName);
	}
	
	public static void closeDB(){
        closeDB(pm);
	}

	    
    public static void addInProceeding(InProceedings in){
        pm.currentTransaction().begin();
    	pm.makePersistent(in);
        pm.currentTransaction().commit();
    }
    
    public static void addProceeding(Proceedings in){
        pm.currentTransaction().begin();
    	pm.makePersistent(in);
        pm.currentTransaction().commit();
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
