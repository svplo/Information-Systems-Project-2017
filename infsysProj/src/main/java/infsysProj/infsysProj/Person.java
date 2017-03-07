package infsysProj.infsysProj;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.zoodb.api.impl.ZooPC;

/**
 * Simple example for a persistent class.
 * 
 * @author ztilmann
 */
public class Person extends ZooPC {

    private String name;
    private Set<Person> friends = new HashSet<Person>();
    
    @SuppressWarnings("unused")
    private Person() {
        // All persistent classes need a no-args constructor. 
        // The no-args constructor can be private.
    }
    
    public Person(String name) {
        // no activation required
        this.name = name;
    }

    public void setName(String name) {
        //activate and flag as dirty
        zooActivateWrite();
        this.name = name;
    }
    
    public String getName() {
        //activate
        zooActivateRead();
        return this.name;
    }
    
    public void addFriend(Person p) {
        //activate and flag as dirty
        zooActivateWrite();
        this.friends.add(p);
    }
    
    public Collection<Person> getFriends() {
        //activate
        zooActivateRead();
        //prevent callers from modifying the set.
        return Collections.unmodifiableSet(friends);
	}
}
