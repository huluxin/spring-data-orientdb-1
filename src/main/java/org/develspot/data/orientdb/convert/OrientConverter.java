package org.develspot.data.orientdb.convert;

import org.develspot.data.orientdb.mapping.OrientPersistentEntity;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.springframework.data.convert.EntityConverter;
import org.springframework.data.convert.EntityReader;

import com.tinkerpop.blueprints.impls.orient.OrientElement;

public interface OrientConverter extends EntityConverter<OrientPersistentEntity<?>, OrientPersistentProperty, Object, OrientElement>,
				EntityReader<Object, OrientElement>{

	
}
