 /**
 * 
 */
package net.CVNIS.DynamicGraphDB.EvolvingGraph.index;

import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicElement;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
public interface Index<T extends DynamicElement> {
	
	/**
	 * Get the name of the index
	 * @return	the name of the index
	 */
	public String getIndexName();
	
	
	/**
	 * Get the class that this index is indexing 
	 * @return the class this index is indexing 
	 */
	public Class<T> getIndexClass();
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param element
	 */
	public void put(String key, Object value, T element);
}
