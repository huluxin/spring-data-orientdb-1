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
package org.develspot.data.orientdb.transaction;

import org.springframework.transaction.support.ResourceHolderSupport;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

public class GraphDatabaseHolder extends ResourceHolderSupport {

	public GraphDatabaseHolder(OGraphDatabase graphDatabase) {
		this.graphDatabase = graphDatabase;
	}
	
	public OGraphDatabase getConnection() {
		return graphDatabase;
	}
	
	public void setTransactionActive(boolean transactionActive) {
		this.transactionActive = transactionActive;
	}
	
	public boolean isTransactionActive() {
		return transactionActive;
	}
	
	public void clear() {
		super.clear();
		transactionActive = false;
	}
	
	private OGraphDatabase graphDatabase;
	private boolean transactionActive;
}
