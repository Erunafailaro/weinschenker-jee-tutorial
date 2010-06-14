/**
 * 
 */
package org.weinschenker.demowebapp.eve;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.weinschenker.demowebapp.cache.MyCache;
import org.weinschenker.demowebapp.dto.Characters;

/**
 * @author jawe1de
 * 
 */
@Service
public class EveServiceImpl {
	private static final Logger LOGGER = Logger.getLogger(EveServiceImpl.class);

	@Resource
	private HttpConnector httpConnector;
	@Resource
	private EveResponseConverter eveResponseConverter;
	@Resource(name = "cacheManager")
	private CacheManager cacheManager;
	@Resource
	private EntityManagerFactory entityManagerFactory;
	@Resource
	private String eveApiKey;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.weinschenker.demowebapp.eve.EveService#getCharacters(java.lang.String
	 * , java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@MyCache(cacheName = "getCharacters")
	public Characters getCharacters(final String eveUserId) {
		final EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
		final EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
		final EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl) emf;
		
		LOGGER.debug(empImpl.getSessionFactory().getStatistics());
		
		final List cache = cacheManager.getCache("getCharacters")
				.getKeysWithExpiryCheck();
		if (!cache.contains(eveUserId)) {
			final Document chars = httpConnector.getCharacters(eveUserId,
					eveApiKey);
			final Characters result = eveResponseConverter.getCharacters(chars);
			LOGGER.debug("Number of characters: "
					+ result.getCharacterList().size());
			final Element e = new Element(eveUserId, result);
			cacheManager.getCache("getCharacters").put(e);
			return result;
		}
		final Element e = cacheManager.getCache("getCharacters").get(eveUserId);
		final Characters c = (Characters) e.getObjectValue();
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.weinschenker.demowebapp.eve.EveService#addCharacter(java.lang.Character
	 * )
	 */
	public boolean addCharacter(final Character character) {
		return true;
	}

}
