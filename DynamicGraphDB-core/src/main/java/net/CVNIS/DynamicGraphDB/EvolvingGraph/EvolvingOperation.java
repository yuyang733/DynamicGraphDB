/**
 * 
 */
package net.CVNIS.DynamicGraphDB.EvolvingGraph;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
public enum EvolvingOperation implements Serializable {
	add_vertex, remove_vertex,mod_vertex_property,
	add_edge, remove_edge, mod_edge_property,
	remove_vertex_property,remove_edge_property,null_operation;
	
	private static Map<EvolvingOperation,Method> operationList = new HashMap<EvolvingOperation,Method>();
	
	public static void setMethod(EvolvingOperation operation, Method method){
		operationList.put(operation, method);
	}
	
	public static Method getMethod(EvolvingOperation operation){
		return operationList.get(operation);
	}
}