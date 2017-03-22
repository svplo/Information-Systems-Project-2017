package infsysProj.infsysProj;

//package ch.ethz.globis.isk.domain;
import javax.jdo.annotations.PersistenceCapable;

import org.zoodb.api.impl.ZooPC;

/**
 * The base class for all domain objects.
 */
@PersistenceCapable
public class DomainObject extends ZooPC {

	String id;

	public String getId() {
		zooActivateRead();
		return id;
	}

	public void setId(String id) {
		zooActivateWrite();
		this.id = id;
	}

}
