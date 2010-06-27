/**
 * 
 */
package org.weinschenker.demowebapp.cache;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;

/**
 * 
 */
@Service
@Aspect
public class CacheInterceptor {
	
	private static final Logger LOGGER = Logger.getLogger(CacheInterceptor.class);

	@Resource(name = "cacheManager")
	private CacheManager cacheManager;
	@Resource
	private EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		LOGGER.debug("initialized CacheInterceptor");
	}

	/**
	 * any public method.
	 */
	@SuppressWarnings("unused")
	@Pointcut(value = "execution(public * *(..))")
	private void anyPublicMethod() {
		// empty
	}

	/**
	 * any method annotated "MyCache".
	 * @param myCache 
	 */
	@SuppressWarnings("unused")
	@Pointcut(value = "@annotation(myCache)", argNames = "myCache")
	private void cachedMethod(final MyCache myCache) {
		// empty
	}

	/**
	 * @see http://static.springsource.org/spring/docs/2.0.x/reference/aop.html
	 * @see http://code.google.com/p/gwt-ent/wiki/AOP
	 * @see http://forum.springsource.org/showthread.php?t=63512
	 * 
	 * @param pjp
	 * @param myCache
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "cachedMethod(myCache)", argNames = "myCache")
	public Object doCachingWrap(final ProceedingJoinPoint pjp,
			final MyCache myCache) {
		Object returnValue = null;
		final String cacheName = myCache.cacheName();
		final Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			LOGGER.error("EhCache " + cacheName + " is null!");
			return null;
		}
		final Object[] args = pjp.getArgs();
		final String cacheKey = getCacheKey(myCache.keyParams(), args);
		
		if (myCache.mode() == MyCache.Mode.PUT) {
			returnValue = doCachePut(pjp, cache, cacheKey);
		}
		if (myCache.mode() == MyCache.Mode.FLUSH) {
			returnValue = doCacheFlush(pjp, cache, cacheKey);
		}
		if (myCache.debug()) {
			debugStatistics();
		}
		return returnValue;
	}
	
	/**
	 * This method will try to obtain a cached object from the cache. If object
	 * is not cached, the wrapped method is executed. The returnvalue will be
	 * put into the cache and then returned by this method.
	 * 
	 * @param pjp
	 * @param cache
	 * @param cacheKey
	 * @return
	 */
	private Object doCachePut(final ProceedingJoinPoint pjp, final Cache cache,
			final String cacheKey) {
		final List<Element> cacheList = (List<Element>) cache
				.getKeysWithExpiryCheck();
		if (!cacheList.contains(cacheKey)) {
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
			cache.put(e);
			return retVal;

		}
		LOGGER.debug("Cache hit - " + cache.getName());
		final Element e = cache.get(cacheKey);
		final Object cachedObject = e.getObjectValue();
		return cachedObject;
	}
	
	/**
	 * This method will remove the cache element identified by the cacheKex from
	 * the supplied cache.
	 * 
	 * @param pjp
	 * @param cache
	 * @param cacheKey
	 * @return
	 */
	private Object doCacheFlush(final ProceedingJoinPoint pjp, final Cache cache,
			final String cacheKey) {
		cache.remove(cacheKey);
		Object retVal = null;
		try {
			retVal = pjp.proceed();
		} catch (Throwable e) {
			LOGGER.debug("Throwable during caching", e);
		}
		return retVal;
	}
	
	
	/**
	 * Generate the cacheKey.
	 * @param params
	 * @param args
	 * @return
	 */
	private String getCacheKey(final int[] params, final Object[] args) {
		String cacheKey = "";
		if (params == null || params.length == 0) {
			cacheKey = (String) args[0];
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < params.length; i++) {
				int parameterIndex = params[i];
				final Object eachStringParam = args[parameterIndex];
				if (eachStringParam instanceof String) {
					if (i > 0) {
						sb.append(".");
					}
					sb.append((String) args[parameterIndex]);
				}
			}
			cacheKey = sb.toString();
		}
		return cacheKey;
	}
	
	/**
	 * Debug cache-statistics.
	 */
	private void debugStatistics() {
		final EntityManagerFactoryInfo emfi =
			(EntityManagerFactoryInfo) entityManagerFactory;
		final EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
		final EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl) emf;
		LOGGER.debug(empImpl.getSessionFactory().getStatistics());
	}

}
