package org.develspot.data.orientdb.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.develspot.data.orientdb.common.LazyLoadingInterceptor;
import org.develspot.data.orientdb.common.MappingInstanceHolder;
import org.develspot.data.orientdb.common.OrientConverterException;
import org.develspot.data.orientdb.mapping.Connected;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.data.mapping.model.SpELContext;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class DefaultOrientPropertyValueProvider extends OrientPropertyValueProvider {

	public DefaultOrientPropertyValueProvider(OrientElement orientElement,	MappingOrientConverter converter, SpELContext spELContext,
								boolean enableLazyLoading) {
		super(orientElement, converter, spELContext);
		this.lazyLoadingProxy = enableLazyLoading;
	}

	
	@SuppressWarnings("unchecked")
	public <T> T getPropertyValue(OrientPersistentProperty property) {
		if(!property.isAssociation()) {
			return getSimplePropertyValue(property);
		}
		
		if(lazyLoadingProxy)
			return (T) createLazyLoadingProxy(property);
		
		
		Connected connected = property.getConnected();
		TypeInformation<?> typeInformation = property.getTypeInformation();
		
		Iterable<Vertex> vertices = ((OrientVertex)orientElement).getVertices(connected.direction(), connected.edgeType());
		
		
		if(property.isCollectionLike()) {
			//create the collection
			Collection<Object> collection = createCollection(typeInformation);
			TypeInformation<?> componentType = typeInformation.getComponentType();
			if(componentType == null) {
				log.error("Cannot get component type of collection for property: " + property);
				throw new OrientConverterException("cannot get componentType for property: " + property);
			}
			
			Iterator<Vertex> iterator = vertices.iterator();
			while(iterator.hasNext()) {
				OrientVertex ov = (OrientVertex) iterator.next();
				Object instance = MappingInstanceHolder.getMappingInstance().get(ov.getId());
				if(instance == null) {
					instance = converter.readInternal(ClassTypeInformation.from(componentType.getType()), ov, false);
					MappingInstanceHolder.getMappingInstance().addInstance(ov.getId(), instance);
				}
				
				collection.add(instance);
			}
			return (T) collection;
		}
		else {
			T result = null;
			Iterator<Vertex> iterator = vertices.iterator();
			while(iterator.hasNext()) {
				result = (T) converter.readInternal(ClassTypeInformation.from(typeInformation.getType()), (OrientVertex)iterator.next(),false);
				
				if(iterator.hasNext()) {
					throw new OrientConverterException("more than one vertex of type connected");
				}
			}
			return result;					
		}
	}
	
	public Object createLazyLoadingProxy(OrientPersistentProperty property) {
		ProxyFactory proxyFactory = new ProxyFactory();
		Class<?> type = property.getType();
		
		Class<?>[] allInterfacesForClass = ClassUtils.getAllInterfacesForClass(type);
		for(Class<?> interf : allInterfacesForClass) {
			proxyFactory.addInterface(interf);
		}
		
		LazyLoadingInterceptor interceptor = new LazyLoadingInterceptor(property,orientElement,converter, spELContext);
		proxyFactory.addAdvice(interceptor);
		
		proxyFactory.setTargetClass(type);
		proxyFactory.setProxyTargetClass(true);
		
		//TODO check instantiation objenesis
		
		return proxyFactory.getProxy();
	}

	
	public <T> T getSimplePropertyValue(OrientPersistentProperty property) {
		Assert.isTrue(!property.isAssociation());
		
		String expression = property.getSpelExpression();
		
		if(property.isIdProperty()) {
			return handleIdProperty(property);
		}
		
		TypeInformation<?> typeInformation = property.getTypeInformation();
		
		Object value = expression != null ? evaluator.evaluate(expression) : orientElement.getProperty(property.getField().getName());
		if (value == null) {
			return null;
		}
		
		return readValue(value, typeInformation);
	}


	private <T> T handleIdProperty(OrientPersistentProperty property) {
		return readValue(orientElement.getId(), property.getTypeInformation());
	}
	
	
	private Collection<Object> createCollection(TypeInformation<?> typeInformation) {
		Class<?> collectionType = typeInformation.getType();
		return (collectionType.isArray()) ? new ArrayList<Object>() : CollectionFactory.createCollection(collectionType, 10);
	}
	
	private static final Logger log = LoggerFactory.getLogger(DefaultOrientPropertyValueProvider.class);
	
	private boolean lazyLoadingProxy;
	
}
