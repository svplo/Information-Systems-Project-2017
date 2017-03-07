package infsysProj.infsysProj;
import javax.jdo.annotations.PersistenceCapable;

import org.zoodb.api.impl.ZooPC;

//package ch.ethz.globis.isk.domain;

/**
 *  The base class for all domain objects.
 */
@PersistenceCapable
public class DomainObject extends ZooPC {

    public String getId(){
    	return "";
    }

    public void setId(String id){
    	
    }
    
}
