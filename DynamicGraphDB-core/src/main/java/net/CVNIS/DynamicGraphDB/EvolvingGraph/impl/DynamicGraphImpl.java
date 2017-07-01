package net.CVNIS.DynamicGraphDB.EvolvingGraph.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.ExceptionFactory;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyGraph;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyVertex;

import net.CVNIS.DynamicGraphDB.Identifiable;
import net.CVNIS.DynamicGraphDB.Label;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicEdge;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicGraph;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicVertex;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.EvolvingOperation;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.UpdateLog;
import net.CVNIS.DynamicGraphDB.exception.DGExceptionFactory;

public class DynamicGraphImpl implements DynamicGraph,Label {
	
	private final String LABEL;					//演化图的标签
	
	protected static final Features FEATURES = new Features();
	
	TinkerGraph graph = null;
	
	/**
	 * vertices和edges的key是id，value是元素
	 */
	protected Map<String,DynamicVertex> vertices = new HashMap<String,DynamicVertex>();
	protected Map<String,DynamicEdge>   edges    = new HashMap<String,DynamicEdge>();
	
	protected final long createTimestamp;
	protected 		long timeStamp = INVALID_TIME;
	
	protected final String directory;			//Snapshot storage directory, XXX 如何指定Snapshot的存放位置
	protected final FileType fileType;			
	
	public enum FileType{						//At present, currently only supports the following two types of file format
		JAVA,
		GRAPHSON
	}
	
	public DynamicGraphImpl(String graphName,long createTimestamp){
		this(graphName,null,null,createTimestamp);
	}
	
	public DynamicGraphImpl(String graphName,String directory,FileType fileType,long createTimestamp){
		this(null,graphName,directory,fileType,createTimestamp);
	}
	
	public DynamicGraphImpl(TinkerGraph graph, String graphName, String directory, FileType fileType, long createTimestamp){
		if(null == graph){
			this.graph = new TinkerGraph();
		}
		else{
			this.graph = graph;
		}
		
		this.LABEL = graphName;
		this.directory = directory;
		this.fileType  = fileType;
		this.createTimestamp = createTimestamp;
	}

	public long getCreateTimestamp() {
		return this.createTimestamp;
	}

	public long getTimestamp() {
		return this.timeStamp;
	}

	public DynamicVertex addVertex(final Object id, final long timeStamp){
		return this.addVertex(id, Label.NO_LABEL, timeStamp);
	}
	
	public DynamicVertex addVertex(final Object id, String label, final long timeStamp) {
		DynamicVertex vertex = new DynamicVertexImpl(this, this.graph.addVertex(id), label, timeStamp);
		
		this.vertices.put(vertex.getId().toString(), vertex);
		
		return vertex;
	}

	public void removeVertex(DynamicVertex vertex, long timeStamp) {
		if(! this.vertices.containsKey(vertex.getId().toString())){
			throw ExceptionFactory.vertexWithIdDoesNotExist(vertex.getId().toString());
		}
				
		for(DynamicEdge edge : vertex.getEdges(Direction.BOTH)){
			this.removeEdge(edge, timeStamp);
		}
		
		this.vertices.remove(vertex.getId().toString());
		this.graph.removeVertex(this.graph.getVertex(vertex.getId()));
		((DynamicVertexImpl)vertex).recordUpdateLog(vertex,timeStamp, EvolvingOperation.remove_vertex, vertex.getLabel(), vertex.getId());
	}
	
	public Iterable<DynamicVertex> getVertices() {
		return new ArrayList<DynamicVertex>(this.vertices.values());
	}

	public Iterable<DynamicVertex> getVertices(String key, Object value) {
		List<DynamicVertex> vertices = new ArrayList<DynamicVertex>();
		for(Vertex snapshotVertex : this.graph.getVertices(key,value)){
			DynamicVertex vertex = this.vertices.get(snapshotVertex.getId());
			vertices.add(vertex);
		}
		
		return vertices;
	}

	public Iterable<ReadOnlyVertex> getSnapshotVertices(long startTimeStamp, long endTimeStamp) {
		
		return null;
	}

	public DynamicEdge addEdge(Object id, DynamicVertex outVertex, DynamicVertex inVertex, String label, long timeStamp) {
		DynamicEdge edge = new DynamicEdgeImpl(this,
											   this.graph.addEdge(id, this.graph.getVertex(outVertex.getId()), this.graph.getVertex(inVertex.getId()), label), 
											   label, outVertex, inVertex, timeStamp);
		
		this.edges.put(edge.getId().toString(), edge);
		
		if( ((DynamicVertexImpl)outVertex).outEdges.get(edge.getLabel()) == null){
			Set<DynamicEdge> edges = new HashSet<DynamicEdge>();
			edges.add(edge);
			((DynamicVertexImpl)outVertex).outEdges.put(edge.getLabel(), edges);
		}
		else{
			((DynamicVertexImpl)outVertex).outEdges.get(edge.getLabel()).add(edge);
		}
		
		if( ((DynamicVertexImpl)inVertex).inEdges.get(edge.getLabel()) == null){
			Set<DynamicEdge> edges = new HashSet<DynamicEdge>();
			edges.add(edge);
			((DynamicVertexImpl)inVertex).inEdges.put(edge.getLabel(), edges);
		}
		else{
			((DynamicVertexImpl)inVertex).outEdges.get(edge.getLabel()).add(edge);
		}
		
		return edge;
	}
	
	public void removeEdge(DynamicEdge edge, long timeStamp) {
		
		DynamicVertexImpl outVertex = (DynamicVertexImpl) edge.getVertex(Direction.OUT);
		DynamicVertexImpl  inVertex = (DynamicVertexImpl) edge.getVertex(Direction.IN);
		
		if(null != outVertex && null != outVertex.outEdges){
			Set<DynamicEdge> edges = outVertex.outEdges.get(edge.getLabel());
			if(null != edges){
				edges.remove(edge);
				if (edges.isEmpty())
					outVertex.outEdges.remove(edge.getLabel());
			}
		}
		
		if(null != inVertex && null != inVertex.inEdges){
			Set<DynamicEdge> edges = inVertex.inEdges.get(edge.getLabel());
			if(null != edges){
				edges.remove(edge);
				if (edges.isEmpty())
					inVertex.inEdges.remove(edge.getLabel());
			}
		}
		
		this.graph.removeEdge( ((DynamicEdgeImpl)edge).edge );				//删除静态图中的顶点
		this.edges.remove(edge.getId());
		((DynamicEdgeImpl)edge).recordUpdateLog(edge,timeStamp, EvolvingOperation.remove_edge, edge.getLabel(), edge.getId());
	}
	

	public DynamicEdge getEdge(Object id) {
		if(null == id){
			throw DGExceptionFactory.edgeIdCanNotBeNull();
		}
		
		String idString = id.toString();
		return this.edges.get(idString);
	}
	
	public ReadOnlyGraph<Graph> getSnapshotGraph(long timeStamp) {
		///TODO 获取指定时间的图快照
		///XXX 快照是否应该放在动态图类型里面进行获取
		return null;
	}

	public void shutdown() {
		//关闭一个
	}

	public Iterable<DynamicEdge> getEdges() {
		return new ArrayList<DynamicEdge>(this.edges.values());
	}
	
	public void clear() {
		this.edges.clear();
		this.vertices.clear();
		this.graph.clear();
	}

	public Iterable<UpdateLog> getVertexActivities(Object id, long startTimestamp, long endTimestamp) {
		//TODO Auto-generated method stub
		return null;
	}

	public Iterable<UpdateLog> getEdgeActivities(Object id, long startTimestamp, long endTimestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<UpdateLog> getActivities(long startTimestamp, long endTimestamp) {
		if(startTimestamp > endTimestamp || startTimestamp < 0 || endTimestamp < 0 ){
			throw DGExceptionFactory.timeRangeIsInvalid(startTimestamp, endTimestamp);
		}
		
		List<UpdateLog> graphActs 	= new LinkedList<UpdateLog>();
		for(DynamicVertex vertex : this.vertices.values()){
			List<UpdateLog> vertexAct = (List<UpdateLog>) vertex.getActivities(startTimestamp, endTimestamp);
			graphActs.addAll(vertexAct);
		}
		
		for(DynamicEdge edge: this.edges.values()){
			List<UpdateLog> edgeAct = (List<UpdateLog>) edge.getActivities(startTimestamp, endTimestamp);
			graphActs.addAll(edgeAct);
		}
		
		//所有的活动，都必须按照时间排序
		Collections.sort(graphActs, new Comparator<UpdateLog>(){

			public int compare(UpdateLog o1, UpdateLog o2) {
				if(o1.getTimestamp() > o2.getTimestamp()){
					return 1;
				}
				else if(o1.getTimestamp() == o2.getTimestamp()){
					return 0;
				}
				else{
					return -1;
				}
			}
			
		});
		
		return graphActs;
	}

	public String getLabel() {
		return this.LABEL;
	}

	public DynamicVertex getVertex(Object id) {
		if(null == id){
			throw DGExceptionFactory.vertexIdCanNotBeNull();
		}
		
		String idString = id.toString();
		return this.vertices.get(idString);
	}
}
