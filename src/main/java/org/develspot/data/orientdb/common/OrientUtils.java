package org.develspot.data.orientdb.common;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrientUtils {
	
	
	public static void closeStream(OutputStream out) {
		if(out != null) {
			try {
				out.close();
			}catch(IOException ioe) {
				log.trace("Unable to close stream: " + ioe.getMessage());
			}
		}
	}
	
	
	/**
	 * calculating sqrt floor with herons formula
	 * 
	 * @param x
	 * @return
	 */
	public static BigInteger bigIntSqRootFloor(BigInteger x) {
		if(x.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException("given value must be greater than 0");
		}
		
		//square root of 0/1 is trivial
		if(x.equals(BigInteger.ZERO) || x.equals(BigInteger.ONE)) {
			return x;
		}
		
		BigInteger two = BigInteger.valueOf(2L);
		//guess a start value
		BigInteger result = x.add(BigInteger.ONE).divide(two);
		
		
		while( result.compareTo(x.divide(result)) > 0 ) {
			result = result.add(x.divide(result)).divide(BigInteger.valueOf(2));
		}
		
		return result;
		
	}
	
	/**
	 * Cantor Pairing
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static BigInteger cantorPair(long n1, long n2) {
		BigInteger two = BigInteger.valueOf(2L);

		BigInteger x = BigInteger.valueOf(n1);
		BigInteger y = BigInteger.valueOf(n2);
		
		return y.add(x.add(y).divide(two).multiply(x.add(y).add(BigInteger.ONE)));
	}
	
	/**
	 * 
	 * @param z a paired bigInteger (by two long values)
	 * @return
	 */
	public static long[] cantorUnpair(BigInteger z) {
		BigInteger two = BigInteger.valueOf(2L);
		
		BigInteger q = bigIntSqRootFloor(BigInteger.valueOf(8).multiply(z).add(BigInteger.ONE)).subtract(BigInteger.ONE).divide(two);
		
		long n2 = z.subtract(q.multiply(q.add(BigInteger.ONE)).divide(two)).longValue();
		long n1 = q.subtract(BigInteger.valueOf(n2)).longValue();
		
		
		return new long[]{n1,n2};
		
		
	}
	
	
	/**
	 * Elegant pairing (@see http://szudzik.com/ElegantPairing.pdf)
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static BigInteger elegantPair(long n1, long n2) {
		BigInteger result;
		
		BigInteger x = BigInteger.valueOf(n1), y = BigInteger.valueOf(n2);
		
		if(x.compareTo(y) == -1) {
			result = y.pow(2).add(x);
		}
		else {
			result = x.pow(2).add(x).add(y);
		}
		return result;
	}
	
	
	public static long[] elegantUnpair(BigInteger z) {
		BigInteger a = bigIntSqRootFloor(z);
		BigInteger b = z.subtract(a.pow(2));
		
		//conditions 
		
		if(b.compareTo(a) == -1) {
			return new long[] {b.longValue(), a.longValue()};
		}
		else {
			return new long[] {a.longValue(), b.subtract(a).longValue()};
		}
	}
	
	
	
	private static final Logger log = LoggerFactory.getLogger(OrientUtils.class);
}
