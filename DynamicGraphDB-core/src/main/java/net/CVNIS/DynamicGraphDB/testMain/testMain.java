package net.CVNIS.DynamicGraphDB.testMain;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.util.wrappers.event.listener.VertexAddedEvent;

import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicEdge;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicGraph;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicVertex;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.UpdateLog;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.impl.DynamicGraphImpl;

public class testMain {

	public static void main(String[] args) throws InterruptedException {
		DynamicGraph graph = new DynamicGraphImpl("test",System.currentTimeMillis());
		
		DynamicVertex vertex1 = graph.addVertex("1",System.currentTimeMillis());
		DynamicVertex vertex2 = graph.addVertex("2", System.currentTimeMillis());
		DynamicEdge		edge1 = graph.addEdge(null, vertex1, vertex2, "classmates", System.currentTimeMillis());
		
		vertex2.setProperty("name", "于飏", System.currentTimeMillis());
		edge1.setProperty("relation", "classmates", System.currentTimeMillis());
		vertex2.setProperty("name", "张晓朦", System.currentTimeMillis());
		graph.removeEdge(edge1, System.currentTimeMillis());
		
		
		//测试日志消解
		List<UpdateLog> updateLogs = new LinkedList<UpdateLog>();
		int index = 0;
		for(UpdateLog updateLog: vertex2.getActivities(0, System.currentTimeMillis())){
			if(0 == index){
				updateLogs.add(updateLog);
			}
			index++;
		}
		index = 0;
		for(UpdateLog updateLog:edge1.getActivities(0, System.currentTimeMillis())){
			if(0 == index){
				updateLogs.add(updateLog);
			}
			index++;
		}
		index = 0;
		for(UpdateLog updateLog:vertex2.getActivities(0, System.currentTimeMillis())){
			if( 1== index){
				updateLogs.add(updateLog);
			}
			index++;
		}
		index = 0;
		for(UpdateLog updateLog:edge1.getActivities(0, System.currentTimeMillis())){
			if( 1 == index){
				updateLogs.add(updateLog);
			}
			index++;
		}
		
		index = 0;
		for(UpdateLog updateLog:vertex2.getActivities(0, System.currentTimeMillis())){
			if(2 == index){
				updateLogs.add(updateLog);
			}
			index++;
		}
		
		index = 0;
		for(UpdateLog updateLog:edge1.getActivities(0, System.currentTimeMillis())){
			if(2 == index){
				updateLogs.add(updateLog);
			}
			index++;
		}
		
		System.out.println("原始活动序列");
		for(UpdateLog updateLog:updateLogs){
			System.out.println(updateLog);
		}
		
		System.out.println("消解以后的活动序列");
		for(UpdateLog updateLog:UpdateLog.SimplifyUpdateLog(updateLogs.toArray(new UpdateLog[updateLogs.size()]))){
			System.out.println(updateLog);
		}
	}

}
