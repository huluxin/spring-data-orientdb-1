package org.develspot.data.orientdb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.develspot.data.orientdb.convert.OrientConverter;
import org.develspot.data.orientdb.mapping.OrientPersistentEntity;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import com.orientechnologies.orient.core.id.ORecordId;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class OrientDBTemplate implements OrientDBOperations {

	public OrientDBTemplate(IOrientDataSource dataSource, OrientConverter converter) {
		this.dataSource = dataSource;
		this.mappingContext = converter.getMappingContext();
		this.converter = converter;
	}
	
	public String getVertexType(Class<?> clazz) {
		Assert.notNull(clazz);
		return mappingContext.getPersistentEntity(clazz).getVertexType();
	}

	public <T> List<T> findAll(Class<T> entityClass) {
		if(log.isTraceEnabled()) {
			log.trace("loading vertices of type: {} ", entityClass);
		}
		
		OrientGraph graph = getGraph();
		Iterable<Vertex> vertices = graph.getVerticesOfClass(getVertexType(entityClass));
		Iterator<Vertex> iterator = vertices.iterator();
		
		List<T> result = new ArrayList<T>();
		
		while(iterator.hasNext()) {
			OrientVertex v = (OrientVertex) iterator.next();
			result.add(converter.read(entityClass, v));
		}
		return result;
	}

	public <T> T findById(Object id, Class<T> entityClass) {
		if(log.isTraceEnabled()) {
			log.trace("loading vertex of type: {} and id: {}", entityClass, id);
		}
		
		ORecordId recordId = getRecordId(id);
		if(recordId == null)
			throw new IllegalArgumentException("Cannot get ORecordId from given id: " + id);
		
		OrientVertex vertex = getGraph().getVertex(recordId);
		
		return (vertex == null) ? null : converter.read(entityClass, vertex);
		
	}

	
	private ORecordId getRecordId(Object id) {
		if(ORecordId.class.isAssignableFrom(id.getClass())) {
			return (ORecordId)id;
		}
		
		if(converter.getConversionService().canConvert(id.getClass(), ORecordId.class)) {
			return converter.getConversionService().convert(id, ORecordId.class);
		}
		
		return null;
	}
	
	
	private OrientGraph getGraph() {
		return new OrientGraph(dataSource.getConnection());
	}
	

	private static final Logger log = LoggerFactory.getLogger(OrientDBTemplate.class);
	
	private MappingContext<? extends OrientPersistentEntity<?>, OrientPersistentProperty> mappingContext;
	private IOrientDataSource dataSource;
	private OrientConverter converter;
}
