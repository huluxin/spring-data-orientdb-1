package org.develspot.data.orientdb.convert;

import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.springframework.data.mapping.model.DefaultSpELExpressionEvaluator;
import org.springframework.data.mapping.model.PropertyValueProvider;
import org.springframework.data.mapping.model.SpELContext;
import org.springframework.data.util.TypeInformation;

import com.tinkerpop.blueprints.impls.orient.OrientElement;

public abstract class OrientPropertyValueProvider implements PropertyValueProvider<OrientPersistentProperty>{

	public OrientPropertyValueProvider(OrientElement orientElement, MappingOrientConverter converter, SpELContext spELContext) {
		this.orientElement = orientElement;
		this.converter = converter;
		this.spELContext = spELContext;
		this.evaluator = new DefaultSpELExpressionEvaluator(orientElement, spELContext);
	}
	
	
	@SuppressWarnings("unchecked")
	protected <T> T readValue(Object value, TypeInformation<?> type) {
		Class<?> target = type.getType();
		return target.isAssignableFrom(value.getClass()) ? (T)value : (T)converter.getConversionService().convert(value, target);	
	}
	
	protected SpELContext spELContext;
	protected MappingOrientConverter converter;
	protected OrientElement orientElement;
	protected DefaultSpELExpressionEvaluator evaluator;
}
