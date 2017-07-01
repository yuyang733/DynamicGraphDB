/**
 * 
 */
package net.CVNIS.DynamicGraphDB.exception;

import com.tinkerpop.blueprints.util.ExceptionFactory;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
public class DGExceptionFactory extends ExceptionFactory {
	public static IllegalArgumentException timestampIsInvalid(long timestamp) {
		return new IllegalArgumentException("timestamp:" + timestamp + "is invalid!");
	}
	
	public static IllegalArgumentException timeRangeIsInvalid(long startTimestamp,long endTimestamp){
		return new IllegalArgumentException("Time range :[" + startTimestamp +"," + endTimestamp + "]" + "is invalid!");
	}
}
