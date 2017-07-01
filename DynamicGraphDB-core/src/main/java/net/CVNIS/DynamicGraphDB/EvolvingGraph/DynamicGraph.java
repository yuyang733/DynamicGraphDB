package net.CVNIS.DynamicGraphDB.EvolvingGraph;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyGraph;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyVertex;

import net.CVNIS.DynamicGraphDB.TimeProperty;

/**
 * 		图模型的类
 * @author iyuya
 *		每个更新操作（包括属性更新和结构更新，
 *		都需要将最新的更新时间戳写到Graph的时间戳中，
 *		这样才能调用TimeProperty的接口获取Graph和Snapshot的时间
 *		
 */
public interface DynamicGraph extends TimeProperty{
	          
	/**
	 * 在图中添加一个顶点
	 * @param vertex
	 * @param timestamp
	 * @return	
	 */
	public DynamicVertex addVertex(Object id, long timeStamp);
	
	/**
	 * 移除一个顶点
	 * @param vertex
	 * @param timestamp
	 */
	public void removeVertex(DynamicVertex vertex,	long timeStamp);
	
	/**
	 * 移除一个顶点
	 * @param edge
	 * @param timeStamp
	 */
	public void removeEdge(DynamicEdge edge, long timeStamp);
	
	/**
	 * 获取的图中的某个顶点
	 * @param id
	 * @return
	 */
	public DynamicVertex getVertex(Object id);
	
	/**
	 * 返回当前图中所有的顶点
	 * @return
	 */
	public Iterable<DynamicVertex> getVertices();
	
	/**
	 * 返回具有指定键值对的顶点集合
	 * @param key
	 * @param value
	 * @return
	 */
	public Iterable<DynamicVertex> getVertices(String key, Object value);
	
	/**
	 * 
	 * @param startTimeStamp
	 * @param endTimeStamp
	 * @re`turn
	 */
	public Iterable<ReadOnlyVertex> getSnapshotVertices(long startTimeStamp, long endTimeStamp);
	
	/**
	 * 在图中添加一条连接两个顶点的边
	 * @param outVertex
	 * @param inVertex
	 * @param label
	 * @param timestamp
	 * @return
	 */
	public DynamicEdge addEdge(Object id, DynamicVertex outVertex, DynamicVertex inVertex, 
								String 			label,		long 	  timeStamp);	
	
	/**                                             
	 * 获取一条指定id的边
	 * @param idX
	 * @return
	 */
	public DynamicEdge getEdge(Object id);
	
	/**
	 * 
	 * @return
	 */
	public Iterable<DynamicEdge> getEdges();
	
	
	public Iterable<UpdateLog> getActivities(long startTimestamp, long endTimestamp);
	
	/**
	 * 获取顶点的演化活动序列
	 * @param startTimestamp
	 * @param endTimestamp
	 * @return
	 */
	public Iterable<UpdateLog> getVertexActivities(Object id, long startTimestamp, long endTimestamp);
	
	/**
	 * 获取边的演化活动序列
	 * @param id
	 * @param startTimestamp
	 * @param endTimestamp
	 * @return
	 */
	public Iterable<UpdateLog> getEdgeActivities(Object id, long startTimestamp, long endTimestamp);
	
	/**
	 * 获取指定时间点的图快照 (READONLY)
	 * @param timeStamp
	 * @return
	 */
	public ReadOnlyGraph<Graph> getSnapshotGraph(long timeStamp);
	
	public void	 clear();
	
	public String getLabel();
	
	/**
	 * 关闭一个图
	 */
	public void shutdown();
	
}
