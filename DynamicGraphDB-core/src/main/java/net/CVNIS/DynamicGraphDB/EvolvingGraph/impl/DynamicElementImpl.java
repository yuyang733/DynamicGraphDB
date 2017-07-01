package net.CVNIS.DynamicGraphDB.EvolvingGraph.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.tinkerpop.blueprints.Element;

import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicElement;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicGraph;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.EvolvingOperation;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.UpdateLog;
import net.CVNIS.DynamicGraphDB.exception.DGExceptionFactory;

abstract class DynamicElementImpl implements DynamicElement {
	private Element element = null;
	private String label = NO_LABEL;
	private final long createTimestamp;
	private long timeStamp = INVALID_TIME;

	protected List<UpdateLog> updateLogs = new LinkedList<UpdateLog>();

	protected DynamicGraph graph;

	/**
	 * @param element 
	 * @param label
	 * @param createTimestamp
	 */
	DynamicElementImpl(DynamicGraph graph, Element element, String label, long createTimestamp) {
		this.graph = graph;
		this.element = element;
		this.label = label;
		this.createTimestamp = createTimestamp;
		this.updateLogs.clear();
	}

	public String getLabel() {
		return this.label;
	}

	public Object getId() {
		return this.element.getId();
	}

	public long getCreateTimestamp() {
		return this.createTimestamp;
	}

	public long getTimestamp() {
		return this.timeStamp;

	}

	public <T> T getProperty(String key) {
		return this.element.getProperty(key);
	}

	public Set<String> getPropertyKeys() {
		return this.element.getPropertyKeys();
	}

	public void setProperty(String key, Object value, long timeStamp) {
		this.element.setProperty(key, value);
		if(this instanceof DynamicVertexImpl){
			recordUpdateLog(this,timeStamp, EvolvingOperation.mod_vertex_property, key, value);
		}
		else{
			recordUpdateLog(this,timeStamp, EvolvingOperation.mod_edge_property, key, value);
		}
	}

	public <T> T removeProperty(String key, long timeStamp) {
		T value = this.element.removeProperty(key);
		
		if(this instanceof DynamicVertexImpl){
			recordUpdateLog(this,timeStamp, EvolvingOperation.remove_vertex_property, key, timeStamp);
		}
		else{
			recordUpdateLog(this,timeStamp, EvolvingOperation.remove_edge_property, key, timeStamp);
		}

		return value;
	}

	void recordUpdateLog(DynamicElement element,long timeStamp, EvolvingOperation operation, String key, Object value) {
		UpdateLog log = new UpdateLog(element, timeStamp, operation, key, value);
		this.updateLogs.add(log);
	}

	public Iterable<UpdateLog> getActivities(long startTimestamp, long endTimestamp) {
		if (startTimestamp > endTimestamp || startTimestamp < 0 || endTimestamp < 0 ) {
			throw DGExceptionFactory.timeRangeIsInvalid(startTimestamp, endTimestamp);
		}

		List<UpdateLog> activities = new LinkedList<UpdateLog>();

		for(UpdateLog act:this.updateLogs){
			if(startTimestamp <= act.getTimestamp() && endTimestamp >= act.getTimestamp()){
				activities.add(act);
			}
		}
		
		Collections.sort(activities,new Comparator<UpdateLog>(){

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
		
		return activities;
	}

}
