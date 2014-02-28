package org.develspot.data.orientdb.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tinkerpop.blueprints.impls.orient.OrientElement;

public class OrientDataWrapper<T extends OrientElement> {

	public OrientDataWrapper(T orientElement) {
		this.elements = new HashSet<T>();
		this.elements.add(orientElement);
		
	}
	
	public OrientDataWrapper(Set<T> elements) {
		this.elements = elements;
	}
	
	
	public boolean isSingleElement() {
		return elements.size() == 1;
	}
	
	
	public T getOrientElement() {
		return elements.iterator().next();
	}
	
	public Collection<T> getElements() {
		return elements;
	}
	
	private Set<T> elements;
}
