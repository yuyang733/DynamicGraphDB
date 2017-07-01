/**
 * 
 */
package net.CVNIS.DynamicGraphDB.CheckpointManager.Event;

import java.util.EventObject;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
public class CheckpointEvent extends EventObject {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -2659557980664811036L;
	
	private Object eventSource;
	
	/**
	 * @param source
	 */
	public CheckpointEvent(Object source) {
		super(source);
		this.eventSource = source;
	}
	
	public Object getEventSource(){
		return this.eventSource;
	}
	
}
