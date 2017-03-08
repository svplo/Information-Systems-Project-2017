package infsysProj.infsysProj;
//package ch.ethz.globis.isk.domain;

/**
 * A type of article that was published as part of a conference proceedings.
 */
public class InProceedings extends Publication {
	String note;
	String pages;
	Proceedings proceedings;
	
    public String getNote(){
    	zooActivateRead();
    	return note;
    }

    public void setNote(String note){
    	zooActivateWrite();
    	this.note = note;
    }

    public String getPages(){
    	zooActivateRead();
    	return pages;
    }

    public void setPages(String pages){
    	zooActivateWrite();
    	this.pages = pages;
    }

    public Proceedings getProceedings(){
    	zooActivateRead();
    	return proceedings;
    }

    public void setProceedings(Proceedings proceedings){
    	zooActivateWrite();
    	this.proceedings = proceedings;
    }
    
}