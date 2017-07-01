/**
 * 
 */
package net.CVNIS.DynamicGraphDB;

/**
 * @author iyuya
 *
 *	所有具有唯一标识符的实体都应该实现这个接口
 */
public interface Identifiable {
	Object getId();
}
