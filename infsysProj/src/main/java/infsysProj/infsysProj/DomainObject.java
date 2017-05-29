package infsysProj.infsysProj;

import javax.validation.constraints.NotNull;

import org.zoodb.api.impl.ZooPC;

/**
 * The base class for all domain objects.
 */

public class DomainObject extends ZooPC  {

	@NotNull(message = "Domain Object needs to have an ID")
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
