package org.develspot.data;

import org.develspot.data.orientdb.IOrientDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context-test.xml"})
public abstract class AbstractDBTest {

	@Before
	public void setup() {
		((MockDataSource)orientDatasource).recreateDatabase();
		
	}
	
	@After
	public void after() {
		((MockDataSource)orientDatasource).dropDatabase();
	}
	
	
	@Autowired
	protected IOrientDataSource orientDatasource;
}
