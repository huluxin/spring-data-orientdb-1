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
package org.develspot.data.orientdb.convert;

import java.math.BigInteger;

import org.develspot.data.orientdb.common.OrientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import com.orientechnologies.orient.core.id.OClusterPositionLong;
import com.orientechnologies.orient.core.id.ORecordId;


public abstract class AbstractOrientConverter implements OrientConverter, InitializingBean {

	public AbstractOrientConverter(GenericConversionService conversionService) {
		this.conversionService = conversionService == null ? new DefaultConversionService()
				: conversionService;
	}
	
	public ConversionService getConversionService() {
		return conversionService;
	}


	/*
	 * converts the orient recordId into a bigInteger using a pairing function
	 */
	public static enum ORecordIdToBigIntegerConverter implements Converter<ORecordId, BigInteger> {
		INSTANCE;

		public BigInteger convert(ORecordId id) {
			if(id == null)
				return null;
			
			return OrientUtils.elegantPair(id.getClusterId(), id.getClusterPosition().longValue());
		}
	}
	
	
	/*
	 * converts the orient recordId into a bigInteger using a pairing function
	 */
	public static enum BigIntegerToORecordIdConverter implements Converter<BigInteger,ORecordId> {
		INSTANCE;

		public ORecordId convert(BigInteger id) {
			if(id == null)
				return null;
			
			long[] idParts = OrientUtils.elegantUnpair(id);
			
			return new ORecordId((int)idParts[0], new OClusterPositionLong(idParts[1]));
		}
	}
	
	
	
	public void afterPropertiesSet() throws Exception {
		initializeConverters();
	}

	
	private void initializeConverters() {
		conversionService.addConverter(ORecordIdToBigIntegerConverter.INSTANCE);
		conversionService.addConverter(BigIntegerToORecordIdConverter.INSTANCE);
	}
	
	protected final GenericConversionService conversionService;
	
	
	private static final Logger log = LoggerFactory.getLogger(AbstractOrientConverter.class);
	
}
