package org.DynamicGraphDB.MongoDB;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.CVNIS.DynamicGraphDB.Conf.CommonConf;

public final class MongoConf {
	private final static Logger logger = (Logger) LogManager.getLogger(MongoConf.class);
	
	private final String CONF_FILE = CommonConf.getInstance().DYNAMICGRAPHDB_HOME + "conf/MongoConf.properties";
	
	public  String server = "127.0.0.1";
	public  int 	 port = 27017;
	public  String 	verticeCollectionName;
	public 	String 	verticeLogCollectionName;
	public  String  edgeCollectionName;
	public  String  edgeLogCollectionName;
	
	
	private static MongoConf instance = null;
	
	public static MongoConf getInstance(){
		if(null == instance){
			synchronized(MongoConf.class){
				if(null == instance){
					instance = new MongoConf();
				}
			}
		}
		
		return instance;
	}
	
	private MongoConf() {
		try {
			
			InputStream in = new BufferedInputStream(new FileInputStream(CONF_FILE));
			Properties conf = new Properties();
			conf.load(in);
			this.server = conf.getProperty("server", "127.0.0.1");
			this.port	= Integer.parseInt(conf.getProperty("port","27017"));
			this.verticeCollectionName = conf.getProperty("vertice.collection.name", "vertices");
			this.verticeLogCollectionName = conf.getProperty("vertices.log.collection.name", "vertice_log");
			this.edgeCollectionName    = conf.getProperty("edge.collection.name", "edge");
			this.edgeLogCollectionName = conf.getProperty("edge.log.collection.name", "edge_log");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.warn("MongoDB config file not found! Server is set as 127.0.0.1 and port is set as 27017", e);
		} catch (IOException e) {
			logger.warn("Config file load error! Server is set as 127.0.0.1 and port is set as 27017", e);
			e.printStackTrace();
		}
	}

}
