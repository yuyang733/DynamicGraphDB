/**
 * 
 */
package net.CVNIS.DynamicGraphDB.Persistence;

/**
 * @author yuyang (yuyang_733@163.com)
 *
 */
public abstract class StorageException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3796126799397481283L;

	public StorageException(String msg){
		super(msg);
	}
	
	public StorageException(String msg, Throwable cause){
		super(msg,cause);
	}
	
	public StorageException(Throwable cause){
		this("Exception in storage backend", cause);
	}
}
