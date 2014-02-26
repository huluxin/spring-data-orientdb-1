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
