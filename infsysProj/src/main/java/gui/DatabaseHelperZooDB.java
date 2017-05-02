package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

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

public class DatabaseHelperZooDB extends DatabaseHelper {
	private  PersistenceManager pm;
	private  String dbStandardName = "TheDatabase";

	public  void openDB() {
		pm = ZooJdoHelper.openDB(dbStandardName);
	}

	public  void closeDB() {
		closeDB(pm);
	}
	
	public void createDB() {
		
		XMLParser xmlparser = new XMLParser();
		xmlparser.parse();
		
	}

	public  void createDBinternal() {
		
		// remove database if it exists
		if (ZooHelper.dbExists(dbStandardName)) {

			ZooHelper.removeDb(dbStandardName);
		}

		// create database
		// By default, all database files will be created in %USER_HOME%/zoodb
		ZooHelper.createDb(dbStandardName);
	}

	private  void closeDB(PersistenceManager pm) {
		if (pm.currentTransaction().isActive()) {
			pm.currentTransaction().rollback();
		}
		pm.close();
		pm.getPersistenceManagerFactory().close();
	}

	// TODO

	public void deleteProceeding(String proceedingName) {
		openDB();
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(proceedingName);
		Proceedings proc = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingName);

		} else {
			proc = proceedings.iterator().next();
			for (InProceedings in : proc.getInProceedings()) {
				in.setProceedings(null);
			}
			for (Person p : proc.getAuthors()) {

				Set<Publication> oldAut = p.getEditedPublications();
				Set<Publication> newAut = new HashSet<Publication>();
				for (Publication ufhe : oldAut) {
					if (!ufhe.getTitle().equals(proc.getTitle())) {
						System.out.println("added" + ufhe.getTitle());
						newAut.add(ufhe);
					}
				}
				p.setEditedPublications(newAut);
				pm.makePersistent(p);

			}

			pm.deletePersistent(proc);
		}
		pm.currentTransaction().commit();

		closeDB();
	}

	public List<Publication> getAllPublications() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Publication> ext = pm.getExtent(Publication.class);
		List<Publication> allPublications = new ArrayList<Publication>();
		for (Publication p : ext) {
			allPublications.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPublications;
	}

	public  List<Publication> getAllProceedings() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
		List<Publication> allPublications = new ArrayList<Publication>();
		for (Proceedings p : ext) {
			allPublications.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPublications;
	}

	public  String getPublisherName(String proceedingName) {
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(proceedingName);
		String result = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingName);

		} else {
			result = proceedings.iterator().next().getPublisher().getName();
		}

		closeDB();
		return result;
	}

	public  String getSeriesName(String proceedingName) {
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(proceedingName);
		String result = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with name: " + proceedingName);

		} else {
			result = proceedings.iterator().next().getSeries().getName();
		}

		closeDB();
		return result;
	}

	public  String getConferenceName(String proceedingName) {
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(proceedingName);
		String result = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with title: " + proceedingName);

		} else {
			result = proceedings.iterator().next().getConferenceEdition().getConference().getName();
		}

		closeDB();
		return result;
	}

	public  String getConferenceEditionName(ConferenceEdition proceedingName) {
		openDB();
		pm.currentTransaction().begin();

		ConferenceEdition confed = (ConferenceEdition) pm.getObjectById(proceedingName.jdoZooGetOid());

		String result = confed.getConference().getName();

		pm.currentTransaction().commit();
		closeDB();
		return result;
	}

	public  String getConferenceEditionProceeding(ConferenceEdition proceedingName) {
		openDB();
		pm.currentTransaction().begin();

		ConferenceEdition confed = (ConferenceEdition) pm.getObjectById(proceedingName.jdoZooGetOid());

		String result = confed.getProceedings().getTitle();
		pm.currentTransaction().commit();
		closeDB();
		return result;
	}

	public  String getConferenceYear(String proceedingName) {
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(proceedingName);

		String result = null;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with name: " + proceedingName);

		} else {
			result = String.valueOf(proceedings.iterator().next().getConferenceEdition().getYear());
		}

		closeDB();
		return result;
	}

	public  List<String> getAuthorsOfProceedings(String proceedingsName) {
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(proceedingsName);
		List<String> result = new ArrayList<String>();
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingsName);

		} else {
			for (Person i : proceedings.iterator().next().getAuthors()) {
				result.add(i.getName());
			}
		}
		closeDB();
		return result;
	}

	public  List<String> getInProceedingsOfProceedings(String proceedingName) {
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(proceedingName);
		List<String> result = new ArrayList<String>();
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + proceedingName);

		} else {
			for (InProceedings i : proceedings.iterator().next().getInProceedings()) {
				result.add(i.getTitle());
			}
		}

		closeDB();
		return result;
	}

	public  List<String> getAuthoredPublicationsForPerson(String personName) {
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name == '" + personName.replaceAll("'", "&#39") + "'");
		Collection<Person> persons = (Collection<Person>) query.execute();
		List<String> result = new ArrayList<String>();
		if (persons.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);

		} else {
			for (Publication i : persons.iterator().next().getAuthoredPublications()) {
				result.add(i.getTitle());
			}
		}

		closeDB();
		return result;
	}

	public  List<String> getEditedPublicationsForPerson(String personName) {
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name == '" + personName.replaceAll("'", "&#39") + "'");
		Collection<Person> persons = (Collection<Person>) query.execute();
		List<String> result = new ArrayList<String>();
		if (persons.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);

		} else {
			for (Publication i : persons.iterator().next().getEditedPublications()) {
				result.add(i.getTitle());
			}
		}

		closeDB();
		return result;
	}

	public  List<Publication> getAllInProceedings() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<InProceedings> ext = pm.getExtent(InProceedings.class);
		List<Publication> allPublications = new ArrayList<Publication>();
		for (InProceedings p : ext) {
			allPublications.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPublications;
	}

	public  List<Publication> searchForPublication(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Publication> proceedings = (Collection<Publication>) query.execute();
		List<Publication> allPublications = new ArrayList<Publication>(proceedings);
		Publication proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public  List<Person> searchForPeople(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Person> proceedings = (Collection<Person>) query.execute();
		List<Person> allPublications = new ArrayList<Person>(proceedings);
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public  List<Conference> searchForConference(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Conference.class, "name.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Conference> proceedings = (Collection<Conference>) query.execute();
		List<Conference> allPublications = new ArrayList<Conference>(proceedings);
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public  List<Series> searchForSeries(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Series.class, "name.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Series> proceedings = (Collection<Series>) query.execute();
		List<Series> allPublications = new ArrayList<Series>(proceedings);
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public  List<ConferenceEdition> searchForConferenceEdition(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(ConferenceEdition.class, "name.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<ConferenceEdition> proceedings = (Collection<ConferenceEdition>) query.execute();
		List<ConferenceEdition> allPublications = new ArrayList<ConferenceEdition>(proceedings);
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public  List<Publisher> searchForPublisher(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publisher.class, "name.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Publisher> proceedings = (Collection<Publisher>) query.execute();
		List<Publisher> allPublications = new ArrayList<Publisher>(proceedings);
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public  List<Publication> searchForProceedings(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Proceedings.class, "title.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute();
		List<Publication> allPublications = new ArrayList<Publication>(proceedings);
		Proceedings proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public  List<Publication> searchForInProceedings(String search) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(InProceedings.class, "title.contains('" + search.replaceAll("'", "&#39") + "')");
		Collection<InProceedings> proceedings = (Collection<InProceedings>) query.execute();
		List<Publication> allPublications = new ArrayList<Publication>(proceedings);
		InProceedings proc;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + search);

		}

		pm.currentTransaction().rollback();
		closeDB(pm);

		return allPublications;

	}

	public  void deletePerson(String personName) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Person.class, "name == '" + personName.replaceAll("'", "&#39") + "'");
		Collection<Person> proceedings = (Collection<Person>) query.execute();
		Person p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with name: " + personName);
		} else {
			p = proceedings.iterator().next();
			for (Publication publ : p.getAuthoredPublications()) {

				List<Person> oldAut = publ.getAuthors();
				List<Person> newAut = new ArrayList<Person>();
				for (Person ufhe : oldAut) {
					if (!ufhe.getName().equals(p.getName())) {
						newAut.add(ufhe);
					}
				}
				publ.setAuthors(newAut);
				pm.makePersistent(p);

			}

			for (Publication publ : p.getEditedPublications()) {

				List<Person> oldAut = publ.getAuthors();
				List<Person> newAut = new ArrayList<Person>();
				for (Person ufhe : oldAut) {
					if (!ufhe.getName().equals(p.getName())) {
						newAut.add(ufhe);
					}
				}
				publ.setAuthors(newAut);
				pm.makePersistent(p);

			}

			pm.deletePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public  void updatePerson(String personName, String newPersonName, List<String> newAuthoredPublications, List<String> newEditedPublications) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		/*
		 * Query query0 = pm.newQuery(Person.class); query0.setFilter("name == a"); query0.declareParameters("String a");
		 * 
		 * Collection<Person> proceedings10 = (Collection<Person>) query0.execute(newPersonName); if (!proceedings10.isEmpty()) { System.out.println("There already exists a Person with name: " + newPersonName); pm.currentTransaction().commit(); closeDB(pm); return; }
		 */
		Query query = pm.newQuery(Person.class);
		query.setFilter("name == a");
		query.declareParameters("String a");

		Collection<Person> proceedings = (Collection<Person>) query.execute(personName);
		List<Person> allPublications = new ArrayList<Person>(proceedings);
		Person p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + personName);
		} else {
			p = allPublications.iterator().next();
			p.setName(newPersonName);

			for (Publication oldPub : p.getAuthoredPublications()) {
				List<Person> oldSet = oldPub.getAuthors();
				List<Person> newSet = new ArrayList<Person>();
				for (Person ufhe : oldSet) {
					if (!ufhe.getName().equals(p.getName())) {
						newSet.add(ufhe);
					}
				}
				oldPub.setAuthors(newSet);
				pm.makePersistent(oldPub);
			}

			for (Publication oldPub : p.getEditedPublications()) {
				List<Person> oldSet = oldPub.getAuthors();
				List<Person> newSet = new ArrayList<Person>();
				for (Person ufhe : oldSet) {
					if (!ufhe.getName().equals(p.getName())) {
						newSet.add(ufhe);
						System.out.println("f");
					} else {
						System.out.println("t");
					}
				}
				oldPub.setAuthors(newSet);
				pm.makePersistent(oldPub);
			}

			Set<Publication> autPubs = new HashSet<Publication>();
			for (String s : newAuthoredPublications) {
				Query query1 = pm.newQuery(InProceedings.class);
				query1.setFilter("title == a");
				query1.declareParameters("String a");

				Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query1.execute(s);
				if (proceedings1.isEmpty()) {
					System.out.println("Error: Did not Find InProceedings: " + s);
				} else {
					InProceedings inProc = proceedings1.iterator().next();
					autPubs.add(inProc);
					List<Person> authors = inProc.getAuthors();
					authors.add(p);
					inProc.setAuthors(authors);
				}
			}
			Set<Publication> editPubs = new HashSet<Publication>();

			for (String s : newEditedPublications) {

				Query query1 = pm.newQuery(Proceedings.class);
				query1.setFilter("title == a");
				query1.declareParameters("String a");

				Collection<Proceedings> proceedings1 = (Collection<Proceedings>) query1.execute(s);
				if (proceedings1.isEmpty()) {
					System.out.println("Error: Did not Find Proceedings: " + s);
				} else {
					Proceedings proc = proceedings1.iterator().next();
					editPubs.add(proc);
					List<Person> authors = proc.getAuthors();
					authors.add(p);
					proc.setAuthors(authors);

				}
			}

			p.setEditedPublications(editPubs);
			p.setAuthoredPublications(autPubs);
			pm.makePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public  void addPerson(String newPersonName, List<String> newAuthoredPublications, List<String> newEditedPublications) {

		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query0 = pm.newQuery(Person.class);
		query0.setFilter("name == a");
		query0.declareParameters("String a");

		Collection<Person> proceedings10 = (Collection<Person>) query0.execute(newPersonName);
		if (!proceedings10.isEmpty()) {
			System.out.println("There already exists a Person with name: " + newPersonName);
			pm.currentTransaction().commit();
			closeDB(pm);
			return;
		}

		Person p = new Person(newPersonName);
		Set<Publication> autPubs = new HashSet<Publication>();
		for (String s : newAuthoredPublications) {
			Query query1 = pm.newQuery(InProceedings.class);
			query1.setFilter("title == a");
			query1.declareParameters("String a");

			Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query1.execute(s);
			if (proceedings1.isEmpty()) {
				System.out.println("Error: Did not Find InProceedings: " + s);
			} else {
				InProceedings inProc = proceedings1.iterator().next();
				autPubs.add(inProc);
				List<Person> authors = inProc.getAuthors();
				authors.add(p);
				inProc.setAuthors(authors);
			}
		}
		Set<Publication> editPubs = new HashSet<Publication>();

		for (String s : newEditedPublications) {

			Query query1 = pm.newQuery(Proceedings.class);
			query1.setFilter("title == a");
			query1.declareParameters("String a");

			Collection<Proceedings> proceedings1 = (Collection<Proceedings>) query1.execute(s);
			if (proceedings1.isEmpty()) {
				System.out.println("Error: Did not Find Proceedings: " + s);
			} else {
				Proceedings proc = proceedings1.iterator().next();
				editPubs.add(proc);
				List<Person> authors = proc.getAuthors();
				authors.add(p);
				proc.setAuthors(authors);
			}
		}

		p.setEditedPublications(editPubs);
		p.setAuthoredPublications(autPubs);
		pm.makePersistent(p);

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public  String getProceedingsName(String inProceedingTitle) {

		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query1 = pm.newQuery(InProceedings.class);
		query1.setFilter("title == a");
		query1.declareParameters("String a");

		Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query1.execute(inProceedingTitle);
		String result = "";
		if (proceedings1.isEmpty()) {
			System.out.println("Error: Did not Find InProceedings: " + inProceedingTitle);
		} else {
			InProceedings inProc = proceedings1.iterator().next();
			if (inProc.getProceedings() != null) {
				result = inProc.getProceedings().getTitle();
			}
		}

		pm.currentTransaction().commit();
		closeDB(pm);
		return result;
	}

	public  List<String> getAuthorsOfInProceeding(String inProceedingTitle) {

		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query1 = pm.newQuery(InProceedings.class);
		query1.setFilter("title == a");
		query1.declareParameters("String a");

		Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query1.execute(inProceedingTitle);
		List<String> result = new ArrayList<String>();
		if (proceedings1.isEmpty()) {
			System.out.println("Error: Did not Find InProceedings: " + inProceedingTitle);
		} else {
			for (Person p : proceedings1.iterator().next().getAuthors()) {
				result.add(p.getName());
			}
		}

		pm.currentTransaction().commit();
		closeDB(pm);
		return result;
	}

	public  void deleteInProceeding(String title) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query = pm.newQuery(InProceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<InProceedings> proceedings1 = (Collection<InProceedings>) query.execute(title);

		InProceedings p;
		if (proceedings1.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + title);
		} else {
			p = proceedings1.iterator().next();
			for (Person pers : p.getAuthors()) {

				Set<Publication> oldAut = pers.getAuthoredPublications();
				Set<Publication> newAut = new HashSet<Publication>();
				for (Publication ufhe : oldAut) {
					if (!ufhe.getTitle().equals(p.getTitle())) {
						newAut.add(ufhe);
					}
				}
				pers.setAuthoredPublications(newAut);
				pm.makePersistent(pers);

			}
			if (p.getProceedings() != null) {
				Proceedings proceed = p.getProceedings();
				Set<InProceedings> oldInProcs = proceed.getInProceedings();
				Set<InProceedings> newInProcs = new HashSet<InProceedings>();
				for (InProceedings ufhe : oldInProcs) {
					if (!ufhe.getTitle().equals(p.getTitle())) {
						newInProcs.add(ufhe);
					}
				}
				proceed.setInProceedings(newInProcs);
				pm.makePersistent(proceed);
			}

			pm.deletePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public  void addInProceedings(List<InProceedings> list) {

		pm.setMultithreaded(true);

		int length = list.size();
		for (int i = 0; i < length; i++) {
			pm.currentTransaction().begin();
			pm.currentTransaction().setRetainValues(true);
			pm.makePersistent(list.get(i));
			pm.currentTransaction().commit();

			if (i % 500 == 0) {

				System.out.println(i + " / " + length + " InProceedings added to Database.");
			} else {
			}
		}
		System.out.println("All InProceedings added to Database");
	}

	public  void updateInProceeding(String name, InProceedings newInProc, String proceedingsName, List<String> authors) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		/*
		 * Query query0 = pm.newQuery(InProceedings.class); query0.setFilter("title == a"); query0.declareParameters("String a");
		 * 
		 * Collection<InProceedings> alreadyExists = (Collection<InProceedings>) query0.execute(newInProc.getTitle()); if (!alreadyExists.isEmpty()) { System.out.println("There already exists an InProceeding with title " + newInProc.getTitle()); pm.currentTransaction().rollback(); closeDB(pm);
		 * 
		 * return;
		 * 
		 * }
		 */
		Query query = pm.newQuery(InProceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<InProceedings> proceedings = (Collection<InProceedings>) query.execute(name);
		InProceedings p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with ID: " + name);
		} else {
			p = proceedings.iterator().next();
			p.setTitle(newInProc.getTitle());
			p.setNote(newInProc.getNote());
			p.setElectronicEdition(newInProc.getElectronicEdition());
			p.setPages(newInProc.getPages());
			p.setYear(newInProc.getYear());

			if (p.getProceedings() != null) {
				Proceedings theOldP = p.getProceedings();
				Set<InProceedings> oldinProcs = theOldP.getInProceedings();
				oldinProcs.remove(p);
				theOldP.setInProceedings(oldinProcs);
			}
			Query query1 = pm.newQuery(Proceedings.class);
			query1.setFilter("title == a");
			query1.declareParameters("String a");

			Collection<Proceedings> theProceeding = (Collection<Proceedings>) query1.execute(proceedingsName);
			Proceedings theP;
			if (theProceeding.isEmpty()) {
				System.out.println("Error: Did not find a publication with ID: " + proceedingsName);
			} else {
				theP = theProceeding.iterator().next();
				p.setProceedings(theP);
				Set<InProceedings> inProcs = theP.getInProceedings();
				inProcs.add(p);
				theP.setInProceedings(inProcs);
			}

			for (Person oldAuthor : p.getAuthors()) {
				Set<Publication> oldSet = oldAuthor.getAuthoredPublications();
				Set<Publication> newSet = new HashSet<Publication>();
				for (Publication ufhe : oldSet) {
					if (!ufhe.getTitle().equals(newInProc.getTitle())) {
						newSet.add(ufhe);
					}
				}
				oldAuthor.setAuthoredPublications(newSet);
				pm.makePersistent(oldAuthor);
			}
			List<Person> theNewAuthors = new ArrayList<Person>();
			for (String newAuthor : authors) {
				Query query2 = pm.newQuery(Person.class);
				query2.setFilter("name == a");
				query2.declareParameters("String a");

				Collection<Person> listOfAuthors = (Collection<Person>) query2.execute(newAuthor);
				Person theA;
				if (listOfAuthors.isEmpty()) {
					System.out.println("Error: Did not find a author with ID: " + newAuthor);
				} else {

					theA = listOfAuthors.iterator().next();

					Set<Publication> authoredPubs = theA.getAuthoredPublications();
					authoredPubs.add((Publication) p);
					theA.setAuthoredPublications(authoredPubs);

					theNewAuthors.add(theA);
					pm.makePersistent(theA);
				}
			}
			p.setAuthors(theNewAuthors);

			pm.makePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public  void updateProceeding(String name, Proceedings newProc, List<String> authors, List<String> inProceedings, String publisherName, String seriesName, String conf, int confEdition) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(name);
		Proceedings p;
		if (proceedings.isEmpty()) {
			System.out.println("Error: Did not find a publication with name: " + name);
		} else {
			p = proceedings.iterator().next();
			p.setTitle(newProc.getTitle());
			p.setNote(newProc.getNote());
			p.setElectronicEdition(newProc.getElectronicEdition());
			p.setYear(newProc.getYear());
			p.setNumber(newProc.getNumber());
			p.setIsbn(newProc.getIsbn());
			p.setVolume(newProc.getVolume());

			// Remove Proceedings from old authors
			for (Person oldAuthor : p.getAuthors()) {
				Set<Publication> oldSet = oldAuthor.getEditedPublications();
				Set<Publication> newSet = new HashSet<Publication>();
				for (Publication ufhe : oldSet) {
					if (!ufhe.getTitle().equals(newProc.getTitle())) {
						newSet.add(ufhe);
					}
				}
				oldAuthor.setEditedPublications(newSet);
				pm.makePersistent(oldAuthor);
			}

			// add new authors to Proceeding and add Proceeding to authors
			List<Person> newAuthors = new ArrayList<Person>();
			for (String s : authors) {
				Query query1 = pm.newQuery(Person.class);
				query1.setFilter("name == a");
				query1.declareParameters("String a");

				Collection<Person> author = (Collection<Person>) query1.execute(s);
				Person person;
				if (author.isEmpty()) {
					System.out.println("Error: Did not find a Person with name: " + s);
				} else {
					person = author.iterator().next();
					newAuthors.add(person);
					Set<Publication> editedPubs = person.getEditedPublications();
					editedPubs.add(p);
					person.setEditedPublications(editedPubs);
				}

			}
			p.setAuthors(newAuthors);

			// remove old Proceeding from old InProceedings
			for (InProceedings i : p.getInProceedings()) {
				i.setProceedings(null);
				pm.makePersistent(i);
			}

			// add new InProceedings to Proceeding and set Proceeding of InProceedings
			Set<InProceedings> newInProceedings = new HashSet<InProceedings>();
			for (String s : inProceedings) {
				Query query1 = pm.newQuery(InProceedings.class);
				query1.setFilter("title == a");
				query1.declareParameters("String a");

				Collection<InProceedings> inProceed = (Collection<InProceedings>) query1.execute(s);
				InProceedings inProc;
				if (inProceed.isEmpty()) {
					System.out.println("Error: Did not find a InProceeding with name: " + s);
				} else {
					inProc = inProceed.iterator().next();
					newInProceedings.add(inProc);
					inProc.setProceedings(p);
				}

			}
			p.setInProceedings(newInProceedings);

			// remove Proceeding from old Publisher
			Query query1 = pm.newQuery(Publisher.class);
			query1.setFilter("name == a");
			query1.declareParameters("String a");

			Collection<Publisher> inProceed = (Collection<Publisher>) query1.execute(p.getPublisher().getName());
			Publisher inProc;
			if (inProceed.isEmpty()) {
				System.out.println("Error: Did not find a Publisher with name: " + p.getPublisher().getName());
			} else {
				inProc = inProceed.iterator().next();
				Set<Publication> oldSet = inProc.getPublications();
				Set<Publication> newSet = new HashSet<Publication>();
				for (Publication ufhe : oldSet) {
					if (!ufhe.getTitle().equals(newProc.getTitle())) {
						newSet.add(ufhe);
					}
				}
				inProc.setPublications(newSet);
				pm.makePersistent(inProc);
			}

			// add Proceeding to new Publisher and add Publisher to Proceeding (possibly creat new Publisher)
			Query query2 = pm.newQuery(Publisher.class);
			query2.setFilter("name == a");
			query2.declareParameters("String a");

			Collection<Publisher> inProceed1 = (Collection<Publisher>) query2.execute(publisherName);
			Publisher inProc1 = new Publisher();
			if (inProceed1.isEmpty()) {
				System.out.println("Did not find a Publisher with name: " + publisherName + " created a new Publisher with that name");
				inProc1.setName(publisherName);
				Set<Publication> newSet = new HashSet<Publication>();
				newSet.add(p);
				inProc1.setPublications(newSet);
			} else {
				inProc1 = inProceed1.iterator().next();
				Set<Publication> oldSet = inProc1.getPublications();
				oldSet.add(p);
				inProc1.setPublications(oldSet);
			}

			p.setPublisher(inProc1);

			// remove Proceedings from old Series
			Query query3 = pm.newQuery(Series.class);
			query3.setFilter("name == a");
			query3.declareParameters("String a");

			Collection<Series> inProceed3 = (Collection<Series>) query3.execute(p.getSeries().getName());
			Series inProc3;
			if (inProceed3.isEmpty()) {
				System.out.println("Error: Did not find a Series with name: " + p.getSeries().getName());
			} else {
				inProc3 = inProceed3.iterator().next();
				Set<Publication> oldSet = inProc3.getPublications();
				Set<Publication> newSet = new HashSet<Publication>();
				for (Publication ufhe : oldSet) {
					if (!ufhe.getTitle().equals(newProc.getTitle())) {
						newSet.add(ufhe);
					}
				}
				inProc3.setPublications(newSet);
				pm.makePersistent(inProc3);
			}

			// add Proceeding to new Series and add Series to Proceeding (possibly create new Series)
			Query query4 = pm.newQuery(Series.class);
			query4.setFilter("name == a");
			query4.declareParameters("String a");

			Collection<Series> inProceed4 = (Collection<Series>) query4.execute(seriesName);
			Series inProc4 = new Series();
			if (inProceed4.isEmpty()) {
				System.out.println("Did not find a Series with name: " + seriesName + " created a new Series with that name");
				inProc4.setName(seriesName);
				Set<Publication> newSet = new HashSet<Publication>();
				newSet.add(p);
				inProc4.setPublications(newSet);
			} else {
				inProc4 = inProceed4.iterator().next();
				Set<Publication> oldSet = inProc4.getPublications();
				oldSet.add(p);
				inProc4.setPublications(oldSet);
			}

			p.setSeries(inProc4);

			ConferenceEdition ed = p.getConferenceEdition();

			Conference oldConf = ed.getConference();
			Set<ConferenceEdition> oldSet = oldConf.getEditions();
			Set<ConferenceEdition> newSet = new HashSet<ConferenceEdition>();
			for (ConferenceEdition ufhe : oldSet) {
				if (!(ufhe.getYear() == newProc.getYear())) {
					newSet.add(ufhe);
				}
			}
			oldConf.setEditions(newSet);
			pm.makePersistent(oldConf);

			Query query5 = pm.newQuery(Conference.class);
			query5.setFilter("name == a");
			query5.declareParameters("String a");

			Collection<Conference> inProceed5 = (Collection<Conference>) query5.execute(conf);
			Conference inProc5;
			if (inProceed5.isEmpty()) {
				System.out.println("Did not find a Conference with name: " + conf + " created a new one.");
				inProc5 = new Conference();
				inProc5.setName(conf);
				Set<ConferenceEdition> newS = new HashSet<ConferenceEdition>();
				newS.add(ed);
				inProc5.setEditions(newS);
			} else {
				inProc5 = inProceed5.iterator().next();
				Set<ConferenceEdition> newS = inProc5.getEditions();
				newS.add(ed);
				inProc5.setEditions(newS);
			}

			ed.setYear(confEdition);
			ed.setConference(inProc5);
			pm.makePersistent(p);
		}

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public  void addProceeding(Proceedings newProc, List<String> authors, List<String> inProceedings, String publisherName, String seriesName, String conf, int confEdition) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query = pm.newQuery(Proceedings.class);
		query.setFilter("title == a");
		query.declareParameters("String a");

		Collection<Proceedings> proceedings = (Collection<Proceedings>) query.execute(newProc.getTitle());
		if (!proceedings.isEmpty()) {
			System.out.println("Error: There exists already a Proceeding with the name: " + newProc.getTitle());
			pm.currentTransaction().rollback();
			closeDB(pm);
			return;
		}
		Proceedings p = newProc;

		// add new authors to Proceeding and add Proceeding to authors
		List<Person> newAuthors = new ArrayList<Person>();
		for (String s : authors) {
			Query query1 = pm.newQuery(Person.class);
			query1.setFilter("name == a");
			query1.declareParameters("String a");

			Collection<Person> author = (Collection<Person>) query1.execute(s);
			Person person;
			if (author.isEmpty()) {
				System.out.println("Error: Did not find a Person with name: " + s);
			} else {
				person = author.iterator().next();
				newAuthors.add(person);
				Set<Publication> editedPubs = person.getEditedPublications();
				editedPubs.add(p);
				person.setEditedPublications(editedPubs);
			}

		}
		p.setAuthors(newAuthors);

		// add new InProceedings to Proceeding and set Proceeding of InProceedings
		Set<InProceedings> newInProceedings = new HashSet<InProceedings>();
		for (String s : inProceedings) {
			Query query1 = pm.newQuery(InProceedings.class);
			query1.setFilter("title == a");
			query1.declareParameters("String a");

			Collection<InProceedings> inProceed = (Collection<InProceedings>) query1.execute(s);
			InProceedings inProc;
			if (inProceed.isEmpty()) {
				System.out.println("Error: Did not find a InProceeding with name: " + s);
			} else {
				inProc = inProceed.iterator().next();
				newInProceedings.add(inProc);
				inProc.setProceedings(p);
			}

		}
		p.setInProceedings(newInProceedings);

		// add Proceeding to new Publisher and add Publisher to Proceeding (possibly creat new Publisher)
		Query query2 = pm.newQuery(Publisher.class);
		query2.setFilter("name == a");
		query2.declareParameters("String a");

		Collection<Publisher> inProceed1 = (Collection<Publisher>) query2.execute(publisherName);
		Publisher inProc1 = new Publisher();
		if (inProceed1.isEmpty()) {
			System.out.println("Did not find a Publisher with name: " + publisherName + " created a new Publisher with that name");
			inProc1.setName(publisherName);
			Set<Publication> newSet = new HashSet<Publication>();
			newSet.add(p);
			inProc1.setPublications(newSet);
		} else {
			inProc1 = inProceed1.iterator().next();
			Set<Publication> oldSet = inProc1.getPublications();
			oldSet.add(p);
			inProc1.setPublications(oldSet);
		}

		p.setPublisher(inProc1);

		// add Proceeding to new Series and add Series to Proceeding (possibly create new Series)
		Query query4 = pm.newQuery(Series.class);
		query4.setFilter("name == a");
		query4.declareParameters("String a");

		Collection<Series> inProceed4 = (Collection<Series>) query4.execute(seriesName);
		Series inProc4 = new Series();
		if (inProceed4.isEmpty()) {
			System.out.println("Did not find a Series with name: " + seriesName + " created a new Series with that name");
			inProc4.setName(seriesName);
			Set<Publication> newSet = new HashSet<Publication>();
			newSet.add(p);
			inProc4.setPublications(newSet);
		} else {
			inProc4 = inProceed4.iterator().next();
			Set<Publication> oldSet = inProc4.getPublications();
			oldSet.add(p);
			inProc4.setPublications(oldSet);
		}

		p.setSeries(inProc4);

		Query query5 = pm.newQuery(Conference.class);
		query5.setFilter("name == a");
		query5.declareParameters("String a");
		ConferenceEdition ed = new ConferenceEdition();
		Collection<Conference> inProceed5 = (Collection<Conference>) query5.execute(conf);
		Conference inProc5;
		if (inProceed5.isEmpty()) {
			System.out.println("Did not find a Conference with name: " + conf + " created a new one.");
			inProc5 = new Conference();
			inProc5.setName(conf);
			Set<ConferenceEdition> newS = new HashSet<ConferenceEdition>();
			newS.add(ed);
			inProc5.setEditions(newS);
		} else {
			inProc5 = inProceed5.iterator().next();
			Set<ConferenceEdition> newS = inProc5.getEditions();
			newS.add(ed);
			inProc5.setEditions(newS);
		}

		ed.setYear(confEdition);
		ed.setConference(inProc5);
		p.setConferenceEdition(ed);
		pm.makePersistent(p);

		pm.currentTransaction().commit();
		closeDB(pm);

	}

	public  void addInProceeding(InProceedings newInProc, String proceedingsName, List<String> authors) {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);

		Query query0 = pm.newQuery(InProceedings.class);
		query0.setFilter("title == a");
		query0.declareParameters("String a");

		Collection<InProceedings> alreadyExists = (Collection<InProceedings>) query0.execute(newInProc.getTitle());
		if (!alreadyExists.isEmpty()) {
			System.out.println("There already exists an InProceeding with title " + newInProc.getTitle());
			pm.currentTransaction().rollback();
			closeDB(pm);

			return;

		}
		Query query2 = pm.newQuery(Proceedings.class);
		query2.setFilter("title == a");
		query2.declareParameters("String a");

		Collection<Proceedings> listOfAuthors = (Collection<Proceedings>) query2.execute(proceedingsName);
		Proceedings theA;
		if (listOfAuthors.isEmpty()) {
			System.out.println("Error: Did not find a Proceeding with name: " + proceedingsName);
		} else {
			theA = listOfAuthors.iterator().next();
			newInProc.setProceedings(theA);
			Set<InProceedings> oldInProcs = theA.getInProceedings();
			oldInProcs.add(newInProc);
			theA.setInProceedings(oldInProcs);
		}

		List<Person> newAuthors = new ArrayList<Person>();
		for (String s : authors) {
			Query query1 = pm.newQuery(Person.class);
			query1.setFilter("name == a");
			query1.declareParameters("String a");

			Collection<Person> theL = (Collection<Person>) query1.execute(s);
			Person author;
			if (theL.isEmpty()) {
				System.out.println("Error: Did not find a author with name: " + s);
			} else {
				author = theL.iterator().next();
				newAuthors.add(author);
				Set<Publication> inProcs = author.getAuthoredPublications();
				inProcs.add(newInProc);
				author.setAuthoredPublications(inProcs);
			}

		}

		newInProc.setAuthors(newAuthors);

		pm.makePersistent(newInProc);

		pm.currentTransaction().commit();
		closeDB(pm);
	}

	public  void addProceedings(List<Proceedings> list) {

		pm.setMultithreaded(true);

		int length = list.size();
		for (int i = 0; i < length; i++) {
			pm.currentTransaction().begin();
			pm.currentTransaction().setRetainValues(true);
			pm.makePersistent(list.get(i));
			pm.currentTransaction().commit();

			if (i % 500 == 0) {

				System.out.println(i + " / " + length + " Proceedings added to Database.");
			}
		}
		System.out.println("All Proceedings added to Database");
	}

	public  List<Person> getAllPeople() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Person> ext = pm.getExtent(Person.class);
		List<Person> allPeople = new ArrayList<Person>();
		for (Person p : ext) {
			allPeople.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPeople;
	}

	public  List<Conference> getAllConference() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Conference> ext = pm.getExtent(Conference.class);
		List<Conference> allConference = new ArrayList<Conference>();
		for (Conference c : ext) {
			allConference.add(c);
		}
		ext.closeAll();

		closeDB(pm);
		return allConference;
	}

	public  List<Publisher> getAllPublisher() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Publisher> ext = pm.getExtent(Publisher.class);
		List<Publisher> allPublisher = new ArrayList<Publisher>();
		for (Publisher p : ext) {
			allPublisher.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allPublisher;
	}

	public  List<Series> getAllSeries() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<Series> ext = pm.getExtent(Series.class);
		List<Series> allSeries = new ArrayList<Series>();
		for (Series p : ext) {
			allSeries.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allSeries;
	}

	public  List<ConferenceEdition> getAllConferenceEdition() {
		PersistenceManager pm = ZooJdoHelper.openDB(dbStandardName);
		pm.currentTransaction().begin();

		Extent<ConferenceEdition> ext = pm.getExtent(ConferenceEdition.class);
		List<ConferenceEdition> allConferenceEdition = new ArrayList<ConferenceEdition>();
		for (ConferenceEdition p : ext) {
			allConferenceEdition.add(p);
		}
		ext.closeAll();

		closeDB(pm);
		return allConferenceEdition;
	}

	// find publication by key (according to xml file)
	public  void query1(String id) {
		String thisQuery = "Query 1";
		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "id == '" + id.replaceAll("'", "&#39") + "'");
		Collection<Publication> publication = (Collection<Publication>) query.execute();
		Publication pub;
		try {
			PrintWriter writer = new PrintWriter(thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (publication.isEmpty()) {
				System.out.println("Error: Did not find a publication with ID: " + id);
			} else {
				pub = publication.iterator().next();
				writer.println("The title of the publication with id " + id + " is " + pub.getTitle() + ".");
				writer.println(pm.getObjectId(pub));
				writer.close();
			}
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// find publication by object id (database)
	// try 4197 (note: solution changes every time you reload database)
	public  void query1Alter(Integer oid) {
		String thisQuery = "Query 1 (alternative)";
		openDB();
		pm.currentTransaction().begin();

		Publication pub = (Publication) pm.getObjectById(oid.longValue());
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (pub == null) {
				writer.println("Error: Did not find a publication with ID: " + oid);
			} else {
				writer.println("The title of the publication with id " + oid + " is " + pub.getTitle() + ".");
				writer.println("The key of the publication is " + pub.getId() + ".");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// Find publications by title, returning only a subset of all found publications
	public  void query2(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 2";

		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains(t)");
		query.declareParameters("String t");
		query.setRange(":start, :end"); // Parameters should not be declared above

		Collection<Publication> proceedings = (Collection<Publication>) query.execute(title, startOffset, endOffset);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (proceedings.isEmpty()) {
				writer.println("Error: Did not find a publication with title: " + title + " in range: " + startOffset + "-" + endOffset);
			} else {
				Iterator<Publication> itr = proceedings.iterator();
				while (itr.hasNext()) {
					writer.println(itr.next().getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// Find publications by title, returning only a subset of all found publications ORDERED by title
	public  void query3(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 3";

		openDB();
		pm.currentTransaction().begin();

		Query query = pm.newQuery(Publication.class, "title.contains(t)");
		query.declareParameters("String t");
		query.setRange(":start, :end"); // Parameters should not be declared above
		// interpretation: order titles before or after setting the offset?
		query.setOrdering("title ascending");
		Collection<Publication> proceedings = (Collection<Publication>) query.execute(title, startOffset, endOffset);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (proceedings.isEmpty()) {
				writer.println("Error: Did not find a publication with title: " + title + " in range: " + startOffset + "-" + endOffset);
			} else {
				Iterator<Publication> itr = proceedings.iterator();
				while (itr.hasNext()) {
					writer.println(itr.next().getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// our own sorting algorithm, for sorting afterwards
	/*
	 * Iterator<Publication> itr = proceedings.iterator(); List<Publication> listOfPublications = new ArrayList<Publication>(); for (int i = 0; itr.hasNext(); i++) { listOfPublications.add(itr.next()); }
	 *
	 * Collections.sort(listOfPublications, new Comparator<Publication>() {
	 *
	 * public int compare(Publication s1, Publication s2) { return s1.getTitle().compareToIgnoreCase(s2.getTitle()); } }); for (Publication p : listOfPublications) { System.out.println(p.getTitle()); } }
	 */

	// returns name of the co-authors of a given author
	public  void query4(String author) {
		String thisQuery = "Query 4";

		openDB();
		pm.currentTransaction().begin();
		Collection<Person> result = new HashSet<Person>();

		Query query = pm.newQuery(Person.class);
		query.setFilter("name == a");
		query.declareParameters("String a");
		Collection<Person> auth = (Collection<Person>) query.execute(author);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (auth.size() == 1) {
				for (Person p : auth) {
					Collection<Publication> publications = p.getAuthoredPublications();
					publications.addAll(p.getEditedPublications());
					result.add(p);
					if (publications.isEmpty()) {
						writer.println("Error: Did not find any publication with an author named: " + author);
					} else {
						Iterator<Publication> itr1 = publications.iterator();
						Publication currentPub;
						while (itr1.hasNext()) {
							currentPub = itr1.next();
							Iterator<Person> itr2 = currentPub.getAuthors().iterator();
							while (itr2.hasNext()) {
								Person currentPers = itr2.next();
								if (!result.contains(currentPers)) {
									// no duplicate values
									result.add(currentPers);
									writer.println(currentPers.getName());
								} else {
									// don't print author's name or any duplicates
								}
							}
						}
					}
				}
			} else if (auth.size() > 1) {
				writer.println("Error: There are several persons whose name equals: " + author);
			} else {
				writer.println("Error: Did not find any person named: " + author);
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// distance between 2 authors
	public  void query5(String author1, String author2) {
		String thisQuery = "Query 5";
		openDB();
		pm.currentTransaction().begin();

		String resultStr = "";
		Query q1 = pm.newQuery(Person.class);
		q1.declareParameters("String authorName");
		q1.setFilter("name == authorName");
		Collection<Person> authors1 = (Collection<Person>) q1.execute(author1);
		Query q2 = pm.newQuery(Person.class);
		q2.declareParameters("String authorName");
		q2.setFilter("name == authorName");
		Collection<Person> authors2 = (Collection<Person>) q2.execute(author2);
		// map to store distances
		HashMap<Person, Integer> distancesMap = new HashMap<Person, Integer>();
		// indicates if we found a path
		Boolean found = false;
		// stores object of author2
		Person authorTwo = null;
		if (authors1.isEmpty()) {
			resultStr = ("Error: No author with name " + author1 + "found.");
		} else if (authors1.size() > 1) {
			resultStr = ("Error: Several authors with name " + author1 + "found.");
		} else if (authors2.isEmpty()) {
			resultStr = ("Error: No author found with name " + author2 + ".");
		} else if (authors2.size() > 1) {
			resultStr = ("Error: Several authors with name " + author2 + "found.");
		} else {
			Iterator<Person> itr1 = authors1.iterator();
			Person authorOne = itr1.next(); // get corresponding object of author1
			Iterator<Person> itr2 = authors2.iterator();
			authorTwo = itr2.next(); // get author2

			Set<Publication> pubOfAuthor1 = authorOne.getAuthoredPublications();
			HashSet<Publication> visitedPublications = new HashSet<Publication>();
			HashSet<Publication> toVisitPublications = new HashSet<Publication>();
			HashSet<Publication> tempPublications = new HashSet<Publication>();
			toVisitPublications.addAll(pubOfAuthor1);
			Iterator<Publication> itrPub = toVisitPublications.iterator();
			int distance = 1;
			List<Person> coAuthors = new ArrayList<Person>();
			Publication currentPub = null;
			// hint: this loop can take very long
			// int no = 0;
			while ((!toVisitPublications.isEmpty())) {
				// no++;
				// if (no == 10){
				// break;
				// }
				// reset itrPub, because toVisitPublications has changed
				itrPub = toVisitPublications.iterator();
				while (itrPub.hasNext()) {
					currentPub = itrPub.next();
					coAuthors.addAll(currentPub.getAuthors());
					for (Person a : coAuthors) {
						distancesMap.putIfAbsent(a, distance);
						if (a == authorTwo) {
							found = true;
							break;
						}
						for (Publication p : a.getAuthoredPublications()) {
							if ((!visitedPublications.contains(p)) || (!toVisitPublications.contains(p))) {
								tempPublications.add(p);
							}
						}
					}
					if (found == true) {
						break;
					}
					visitedPublications.add(currentPub);
				}
				if (found == true) {
					break;
				}
				// add publications of co-authors
				toVisitPublications.addAll(tempPublications);
				tempPublications.clear();
				// remove the publications we have already visited (--> avoid loops)
				for (Publication p : visitedPublications) {
					toVisitPublications.remove(p);
				}
				distance++;
			}
		}
		if (found == true) {
			resultStr = ("The distance between the authors " + author1 + " and " + author2 + " is " + distancesMap.get(authorTwo) + ".");
		} else {
			resultStr = ("The authors " + author1 + " and " + author2 + " are not connected.");
		}
		closeDB();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println(resultStr);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
	}

	// global average of authors / publication (InProceedings + Proceedings)
	public  void query6() {
		String thisQuery = "Query 6";
		openDB();
		pm.currentTransaction().begin();

		Extent<Publication> ext = pm.getExtent(Publication.class);
		double sum = 0.;
		double total = 0.;
		Iterator<Publication> itr = ext.iterator();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (itr.hasNext() == false) {
				writer.println("Error: No publications available.");
			} else {
				while (itr.hasNext()) {
					total += 1;
					sum += itr.next().getAuthors().size();
				}
				writer.println("Average authors " + (double) (sum / total) + ".");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// Returns the number of publications per year between the interval year1 and year 2
	public  void query7(int year1, int year2) {
		String thisQuery = "Query 7";
		openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Publication.class);
		q1.setFilter("(this.year >= year1) && (this.year <= year2)");
		// TODO: is setGrouping not supported?
		// q1.setGrouping("this.year");
		q1.declareParameters("int year1, int year2");
		Collection<Publication> publications = (Collection<Publication>) q1.execute(year1, year2);
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (publications.isEmpty()) {
				writer.println("Error: No publications available.");
			} else {
				// group by year
				Iterator<Publication> itr = publications.iterator();
				Publication p;
				int currYear;
				while (itr.hasNext()) {
					p = itr.next();
					currYear = p.getYear();
					if (map.containsKey(currYear)) {
						int current = map.get(currYear);
						map.put(currYear, current + 1);
					} else {
						map.put(currYear, 1);
					}
				}

				// print Hashtable
				writer.printf("%-12s%-17s\n", "Year", "No. Publications");
				for (int i = year1; i <= year2; i++) {
					writer.printf("%-12d%-12d\n", i, map.get(i));
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// No of all publications of a conference, except proceedings
	public  void query8(String conferenceName) {
		String thisQuery = "Query 8";
		String resultStr = "";
		openDB();
		pm.currentTransaction().begin();

		int result = 0;
		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		if (conferences.isEmpty()) {
			resultStr = ("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1) {
			resultStr = ("Multiple conferences found with name " + conferenceName);
		} else {
			// only interested in 1 conference
			Iterator<Conference> itr = conferences.iterator();
			Collection<ConferenceEdition> editions = itr.next().getEditions();
			Iterator<ConferenceEdition> itrEditions = editions.iterator();
			// System.out.println(editions.size()); // I think it should be two, 1979 and 1983
			while (itrEditions.hasNext()) {
				ConferenceEdition e = itrEditions.next();
				// System.out.println(e.getYear()); // only finds 1979, solution prints all 20 proceedings from this edition, but non of the 1983 publications
				// System.out.println(e.getConference().getId());
				Proceedings proceedings = e.getProceedings();
				result += proceedings.getInProceedings().size();
			}
			resultStr = ("The number of in proceedings presented at the conference " + conferenceName + " was " + result + ".");
		}
		closeDB();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println(resultStr);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

	}

	// count all people involved in a given conference
	public  void query9(String conferenceName) {
		String thisQuery = "Query 9";
		String resultStr = "";
		openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		if (conferences.isEmpty()) {
			resultStr = ("No conference found with name " + conferenceName);
		} else if (conferences.size() > 1) {
			resultStr = ("Multiple conferences found with name " + conferenceName);
		} else {
			// only interested in 1 conference
			//one list to avoid double counting elements
			HashSet<Person> authors = new HashSet<Person>();
			Iterator<Conference> itr = conferences.iterator();
			Collection<ConferenceEdition> editions = itr.next().getEditions();
			Iterator<ConferenceEdition> itrEditions = editions.iterator();
			while (itrEditions.hasNext()) {
				ConferenceEdition e = itrEditions.next();
				Proceedings proc = e.getProceedings();
				authors.addAll(proc.getAuthors());
				Set<InProceedings> inproceedings = proc.getInProceedings();
				Iterator<InProceedings> inProcItr = inproceedings.iterator();
				while (inProcItr.hasNext()) {
					InProceedings inProc = inProcItr.next();
					Iterator<Person> inProcAuthorsItr = inProc.getAuthors().iterator();
					while (inProcAuthorsItr.hasNext()) {
						Person auth = inProcAuthorsItr.next();
						authors.add(auth);
					}
				}
			}
			int result = authors.size();
			resultStr = ("Number of authors and editors at the conference " + conferenceName + " was " + result + ".");
		}
		closeDB();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println(resultStr);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
	}

	// all authors of conferenceName
	public  void query10(String conferenceName) {
		String thisQuery = "Query 10";
		openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (conferences.isEmpty()) {
				writer.println("No conference found with name " + conferenceName);
			} else if (conferences.size() > 1) {
				writer.println("Multiple conferences found with name " + conferenceName);
			} else {
				// only interested in 1 conference
				//only one list to avoid double counting
				HashSet<Person> authors = new HashSet<Person>();
				Iterator<Conference> itr = conferences.iterator();
				Collection<ConferenceEdition> editions = itr.next().getEditions();
				Iterator<ConferenceEdition> itrEditions = editions.iterator();
				while (itrEditions.hasNext()) {
					ConferenceEdition e = itrEditions.next();
					Proceedings proc = e.getProceedings();
					authors.addAll(proc.getAuthors());
					Set<InProceedings> inproceedings = proc.getInProceedings();
					Iterator<InProceedings> inProcItr = inproceedings.iterator();
					while (inProcItr.hasNext()) {
						InProceedings inProc = inProcItr.next();
						Iterator<Person> inProcAuthorsItr = inProc.getAuthors().iterator();
						while (inProcAuthorsItr.hasNext()) {
							Person auth = inProcAuthorsItr.next();
							authors.add(auth);
						}

					}
				}
				writer.println("List of all authors and editors at the conference " + conferenceName + ":");
				for (Person p : authors) {
					writer.println(p.getName());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();

	}

	// all publications of conferenceName
	public  void query11(String conferenceName) {
		String thisQuery = "Query 11";
		openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Conference.class, "name == confName");
		q1.declareParameters("String confName");
		Collection<Conference> conferences = (Collection<Conference>) q1.execute(conferenceName);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (conferences.isEmpty()) {
				writer.println("No conference found with name " + conferenceName);
			} else if (conferences.size() > 1) {
				writer.println("Multiple conferences found with name " + conferenceName);
			} else {
				// only interested in 1 conference
				Iterator<Conference> itr = conferences.iterator();
				Collection<ConferenceEdition> editions = itr.next().getEditions();
				Iterator<ConferenceEdition> itrEditions = editions.iterator();
				List<InProceedings> allPublications = new ArrayList<InProceedings>();
				while (itrEditions.hasNext()) {
					ConferenceEdition e = itrEditions.next();
					Proceedings proc = e.getProceedings();
					Set<InProceedings> inproceedings = proc.getInProceedings();
					allPublications.addAll(inproceedings);
				}
				writer.println("List of all Publications at the conference " + conferenceName + ":");
				for (InProceedings p : allPublications) {
					writer.println(p.getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// list of persons which were authors of an in proceeding and editor of the corresponding proceeding.
	public  void query12() {
		String thisQuery = "Query 12";
		openDB();
		pm.currentTransaction().begin();

		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
		List<Person> editorAndAuthor = new ArrayList<Person>();
		for (Proceedings p : ext) {
			List<Person> authors = p.getAuthors();
			for (InProceedings inP : p.getInProceedings()) {
				for (Person editor : inP.getAuthors()) {
					if (authors.contains(editor) && !editorAndAuthor.contains(editor)) {
						editorAndAuthor.add(editor);
					}
				}
			}

		}
		ext.closeAll();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of " + editorAndAuthor.size() + " authors that are also editor in the corresponding proceeding.");
			for (Person p : editorAndAuthor) {
				writer.println(p.getName());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// list of in proceedings where the given author appears as the last author
	public  void query13(String authorName) {
		String thisQuery = "Query 13";
		openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(Person.class, "name == authName");
		q1.declareParameters("String authName");
		Collection<Person> authors = (Collection<Person>) q1.execute(authorName);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (authors.isEmpty()) {
				writer.println("No author found with name " + authorName);
			} else if (authors.size() > 1) {
				writer.println("Multiple authors found with name " + authorName);
			} else {
				Iterator<Person> itrPers = authors.iterator();
				Query q2 = pm.newQuery(InProceedings.class, "this.authors.contains(authPerson)");
				q2.declareParameters("infsysProj.infsysProj.Person authPerson");
				Person aPerson = itrPers.next();
				Collection<InProceedings> pub = (Collection<InProceedings>) q2.execute(aPerson);
				List<InProceedings> result = new ArrayList<InProceedings>();
				Iterator<InProceedings> pubItr = pub.iterator();
				while (pubItr.hasNext()) {
					InProceedings i = pubItr.next();
					Person lastAuthor = i.getAuthors().get(i.getAuthors().size() - 1);
					if (lastAuthor.equals(aPerson)) {
						result.add(i);
					}
				}
				writer.println("List of " + result.size() + " in proceeding(s) where " + authorName + " was listed as last author.");
				for (InProceedings p : result) {
					writer.println(p.getTitle());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	// list of publishers (Proceedings) whose authors appear in any in proceedings between year1 and year2
	public  void query14(int year1, int year2) {
		String thisQuery = "Query 14";
		openDB();
		pm.currentTransaction().begin();

		Query q1 = pm.newQuery(InProceedings.class);
		q1.setFilter("(this.year >= year1) && (this.year <= year2)");
		q1.declareParameters("int year1, int year2");
		Collection<InProceedings> inProcs = (Collection<InProceedings>) q1.execute(year1, year2);
		HashSet<Person> setOfAuthors = new HashSet<Person>();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (inProcs.isEmpty()) {
				writer.println("Error: No in proceedings available.");
			} else {
				// create list of authors
				Iterator<InProceedings> itr = inProcs.iterator();
				InProceedings inP;
				while (itr.hasNext()) {
					inP = itr.next();
					setOfAuthors.addAll(inP.getAuthors());
				}
				// result set
				HashSet<Publisher> result = new HashSet<Publisher>();
				Extent<Proceedings> proceedings = pm.getExtent(Proceedings.class);
				for (Proceedings proc : proceedings) {
					if (setOfAuthors.containsAll(proc.getAuthors())) {
						result.add(proc.getPublisher());
					}
				}
				// print list of publishers
				writer.println("List of publishers of proceedings whose authors appear in any in proceedings which were published between " + year1 + " and " + year2 + ".");
				for (Publisher p : result) {
					writer.println(p.getName());
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeDB();
	}

	@Override
	String getNumberOfPublicationsForPublisher(Publisher name) {
		return String.valueOf(name.getPublications().size());
	}

	@Override
	Proceedings getProceedingOfInproceeding(InProceedings inProceedings) {
		Proceedings result = new Proceedings();
		result.setTitle(getProceedingsName(inProceedings.getTitle()));
		return result;
	}

	@Override
	List<String> getAuthorsOfInProceeding(InProceedings inProceeding) {
		return getAuthorsOfInProceeding(inProceeding.getTitle());
	}

}