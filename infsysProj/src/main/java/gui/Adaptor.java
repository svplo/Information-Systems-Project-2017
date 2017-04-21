package gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.basex.query.QueryIOException;
import org.basex.query.value.item.Item;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.query.value.node.FElem;

import infsysProj.infsysProj.DomainObject;

public class Adaptor {

	public static DomainObject toJava(Item n, Class<? extends DomainObject> c) {

		try {
			Constructor<?> constructor;
			constructor = c.getConstructor();
			DomainObject result = (DomainObject) constructor.newInstance(new Object[] { });
			
			if(n instanceof FElem){
				Iterator<ANode> i = ((FElem)n).children().iterator();
				
				while (i.hasNext()) {
					if(i.next().qname() == null){
						result = updateObject(((FElem)n),((FElem)n).qname().toString(), result);
					}
					else{
						result = updateObject(i.next(), i.next().qname().toString(), result);
					}
				}
			}
			else{
				if(((DBNode)n).attributes().size()>1){
					result = updateObject((DBNode)n,"key", result);
				}
				Iterator<ANode> i = ((DBNode)n).children().iterator();
				
				while (i.hasNext()) {
					if(i.next().qname() == null){
						result = updateObject(((DBNode)n),((DBNode)n).qname().toString(), result);
					}
					else{
						result = updateObject(i.next(), i.next().qname().toString(), result);
					}
				}

			}

			return result;

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException  e) {
			e.printStackTrace();
			return null;
		}

	}

	private static DomainObject updateObject(ANode n, String qName, DomainObject o) {

		try {
			String value;
			switch (qName) {
			case "title":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setTitle", String.class).invoke(o, value);
				break;
			case "year":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setYear", int.class).invoke(o, Integer.valueOf(value));
				break;
			case "ee":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setElectronicEdition", String.class).invoke(o, value);
				break;
			case "number":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setNumber", int.class).invoke(o, Integer.valueOf(value));
				break;
			case "isbn":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setIsbn", String.class).invoke(o, value);
				break;
			case "note":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setNote", String.class).invoke(o, value);
				break;
			case "volume":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setVolume", String.class).invoke(o, value);
				break;
			case "name":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setName", String.class).invoke(o, value);
				break;
			case "pages":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setPages", String.class).invoke(o, value);
				break;
			case "publisher":
				value = n.children().next().serialize().toString();
				o.getClass().getMethod("setName", String.class).invoke(o, value);
				break;
			case "key":
				value = n.attributes().get(1).serialize().toString();
				o.getClass().getMethod("setId", String.class).invoke(o, value);

				break;


			default:
				break;
			}

		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | QueryIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NoSuchMethodException e){

		}
		
		return o;
	}
}
