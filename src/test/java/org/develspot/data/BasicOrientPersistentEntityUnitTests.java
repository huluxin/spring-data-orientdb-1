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
import org.develspot.data.orientdb.mapping.OrientPersistentProperty;
import org.junit.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.util.ClassTypeInformation;

public class BasicOrientPersistentEntityUnitTests {

	@Test
	public void testIt() {
		BasicOrientPersistentEntity<Person> entity = new BasicOrientPersistentEntity<Person>(
									ClassTypeInformation.from(Person.class));
		
		
		OrientPersistentProperty idProperty = entity.getIdProperty();
		
		idProperty.getField();
	}
	
	
	class Person {
		private String name;
		
		@Id
		private String id;
	}
}
