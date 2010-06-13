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
import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;
import org.w3c.dom.Document;
import org.weinschenker.demowebapp.cache.MyCache;
import org.weinschenker.demowebapp.dto.Characters;

/**
 * @author jawe1de
 * 
 */
@Service
public class EveServiceImpl implements EveService {
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
	/* @Cacheable(modelId = "getCharactersModel") */
	@MyCache(cacheName = "getCharacters")
	public Characters getCharacters(final String eveUserId) {
		cacheManager.toString();

		EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
		EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
		EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl) emf;
		LOGGER.debug(empImpl.getSessionFactory().getStatistics());
		List cache = cacheManager.getCache("getCharacters")
				.getKeysWithExpiryCheck();

		if (!cache.contains(eveUserId)) {
			final Document chars = httpConnector.getCharacters(eveUserId,
					eveApiKey);
			final Characters result = eveResponseConverter.getCharacters(chars);
			LOGGER.debug("Number of characters: "
					+ result.getCharacterList().size());
			Element e = new Element(eveUserId, result);
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
	@CacheFlush(modelId = "getCharactersFlushingModel")
	public boolean addCharacter(final Character character) {
		return true;
	}

}
