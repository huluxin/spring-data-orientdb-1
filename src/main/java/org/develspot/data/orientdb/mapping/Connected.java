package org.develspot.data.orientdb.mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.Reference;

/**
 * An annotation that indicates that an entity is connected with an another
 * 
 * @author rico
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Reference
public @interface Connected {

	String edgeType();
	
	boolean lazy() default false;
}
