/**
 * 
 */
package org.DynamicGraphDB.MongoDB;

import java.io.IOException;

import net.CVNIS.DynamicGraphDB.CheckpointManager.CheckpointManager;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicElement;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicGraph;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicVertex;

/**
 * @author iyuya (yuyang_733@163.com)
 *
 */
public class MongoCheckpointManager extends CheckpointManager {
	
	MongoStorage storage = null;
	
	/**
	 * @param graph
	 */
	public MongoCheckpointManager(DynamicGraph graph) {
		super(graph);
		
	}
	
	@Override
	public void init() {
		this.storage = MongoStorage.getInstance();
		try {
			this.storage.openGraph(this.graph.getLabel());
			this.isInit = true;
		} catch (IOException e) {
			this.isInit = false;
			e.printStackTrace();
		}
	}

	@Override
	public void doCheckpoint(DynamicElement element) {
		if(element instanceof DynamicVertex){
			
		}
		else{
			
		}
	}

}
