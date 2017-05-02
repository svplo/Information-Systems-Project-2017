package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.basex.core.*;
import org.basex.core.cmd.*;
import org.basex.query.*;
import org.basex.query.iter.*;
import org.basex.query.value.item.*;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.DomainObject;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class DatabaseHelperBaseX extends DatabaseHelper{
	private Context context;
	private String dbStandardName = "TheBaseXDatabase";

	 List<DomainObject> myQuery(final String query, Class<? extends DomainObject> c) {

		List<DomainObject> result = new ArrayList<DomainObject>();
		// Create a query processor
		try (QueryProcessor proc = new QueryProcessor(query, context)) {
			// Store the pointer to the result in an iterator:
			Iter iter = proc.iter();
			// Iterate through all items and serialize
			for (Item item; (item = iter.next()) != null;) {
				DomainObject obj = Adaptor.toJava(item, c);
				result.add(obj);

			}
		} catch (QueryException e) {
			e.printStackTrace();
		}

		return result;
	}

	
	 List<String> myQuery(final String query) {

		List<String> result = new ArrayList<String>();
		// Create a query processor
		try (QueryProcessor proc = new QueryProcessor(query, context)) {
			// Store the pointer to the result in an iterator:
			Iter iter = proc.iter();
			// Iterate through all items and serialize
			for (Item item; (item = iter.next()) != null;) {

				result.add(item.serialize().toString());

			}
		} catch (QueryException | QueryIOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private  void optimize(){
		try {
			new Optimize().execute(context);
		} catch (BaseXException e) {
			e.printStackTrace();
		}
	}
	public  void connectToDB() {
		context = new Context();
		try {
			new Open(dbStandardName).execute(context);
		} catch (BaseXException e) {
			System.out.println("There was an Error opening the Database");

		}

	}

	public  void closeConnectionDB() {
		try {
			new Close().execute(context);
		} catch (BaseXException e) {
			System.out.println("There was an Error closing the Database");
		}
	}

	public  void createDB() {

		context = new Context();

		System.out.println("Creating Database");

		// Create a database from a local or remote XML document or XML String
		try {
			new CreateDB(dbStandardName, "dblp_filtered.xml").execute(context);

			System.out.print(new InfoDB().execute(context));

		} catch (BaseXException e) {
			System.out.println("Something went wrong while creating the database");
			e.printStackTrace();
		}

		// Close the database context
		context.close();

	}

	public  String escape(String s) {
		return (s.replaceAll("\"", "&#34;"));
	}

	public List<Publication> getAllPublications() {
		List<Publication> result = getAllInProceedings();
		result.addAll(getAllProceedings());
		return result;
	}

	public List<Publication> getAllInProceedings() {
		connectToDB();

		String query = "/root/inproceedings";
		List<Publication> result = (List<Publication>) (List<?>) myQuery(query, InProceedings.class);

		closeConnectionDB();
		return result;
	}

	public  List<Publication> getAllProceedings() {
		connectToDB();

		String query = "/root/proceedings";
		List<Publication> result = (List<Publication>) (List<?>) myQuery(query, Proceedings.class);

		closeConnectionDB();
		return result;
	}

	public  List<Person> getAllPeople() {
		connectToDB();

		String query = "for $x in distinct-values((root/proceedings/editor|root/inproceedings/author)) return <name>{$x}</name>";
		List<Person> result = (List<Person>) (List<?>) myQuery(query, Person.class);

		for (Person p : result) {
			query = "count(for $x in distinct-values((root/proceedings[editor = \"" + p.getName() + "\"]/title)) return $x)";
			int size = Integer.valueOf(myQuery(query).get(0));
			for (int i = 0; i < size; i++) {
				p.addEditedPublication(new Proceedings());
			}

			query = "count(for $x in distinct-values((root/inproceedings[author = \"" + p.getName() + "\"]/title)) return $x)";
			size = Integer.valueOf(myQuery(query).get(0));
			for (int i = 0; i < size; i++) {
				p.addAuthoredPublication(new InProceedings());
			}

		}
		closeConnectionDB();
		return result;
	}

	public  List<Conference> getAllConference() {
		connectToDB();

		String query = "for $x in distinct-values(root/proceedings/booktitle) return <name>{$x}</name>";
		List<Conference> result = (List<Conference>) (List<?>) myQuery(query, Conference.class);
		for (Conference conf : result) {
			query = "count(/root/proceedings[booktitle = \"" + conf.getName().replaceAll("\"", "'") + "\"])";
			int size = Integer.valueOf(myQuery(query).get(0));
			for (int i = 0; i < size; i++) {
				conf.addEdition(new ConferenceEdition());
			}
		}

		closeConnectionDB();
		return result;
	}

	public  List<Series> getAllSeries() {
		connectToDB();

		String query = "for $x in distinct-values(root/proceedings/series)    order by $x    return <name>{$x}</name>";
		List<Series> result = (List<Series>) (List<?>) myQuery(query, Series.class);

		for (Series s : result) {
			query = "count(for $x in (root/proceedings[series = \"" + s.getName() + "\"]/series)  return <series>{$x}</series>)";
			int size = Integer.valueOf(myQuery(query).get(0));
			for (int i = 0; i < size; i++) {
				s.addPublication(new Proceedings());
			}
		}

		closeConnectionDB();
		return result;
	}

	public  List<Publisher> getAllPublisher() {
		connectToDB();

		String query = "for $x in distinct-values(root/proceedings/publisher)    order by $x    return <publisherName>{$x}</publisherName>";
		List<Publisher> result = (List<Publisher>) (List<?>) myQuery(query, Publisher.class);

		closeConnectionDB();
		return result;
	}

	public  List<ConferenceEdition> getAllConferenceEdition() {
		List<Publication> proceedings = getAllProceedings();
		List<ConferenceEdition> result = new ArrayList<ConferenceEdition>();
		for (Publication p : proceedings) {
			ConferenceEdition e = ((Proceedings) p).getConferenceEdition();
			if(!(e == null)){
				e.setYear(p.getYear());
				e.setProceedings((Proceedings) p);
				result.add(e);
			}
		}
		return result;
	}

	public  String getNumberOfPublicationsForPublisher(Publisher name) {
		connectToDB();
		String query = "count(root/proceedings[publisher = \"" + name.getName() + "\"])";
		String result = myQuery(query).get(0);

		closeConnectionDB();
		return result;

	}

	public  String getConferenceName(String proceedingName) {
		connectToDB();

		String result;
		String query = "/root/proceedings[title = \"" + escape(proceedingName) + "\"]/booktitle/text()";
		List<String> confName = myQuery(query);
		if(confName.size() == 0){
			result = "";
		}
		else{
			result = confName.get(0);
		}
		closeConnectionDB();
		return result;
	}

	public  String getConferenceEditionName(ConferenceEdition edition) {
		connectToDB();
		String query = "for $x in distinct-values(root/proceedings[title = \"" + escape(edition.getProceedings().getTitle()) + "\"]/booktitle) return <name>{$x}</name>";
		List<Conference> result = (List<Conference>) (List<?>) myQuery(query, Conference.class);

		closeConnectionDB();
		if (result.size() != 0) {
			return result.get(0).getName();
		} else {
			return "";
		}
	}

	public  String getConferenceEditionProceeding(ConferenceEdition edition) {
		return edition.getProceedings().getTitle();
	}

	public  String getConferenceYear(String proceedingName) {
		connectToDB();
		String query = "/root/proceedings[title = \"" + escape(proceedingName) + "\"]/year/text()";
		String result = myQuery(query).get(0);

		closeConnectionDB();
		return result;
	}

	public  List<String> getInProceedingsOfProceedings(String proceedingId) {

		connectToDB();

		String query = "for $x in (/root/inproceedings) where $x/crossref = \"" + proceedingId.replace("key=\"", "").replaceAll("\"", "") + "\" return $x/title/text()";
		List<String> result = myQuery(query);

		closeConnectionDB();
		return result;

	}

	public  List<String> getAuthoredPublicationsForPerson(String personName) {
		connectToDB();
		String query = "for $x in distinct-values((root/inproceedings[author = \"" + personName + "\"]/title)) return $x";
		List<String> result = myQuery(query);
		closeConnectionDB();
		return result;
	}

	public  List<String> getEditedPublicationsForPerson(String personName) {
		connectToDB();
		String query = "for $x in distinct-values((root/proceedings[editor = \"" + personName + "\"]/title)) return $x";
		List<String> result = myQuery(query);
		closeConnectionDB();
		return result;
	}

	public  Proceedings getProceedingOfInproceeding(InProceedings inProceedings) {
		connectToDB();

		String query = "/root/proceedings[@key = \"" + escape(inProceedings.getProceedings().getId()) + "\"]";
		List<Publication> result =(List<Publication>)(List<?>) myQuery(query, Proceedings.class);

		System.out.println(result.get(0).getTitle());
		closeConnectionDB();
		if (result.get(0) == null) {
			return new Proceedings();
		} else {
			return (Proceedings) result.get(0);
		}
	}

	public  List<String> getAuthorsOfInProceeding(InProceedings inProceeding) {

		List<String> result = new ArrayList<String>();
		for (Person p : inProceeding.getAuthors()) {
			result.add(p.getName());
		}
		return result;
	}

	public  List<Publication> searchForPublication(String search) {
		List<Publication> result = searchForProceedings(search);
		result.addAll(searchForInProceedings(search));
		return result;

	}

	public  List<Person> searchForPeople(String search) {
		connectToDB();
		String query = "for $x in distinct-values((root/proceedings/editor|root/inproceedings/author)) where contains($x,\"" + escape(search) + "\" ) return <name>{$x}</name>";
		List<Person> result = (List<Person>) (List<?>) myQuery(query, Person.class);

		for (Person p : result) {
			query = "count(for $x in distinct-values((root/proceedings[editor = \"" + p.getName() + "\"]/title)) return $x)";
			int size = Integer.valueOf(myQuery(query).get(0));
			for (int i = 0; i < size; i++) {
				p.addEditedPublication(new Proceedings());
			}

			query = "count(for $x in distinct-values((root/inproceedings[author = \"" + p.getName() + "\"]/title)) return $x)";
			size = Integer.valueOf(myQuery(query).get(0));
			for (int i = 0; i < size; i++) {
				p.addAuthoredPublication(new InProceedings());
			}

		}

		closeConnectionDB();

		return result;

	}

	public  List<Conference> searchForConference(String search) {
		connectToDB();
		String query = "for $x in distinct-values(root/proceedings/booktitle) where contains($x,\"" + escape(search) + "\" ) return <name>{$x}</name>";
		List<Conference> result = (List<Conference>) (List<?>) myQuery(query, Conference.class);
		for (Conference conf : result) {
			query = "count(/root/proceedings[booktitle = \"" + conf.getName().replaceAll("\"", "'") + "\"])";
			int size = Integer.valueOf(myQuery(query).get(0));
			for (int i = 0; i < size; i++) {
				conf.addEdition(new ConferenceEdition());
			}
		}

		closeConnectionDB();

		return result;
	}

	public  List<Series> searchForSeries(String search) {
		connectToDB();
		String query = "for $x in distinct-values(root/proceedings/series) where contains($x,\"" + escape(search) + "\" ) return <name>{$x}</name>";
		List<Series> result = (List<Series>) (List<?>) myQuery(query, Series.class);

		closeConnectionDB();

		return result;
	}

	public  List<ConferenceEdition> searchForConferenceEdition(String search) {

		connectToDB();
		String query = "for $x in /root/proceedings where contains($x/year,\"" + escape(search) + "\") return $x";
		List<Publication> proceedings = (List<Publication>) (List<?>) myQuery(query, Proceedings.class);
		closeConnectionDB();

		List<ConferenceEdition> result = new ArrayList<ConferenceEdition>();
		for (Publication p : proceedings) {
			ConferenceEdition e = ((Proceedings) p).getConferenceEdition();
			if(!(e == null)){
				e.setYear(p.getYear());
				e.setProceedings((Proceedings) p);
				result.add(e);
			}
		}
		return result;

	}

	public  List<Publisher> searchForPublisher(String search) {
		connectToDB();

		String query = "for $x in distinct-values(root/proceedings/publisher) where contains($x,\"" + escape(search) + "\" ) return <publisherName>{$x}</publisherName>";
		List<Publisher> result = (List<Publisher>) (List<?>) myQuery(query, Publisher.class);

		closeConnectionDB();

		return result;
	}

	public  List<Publication> searchForProceedings(String search) {
		connectToDB();
		String query = "for $x in /root/proceedings where contains($x/title,\"" + escape(search) + "\") return $x";
		List<Publication> result = (List<Publication>) (List<?>) myQuery(query, Proceedings.class);
		closeConnectionDB();

		return result;

	}

	public  List<Publication> searchForInProceedings(String search) {
		connectToDB();
		String query = "for $x in /root/inproceedings where contains($x/title,\"" + escape(search) + "\") return $x";
		List<Publication> result = (List<Publication>) (List<?>) myQuery(query, InProceedings.class);
		closeConnectionDB();

		return result;
	}

	public  void updatePerson(String oldName, String name, List<String> authoredPublications, List<String> editedPublications) {
		deletePerson(oldName);
		addPerson(name, authoredPublications, editedPublications);
	}

	public  void deletePerson(String name) {
		connectToDB();

		String query = "delete node (/root/proceedings/editor[text() = \"" + escape(name) + "\"]|/root/inproceedings/author[text() = \"" + escape(name) + "\"])";
		myQuery(query);
		
		optimize();
		closeConnectionDB();
	}

	public  void updateProceeding(String title, Proceedings newProc, List<String> authors, List<String> inProcNames, String publisherName, String seriesName, String conferenceName, int confYear) {

		deleteProceeding(title);
		addProceeding(newProc, authors, inProcNames, publisherName, seriesName, conferenceName, confYear);
	}

	public  void deleteProceeding(String title) {
		connectToDB();
		String query = "delete node /root/proceedings[title = \"" + escape(title) + "\"]";
		myQuery(query);
		optimize();
		closeConnectionDB();
	}

	public  void addPerson(String newName, List<String> authoredPublications, List<String> editedPublications) {
		connectToDB();
		
		for(String title : authoredPublications){
			String query = "insert node ("+ toxml("author", newName)+") into /root/inproceedings[title = \"" + escape(title) + "\"]";
			myQuery(query);
		}
		
		for(String title : editedPublications){
			String query = "insert node ("+ toxml("editor", newName)+") into /root/proceedings[title = \"" + escape(title) + "\"]";
			myQuery(query);
		}
		
		optimize();
		closeConnectionDB();
	}

	public  void deleteInProceeding(String id) {
		connectToDB();
		String query = "delete node /root/inproceedings[@key = \"" + id.replaceAll("key=", "").replaceAll("\"", "") + "\"]";
		myQuery(query);
		optimize();
		closeConnectionDB();
	}

	public  void updateInProceeding(String id, InProceedings newInProceeding, String procTitle, List<String> authors) {
		deleteInProceeding(id);
		addInProceeding(newInProceeding, procTitle, authors);
		return;
	}

	public  void p(Object o) {
		System.out.println(o);
	}
	
	public  String toxml(String tag, String value){
			
		if(value.equals("")){
			return "";
		}
		if(value == null){
			return "";
		}
		return "<" + tag + ">" + value + "</" + tag + ">";
	}

	public  void addInProceeding(InProceedings newInProceeding, String procTitle, List<String> authors) {
		connectToDB();

		// create new ID for new InProceedings
		String id = java.util.UUID.randomUUID().toString();
		newInProceeding.setId(id);
		
		String authorXml = "";
		for(String s : authors){
			authorXml = authorXml + toxml("author", s);
		}
			
		String crossrefXml = "";
		String queryProc = "/root/proceedings[title = \"" + procTitle + "\"]/@key";
		List<String> procKey = myQuery(queryProc);
		if(procKey.size() != 0){
			crossrefXml = toxml("crossref", procKey.get(0).replaceAll("key=", "").replaceAll("\"", ""));
		}

		String query1 = "insert node (<inproceedings><tag>nodeToEdit</tag>"
						+authorXml
						+toxml("pages",newInProceeding.getPages())
						+toxml("title",newInProceeding.getTitle())
						+toxml("year",String.valueOf(newInProceeding.getYear()))
						+toxml("note",newInProceeding.getNote())
						+toxml("ee",newInProceeding.getElectronicEdition()) 
						+crossrefXml

						+ "</inproceedings>) into /root";
		myQuery(query1);
		
		//add key attribute
		String query2 = "insert node (attribute {'mdate'} {'newDate'}, attribute {'key'} {'"+ id +"'}) into /root/inproceedings[tag = \"nodeToEdit\"]";
		myQuery(query2);
		
		//remove tag
		String query3 = "delete node /root/inproceedings/tag";
		myQuery(query3);
		
		optimize();
		closeConnectionDB();

	}

	public  void addProceeding(Proceedings newProceeding, List<String> authors, List<String> inProceedings, String pubName, String seriesName, String confName, int confYear) {
		connectToDB();
		
		//TODO: escape characters like < >
		// create new ID for new InProceedings
		String id = java.util.UUID.randomUUID().toString();
		newProceeding.setId(id);
		
		String authorXml = "";
		for(String s : authors){
			authorXml = authorXml + toxml("editor", s);
		}
			
		String query1 = "insert node (<proceedings><tag>nodeToEdit</tag>"
						+authorXml
						+toxml("isbn",newProceeding.getIsbn())
						+toxml("title",newProceeding.getTitle())
						+toxml("year",String.valueOf(newProceeding.getYear()))
						+toxml("note",newProceeding.getNote())
						+toxml("ee",newProceeding.getElectronicEdition()) 
						+toxml("volume",newProceeding.getVolume()) 
						+toxml("number",String.valueOf(newProceeding.getNumber())) 
						+toxml("publisher",pubName) 
						+toxml("series",seriesName) 
						+toxml("booktitle",confName) 

						+ "</proceedings>) into /root";
		myQuery(query1);
		
		//add key attribute
		String query2 = "insert node (attribute {'mdate'} {'newDate'}, attribute {'key'} {'"+ id +"'}) into /root/proceedings[tag = \"nodeToEdit\"]";
		myQuery(query2);
		
		//remove tag
		String query3 = "delete node /root/proceedings/tag";
		myQuery(query3);
		
		optimize();

		for(String s : inProceedings){
			String query4 = "delete node /root/inproceedings[title = \"" + s + "\"]/crossref";
			myQuery(query4);
			String query5 = "insert node (" + toxml("crossref", id) + ") into /root/inproceedings[title = \"" + s + "\"]";
			myQuery(query5);
		}
			
		
		optimize();
		closeConnectionDB();

	}

	/**
	 * 
	 * Queries
	 * 
	 **/

	// find title of publication by key (according to xml file)
	public  void query1(String id) {
		String thisQuery = "Query 1";
		connectToDB();
		String queryProc = "for $x in /root/proceedings where contains($x/@key,\"" + escape(id) + "\") return $x/title/text()";
		List<String> resProc = myQuery(queryProc);
		String queryInProc = "for $x in /root/inproceedings where contains($x/@key,\"" + escape(id) + "\") return $x/title/text()";
		List<String> resInProc = myQuery(queryInProc);
		String result = "";
		if (!resProc.isEmpty()) {
			result = resProc.get(0);
		} else if (!resInProc.isEmpty()) {
			result = resInProc.get(0);
		} else {
			result = "No proceeding found with id " + id;
		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println(result);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	// Find publications by title, returning only a subset of all found publications
	public  void query2(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 2";
		connectToDB();
		String queryProc = "for $x in /root/proceedings where contains($x/title,\"" + escape(title) + "\") return $x/title/text()";
		List<String> resProc = myQuery(queryProc);
		String queryInProc = "for $x in /root/inproceedings where contains($x/title,\"" + escape(title) + "\") return $x/title/text()";
		List<String> resInProc = myQuery(queryInProc);
		int i = 0;
		Iterator<String> itrProc = resProc.iterator();
		Iterator<String> itrInProc = resInProc.iterator();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if ((!itrProc.hasNext()) && (!itrInProc.hasNext())) {
				System.out.println("Error: Did not find a publication with title: " + title);
			} else {
				while ((itrProc.hasNext() || itrInProc.hasNext()) && i < endOffset) {
					if (i < startOffset) {
						if (itrInProc.hasNext()) {
							itrInProc.next();
						} else {
							itrProc.next();
						}
						i++;
						continue;
					} else if (itrProc.hasNext()) {
						writer.println(itrProc);
						i++;

					} else if (itrInProc.hasNext()) {
						writer.println(itrInProc.next());
						i++;
					}
				}
			}

			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	// Find publications by title, returning only a subset of all found publications ORDERED by title
	public  void query3(String title, int startOffset, int endOffset) {
		String thisQuery = "Query 3";
		connectToDB();
		String queryProc = "for $x in /root/proceedings where contains($x/title,\"" + escape(title) + "\") return $x/title/text()";
		List<String> resProc = myQuery(queryProc);
		String queryInProc = "for $x in /root/inproceedings where contains($x/title,\"" + escape(title) + "\") return $x/title/text()";
		List<String> resInProc = myQuery(queryInProc);
		Iterator<String> itrProc = resProc.iterator();
		Iterator<String> itrInProc = resInProc.iterator();
		ArrayList<String> all = new ArrayList<String>();
		boolean non = true;
		while (itrProc.hasNext()) {
			all.add(itrProc.next());
			non = false;
		}
		while (itrInProc.hasNext()) {
			non = false;
			all.add(itrInProc.next());
		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (non) {
				System.out.println("Error: Did not find a publication with title: " + title);
			} else {
				Collections.sort(all, new Comparator<String>() {
					public int compare(String s1, String s2) {
						return s1.compareToIgnoreCase(s2);
					}
				});
				for (int i = startOffset; i < endOffset; i++) {
					writer.println(all.get(i));
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	// returns name of the co-authors of a given author
	public  void query4(String author) {
		String thisQuery = "Query 4";
		connectToDB();
		String queryAuth = "for $x in /root/proceedings/editor where contains($x,\"" + escape(author) + "\") return $x/../title/text()";
		List<String> resAuth = myQuery(queryAuth);
		Iterator<String> itr1 = resAuth.iterator();
		String queryEdit = "for $x in /root/inproceedings/author where contains($x,\"" + escape(author) + "\") return $x/../title/text()";
		List<String> resEdit = myQuery(queryEdit);
		Iterator<String> itr2 = resEdit.iterator();
		if (!itr1.hasNext() && !itr2.hasNext()) {
			System.out.println("No publications found with author " + author + ".");
			return;
		}
		HashSet<String> res = new HashSet<String>();
		while (itr1.hasNext()) {
			String queryProc = "for $x in /root/proceedings where contains($x/title,\"" + escape(itr1.next()) + "\") return $x/author/text()";
			res.addAll(myQuery(queryProc));
		}

		while (itr2.hasNext()) {
			String queryProc = "for $x in /root/inproceedings where contains($x/title,\"" + escape(itr2.next()) + "\") return $x/author/text()";
			res.addAll(myQuery(queryProc));
		}
		// remove author's own name
		res.remove(author);
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			Iterator<String> resItr = res.iterator();
			while (resItr.hasNext()) {
				writer.println(resItr.next());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public  void query5(String name1, String name2) {
		String thisQuery = "Query 5";
		connectToDB();

		String queryAuth = "for $x in /root/inproceedings/author where contains($x,\"" + escape(name1) + "\") return $x/../author/text()";
		List<String> resCoAuth = myQuery(queryAuth);
		HashSet<String> coAuth = new HashSet<String>();
		boolean found = false;
		for (String s : resCoAuth){
			coAuth.add(s);
			if (s.contains(name2)){
				found = true;
			}
		}
		int distance = 0;
		int maxDepth = 15;
		HashSet<String> done = new HashSet<String>();
		done.add(name1);
		if(!found){
			distance = query5rec(coAuth, done, name1, name2, 2, maxDepth);
		} else {
			distance = 1;
		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (distance == Integer.MAX_VALUE) {
				writer.println("No path between the two authors has been found");
			} else {
				writer.println("The shortest path between " + name1 + " and " + name2 + " has length:" + distance);
			}
			writer.close();

		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public  int query5rec(HashSet<String> coAuthors, HashSet<String> done, String name1, String name2, int currDepth, int maxDepth) {
		Iterator<String> itrCoAuthors = coAuthors.iterator();
		HashSet<String> nextList = new HashSet<String>();
		System.out.println(currDepth);
		if (!itrCoAuthors.hasNext()) {
			System.out.println("dead end");
			return Integer.MAX_VALUE;
		} else {
			while(itrCoAuthors.hasNext()){
				String curr = itrCoAuthors.next();
				if(curr.contains(name1)){
					continue;
				}
				System.out.println("Current coAuthor: "+ curr);
				String queryCoAuth = "for $x in /root/inproceedings/author where contains($x,\"" + escape(curr) + "\") return $x/../author/text()";
				List<String> resCoCoAuth = myQuery(queryCoAuth);
				Iterator<String> itrCoCoAuthors = resCoCoAuth.iterator();
				if(!itrCoCoAuthors.hasNext()){
					//coauthor does not have any other coauthors, dead end
					continue;
				} else {
					while (itrCoCoAuthors.hasNext()){
						String coCoAuthor = itrCoCoAuthors.next();
						System.out.println("CoCoAuthor "+ coCoAuthor);
						if(coCoAuthor.contains(name2)){
							return currDepth;
						} else if(done.contains(coCoAuthor)) {
							//already visited coCoAuthor
							continue;
						} else {
							//list of co-authors with depth currDepth + 1, will be investigated in next function call
							nextList.add(coCoAuthor);
						}
					}
				}
				done.add(curr);
			}
			int newDepth = currDepth+1;
			if(newDepth <= maxDepth){
				return query5rec( nextList, done, name1, name2, newDepth, maxDepth);
			} else {
				return Integer.MAX_VALUE;
			}
		}
	}

	// global average of authors / publication (only InProceedings)
	public  void query6() {
		String thisQuery = "Query 6";
		connectToDB();
		String queryInProc = "count(root/inproceedings)";
		double noInProc = Double.valueOf(myQuery(queryInProc).get(0));
		String queryAuthor = "count(root/inproceedings/author)";
		double authors = Double.valueOf(myQuery(queryAuthor).get(0));
		closeConnectionDB();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			if (noInProc != 0) {
				writer.println("Average authors " + (double) (authors / noInProc) + ".");
			} else {
				writer.println("There are no publications available.");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
	}

	// Returns the number of publications per year between the interval year1 and year 2
	public  void query7(int year1, int year2) {
		String thisQuery = "Query 7";
		connectToDB();

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.printf("%-20s%-12s%-17s%n%n", "Publication Type", "Year", "No. Publications");
			int all = 0;
			for (int y = year1; y <= year2; y++) {
				String queryAuth = "count(for $x in (root/inproceedings/[year=" + y + "]) return $x)";
				List<String> resAuth = myQuery(queryAuth);
				writer.printf("%-20s%-12s%-17s%n", "InProceedings ", y, resAuth.get(0));
				String queryProc = "count(for $x in (root/proceedings/[year=" + y + "]) return $x)";
				List<String> resProc = myQuery(queryProc);
				writer.printf("%-20s%-12s%-17s%n", "Proceedings ", y, resProc.get(0));
				all = Integer.valueOf(resProc.get(0)) + Integer.valueOf(resAuth.get(0));
				writer.printf("%-20s%-12s%-17s%n%n", "All Publications", y, all);
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	// No of all publications of a conference, for every edition except proceedings
	public  void query8(String conferenceName) {
		connectToDB();
		// TODO: why does below not work? Is this the right approach?
		// "count(for $x in /root/inproceedings where contains($x/crossref, \""+escape(conferenceName)+"\") return $x)";
		// this works
		String queryAuth = "count(for $x in /root/inproceedings where contains($x/booktitle, \"" + escape(conferenceName) + "\") return $x)";
		List<String> resAuth = myQuery(queryAuth);
		String thisQuery = "Query 8";

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("The number of all InProceedings of Conference " + conferenceName + " is  " + resAuth.get(0) + ".");
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public  void query9(String confName) {
		connectToDB();
		String thisQuery = "Query 9";

		String queryAuth = "for $x in /root/inproceedings where contains($x/booktitle, \"" + escape(confName) + "\") return $x/author/text()";
		List<String> resAuth = myQuery(queryAuth);
		HashSet<String> result =new HashSet<String>();
		for (String s : resAuth){
			result.add(s);
		}
		String queryEdit = "for $x in /root/proceedings where contains($x/booktitle, \"" + escape(confName) + "\") return $x/editor/text()";
		List<String> resEdit = myQuery(queryEdit);
		for (String s : resEdit){
			result.add(s);
		}
		int res = result.size();

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("Number of authors and editors: " + res);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public  void query10(String confName) {
		connectToDB();
		String thisQuery = "Query 10";

		String queryAuth = "for $x in /root/inproceedings where contains($x/booktitle, \"" + escape(confName) + "\") return $x/author/text()";
		List<String> resAuth = myQuery(queryAuth);

		String queryEdit = "for $x in /root/proceedings where contains($x/booktitle, \"" + escape(confName) + "\") return $x/editor/text()";
		List<String> resEdit = myQuery(queryEdit);
		resAuth.addAll(resEdit);
		Iterator<String> itr = resAuth.iterator();
		if (!itr.hasNext()) {
			System.out.println("Did not find a Conference with any authors with name: " + confName);
			return;
		}
		HashSet<String> resultList = new HashSet<String>();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			while (itr.hasNext()) {
				String next = itr.next();
				if(resultList.contains(next)){
					//already printed
					continue;
				} else {
					resultList.add(next);
					writer.println(next);
				}
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public  void query11(String confName) {
		connectToDB();
		String thisQuery = "Query 11";

		String queryPub = "for $x in /root/inproceedings where contains($x/booktitle, \"" + escape(confName) + "\") return $x/title/text()";
		List<String> resPub = myQuery(queryPub);

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of all " + resPub.size() + " InProceedings of Conference " + confName);
			for (String title : resPub) {
				writer.println(title);
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}

		closeConnectionDB();
	}

	public  void query12() throws ParserConfigurationException, SAXException, IOException {
		String thisQuery = "Query 12";
		HashSet<String> resultList = new HashSet<String>();
		connectToDB();
		
		//crossref is the foreign key in InProceedings to Proceedings (key)
/*		String queryPub = "for $x in /root/inproceedings,"
				+ "$y in /root/proceedings[@key = $x/crossref]"
				+ "return <item procAuthor=\"{$y/editor}\" inAuthor=\"{$x/author}\"/>";
		List<String> resPub = myQuery(queryPub);
*/
//example output: F. Ron Bailey
//inAuthor="Ramesh C. Agarwal Fred G. Gustavson"
		
		String queryInProc = "for $x in /root/proceedings return <item key=\"{$x/@key}\" editor=\"{$x/editor/text()}\"/>";
		List<String> resInProc = myQuery(queryInProc);
		Iterator<String> inProcItr = resInProc.iterator();
		while(inProcItr.hasNext()){
			//String inProc = inProcItr.next();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(inProcItr.next())));
			Element rootElement = document.getDocumentElement();
			String crossref = rootElement.getAttribute("key");
			if(crossref.equals("") || crossref == null){
				//System.out.println("continue");
				continue;
			}
			// Expecting separator after number.
			String queryProc = "for $x in /root/inproceedings where $x/crossref=\""+ escape(crossref) +"\" return $x/author/text()";
			List<String> resProc = myQuery(queryProc);
			Iterator<String> procItr = resProc.iterator();
			while(procItr.hasNext()){
				String author = procItr.next();
				//System.out.println(author);
				//System.out.println(rootElement.getAttributeNode("editor"));
				//example output 
					//G. Spettel
					//editor="Ursula Klenk Peter Scherber Manfred Thaller" 
				if((rootElement.getAttributeNode("editor")).toString().contains(author.replaceAll("editor=", "").replaceAll("\"", ""))){
					resultList.add(author);
				}
			}
		}

		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of all " + resultList.size() + " authors that occur as editor in Proceeding as well as in InProceeding as and author:");
			for(String s : resultList){
				writer.println(s);
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();		
	}

	// all publications, where given author is mentioned last
	public  void query13(String author) throws Exception {
		int count = 0;
		String thisQuery = "Query 13";
		connectToDB();

		HashSet<String> result = new HashSet<String>();
		String queryPub = "for $x in /root/inproceedings/author where contains($x, \"" + escape(author) + "\") return <item title=\"{$x/../title}\" authors=\"{$x/../author/text()}\"/>";
		List<String> resPub = myQuery(queryPub);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		for(int i = 0; i < resPub.size(); i++){
			//examples outputs for Adi Shadmir
			//<item title="Polymorphic Arrays: An Architecture for a Programmable Systolic Machine." authors="Amos Fiat Adi Shamir Ehud Y. Shapiro"/>
			//<item title="Shear Sort: A True Two-Dimensional Sorting Techniques for VLSI Networks." authors="Sandeep Sen Isaac D. Scherson Adi Shamir"/>
			//System.out.println(resPub.get(i));
			String curr = resPub.get(i);
			String subStr = curr.substring(curr.length()-author.length()-3);
			if(subStr.contains(author)){
				count++;
				org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(resPub.get(i))));
				Element rootElement = document.getDocumentElement();
				result.add(rootElement.getAttribute("title"));
			}
		}
		
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("The number of publications where the author named " + author + " is mentioned last is " + count + ".");
			for(String s : result){
				writer.println(s);
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}

	public  void query14(int year1, int year2) {
		String thisQuery = "Query 14";
		HashSet<String> resultList = new HashSet<String>();

		connectToDB();
		List<String> resPub = new LinkedList<String>();
		for (int i = year1; i <= year2; i++) {
			String queryPub = "for $x in root/inproceedings where $x/year=" + i + " return $x/publisher/text()";
			resPub = myQuery(queryPub);
			resultList.addAll(resPub);
		}
		for (int i = year1; i <= year2; i++) {
			String queryPub = "for $x in root/proceedings where $x/year=" + i + " return $x/publisher/text()";
			resPub = myQuery(queryPub);
			resultList.addAll(resPub);
		}
		Iterator<String> itr = resultList.iterator();
		try {
			PrintWriter writer = new PrintWriter("QueryResults/" + thisQuery + ".txt", "UTF-8");
			writer.println(thisQuery);
			writer.println("List of all " + resultList.size() + " Publishers.....:");
			while (itr.hasNext()) {
				writer.println(itr.next());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not print to file.");
		}
		closeConnectionDB();
	}



}
