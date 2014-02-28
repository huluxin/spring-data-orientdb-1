package org.develspot.data.orientdb.convert;

import org.develspot.data.orientdb.common.OrientDataWrapper;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.springframework.data.mapping.model.DefaultSpELExpressionEvaluator;
import org.springframework.data.mapping.model.PropertyValueProvider;
import org.springframework.data.mapping.model.SpELContext;
import org.springframework.data.mapping.model.SpELExpressionEvaluator;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class OrientDBPropertyValueProvider implements PropertyValueProvider<OrientPersistentProperty> {

	public OrientDBPropertyValueProvider(OrientDataWrapper<OrientVertex> source, SpELContext factory, MappingOrientConverter converter) {
		this(source, new DefaultSpELExpressionEvaluator(source, factory), converter);
	}

	public OrientDBPropertyValueProvider(OrientDataWrapper<OrientVertex> source, DefaultSpELExpressionEvaluator evaluator, MappingOrientConverter converter) {

		Assert.notNull(source);
		Assert.notNull(evaluator);
		Assert.notNull(converter);

		this.source = source;
		this.evaluator = evaluator;
		this.converter = converter;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.convert.PropertyValueProvider#getPropertyValue(org.springframework.data.mapping.PersistentProperty)
	 */
	public <T> T getPropertyValue(OrientPersistentProperty property) {

		String expression = property.getSpelExpression();
		if(source.isSingleElement()) {
			OrientVertex orientElement = source.getOrientElement();
			TypeInformation<?> typeInformation = property.getTypeInformation();
			
			if(property.isAssociation()) {
				return (T) converter.read(typeInformation.getType(), orientElement);
			}
			else {
				Object value = expression != null ? evaluator.evaluate(expression) : orientElement.getProperty(property.getField().getName());
				if (value == null) {
					return null;
				}
	
				return readValue(value, typeInformation);				
			}
			
		}
		else {
			throw new UnsupportedOperationException("not supported at the moment");
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	private <T> T readValue(Object value, TypeInformation<?> type) {
		Class<?> target = type.getType();
		return target.isAssignableFrom(value.getClass()) ? (T)value : (T)converter.conversionService.convert(value, target);	
	}
	
	
	private MappingOrientConverter converter;
	private final SpELExpressionEvaluator evaluator;
	private final OrientDataWrapper<OrientVertex> source;
}
