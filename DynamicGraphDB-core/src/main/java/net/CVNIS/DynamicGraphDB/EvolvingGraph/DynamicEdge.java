/**
 * 
 */
package net.CVNIS.DynamicGraphDB.EvolvingGraph;

import com.tinkerpop.blueprints.Direction;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
public interface DynamicEdge extends DynamicElement{
	
	/**
	 * @param direction
	 * @return
	 * @throws IllegalArgumentException
	 */
	DynamicVertex getVertex(Direction direction) throws IllegalArgumentException;
}
