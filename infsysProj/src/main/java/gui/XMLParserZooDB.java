package gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class XMLParserZooDB {

	private static String xmlFileName = "dblp_filtered.xml";
	List<Proceedings> readProceedings = new ArrayList<Proceedings>();
	List<InProceedings> readInProceedings = new ArrayList<InProceedings>();
	List<Person> readPeople = new ArrayList<Person>();
	List<Conference> readConferences = new ArrayList<Conference>();
	List<ConferenceEdition> readConferenceEditions = new ArrayList<ConferenceEdition>();
	List<Publisher> readPublishers = new ArrayList<Publisher>();
	List<Series> readSeries = new ArrayList<Series>();

	public void parse() {

		System.out.println("Parsing Proceedings...");
		parseProceedings(xmlFileName);
		System.out.println("Parsing InProceedings...");

		parseInProceedings(xmlFileName);
		
		DatabaseHelperZooDB dh = (DatabaseHelperZooDB)DatabaseHelper.get();
		
		dh.createDBinternal();
		dh.openDB();
		dh.addProceedings(readProceedings);
		dh.addInProceedings(readInProceedings);
		dh.closeDB();

	}

	private void parseProceedings(String xmlName) {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				Proceedings procTemp = null;
				ArrayList<Person> editors = new ArrayList<Person>();
				String title = "";
				int year;
				String booktitle = "";
				String isbn = "";
				String publisher = "";
				String series = "";
				String volume = "";
				String ee = "";
				boolean inEditor = false;
				boolean inTitle = false;
				boolean inYear = false;
				boolean inBooktitle = false;
				boolean inISBN = false;
				boolean inPublisher = false;
				boolean inSeries = false;
				boolean inVolume = false;
				boolean inEE = false;

				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("proceedings")) {

						title = "";
						year = 0;
						booktitle = "";
						publisher = "";
						series = "";
						volume = "";
						ee = "";
						editors.clear();

						procTemp = new Proceedings(3);
						procTemp.setId(attributes.getValue(1));
						// procTemp.setElectronicEdition(attributes.getValue(0));

					}

					else if (qName.equalsIgnoreCase("editor")) {
						inEditor = true;
					}

					else if (qName.equalsIgnoreCase("title")) {
						inTitle = true;
					} else if (qName.equalsIgnoreCase("year")) {
						inYear = true;
					}

					else if (qName.equalsIgnoreCase("booktitle")) {
						inBooktitle = true;
					} else if (qName.equalsIgnoreCase("isbn")) {
						inISBN = true;
					} else if (qName.equalsIgnoreCase("publisher")) {
						inPublisher = true;
					} else if (qName.equalsIgnoreCase("series")) {
						inSeries = true;
					} else if (qName.equalsIgnoreCase("volume")) {
						inVolume = true;
					} else if (qName.equalsIgnoreCase("ee")) {
						inEE = true;
					}

				}

				public void endElement(String uri, String localName, String qName) throws SAXException {

					if (qName.equalsIgnoreCase("proceedings")) {
						procTemp.setAuthors(editors);
						procTemp.setYear(year);
						procTemp.setTitle(title);
						procTemp.setIsbn(isbn);
						procTemp.setVolume(volume);
						procTemp.setElectronicEdition(ee);

						addProceeding(procTemp, booktitle, publisher, series);

						procTemp = null;
					}

					else if (qName.equalsIgnoreCase("editor")) {
						inEditor = false;
					} else if (qName.equalsIgnoreCase("title")) {
						inTitle = false;
					} else if (qName.equalsIgnoreCase("year")) {
						inYear = false;
					} else if (qName.equalsIgnoreCase("booktitle")) {
						inBooktitle = false;
					} else if (qName.equalsIgnoreCase("isbn")) {
						inISBN = false;
					} else if (qName.equalsIgnoreCase("publisher")) {
						inPublisher = false;
					} else if (qName.equalsIgnoreCase("series")) {
						inSeries = false;
					} else if (qName.equalsIgnoreCase("volume")) {
						inVolume = false;
					} else if (qName.equalsIgnoreCase("ee")) {
						inEE = false;
					}

					// System.out.println("End Element :" + qName);

				}

				public void characters(char ch[], int start, int length) throws SAXException {

					if (procTemp != null) {
						if (inEditor) {
							editors.add(new Person(new String(ch, start, length)));
						} else if (inTitle) {
							title = new String(ch, start, length);
						} else if (inYear) {
							year = Integer.parseInt(new String(ch, start, length));
						} else if (inBooktitle) {
							booktitle = new String(ch, start, length);
						} else if (inISBN) {
							isbn = new String(ch, start, length);
						} else if (inPublisher) {
							publisher = new String(ch, start, length);
						} else if (inSeries) {
							series = new String(ch, start, length);
						} else if (inVolume) {
							volume = new String(ch, start, length);
						} else if (inEE) {
							ee = new String(ch, start, length);
						}

					}

				}

			};

			saxParser.parse(xmlName, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void parseInProceedings(String xmlName) {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				InProceedings inProcTemp = null;
				ArrayList<Person> authors = new ArrayList<Person>();
				String title = "";
				int year;
				String pages = "";
				String crossref = "";
				String ee = "";
				boolean inAuthor = false;
				boolean inTitle = false;
				boolean inPages = false;
				boolean inYear = false;
				boolean inCrossRef = false;
				boolean inEE = false;

				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("inproceedings")) {

						title = "";
						year = 0;
						pages = "";
						crossref = "";
						ee = "";

						authors.clear();

						inProcTemp = new InProceedings(3);
						inProcTemp.setId(attributes.getValue(1));
						inProcTemp.setElectronicEdition(attributes.getValue(0));
					}

					else if (qName.equalsIgnoreCase("author")) {
						inAuthor = true;
					}

					else if (qName.equalsIgnoreCase("title")) {
						inTitle = true;
					} else if (qName.equalsIgnoreCase("pages")) {
						inPages = true;
					} else if (qName.equalsIgnoreCase("year")) {
						inYear = true;
					} else if (qName.equalsIgnoreCase("crossref")) {
						inCrossRef = true;
					} else if (qName.equalsIgnoreCase("ee")) {
						inEE = true;
					}

				}

				public void endElement(String uri, String localName, String qName) throws SAXException {

					if (qName.equalsIgnoreCase("inproceedings")) {
						inProcTemp.setAuthors(authors);
						inProcTemp.setYear(year);
						inProcTemp.setPages(pages);
						inProcTemp.setTitle(title);
						inProcTemp.setElectronicEdition(ee);

						addInProceeding(inProcTemp, crossref);

						inProcTemp = null;
					}

					else if (qName.equalsIgnoreCase("author")) {
						inAuthor = false;
					} else if (qName.equalsIgnoreCase("title")) {
						inTitle = false;
					} else if (qName.equalsIgnoreCase("pages")) {
						inPages = false;
					} else if (qName.equalsIgnoreCase("year")) {
						inYear = false;
					} else if (qName.equalsIgnoreCase("crossref")) {
						inCrossRef = false;
					} else if (qName.equalsIgnoreCase("ee")) {
						inEE = false;
					}

					// System.out.println("End Element :" + qName);

				}

				public void characters(char ch[], int start, int length) throws SAXException {

					if (inProcTemp != null) {

						if (inAuthor) {
							authors.add(new Person(new String(ch, start, length)));
						} else if (inTitle) {
							title = new String(ch, start, length);
						} else if (inPages) {
							pages = new String(ch, start, length);
						} else if (inYear) {
							year = Integer.parseInt(new String(ch, start, length));
						} else if (inCrossRef) {
							crossref = new String(ch, start, length);
						} else if (inEE) {
							ee = new String(ch, start, length);
						}

					}

				}

			};

			saxParser.parse(xmlName, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addProceeding(Proceedings in, String booktitle, String publisher, String series) {

		List<Conference> conferences = readConferences.parallelStream().filter(a -> Objects.equals(a.getName(), booktitle)).collect(Collectors.toList());
		Conference conf;
		ConferenceEdition edition = new ConferenceEdition();
		if (conferences.isEmpty()) {
			conf = new Conference();
			conf.setName(booktitle);
			edition.setConference(conf);
			edition.setYear(in.getYear());
			edition.setProceedings(in);
			Set<ConferenceEdition> conferenceEditions = new HashSet<ConferenceEdition>();
			conferenceEditions.add(edition);
			conf.setEditions(conferenceEditions);
			readConferences.add(conf);

		} else {
			conf = conferences.iterator().next();
			readConferences.remove(conf);
			edition.setConference(conf);
			edition.setYear(in.getYear());
			edition.setProceedings(in);
			Set<ConferenceEdition> conferenceEditions = conf.getEditions();
			conferenceEditions.add(edition);
			conf.setEditions(conferenceEditions);
			readConferences.add(conf);
		}

		List<Publisher> publishers = readPublishers.parallelStream().filter(a -> Objects.equals(a.getName(), publisher)).collect(Collectors.toList());
		Publisher pub;
		if (publishers.isEmpty()) {
			pub = new Publisher();
			pub.setName(publisher);
			Set<Publication> publications = new HashSet<Publication>();
			publications.add(in);
			pub.setPublications(publications);
			readPublishers.add(pub);

		} else {
			pub = publishers.iterator().next();
			readPublishers.remove(pub);
			Set<Publication> publications = pub.getPublications();
			publications.add(in);
			pub.setPublications(publications);
			readPublishers.add(pub);
		}

		List<Series> seriesList = readSeries.parallelStream().filter(a -> Objects.equals(a.getName(), series)).collect(Collectors.toList());
		Series seriesResult;
		if (seriesList.isEmpty()) {
			seriesResult = new Series();
			seriesResult.setName(series);
			Set<Publication> publications = new HashSet<Publication>();
			publications.add(in);
			seriesResult.setPublications(publications);
			readSeries.add(seriesResult);

		} else {
			seriesResult = seriesList.iterator().next();
			readSeries.remove(seriesResult);
			Set<Publication> publications = seriesResult.getPublications();
			publications.add(in);
			seriesResult.setPublications(publications);
			readSeries.add(seriesResult);
		}

		// Handle authors
		List<Person> editors = new ArrayList<Person>();

		for (Person p : in.getAuthors()) {
			List<Person> editorList = readPeople.parallelStream().filter(a -> Objects.equals(a.getName(), p.getName())).collect(Collectors.toList());
			Person editorResult;
			if (editorList.isEmpty()) {
				editorResult = p;
				Set<Publication> publications = new HashSet<Publication>();
				publications.add(in);
				editorResult.setEditedPublications(publications);
				readPeople.add(editorResult);

			} else {
				editorResult = editorList.iterator().next();
				readPeople.remove(editorResult);
				Set<Publication> publications = editorResult.getEditedPublications();
				publications.add(in);
				editorResult.setEditedPublications(publications);
				readPeople.add(editorResult);
			}
			editors.add(editorResult);

		}

		in.setAuthors(editors);
		in.setSeries(seriesResult);
		in.setPublisher(pub);
		in.setConferenceEdition(edition);
		readProceedings.add(in);
	}

	private void addInProceeding(InProceedings in, String crossref) {

		if (crossref != "") {
			List<Proceedings> proceedings = readProceedings.parallelStream().filter(a -> Objects.equals(a.getID(), crossref)).collect(Collectors.toList());
			Proceedings proc;
			if (proceedings.isEmpty()) {
				System.out.println("Error: Did not find corresponding Proceeding while attempting to add InProceeding to Database. Crossref: " + crossref);
				in.setNote("missing Proceedings reference");
			} else {
				proc = proceedings.iterator().next();
				in.setProceedings(proc);
				Set<InProceedings> publications = proc.getInProceedings();
				publications.add(in);
				proc.setInProceedings(publications);
			}
		}

		// Handle authors

		List<Person> authors = new ArrayList<Person>();
		for (Person p : in.getAuthors()) {
			List<Person> editorObjects = readPeople.parallelStream().filter(a -> Objects.equals(a.getName(), p.getName())).collect(Collectors.toList());
			Person author;
			if (editorObjects.isEmpty()) {
				author = p;
				Set<Publication> publications = new HashSet<Publication>();
				publications.add(in);
				author.setAuthoredPublications(publications);
				readPeople.add(author);

			} else {
				author = editorObjects.iterator().next();
				readPeople.remove(author);
				Set<Publication> publications = author.getAuthoredPublications();
				publications.add(in);
				author.setAuthoredPublications(publications);
				readPeople.add(author);
			}
			authors.add(author);

		}
		// System.out.println("Added InProceeding: " + in.getTitle());

		in.setAuthors(authors);

		readInProceedings.add(in);
	}

}
