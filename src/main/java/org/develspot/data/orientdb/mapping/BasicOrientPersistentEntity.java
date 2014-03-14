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
package org.develspot.data.orientdb.mapping;

import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;

public class BasicOrientPersistentEntity<T> extends BasicPersistentEntity<T, OrientPersistentProperty> implements OrientPersistentEntity<T> {

	public BasicOrientPersistentEntity(TypeInformation<T> information) {
		super(information);
		
		Class<T> rawType = information.getType();
		//default
		this.vertexType = rawType.getSimpleName();
		//check if annotation is present
		if(rawType.isAnnotationPresent(VertexType.class)) {
			VertexType annotation = rawType.getAnnotation(VertexType.class);
			if(!annotation.value().isEmpty()) {
				this.vertexType = annotation.value();
			}
		}
	}
	
	

	public String getVertexType() {
		return vertexType;
	}

	
	private String vertexType;
}
