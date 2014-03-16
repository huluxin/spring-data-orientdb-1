package org.develspot.data.orientdb.convert;

import java.util.HashMap;

public class ReadSession {

	public ReadSession() {
		loadedEntities = new HashMap<Object, Object>();
	}
	
	public boolean instanceAvailable(Object dbId) {
		return loadedEntities.containsKey(dbId);
	}
	
	public Object get(Object dbId) {
		return loadedEntities.get(dbId);
	}
	
	
	public void add(Object dbId, Object loadedEntity) {
		this.loadedEntities.put(dbId, loadedEntity);
	}
	private HashMap<Object, Object> loadedEntities;
}
