package org.develspot.data.orientdb.mapping;

import org.springframework.data.mapping.PersistentProperty;

public interface OrientPersistentProperty extends PersistentProperty<OrientPersistentProperty>{

	Connected getConnected();
}
