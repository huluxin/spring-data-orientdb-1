package org.develspot.data;

import java.math.BigInteger;

import org.develspot.data.orientdb.convert.MappingOrientConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.orientechnologies.orient.core.id.OClusterPositionLong;
import com.orientechnologies.orient.core.id.ORecordId;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context-test.xml"})
public class ConversionTest {

	
	@Test
	public void testRecordIdConversion() {
		Assert.assertTrue(converter.getConversionService().canConvert(ORecordId.class, BigInteger.class));
		Assert.assertTrue(converter.getConversionService().canConvert(BigInteger.class, ORecordId.class));

		int clusterId = 13;
		long clusterPosition_test1 = 0;
		int clusterPosition_test2 = 1;
		
		ORecordId recordId = new ORecordId(clusterId, new OClusterPositionLong(clusterPosition_test1));
		BigInteger converted = converter.getConversionService().convert(recordId, BigInteger.class);
		ORecordId convertedBack = converter.getConversionService().convert(converted, ORecordId.class);
		
		Assert.assertEquals(recordId, convertedBack);
		
		
		recordId = new ORecordId(clusterId, new OClusterPositionLong(clusterPosition_test2));
		converted = converter.getConversionService().convert(recordId, BigInteger.class);
		convertedBack = converter.getConversionService().convert(converted, ORecordId.class);
		Assert.assertEquals(recordId, convertedBack);
		
	}
	
	@Autowired
	private MappingOrientConverter converter;
}
