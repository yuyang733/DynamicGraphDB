/**
 * 
 */
package net.CVNIS.DynamicGraphDB;

/**
 * @author yuyang (yuyang_733@163.com)
 *	
 *	所有具有标签属性的实体都应该实现这个接口
 *	这个接口不提供set方法的原因是，一个实体的标签从创建的时候应该就确定了
 *	如果标签是变化的，则应该考虑将这个标签设置成为实体的属性
 */
public interface Label {
	public static final String NO_LABEL = "NIL"; 
	
	String getLabel();
}
