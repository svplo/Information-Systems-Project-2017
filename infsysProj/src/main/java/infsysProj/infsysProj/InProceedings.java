package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

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
		zooActivateRead();
		return note;
	}

	public void setNote(String note) {
		zooActivateWrite();
		this.note = note;
	}

	public String getPages() {
		zooActivateRead();
		return pages;
	}

	public void setPages(String pages) {
		zooActivateWrite();
		this.pages = pages;
	}

	public Proceedings getProceedings() {
		zooActivateRead();
		return proceedings;
	}

	public void setProceedings(Proceedings proceedings) {
		zooActivateWrite();
		this.proceedings = proceedings;
	}

}