package org.develspot.data.orientdb.transaction;

import org.springframework.transaction.support.ResourceHolderSupport;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

public class GraphDatabaseHolder extends ResourceHolderSupport {

	public GraphDatabaseHolder(OGraphDatabase graphDatabase) {
		this.graphDatabase = graphDatabase;
	}
	
	public OGraphDatabase getConnection() {
		return graphDatabase;
	}
	
	public void setTransactionActive(boolean transactionActive) {
		this.transactionActive = transactionActive;
	}
	
	public boolean isTransactionActive() {
		return transactionActive;
	}
	
	public void clear() {
		super.clear();
		transactionActive = false;
	}
	
	private OGraphDatabase graphDatabase;
	private boolean transactionActive;
}
