package infsysProj.infsysProj;

import org.zoodb.api.impl.ZooPC;

/**
 * The base class for all domain objects.
 */

public class DomainObject extends ZooPC  {

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
