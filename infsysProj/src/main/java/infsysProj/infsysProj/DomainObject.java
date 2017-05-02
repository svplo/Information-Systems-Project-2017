package infsysProj.infsysProj;

import org.zoodb.api.impl.ZooPC;

/**
 * The base class for all domain objects.
 */

public class DomainObject extends ZooPC  {

	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
