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

import java.math.BigInteger;

import org.develspot.data.orientdb.convert.MappingOrientConverter;
import org.develspot.data.orientdb.mapping.Connected;
import org.develspot.data.orientdb.mapping.OrientMappingContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.test.context.ContextConfiguration;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;



@ContextConfiguration(locations = {"classpath:context-test.xml"})
public class MappingOrientConverterTest extends AbstractDBTest {

	@Test
	public void read() {
//		OrientMappingContext mappingContext = new OrientMappingContext();
//		MappingOrientConverter converter = new MappingOrientConverter(mappingContext);
//		converter.afterPropertiesSet();
		
		OrientGraph orientGraph = new OrientGraph(orientDatasource.getConnection());
		OrientVertex customerVertex = orientGraph.addVertex("class:Customer");
		customerVertex.setProperty("firstname", "Rheuma");
		customerVertex.setProperty("lastname", "Kai");
		
		OrientVertex addressVertex = orientGraph.addVertex("class:Address");
		addressVertex.setProperty("city", "Saarlouis");
		
		orientGraph.addEdge(null, customerVertex, addressVertex, "has");
		
		
		OrientVertex relatedVertex = orientGraph.addVertex("class:Related");
		relatedVertex.setProperty("a", "1");
		
		orientGraph.addEdge(null, customerVertex, relatedVertex, "contains");
		orientGraph.addEdge(null, addressVertex, relatedVertex, "contains");
		
		Customer cust = converter.read(Customer.class, customerVertex);
		
		Assert.assertNotNull(cust.id);
		Assert.assertEquals("Rheuma", cust.provideFirstname());
		Assert.assertEquals("Kai", cust.provideLastname());
		
		System.out.println("address: " + cust.address);
		
		Related x = cust.address.x;
		Related y = cust.x;
		
		Assert.assertNotNull(x);
		Assert.assertEquals("1",x.getA());
		
		Assert.assertEquals(x, y);
		
	}
	
	@Autowired
	private MappingOrientConverter converter;
	
	
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
		
		@Id
		private BigInteger id;
		
	}

	class Address {
		private String city;
		@Connected(edgeType="contains")
		private Related x;
	}
	
	
	class Related {
		public String getA() {
			return a;
		}
		
		private String a;
		
	}
}
