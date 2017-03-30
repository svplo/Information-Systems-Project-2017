package infsysProj.infsysProj;

/**
 * A type of article that was published as part of a conference proceedings (one conference proceeding features many in proceedings).
 */
public class InProceedings extends Publication {
	public InProceedings(Integer num) {
		super(num);
		// TODO Auto-generated constructor stub
	}

	public InProceedings() {
		super(1);
	}

	String note;
	String pages;
	Proceedings proceedings;

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