/**
 *
 */
package net.CVNIS.DynamicGraphDB.EvolvingGraph;

import java.util.Set;

import net.CVNIS.DynamicGraphDB.Identifiable;
import net.CVNIS.DynamicGraphDB.Label;
import net.CVNIS.DynamicGraphDB.TimeProperty;

/**
 * @author yuyang (yuyang_733@163.com)
 * 	
 *	属性图模型的基本元素
 */ 
public abstract interface DynamicElement extends Label,Identifiable, TimeProperty{
	
	/**
	 * 根据键获取该节点的属性
	 * @param key
	 * @return 
	 */
	public <T> T getProperty(String key);
	
	/**
	 * 获取属性列表的所有键
	 * @return
	 */
	public Set<String> getPropertyKeys();
	
	/**
	 * 设置属性 
	 * @param key
	 * @param value
	 * @param timeStamp
	 */
	public void setProperty(String key, Object value, long timeStamp);
	
	/**
	 * 获取指定时间周期上的活动序列，若[startTimestamp,endTimestamp]超出了的元素的生存周期，则返回其的生存周期上的所有活动序列
	 * @param startTimestamp
	 * @param endTimestamp
	 * @return
	 */
	
	public Iterable<UpdateLog> getActivities(long startTimestamp, long endTimestamp);
	
	/**
	 * 移除属性
	 * @param key
	 * @param timeStamp
	 * @return 
	 */
	public <T> T removeProperty(String key, long timeStamp);
	
	/**
	 * @deprecated use removeVertex or removeEdge in DynamicGraph instead
	 * @param timeStamp
	 */
	public void remove(long timeStamp);				//不应该使用这个方法，而应该使用Graph的remove方法
}