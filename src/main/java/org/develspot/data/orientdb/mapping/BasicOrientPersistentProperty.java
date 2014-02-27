/*******************************************************************************
 * Copyright 2013-2014 the original author or authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
