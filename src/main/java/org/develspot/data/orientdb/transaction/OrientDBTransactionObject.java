package org.develspot.data.orientdb.transaction;

import org.springframework.transaction.support.SmartTransactionObject;

public class OrientDBTransactionObject implements SmartTransactionObject {

	
	public boolean isRollbackOnly() {
		return connectionHolder.isRollbackOnly();
	}

	public void flush() {
		// TODO Auto-generated method stub
		
	}

	public void setConnectionHolder(GraphDatabaseHolder connectionHolder) {
		this.connectionHolder = connectionHolder;
	}
	
	
	public GraphDatabaseHolder getConnectionHolder() {
		return connectionHolder;
	}
	
	public void setRollbackOnly() {
		connectionHolder.setRollbackOnly();
	}
	
	
	private GraphDatabaseHolder connectionHolder;	
	
}
