/**
 * 
 */
package org.weinschenker.demowebapp.eve;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
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
	@Resource(name="cacheManager")
	private CacheManager cacheManager;
	@Resource 
	private EntityManagerFactory entityManagerFactory;

	/* (non-Javadoc)
	 * @see org.weinschenker.demowebapp.eve.EveService#getCharacters(java.lang.String, java.lang.String)
	 */
	public Characters getCharacters(final String eveUserId,
			final String eveApiKey) {
		final Document chars = httpConnector
				.getCharacters(eveUserId, eveApiKey);
		cacheManager.toString();
		final Characters result = eveResponseConverter.getCharacters(chars);
		LOGGER.debug("Number of characters: "
				+ result.getCharacterList().size());
		
		EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo) entityManagerFactory;
		EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
		EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl)emf;
		LOGGER.debug(empImpl.getSessionFactory().getStatistics());
		
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.weinschenker.demowebapp.eve.EveService#addCharacter(java.lang.Character)
	 */
	public boolean addCharacter(final Character character){
		return true;
	}
	
	
}
