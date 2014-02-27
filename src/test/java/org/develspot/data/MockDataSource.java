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

import org.develspot.data.orientdb.IOrientDataSource;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

public class MockDataSource implements IOrientDataSource {

	
	public static ThreadLocal<OGraphDatabase> THREAD_LOCAL_INSTANCE = new ThreadLocal<OGraphDatabase>();
	
	
	public void recreateDatabase() {
		OGraphDatabase graphDatabase = new OGraphDatabase("local:testdb");
		if(graphDatabase.exists()) {
			graphDatabase.open("admin", "admin");
			graphDatabase.drop();
		}
		THREAD_LOCAL_INSTANCE.set(null);
		graphDatabase.create();
	}
	
	public void dropDatabase() {
		if(isConnectionOpen())
			closeCurrentConnection();
		
		OGraphDatabase graphDatabase = new OGraphDatabase("local:testdb");
		if(graphDatabase.exists()) {
			graphDatabase.open("admin", "admin");
			graphDatabase.drop();
			graphDatabase.close();
			THREAD_LOCAL_INSTANCE.set(null);
		}
	}
	

	public boolean isConnectionOpen() {
		return THREAD_LOCAL_INSTANCE.get() != null;
	}
	
	
	
	public OGraphDatabase getConnection() {
		OGraphDatabase graphDatabase = THREAD_LOCAL_INSTANCE.get();
		if(graphDatabase == null)
			return newConnection();
		
		return graphDatabase;
	}
	
	public void closeCurrentConnection() {
		OGraphDatabase graphDatabase = THREAD_LOCAL_INSTANCE.get();
		if(graphDatabase != null)
			graphDatabase.close();
		
		THREAD_LOCAL_INSTANCE.set(null);			
	}
	
	
	
	private OGraphDatabase newConnection() {
		OGraphDatabase graphDatabase = new OGraphDatabase("local:testdb");
		if(graphDatabase.isClosed()) {
			graphDatabase.open("admin", "admin");
		}
		THREAD_LOCAL_INSTANCE.set(graphDatabase);
		return graphDatabase;
	}
	
}
