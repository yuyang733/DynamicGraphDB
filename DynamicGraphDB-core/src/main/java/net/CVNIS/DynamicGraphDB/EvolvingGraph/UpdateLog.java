package net.CVNIS.DynamicGraphDB.EvolvingGraph;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import net.CVNIS.DynamicGraphDB.TimeProperty;

public class UpdateLog implements Serializable,Comparable<UpdateLog> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7951649379859116283L;
	
	protected DynamicElement object = null;
	protected long timestamp = 0L;
	protected EvolvingOperation operation = null;
	protected Map<String,Object> parameters = new HashMap<String,Object>();

	@Override
	public String toString() {
		return "UpdateLog [ElementId = " + object.getId() +" timestamp=" + timestamp + ", operation=" + operation + ", parameters=" + parameters + "]";
	}

	/**
	 * @param operation
	 * @param timeStamp
	 */
	public UpdateLog(DynamicElement object, long timestamp, EvolvingOperation operation,String key, Object value) {
		this.object = object;
		this.timestamp = timestamp;
		this.operation = operation;
		this.parameters.put(key, value);
	}
	
	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * @return the operation
	 */
	public EvolvingOperation getOperation() {
		return operation;
	}
	
	/**
	 * @param operation the operation to set
	 */
	public void setOperation(EvolvingOperation operation) {
		this.operation = operation;
	}

	/**
	 * @return the parameters
	 */
	public Map<String,Object> getParameters() {
		return this.parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(String key, Object value) {
		this.parameters.put(key, value);
	}
	
	public int compareTo(UpdateLog o) {
		
		//NOTE 这个地方不建议使用  return (int)(this.timestamp - o.timestamp);
		//因为强制类型转换有可能导致溢出错误
		
		if(this.timestamp > o.timestamp){
			return 1;
		}
		else if(this.timestamp == o.timestamp){
			return 0;
		}
		else{
			return -1;
		}
	}
	
	/**
	 * 日志消解
	 * @param updateLogs
	 * @return
	 */
	public static Iterable<UpdateLog> SimplifyUpdateLog(UpdateLog[] OriginArray){
		UpdateLog[] InducedArray = (UpdateLog[]) ArrayUtils.clone(OriginArray);
		
		int curr = InducedArray.length - 1;
		while(curr >= 0){
			for(int k = curr - 1; k>=0; ){
				UpdateLog tempOp = InducedArray[k];
				UpdateLog mergeLog = merge(tempOp,InducedArray[curr]);
				if(null != mergeLog){
					if(mergeLog.getOperation().compareTo(EvolvingOperation.null_operation) == 0){
						InducedArray = (UpdateLog[]) ArrayUtils.remove(InducedArray, curr);
						InducedArray = (UpdateLog[]) ArrayUtils.remove(InducedArray, k);
						curr -= 2;
						if( curr > k ){
							continue;
						}
					}
					else{
						InducedArray[curr] = mergeLog;
						InducedArray = (UpdateLog[]) ArrayUtils.remove(InducedArray, k);
						curr--;
						if(curr > k){
							continue;
						}
					}
				}
				k--;
			}
			curr--;
		}
		
		return Arrays.asList(InducedArray);
	}
	
	/**
	 * 消解规则
	 * @return	null表示不可消解或者合并，EvolvingOperation.null_operation表示两个操作消解，否则合并为一个UpdateLog
	 */
	public static UpdateLog merge(final UpdateLog log1, final UpdateLog log2){
		if(null == log1 && null == log2){
			return null;
		}
		if(null != log1 && null == log2){
			return log1;
		}
		if(null == log1 && null != log2){
			return log2;
		}
		if(log1.object.getClass() != log2.object.getClass() ||log1.object.getId() != log2.object.getId()){			//所有消解操作必须在同一对象上进行
			return null;
		}
		
		
		//以下是消解规则的判断
		switch(log1.getOperation()){
		case add_vertex:
			if(log2.getOperation().compareTo(EvolvingOperation.remove_vertex) == 0 &&
			   log2.getTimestamp() >= log1.getTimestamp()){
				UpdateLog log = new UpdateLog(log1.object,TimeProperty.INVALID_TIME,EvolvingOperation.null_operation,null,null);
				return log;
			}
			break;
		case mod_vertex_property:
			if(log2.getOperation().compareTo(EvolvingOperation.mod_vertex_property) == 0 &&
			   log2.getTimestamp() >= log1.getTimestamp()){
				return log2;
			}
			if(log2.getOperation().compareTo(EvolvingOperation.remove_vertex) == 0 &&
			   log2.getTimestamp() >= log1.getTimestamp()){
				return log2;
			}
			break;
		case add_edge:
			if(log2.getOperation().compareTo(EvolvingOperation.remove_edge) == 0 &&
			   log2.getTimestamp() >= log1.getTimestamp()){
				UpdateLog log = new UpdateLog(log1.object,TimeProperty.INVALID_TIME,EvolvingOperation.null_operation,null,null);
				return log;
			}
			break;
		case mod_edge_property:
			if(log2.getOperation().compareTo(EvolvingOperation.remove_edge) == 0 &&
			   log2.getTimestamp() >= log1.getTimestamp()){
				return log2;
			}
			if(log2.getOperation().compareTo(EvolvingOperation.mod_edge_property) == 0 &&
			   log2.getTimestamp() >= log1.getTimestamp()){
				return log2;
			}
		default:
			break;
		}
		
		return null;
	}
}
