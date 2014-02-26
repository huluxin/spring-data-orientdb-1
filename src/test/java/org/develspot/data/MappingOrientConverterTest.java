package org.develspot.data;

import org.develspot.data.orientdb.convert.DefaultConnectionResolver;
import org.develspot.data.orientdb.convert.MappingOrientConverter;
import org.develspot.data.orientdb.mapping.OrientMappingContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;



@ContextConfiguration(locations = {"classpath:context-test.xml"})
public class MappingOrientConverterTest extends AbstractDBTest {

	@Test
	public void read() {
		OrientMappingContext mappingContext = new OrientMappingContext();
		MappingOrientConverter converter = new MappingOrientConverter(mappingContext, new DefaultConnectionResolver());
		
		
		OrientGraph orientGraph = new OrientGraph(orientDatasource.getConnection());
		OrientVertex customerVertex = orientGraph.addVertex("class:Customer");
		customerVertex.setProperty("firstname", "Rheuma");
		customerVertex.setProperty("lastname", "Kai");
		
		Customer cust = converter.read(Customer.class, customerVertex);
		Assert.assertEquals("Rheuma", cust.provideFirstname());
		Assert.assertEquals("Kai", cust.provideLastname());
	}
	
	
	
	class Customer {
		
		public String provideFirstname() {
			return firstname;
		}
		
		public String provideLastname() {
			return lastname;
		}
		
		private String firstname;
		private String lastname;
		
	}
	
}
