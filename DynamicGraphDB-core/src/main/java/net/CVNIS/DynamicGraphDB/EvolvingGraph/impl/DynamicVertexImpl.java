/**
 * 
 */
package net.CVNIS.DynamicGraphDB.EvolvingGraph.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import net.CVNIS.DynamicGraphDB.Label;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicEdge;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicGraph;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicVertex;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.EvolvingOperation;
import net.CVNIS.DynamicGraphDB.exception.DGExceptionFactory;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
class DynamicVertexImpl extends DynamicElementImpl implements DynamicVertex, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1671756311395666736L;

	protected Vertex vertex = null;

	protected Map<String, Set<DynamicEdge>> inEdges = new HashMap<String, Set<DynamicEdge>>();
	protected Map<String, Set<DynamicEdge>> outEdges = new HashMap<String, Set<DynamicEdge>>();

	DynamicVertexImpl(DynamicGraph graph, Vertex vertex, String label, long createTimestamp) {
		super(graph, vertex, label, createTimestamp);
		this.vertex = vertex;
		this.recordUpdateLog(this,createTimestamp, EvolvingOperation.add_vertex, label, this.vertex.getId());
	}

	DynamicVertexImpl(DynamicGraph graph, Vertex vertex, long createTimestamp) {
		this(graph, vertex, Label.NO_LABEL, createTimestamp);
	}

	protected Iterable<DynamicEdge> getInEdges(String... labels) {
		List<DynamicEdge> inEdges = new ArrayList<DynamicEdge>();

		if (labels.length == 0) {
			for (Collection<DynamicEdge> edges : this.inEdges.values()) {
				inEdges.addAll(edges);
			}
		} else if (labels.length == 1) {
			Set<DynamicEdge> edges = this.inEdges.get(labels[0]);
			if (null != edges) {
				inEdges.addAll(edges);
			}
		} else {
			for (String label : labels) {
				Set<DynamicEdge> edges = this.inEdges.get(label);
				if (null != edges) {
					inEdges.addAll(edges);
				}
			}
		}
		return inEdges;
	}

	protected Iterable<DynamicEdge> getOutEdges(String... labels) {
		List<DynamicEdge> outEdges = new ArrayList<DynamicEdge>();

		if (labels.length == 0) {
			for (Collection<DynamicEdge> edges : this.outEdges.values()) {
				outEdges.addAll(edges);
			}
		} else if (labels.length == 1) {
			Set<DynamicEdge> edges = this.outEdges.get(labels[0]);

			if (null != edges) {
				outEdges.addAll(edges);
			}
		} else {
			for (String label : labels) {
				Set<DynamicEdge> edges = this.outEdges.get(label);
				if (null != edges) {
					outEdges.addAll(edges);
				}
			}
		}

		return outEdges;
	}

	public Iterable<DynamicEdge> getEdges(Direction direction, String... labels) throws IllegalArgumentException {
		if (direction.equals(Direction.IN)) {
			return getInEdges(labels);
		} else if (direction.equals(Direction.OUT)) {
			return getOutEdges(labels);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public DynamicEdge addEdge(String label, DynamicVertex inVertex, long timeStamp) throws IllegalArgumentException {
		return this.graph.addEdge(null, this, inVertex, label, timeStamp);
	}

	public void remove(long timestamp) {
		if (timestamp < 0) {
			throw DGExceptionFactory.timestampIsInvalid(timestamp);
		}

		this.graph.removeVertex(this, timestamp);
	}

	public Iterable<DynamicVertex> getVertices(Direction direction, String... labels) {
		Set<DynamicVertex> vertices = new HashSet<DynamicVertex>();
		for (Vertex vertex : this.vertex.getVertices(direction, labels)) {
			vertices.add(this.graph.getVertex(vertex.getId()));
		}

		return vertices;
	}
}
