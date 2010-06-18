/**
 * 
 */
package org.weinschenker.demowebapp.eve;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.weinschenker.demowebapp.cache.MyCache;
import org.weinschenker.demowebapp.cache.MyCache.Mode;
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

	/**
	 * (non-Javadoc).
	 * 
	 * @see
	 * org.weinschenker.demowebapp.eve.EveService#getCharacters(java.lang.String
	 * , java.lang.String)
	 */
	@MyCache(cacheName = "getCharacters", keyParams = {0, 1}, debug = true)
	public Characters getCharacters(final String eveUserId,
			final String eveApiKey) {

		final Document chars = httpConnector.getCharacters(eveUserId, eveApiKey);
		final Characters result = eveResponseConverter.getCharacters(chars);
		LOGGER.debug("Number of characters: " + result.getCharacterList().size());
		return result;
	}

	/**
	 * (non-Javadoc).
	 * 
	 * @see
	 * org.weinschenker.demowebapp.eve.EveService#addCharacter(java.lang.Character
	 * )
	 */
	@MyCache(cacheName = "getCharacters", keyParams = {1, 2},mode = Mode.FLUSH)
	public boolean addCharacter(final Character character, final String eveUserId,
			final String eveApiKey) {
		return true;
	}

}
