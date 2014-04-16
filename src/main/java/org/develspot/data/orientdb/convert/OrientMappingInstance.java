package org.develspot.data.orientdb.convert;

import java.util.HashMap;

public class OrientMappingInstance {
	
	public OrientMappingInstance() {
		this.instanceMap = new HashMap<Object, Object>();
	}

	public Object get(Object objectId) {
		return instanceMap.get(objectId);
	}
	
	public boolean instanceLoaded(Object objectId) {
		return instanceMap.containsKey(objectId);
	}
	
	public void addInstance(Object objectId, Object instance) {
		if(instanceMap.containsKey(objectId)) {
			throw new IllegalArgumentException("instance already loaded!");
		}
		instanceMap.put(objectId, instance);
	}
	
	
	
	private HashMap<Object, Object> instanceMap;
	
}
