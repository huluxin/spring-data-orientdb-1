package org.develspot.data;

import org.develspot.data.orientdb.IOrientDataSource;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

public class MockDataSource implements IOrientDataSource {

	
	public static ThreadLocal<OGraphDatabase> THREAD_LOCAL_INSTANCE = new ThreadLocal<OGraphDatabase>();
	
	
	public void recreateDatabase() {
		OGraphDatabase graphDatabase = new OGraphDatabase("local:testdb");
		if(graphDatabase.exists()) {
			graphDatabase.open("admin", "admin");
			graphDatabase.drop();
		}
		THREAD_LOCAL_INSTANCE.set(null);
		graphDatabase.create();
	}
	
	public void dropDatabase() {
		if(isConnectionOpen())
			closeCurrentConnection();
		
		OGraphDatabase graphDatabase = new OGraphDatabase("local:testdb");
		if(graphDatabase.exists()) {
			graphDatabase.open("admin", "admin");
			graphDatabase.drop();
			graphDatabase.close();
			THREAD_LOCAL_INSTANCE.set(null);
		}
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
	
	
	
	private OGraphDatabase newConnection() {
		OGraphDatabase graphDatabase = new OGraphDatabase("local:testdb");
		if(graphDatabase.isClosed()) {
			graphDatabase.open("admin", "admin");
		}
		THREAD_LOCAL_INSTANCE.set(graphDatabase);
		return graphDatabase;
	}
	
}
