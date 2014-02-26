package org.develspot.data.orientdb.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.SimpleTypeHolder;

public class BasicOrientPersistentProperty extends AnnotationBasedPersistentProperty<OrientPersistentProperty> implements OrientPersistentProperty {

	
	public BasicOrientPersistentProperty(Field field, PropertyDescriptor propertyDescriptor, OrientPersistentEntity<?> owner,
			SimpleTypeHolder simpleTypeHolder) {
		super(field, propertyDescriptor,owner, simpleTypeHolder);
	}
	
	
	@Override
	protected Association<OrientPersistentProperty> createAssociation() {
		return new Association<OrientPersistentProperty>(this, null);
	}


	@Override
	public boolean isIdProperty() {
		if(super.isIdProperty()) {
			return true;
		}
		return SUPPORTED_ID_PROPERTY_NAMES.contains(field.getName());
	}

	
	@Override
	public boolean isAssociation() {
		return super.isAssociation() || field.isAnnotationPresent(Connected.class);
	}


	public Connected getConnected() {
		return getField().getAnnotation(Connected.class);
	}



	private static final Set<String> SUPPORTED_ID_PROPERTY_NAMES = new HashSet<String>();
	
	static {
		SUPPORTED_ID_PROPERTY_NAMES.add("id");
		SUPPORTED_ID_PROPERTY_NAMES.add("rid");
	}
}
