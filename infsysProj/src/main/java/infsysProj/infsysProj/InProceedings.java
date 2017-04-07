package infsysProj.infsysProj;

import java.util.List;

/**
 * A type of article that was published as part of a conference proceedings (one conference proceeding features many in proceedings).
 */
public class InProceedings extends Publication {
	String note;
	String pages;
	Proceedings proceedings;
	

	public InProceedings(Integer num) {
		super(num);
		// TODO Auto-generated constructor stub
	}

	public InProceedings() {
		super(1);
	}

	public InProceedings(String id, String title, List<Person> authors, int year, String electronicEdition, String note, String pages, Proceedings proceedings){
		super(1);
		this.setId(id);
		this.setTitle(title);
		this.setAuthors(authors);
		this.setYear(year);
		this.setElectronicEdition(electronicEdition);
		this.setNote(note);
		this.setPages(pages);
		this.setProceedings(proceedings);
	}

	public InProceedings(String title, List<Person> authors, int year, String electronicEdition, String note, String pages, Proceedings proceedings){
		super(1);
		this.setTitle(title);
		this.setAuthors(authors);
		this.setYear(year);
		this.setElectronicEdition(electronicEdition);
		this.setNote(note);
		this.setPages(pages);
		this.setProceedings(proceedings);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public Proceedings getProceedings() {
		return proceedings;
	}

	public void setProceedings(Proceedings proceedings) {
		this.proceedings = proceedings;
	}

}