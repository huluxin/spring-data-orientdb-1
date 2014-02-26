package org.develspot.data.orientdb.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

public class OrientMappingContext extends AbstractMappingContext<BasicOrientPersistentEntity<?>, OrientPersistentProperty> {

	@Override
	protected <T> BasicOrientPersistentEntity<?> createPersistentEntity(TypeInformation<T> typeInformation) {
		BasicOrientPersistentEntity<T> entity = new BasicOrientPersistentEntity<T>(typeInformation);
		
		return entity;
	}

	@Override
	protected BasicOrientPersistentProperty createPersistentProperty(Field field, PropertyDescriptor descriptor,
						BasicOrientPersistentEntity<?> owner, SimpleTypeHolder simpleTypeHolder) {
		
		BasicOrientPersistentProperty property = new BasicOrientPersistentProperty(field, descriptor, owner, simpleTypeHolder);
		return property;
	}

}
