package org.develspot.data.orientdb;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

public interface IOrientDataSource {

	public boolean isConnectionOpen();
	
	public OGraphDatabase getConnection();
	
	public void closeCurrentConnection();
}
