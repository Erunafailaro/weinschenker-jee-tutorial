/**
 * 
 */
package org.weinschenker.demowebapp.eve;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

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
	@Resource
	private EntityManagerFactory entityManagerFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.weinschenker.demowebapp.eve.EveService#getCharacters(java.lang.String
	 * , java.lang.String)
	 */
	@MyCache(cacheName = "getCharacters", keyParams = {0, 1})
	public Characters getCharacters(final String eveUserId,
			final String eveApiKey) {
		final EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
		final EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
		final EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl) emf;

		LOGGER.debug(empImpl.getSessionFactory().getStatistics());

		final Document chars = httpConnector.getCharacters(eveUserId, eveApiKey);
		final Characters result = eveResponseConverter.getCharacters(chars);
		LOGGER.debug("Number of characters: " + result.getCharacterList().size());
		return result;
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
