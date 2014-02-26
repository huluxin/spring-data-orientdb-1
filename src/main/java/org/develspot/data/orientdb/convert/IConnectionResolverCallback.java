package org.develspot.data.orientdb.convert;

import org.develspot.data.orientdb.mapping.OrientPersistentProperty;

public interface IConnectionResolverCallback {

	Object resolve(OrientPersistentProperty property);
}
