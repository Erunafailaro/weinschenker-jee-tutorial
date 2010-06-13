/**
 * 
 */
package org.weinschenker.demowebapp.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author jawe1de
 *
 */
@Aspect
public class CacheInterceptor {
	
	/**
	 * @see http://static.springsource.org/spring/docs/2.0.x/reference/aop.html
	 * @see http://code.google.com/p/gwt-ent/wiki/AOP
	 * @see http://forum.springsource.org/showthread.php?t=63512
	 * @param pjp
	 * @param myCache
	 * @return
	 */
	
	@SuppressWarnings("unused")
	@Pointcut(value="execution(public * *(..)) && args(eveUserId) && " +
			"@annotation(myCache)",
			   argNames="eveUserId, myCache")
	private void serviceCall(final String eveUserId, final MyCache myCache){}
	
	@Around(value = "serviceCall(eveUserId, myCache)")
	@SuppressWarnings("unused")
	public Object doBasicProfiling(ProceedingJoinPoint pjp, final String eveUserId, final MyCache myCache){
		final Object[] args = pjp.getArgs();
		//final String cacheName= myCache.cacheName();

		return null;
	}

}
