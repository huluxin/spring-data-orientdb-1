package org.develspot.data.orientdb.convert;

import org.develspot.data.orientdb.IOrientDataSource;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;

public class DefaultConnectionResolver implements IConnectionResolver {

	public Object resolveConnected(OrientPersistentProperty property,
			IConnectionResolverCallback callback) {
		
		return callback.resolve(property);
	}

	public IOrientDataSource getDatasource() {
		// TODO Auto-generated method stub
		return null;
	}

}
