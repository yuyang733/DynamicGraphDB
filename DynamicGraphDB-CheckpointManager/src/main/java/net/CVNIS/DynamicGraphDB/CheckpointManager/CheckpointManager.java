/**
 * 
 */
package net.CVNIS.DynamicGraphDB.CheckpointManager;

import java.util.EventListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.CVNIS.DynamicGraphDB.Conf.CheckpointConf;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicElement;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicGraph;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
public abstract class CheckpointManager implements EventListener {
	
	protected DynamicGraph graph;
	
	protected Map<Object,AtomicLong> updatedVertex = new ConcurrentHashMap<Object,AtomicLong>();
	protected Map<Object,AtomicLong>   updatedEdge   = new ConcurrentHashMap<Object,AtomicLong>();
	
	
	protected   boolean      isInit = false;
	
	public CheckpointManager(DynamicGraph graph){
		this.graph = graph;
	}
	
	public void addUpdatedVertex(Object id, boolean isAdd){
		if(updatedVertex.get(id) == null){
			updatedVertex.put(id, new AtomicLong(1));
		}
		else{
			AtomicLong count = updatedVertex.get(id);
			updatedVertex.put(id,new AtomicLong(count.incrementAndGet()));
		}
		
		if(updatedVertex.get(id).get() == CheckpointConf.getInstance().vertexThreshold){
			doCheckpoint(this.graph.getVertex(id));
			updatedVertex.remove(id);
		}
	}
	
	public void addUpdatedEdge(Object id, boolean isAdd){
		if(updatedEdge.get(id) == null){
			updatedVertex.put(id, new AtomicLong(1));
		}
		else{
			AtomicLong count = updatedEdge.get(id);
			updatedEdge.put(id, new AtomicLong(count.incrementAndGet()));
		}
		
		if(updatedEdge.get(id).get() == CheckpointConf.getInstance().edgeThreshold){
			doCheckpoint(this.graph.getEdge(id));
			updatedEdge.remove(id);
		}
	}
	
	/**
	 * 初始化底层存储的连接等
	 */
	public abstract void init();
	
	
	public boolean isInit(){
		return isInit;
	}
	
	public abstract void doCheckpoint(DynamicElement element);
	
}