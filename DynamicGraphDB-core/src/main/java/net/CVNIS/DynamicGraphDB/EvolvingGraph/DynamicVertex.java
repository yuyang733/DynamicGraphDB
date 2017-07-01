/**
 * 
 */
package net.CVNIS.DynamicGraphDB.EvolvingGraph;

import com.tinkerpop.blueprints.Direction;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
public interface DynamicVertex extends DynamicElement {
	
	/**
	 * 获取指定标签的邻接顶点
	 * @param direction
	 * @param labels
	 * @return
	 */
	public Iterable<DynamicVertex> getVertices(Direction direction,String... labels);
	
	/**
	 * 获取指定标签的边
	 * @param direction
	 * @param labels
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Iterable<DynamicEdge> getEdges(Direction direction, String... labels) throws IllegalArgumentException;
	
	/**
	 * 给顶点加上一条边   
	 * @param label
	 * @param inVertex
	 * @param timestamp
	 * @return
	 */
	public DynamicEdge addEdge(String label, DynamicVertex inVertex, long timestamp) throws IllegalArgumentException;
	
}
