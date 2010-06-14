/**
 * 
 */
package org.weinschenker.demowebapp.cache;

import java.util.List;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/**
 * @author jawe1de
 * 
 */
@Service
@Aspect
public class CacheInterceptor {
	
	private static final Logger LOGGER = Logger.getLogger(CacheInterceptor.class);

	@Resource(name = "cacheManager")
	private CacheManager cacheManager;

	/**
	 * any public method
	 */
	@SuppressWarnings("unused")
	@Pointcut(value = "execution(public * *(..))")
	private void anyPublicMethod(){}

	/**
	 * any method annotated "MyCache"
	 * @param myCache
	 */
	@SuppressWarnings("unused")
	@Pointcut(value = "@annotation(myCache)", argNames = "myCache")
	private void serviceCall(final MyCache myCache) {
	}

	/**
	 * @see http://static.springsource.org/spring/docs/2.0.x/reference/aop.html
	 * @see http://code.google.com/p/gwt-ent/wiki/AOP
	 * @see http://forum.springsource.org/showthread.php?t=63512
	 * @param pjp
	 * @param myCache
	 * @return
	 */
	@Around(value = "anyPublicMethod() && serviceCall(myCache)")
	public Object doBasicProfiling(ProceedingJoinPoint pjp,
			final MyCache myCache) {
		final Object[] args = pjp.getArgs();
		final String cacheName = myCache.cacheName();
		final List<Element> cache = (List<Element>) cacheManager.getCache(cacheName)
				.getKeysWithExpiryCheck();
		final String cacheKey = (String)args[0];
		if (!cache.contains(cacheKey)) {
		    Object retVal = null;
			try {
				retVal = pjp.proceed();
			} catch (Throwable e) {
				LOGGER.debug("Throwable during caching", e);
			}
			if (retVal == null) {
				return null;
			}
			final Element e = new Element(cacheKey, retVal);
			cacheManager.getCache("cacheName").put(e);
			return retVal;

		}
		final Element e = cacheManager.getCache(cacheName).get(cacheKey);
		final Object cachedObject =  e.getObjectValue();
		return cachedObject;

	}

}
