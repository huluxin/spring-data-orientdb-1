package org.develspot.data.orientdb;

import java.util.List;

public interface OrientDBOperations {

	public String getVertexType(Class<?> clazz);
	
	public <T> List<T> findAll(Class<T> entityClass);
	
	public <T> T findById(Object id, Class<T> entityClass);
	
}
