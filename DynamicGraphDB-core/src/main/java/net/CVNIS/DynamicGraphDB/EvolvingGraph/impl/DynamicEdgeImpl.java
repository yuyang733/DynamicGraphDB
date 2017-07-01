/**
 * 
 */
package net.CVNIS.DynamicGraphDB.EvolvingGraph.impl;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;

import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicEdge;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicGraph;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicVertex;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.EvolvingOperation;
import net.CVNIS.DynamicGraphDB.exception.DGExceptionFactory;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
class DynamicEdgeImpl extends DynamicElementImpl implements DynamicEdge {
	
	protected Edge edge = null;
	
	protected DynamicVertex inVertex;
	protected DynamicVertex outVertex;

	DynamicEdgeImpl(DynamicGraph graph, Edge edge, String label, DynamicVertex outVertex, DynamicVertex inVertex, long createTimestamp) {
		super(graph, edge, label, createTimestamp);
		this.inVertex = inVertex;
		this.outVertex = outVertex;
		this.edge = edge;
		this.recordUpdateLog(this,createTimestamp,EvolvingOperation.add_edge, "label", label);
	}

	public DynamicVertex getVertex(Direction direction) throws IllegalArgumentException {
		if(direction.equals(Direction.IN)){
			return this.inVertex;
		}
		else if(direction.equals(Direction.OUT)){
			return this.outVertex;
		}
		else{
			throw new IllegalArgumentException();			//参数不对
		}
	}

	public void remove(long timeStamp) {
		if(timeStamp < 0){
			throw DGExceptionFactory.timestampIsInvalid(timeStamp);
		}
		
		this.graph.removeEdge(this, timeStamp);
	}
}
