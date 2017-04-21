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

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

	}

	private static DomainObject updateObject(ANode n, String qName, DomainObject o) {

		try {
			String value = n.children().next().serialize().toString();
			switch (qName) {
			case "title":
				o.getClass().getMethod("setTitle", String.class).invoke(o, value);
				break;
			case "year":
				o.getClass().getMethod("setYear", int.class).invoke(o, Integer.valueOf(value));
				break;
			case "ee":
				o.getClass().getMethod("setElectronicEdition", String.class).invoke(o, value);
				break;
			case "number":
				o.getClass().getMethod("setNumber", int.class).invoke(o, Integer.valueOf(value));
				break;
			case "isbn":
				o.getClass().getMethod("setIsbn", String.class).invoke(o, value);
				break;
			case "note":
				o.getClass().getMethod("setNote", String.class).invoke(o, value);
				break;
			case "volume":
				o.getClass().getMethod("setVolume", String.class).invoke(o, value);
				break;
			case "name":
				o.getClass().getMethod("setName", String.class).invoke(o, value);
				break;
			case "pages":
				o.getClass().getMethod("setPages", String.class).invoke(o, value);
				break;
			case "publisher":
				o.getClass().getMethod("setName", String.class).invoke(o, value);
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
