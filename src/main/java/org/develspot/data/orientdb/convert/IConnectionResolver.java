package org.develspot.data.orientdb.convert;

import org.develspot.data.orientdb.mapping.OrientPersistentProperty;

public interface IConnectionResolver {

	Object resolveConnected(OrientPersistentProperty property, IConnectionResolverCallback callback);
	
}
