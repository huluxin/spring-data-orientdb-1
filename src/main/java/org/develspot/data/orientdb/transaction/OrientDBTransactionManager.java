package org.develspot.data.orientdb.transaction;

import org.develspot.data.orientdb.IOrientDataSource;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

@SuppressWarnings("serial")
public class OrientDBTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager {

	public OrientDBTransactionManager(IOrientDataSource orientDataSource) {
		//TODO allow nested transactions
		setNestedTransactionAllowed(false);
		if(orientDataSource == null)
			throw new IllegalArgumentException("given dataSource should be non null");
		
		this.orientDataSource = orientDataSource;
	}
	
	
	public Object getResourceFactory() {
		return getDataSource();
	}


	@Override
	protected Object doGetTransaction() throws TransactionException {
		OrientDBTransactionObject txObject = new OrientDBTransactionObject();
		
		return txObject;
	}

	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition)
			throws TransactionException {
		
		OrientDBTransactionObject txObject = (OrientDBTransactionObject) transaction;
		
		try {
			
			if(txObject.getConnectionHolder() == null) {
				OGraphDatabase con = this.orientDataSource.getConnection();
				txObject.setConnectionHolder(new GraphDatabaseHolder(con));
			}
			
			txObject.getConnectionHolder().setTransactionActive(true);
			TransactionSynchronizationManager.bindResource(getDataSource(), txObject.getConnectionHolder());
			txObject.getConnectionHolder().getConnection().begin();
		}catch(Exception e) {
			release(txObject);
			throw new CannotCreateTransactionException(e.getMessage(),e);
		}
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status)
			throws TransactionException {
		
		OrientDBTransactionObject txObject = (OrientDBTransactionObject)status.getTransaction();
		OGraphDatabase connection = txObject.getConnectionHolder().getConnection();
		if(status.isDebug()) {
			logger.debug("Commiting orientdb transaction on connection [" + connection + "]");
		}
		try {
			connection.commit();
		}catch(Exception e) {
			throw new TransactionSystemException("Could not commit orientdb transaction:" + e.getMessage());
		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status)
			throws TransactionException {
		
		OrientDBTransactionObject txObject = (OrientDBTransactionObject)status.getTransaction();
		OGraphDatabase connection = txObject.getConnectionHolder().getConnection();
		if(status.isDebug()) {
			logger.debug("Rolling back transaction on connection [" + connection + "]");
		}
		try {
			connection.rollback();
		}catch(Exception e) {
			throw new TransactionSystemException("Could not rollback orientdb transaction: " + e.getMessage());
		}
	}
	
	
	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		OrientDBTransactionObject txObject = (OrientDBTransactionObject)transaction;
		
		TransactionSynchronizationManager.unbindResource(getDataSource());
		txObject.getConnectionHolder().clear();
		orientDataSource.closeCurrentConnection();
	}


	public IOrientDataSource getDataSource() {
		return orientDataSource;
	}
	
	
	private void release(OrientDBTransactionObject txObject) {
		GraphDatabaseHolder connectionHolder = txObject.getConnectionHolder();
		OGraphDatabase connection = connectionHolder.getConnection();
		try {
			if(connection.getTransaction().isActive()) {
				connection.rollback();
			}			
		}catch(Exception e) {
			logger.error("Could not terminate transaction: " + e.getMessage());
		}
		finally {
			txObject.setConnectionHolder(null);
			orientDataSource.closeCurrentConnection();			
		}
	}
	
	
	private IOrientDataSource orientDataSource;
}
