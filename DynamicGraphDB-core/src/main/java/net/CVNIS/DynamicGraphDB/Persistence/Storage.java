package net.CVNIS.DynamicGraphDB.Persistence;

import java.io.IOException;

import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicEdge;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicGraph;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicVertex;

/**
 * 
 * @author iyuya (yuyang_733@163.com)
 *
 */
public abstract class Storage {
	private String graphLabel = null;
	
	public Storage() {
	}
	
	public abstract void 	 		 openGraph(final String label) throws IOException;
	
	public abstract void 	 		 closeGraph() throws IOException;
	
	public abstract void 	 		 saveVertex(final DynamicVertex vertex) throws IOException;
	
	public abstract void 	 		 saveEdge(final   DynamicEdge	edge) throws IOException;
	
	
}
