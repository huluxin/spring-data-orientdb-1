package org.develspot.data.orientdb.config;

import org.develspot.data.orientdb.mapping.OrientMappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AbstractOrientConfiguration {

	
	@Bean
	public OrientMappingContext mappingContext() throws ClassNotFoundException {

		OrientMappingContext mappingContext = new OrientMappingContext();
		return mappingContext;
	}

}
