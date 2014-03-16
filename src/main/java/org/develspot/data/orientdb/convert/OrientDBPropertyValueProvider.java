package org.develspot.data.orientdb.convert;

import java.util.ArrayList;
import java.util.Collection;

import org.develspot.data.orientdb.common.OrientConverterException;
import org.develspot.data.orientdb.common.OrientDataWrapper;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.data.mapping.model.DefaultSpELExpressionEvaluator;
import org.springframework.data.mapping.model.PropertyValueProvider;
import org.springframework.data.mapping.model.SpELContext;
import org.springframework.data.mapping.model.SpELExpressionEvaluator;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class OrientDBPropertyValueProvider implements PropertyValueProvider<OrientPersistentProperty> {

	public OrientDBPropertyValueProvider(OrientDataWrapper<OrientVertex> source, SpELContext factory, MappingOrientConverter converter, ReadSession readSession) {
		this(source, new DefaultSpELExpressionEvaluator(source, factory), converter, readSession);
	}

	public OrientDBPropertyValueProvider(OrientDataWrapper<OrientVertex> source, DefaultSpELExpressionEvaluator evaluator, MappingOrientConverter converter,
			ReadSession readSession) {

		Assert.notNull(source);
		Assert.notNull(evaluator);
		Assert.notNull(converter);
		Assert.notNull(readSession);

		this.source = source;
		this.evaluator = evaluator;
		this.converter = converter;
		this.readSession = readSession;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.convert.PropertyValueProvider#getPropertyValue(org.springframework.data.mapping.PersistentProperty)
	 */
	public <T> T getPropertyValue(OrientPersistentProperty property) {

		String expression = property.getSpelExpression();
		TypeInformation<?> typeInformation = property.getTypeInformation();
		
		if(property.isAssociation()) {
			if(source.isEmpty()) {
				return null;
			}
			
			if(property.isCollectionLike()) {
				//create the collection
				Collection<Object> collection = createCollection(typeInformation);
				TypeInformation<?> componentType = typeInformation.getComponentType();
				if(componentType == null) {
					log.error("Cannot get component type of collection for property: " + property);
					throw new OrientConverterException("cannot get componentType for property: " + property);
				}
				
				for(OrientVertex  ov : source.getElements()) {
					collection.add(converter.read(ClassTypeInformation.from(componentType.getType()), ov, readSession));
				}
				return (T) collection;
			}
			else {
				return (T) converter.read(ClassTypeInformation.from(typeInformation.getType()), source.getOrientElement(), readSession);					
			}
		}
		else {
			
			Object value = expression != null ? evaluator.evaluate(expression) : source.getOrientElement().getProperty(property.getField().getName());
			if (value == null) {
				return null;
			}

			return readValue(value, typeInformation);		
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	private <T> T readValue(Object value, TypeInformation<?> type) {
		Class<?> target = type.getType();
		return target.isAssignableFrom(value.getClass()) ? (T)value : (T)converter.conversionService.convert(value, target);	
	}
	
	
	private Collection<Object> createCollection(TypeInformation<?> typeInformation) {
		Class<?> collectionType = typeInformation.getType();
		
		return (collectionType.isArray()) ? new ArrayList<Object>() : CollectionFactory.createCollection(collectionType, source.size());
	}
	
	
	private static final Logger log = LoggerFactory.getLogger(OrientDBPropertyValueProvider.class);
	
	private MappingOrientConverter converter;
	private final SpELExpressionEvaluator evaluator;
	private final OrientDataWrapper<OrientVertex> source;
	private ReadSession readSession;
}
