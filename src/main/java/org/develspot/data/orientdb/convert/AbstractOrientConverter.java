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
	 * converts the orient recordId into a string, the bytes of this String will be used to create a bigInteger
	 */
	public static enum ORecordIdToBigIntegerConverter implements Converter<ORecordId, BigInteger> {
		INSTANCE;

		public BigInteger convert(ORecordId id) {
			if(id == null)
				return null;
			
			return OrientUtils.cantorPair(id.getClusterId(), id.getClusterPosition().longValue());
		}
	}
	
	
	public void afterPropertiesSet() throws Exception {
		initializeConverters();
	}

	
	private void initializeConverters() {
		conversionService.addConverter(ORecordIdToBigIntegerConverter.INSTANCE);
	}
	
	protected final GenericConversionService conversionService;
	
	
	private static final Logger log = LoggerFactory.getLogger(AbstractOrientConverter.class);
	
}
