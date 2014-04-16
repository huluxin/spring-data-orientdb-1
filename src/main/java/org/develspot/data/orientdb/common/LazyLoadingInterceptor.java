package org.develspot.data.orientdb.common;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.develspot.data.orientdb.convert.MappingOrientConverter;
import org.develspot.data.orientdb.convert.DefaultOrientPropertyValueProvider;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.model.SpELContext;

import com.tinkerpop.blueprints.impls.orient.OrientElement;

public class LazyLoadingInterceptor implements MethodInterceptor {

	public LazyLoadingInterceptor(OrientPersistentProperty property, OrientElement orientElement, MappingOrientConverter converter,
						SpELContext spELContext) {
		this.property = property;
		this.orientElement = orientElement;
		this.converter = converter;
		this.spELContext = spELContext;
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		if(log.isTraceEnabled()) {
			log.trace("lazy loading for property: " + property);
		}
		
		Object target = ensureResolved();

		if (target == null) {
			return null;
		}

		return method.invoke(target, invocation.getArguments());
		
	}
	
	/**
	 * Will trigger the resolution if the proxy is not resolved already or return a previously resolved result.
	 * 
	 * @return
	 */
	private Object ensureResolved() {

		if (!resolved) {
			this.result = resolve();
			this.resolved = true;
		}

		return this.result;
	}

	/**
	 * Resolves the proxy into its backing object.
	 * 
	 * @return
	 */
	private synchronized Object resolve() {

		if (!resolved) {
			try {
				DefaultOrientPropertyValueProvider resolver = new DefaultOrientPropertyValueProvider(orientElement, converter, spELContext,false);
				result = resolver.getPropertyValue(property);

			} catch (RuntimeException ex) {
				//TODO translate exception
				throw new RuntimeException(ex);
			}
		}
		return result;
	}

	private static final Logger log = LoggerFactory.getLogger(LazyLoadingInterceptor.class);
	
	private boolean resolved = false;
	private Object result;
	private OrientPersistentProperty property;
	private OrientElement orientElement;
	
	private MappingOrientConverter converter;
	private SpELContext spELContext;
	
}
