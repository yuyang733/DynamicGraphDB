package net.CVNIS.DynamicGraphDB.Conf;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class CheckpointConf {
	private final static Logger logger = (Logger) LogManager.getLogger(CheckpointConf.class);
	
	private static CheckpointConf instance = null;
	private final String CONF_FILE = CommonConf.getInstance().DYNAMICGRAPHDB_HOME + "conf/settings.properties";
	public  int graphThreshold = 1000;
	public  int vertexThreshold= 100;
	public  int edgeThreshold  = 100;
	
	public static CheckpointConf getInstance(){
		if(null == instance){
			synchronized(CheckpointConf.class){
				if(null == instance){
					instance = new CheckpointConf();
				}
			}
		}
		
		return instance;
	}

	public CheckpointConf() {
			InputStream in;
			try {
				in = new BufferedInputStream(new FileInputStream(CONF_FILE));
				Properties conf = new Properties();
				conf.load(in);
				this.graphThreshold = Integer.parseInt(conf.getProperty("Checkpoint.operation.graph.threshold", "1000"));
				this.vertexThreshold = Integer.parseInt(conf.getProperty("Checkpoint.operation.vertex.threshold","100"));
				this.edgeThreshold = Integer.parseInt(conf.getProperty("Checkpoint.operation.edge.threshold","100"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.warn("Settings file not found!",e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.warn("Config file load error!",e);
			}		
	}
}
