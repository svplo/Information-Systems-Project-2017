package infsysProj.infsysProj;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a certain conference. A conference has more conference editions, usually one edition per year.
 */
public class Conference extends DomainObject {
	String name;
	Set<ConferenceEdition> editions = new HashSet<ConferenceEdition>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ConferenceEdition> getEditions() {
		return editions;
	}

	public void setEditions(Set<ConferenceEdition> editions) {
		this.editions = editions;
	}

	public void addEdition(ConferenceEdition edition){
		editions.add(edition);
	}
}