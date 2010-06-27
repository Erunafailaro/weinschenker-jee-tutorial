/**
 * 
 */
package org.weinschenker.demowebapp.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCache {

	/**
	 * The two modes in which caching can be done.
	 * 
	 * <ul>
	 * <li>{@link #PUT}: Add data to the cache.</li>
	 * <li>{@link #FLUSH}: Remove data from the cache.</li>
	 * </ul>
	 * 
	 */
	public enum Mode {PUT, FLUSH}
	
	/**
	 * The name of the cache that this method should use.
	 * 
	 * @return
	 */
	String cacheName();

	/**
	 * The indexes of the method parameters that should be used to construct the
	 * cache-key.
	 * 
	 * @return
	 */
	int[] keyParams() default { };

	/**
	 * Set this to true if you want to log cache-statistics. Will perform a
	 * LOGGER.debug()-call. Default is false.
	 * 
	 * @return
	 */
	boolean debug() default false;
	
	/**
	 * Sets the behavior of this caching-operation. 
	 * @return
	 */
	Mode mode() default Mode.PUT;

}
