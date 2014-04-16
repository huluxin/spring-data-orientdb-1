package org.develspot.data.orientdb.common;

import org.develspot.data.orientdb.convert.OrientMappingInstance;

public abstract class MappingInstanceHolder {

	private static final ThreadLocal<OrientMappingInstance> mappingInstanceHolder = new ThreadLocal<OrientMappingInstance>();
	
	
	public static OrientMappingInstance getMappingInstance() {
		OrientMappingInstance mappingInstance = mappingInstanceHolder.get();
		if(mappingInstance == null) {
			mappingInstance = new OrientMappingInstance();
			mappingInstanceHolder.set(mappingInstance);
		}
		
		return mappingInstance;
	}
}
