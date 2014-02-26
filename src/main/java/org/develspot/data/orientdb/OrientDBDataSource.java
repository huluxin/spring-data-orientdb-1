package org.develspot.data.orientdb;

import javax.annotation.PostConstruct;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.graph.OGraphDatabasePool;

public class OrientDBDataSource implements IOrientDataSource {

	public static final int DEFAULT_MIN_POOLSIZE = 1;
	public static final int DEFAULT_MAX_POOLSIZE = 20;
	
	public static ThreadLocal<OGraphDatabase> THREAD_LOCAL_INSTANCE = new ThreadLocal<OGraphDatabase>();
	
	public OrientDBDataSource(String databaseURL, String databaseUser, String databasePass) {
		this.databaseURL = databaseURL;
		this.databaseUser = databaseUser;
		this.databasePass = databasePass;
	}
	
	@PostConstruct
	public void initConnectionPool() {
		OGlobalConfiguration.CLIENT_CHANNEL_MIN_POOL.setValue(minPoolSize);
		OGlobalConfiguration.CLIENT_CHANNEL_MAX_POOL.setValue(maxPoolSize);
		
		OGlobalConfiguration.MVRBTREE_TIMEOUT.setValue(20000);
		OGlobalConfiguration.STORAGE_RECORD_LOCK_TIMEOUT.setValue(20000);
		OGlobalConfiguration.STORAGE_KEEP_OPEN.setValue(false);
			
		
		connectionPool = new OGraphDatabasePool(databaseURL, databaseUser, databasePass);
	}

	public boolean isConnectionOpen() {
		return THREAD_LOCAL_INSTANCE.get() != null;
	}
	
	
	
	public OGraphDatabase getConnection() {
		OGraphDatabase graphDatabase = THREAD_LOCAL_INSTANCE.get();
		if(graphDatabase == null)
			return newConnection();
		
		return graphDatabase;
	}
	
	public void closeCurrentConnection() {
		OGraphDatabase graphDatabase = THREAD_LOCAL_INSTANCE.get();
		if(graphDatabase != null)
			graphDatabase.close();
		
		THREAD_LOCAL_INSTANCE.set(null);			
	}
	
	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}
	
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	
	private OGraphDatabase newConnection() {
		OGraphDatabase graphDatabase = connectionPool.acquire();
		THREAD_LOCAL_INSTANCE.set(graphDatabase);
		return graphDatabase;
	}
	
	
	
	private OGraphDatabasePool connectionPool;
	private String databaseURL;
	private String databaseUser;
	private String databasePass;
	
	private int minPoolSize = DEFAULT_MIN_POOLSIZE;
	private int maxPoolSize = DEFAULT_MAX_POOLSIZE;
}
