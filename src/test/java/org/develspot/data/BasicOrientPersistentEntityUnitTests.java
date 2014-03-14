/*******************************************************************************
 * Copyright 2013-2014 the original author or authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.develspot.data;

import org.develspot.data.orientdb.mapping.BasicOrientPersistentEntity;
import org.develspot.data.orientdb.mapping.Connected;
import org.develspot.data.orientdb.mapping.OrientMappingContext;
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.develspot.data.orientdb.mapping.VertexType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context-test.xml"})
public class BasicOrientPersistentEntityUnitTests {

	@Test
	public void testEntity() {
		BasicOrientPersistentEntity<?> entity = mappingContext.getPersistentEntity(Person.class);
		Assert.assertEquals("Person", entity.getVertexType());
		
		entity = mappingContext.getPersistentEntity(Address.class);
		Assert.assertEquals("DefaultAddress", entity.getVertexType());
		
	}
	
	@Test
	public void testEntityProperties() {
		BasicOrientPersistentEntity<?> entity = mappingContext.getPersistentEntity(Person.class);
		Assert.assertNotNull(entity.getIdProperty());
		OrientPersistentProperty idProperty = entity.getIdProperty();
		Assert.assertEquals("id", idProperty.getField().getName());
		Assert.assertNotNull(entity.getPersistentProperty("name"));
		
		OrientPersistentProperty addressProperty = entity.getPersistentProperty("defaultAddress");
		Assert.assertTrue(addressProperty.isAssociation());
		
		Assert.assertEquals("lives", addressProperty.getConnected().edgeType());
	}
	
	
	class Person {
		private String name;
		
		@Id
		private String id;
		
		@Connected(edgeType="lives")
		private Address defaultAddress;
		
	}
	
	@VertexType("DefaultAddress")
	class Address {
		
	}

	@Autowired
	private OrientMappingContext mappingContext;
	
}
