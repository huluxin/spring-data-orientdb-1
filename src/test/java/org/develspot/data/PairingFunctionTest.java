package org.develspot.data;

import java.math.BigInteger;

import org.develspot.data.orientdb.common.OrientUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context-test.xml"})
public class PairingFunctionTest {

	@Test
	public void testCantorPairing() {
		long n1 = 1212;
		long n2 = 1212;
		
		BigInteger cantorPair = OrientUtils.cantorPair(n1, n2);
		
		long[] cantorUnpair = OrientUtils.cantorUnpair(cantorPair);
		
		Assert.assertEquals(n1, cantorUnpair[0]);
		Assert.assertEquals(n2, cantorUnpair[1]);
		
	}
	
	@Test
	public void testElegantPairing() {
		long n1 = 1212;
		long n2 = 1212;
		
		BigInteger elegantPair = OrientUtils.elegantPair(n1, n2);
		
		long[] elegantUnpair = OrientUtils.elegantUnpair(elegantPair);
		
		Assert.assertEquals(n1, elegantUnpair[0]);
		Assert.assertEquals(n2, elegantUnpair[1]);
		
	}
}
