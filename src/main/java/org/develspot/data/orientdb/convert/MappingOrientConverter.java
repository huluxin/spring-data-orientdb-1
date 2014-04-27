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
package org.develspot.data.orientdb.convert;

import org.develspot.data.orientdb.common.MappingInstanceHolder;
import org.develspot.data.orientdb.mapping.OrientPersistentEntity;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.EntityInstantiator;
import org.springframework.data.convert.EntityInstantiators;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.AssociationHandler;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.mapping.model.PersistentEntityParameterValueProvider;
import org.springframework.data.mapping.model.SpELContext;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

import com.tinkerpop.blueprints.impls.orient.OrientElement;

public class MappingOrientConverter extends AbstractOrientConverter {

	public MappingOrientConverter(MappingContext<? extends OrientPersistentEntity<?>, OrientPersistentProperty> mappingContext) {
		super(new DefaultConversionService());
		Assert.notNull(mappingContext, "MappingContext should not be null!");
		this.mappingContext = mappingContext;
		this.spELContext = new SpELContext(new MapAccessor());
		this.fieldAccessOnly = true;
	}
	
	public MappingContext<? extends OrientPersistentEntity<?>, OrientPersistentProperty> getMappingContext() {
		return mappingContext;
	}

	
	public <R> R read(Class<R> type, OrientElement source) {
		if(log.isTraceEnabled()) {
			log.trace("reading type: " + type + " from orientElement: " + source.getId());
		}
		
		return readInternal(ClassTypeInformation.from(type), source, false);
		
	}
	
	
	@SuppressWarnings("unchecked")
	protected <S extends Object> S readInternal(TypeInformation<S> typeInformation, OrientElement dbObject, boolean lazy) {
		OrientPersistentEntity<S> persistentEntity = (OrientPersistentEntity<S>)mappingContext.getPersistentEntity(typeInformation);
		
		return read(persistentEntity, dbObject, lazy);
	}
	
	

	public void write(Object source, OrientElement sink) {
		// TODO Auto-generated method stub

	}

	
	
	@SuppressWarnings("unchecked")
	private <S extends Object> S read(final OrientPersistentEntity<S> entity, final OrientElement dbObject, boolean lazy) {
		OrientMappingInstance mappingInstance = MappingInstanceHolder.getMappingInstance();
		
		//check if entity is already loaded
		if(mappingInstance.instanceLoaded(dbObject.getId())) {
			return (S)mappingInstance.get(dbObject.getId());
		}
				
		EntityInstantiator instantiator = instantiators.getInstantiatorFor(entity);
		final DefaultOrientPropertyValueProvider objectResolver = new DefaultOrientPropertyValueProvider(dbObject, this, spELContext, lazy);
		
		PersistentEntityParameterValueProvider<OrientPersistentProperty> parameterProvider = new PersistentEntityParameterValueProvider<OrientPersistentProperty>(
				entity, objectResolver, null);
		
		S instance = instantiator.createInstance(entity, parameterProvider);
		final BeanWrapper<OrientPersistentEntity<S>, S> wrapper = BeanWrapper.create(instance, conversionService);
		
		
		final S result = wrapper.getBean();
		mappingInstance.addInstance(dbObject.getId(), result);
		
		
		// Set properties not already set in the constructor
		entity.doWithProperties(new PropertyHandler<OrientPersistentProperty>() {
			public void doWithPersistentProperty(OrientPersistentProperty prop) {
				if (!prop.isIdProperty() && 
						(dbObject.getProperty(prop.getField().getName()) == null || entity.isConstructorArgument(prop))) {
					return;
				}
				Object obj = objectResolver.getPropertyValue(prop);
				wrapper.setProperty(prop, obj, fieldAccessOnly);
			}
		});
		
		entity.doWithAssociations(new AssociationHandler<OrientPersistentProperty>() {
			public void doWithAssociation(Association<OrientPersistentProperty> association) {
				OrientPersistentProperty inverse = association.getInverse();
				Object resolved = objectResolver.getPropertyValue(inverse);
				wrapper.setProperty(inverse, resolved);
				
			}
		});
		return result;
	}

	
	
	protected EntityInstantiators instantiators = new EntityInstantiators();
	
	private final MappingContext<? extends OrientPersistentEntity<?>, OrientPersistentProperty> mappingContext;
	private SpELContext spELContext;
	private boolean fieldAccessOnly;
	
	
	private static final Logger log = LoggerFactory.getLogger(MappingOrientConverter.class);
}
