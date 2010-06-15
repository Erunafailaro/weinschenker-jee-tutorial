/**
 * 
 */
package org.weinschenker.demowebapp.cache;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.sf.ehcache.Cache;
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
	
	@PostConstruct
	public void init() {
		LOGGER.debug("initialized CacheInterceptor");
	}

	/**
	 * any public method
	 */
	@SuppressWarnings("unused")
	@Pointcut(value = "execution(public * *(..))")
	private void anyPublicMethod(){
		// empty
	}

	/**
	 * any method annotated "MyCache"
	 * @param myCache 
	 */
	@SuppressWarnings("unused")
	@Pointcut(value = "@annotation(myCache)", argNames = "myCache")
	private void serviceCall(final MyCache myCache) {
		// empty
	}

	/**
	 * @see http://static.springsource.org/spring/docs/2.0.x/reference/aop.html
	 * @see http://code.google.com/p/gwt-ent/wiki/AOP
	 * @see http://forum.springsource.org/showthread.php?t=63512
	 * @param pjp
	 * @param myCache
	 * @return Object
	 */
	@Around(value = "serviceCall(myCache)", argNames = "myCache")
	public Object doCachingWrap(final ProceedingJoinPoint pjp,
			final MyCache myCache) {
		final String cacheName = myCache.cacheName();
		final Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			LOGGER.error("EhCache " + cacheName + " is null!");
			return null;
		}
		final Object[] args = pjp.getArgs();
		final List<Element> cacheList = (List<Element>) cache.getKeysWithExpiryCheck();

		String cacheKey = "";
		if (myCache.keyParams() == null || myCache.keyParams().length == 0) {
			cacheKey = (String)args[0];
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < myCache.keyParams().length; i++) {
				int parameterIndex = myCache.keyParams()[i];
				if (i > 0) {
					sb.append(".");
				}
				sb.append((String)args[parameterIndex]);
			}
			cacheKey = sb.toString();
		}
		if (! cacheList.contains(cacheKey)) {
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
			cacheManager.getCache(cacheName).put(e);
			return retVal;

		}
		LOGGER.debug("Cache hit - " + cacheName);
		final Element e = cache.get(cacheKey);
		final Object cachedObject =  e.getObjectValue();
		return cachedObject;

	}

}
