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

import org.develspot.data.orientdb.convert.DefaultConnectionResolver;
import org.develspot.data.orientdb.convert.MappingOrientConverter;
import org.develspot.data.orientdb.mapping.Connected;
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
		
		OrientVertex address = orientGraph.addVertex("class:Address");
		orientGraph.addEdge(null, customerVertex, address, "has");
		
		
		OrientVertex related = orientGraph.addVertex("class:Related");
		orientGraph.addEdge(null, customerVertex, related, "contains");
		orientGraph.addEdge(null, address, related, "contains");
		
		Customer cust = converter.read(Customer.class, customerVertex);
		
		Assert.assertEquals("Rheuma", cust.provideFirstname());
		Assert.assertEquals("Kai", cust.provideLastname());
		
		Assert.assertEquals(cust.x, cust.address.x);
		
		
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
		
		@Connected(edgeType="has")
		private Address address;
		
		@Connected(edgeType="contains")
		private Related x;
		
	}

	class Address {
		@Connected(edgeType="contains")
		private Related x;
	}
	
	
	class Related {
		
	}
}
