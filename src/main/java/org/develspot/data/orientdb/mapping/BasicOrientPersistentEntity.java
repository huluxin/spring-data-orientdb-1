package org.develspot.data.orientdb.mapping;

import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;

public class BasicOrientPersistentEntity<T> extends BasicPersistentEntity<T, OrientPersistentProperty> implements OrientPersistentEntity<T> {

	public BasicOrientPersistentEntity(TypeInformation<T> information) {
		super(information);
		// TODO Auto-generated constructor stub
	}

}
