/**
 * 
 */
package net.CVNIS.DynamicGraphDB;

import java.lang.reflect.Field;

/**
 * @author yuyang (yuyang_733@163.com)
 *	支持的特性
 */
public class Features extends com.tinkerpop.blueprints.Features {
	/**
	 * Does the graph allow for recording and querying the creation timestamp of this graph;
	 */
	public Boolean supportCreateTimestamp = null;
	
	/**
	 * Does the graph allow for recording and querying the last updated timestamp of this graph;
	 */
	public Boolean supportTimestamp 	  = null;
	
	public Features copyFeatures(){
		try{
			final Features features = new Features();
			for(final Field field: this.getClass().getFields()){
				field.set(features, field.get(this));
			}
			
			return features;
		}catch(IllegalAccessException e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
}