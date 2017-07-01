/**
 * 
 */
package net.CVNIS.DynamicGraphDB.Conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;


/**
 * @author iyuya (yuyang_733@163.com)
 *
 */
public class CommonConf {
	private static Logger logger = (Logger) LogManager.getLogger(CommonConf.class.getName());
	
	private static CommonConf instance= null;
	
	public static CommonConf getInstance(){
		if(null == instance){
			synchronized(CommonConf.class){
				if(null == instance)
					instance = new CommonConf();
			}
		}
		return instance;
	}
	
	private CommonConf(){
		if(System.getProperty("DynamicGraphDB.home") == null){
			logger.warn("DynamicGraphDB.home is not set. Using /mnt/DynamicGraphDB_default_home as the default value");
		}
		
		this.DYNAMICGRAPHDB_HOME = System.getProperty("DynamicGraphDB.home","/mnt/DynamicGraphDB_Default");
	}
	
	public final String DYNAMICGRAPHDB_HOME;
	
}
