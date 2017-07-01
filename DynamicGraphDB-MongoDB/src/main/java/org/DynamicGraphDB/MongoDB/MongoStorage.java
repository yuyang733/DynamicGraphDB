/**
 * 
 */
package org.DynamicGraphDB.MongoDB;

import java.io.IOException;

import org.bson.BsonArray;
import org.bson.BsonString;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tinkerpop.blueprints.Direction;

import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicEdge;
import net.CVNIS.DynamicGraphDB.EvolvingGraph.DynamicVertex;
import net.CVNIS.DynamicGraphDB.Persistence.Storage;

/**
 * @author iyuya (yuyang_733@163.com)
 *
 */
public class MongoStorage extends Storage {

	private MongoClient client = null;
	private MongoDatabase db = null;
	
	private static MongoStorage instance = null;
	
	public static MongoStorage getInstance(){
		if(null == instance){
			synchronized(MongoStorage.class){
				if(null == instance){
					instance = new MongoStorage();
				}
			}
		}
		return instance;
	}

	public MongoStorage() {
		this.client = new MongoClient(MongoConf.getInstance().server,MongoConf.getInstance().port);
	}
	
	@Override
	public void openGraph(final String label) throws IOException {
		if(null == this.db){
			this.db = this.client.getDatabase(label);
			return;
		}
		
		if(this.db != null && this.db.getName() == label){
			return;
		}
		
		if(this.db != null && this.db.getName() != label){
			throw new IOException("A Dynamic Graph Database instance has been opened! Please close it and try again!");
		}
	}


	@Override
	public void saveVertex(DynamicVertex vertex) {
		DBCollection verticeCollection = (DBCollection) db.getCollection(MongoConf.getInstance().verticeCollectionName);
		BasicDBObject vertexDoc = new BasicDBObject("_id", vertex.getId().toString() + String.valueOf(System.currentTimeMillis())); //XXX 后续应该加入机器号
		vertexDoc.append("label", vertex.getLabel());
		vertexDoc.append("id", vertex.getId());
		vertexDoc.append("createTimestamp", vertex.getCreateTimestamp());
		vertexDoc.append("timestamp", vertex.getTimestamp());
		BsonArray inEdges = new BsonArray();
		for(DynamicEdge inEdge: vertex.getEdges(Direction.IN)){
			inEdges.add(new BsonString(inEdge.getId().toString()));
		}
		vertexDoc.append("inEdges", inEdges);
		
		BsonArray outEdges = new BsonArray();
		for(DynamicEdge outEdge: vertex.getEdges(Direction.OUT)){
			outEdges.add(new BsonString(outEdge.getId().toString()));
		}
		vertexDoc.append("outEdges", outEdges);
		
		vertexDoc.append("vertex-log", ""); 	//TODO 对应的活动日志
		
		for(String key: vertex.getPropertyKeys()){
			vertexDoc.append(key, vertex.getProperty(key));
		}
		verticeCollection.insert(vertexDoc);
	}

	@Override
	public void saveEdge(DynamicEdge edge) {
		DBCollection edgeCollection = (DBCollection) db.getCollection(MongoConf.getInstance().edgeCollectionName);
		BasicDBObject edgeDoc = new BasicDBObject("_id",edge.getId().toString() + String.valueOf(System.currentTimeMillis()));
		
		edgeDoc.append("id", edge.getId());
		edgeDoc.append("label", edge.getLabel());
		edgeDoc.append("createTimestamp", edge.getCreateTimestamp());
		edgeDoc.append("timestamp", edge.getTimestamp());
		edgeDoc.append("inVertex", edge.getVertex(Direction.IN).getId());
		edgeDoc.append("outVertex", edge.getVertex(Direction.OUT).getId());
		edgeDoc.append("edge-log", "");  		//TODO 对应的活动日志
		
		for(String key: edge.getPropertyKeys()){
			edgeDoc.append(key, edge.getProperty(key));
		}
		
		edgeCollection.insert(edgeDoc);
	}

	@Override
	public void closeGraph() throws IOException {
		// TODO Auto-generated method stub
	}

}
