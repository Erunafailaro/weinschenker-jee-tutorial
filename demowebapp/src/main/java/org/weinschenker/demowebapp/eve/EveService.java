package org.weinschenker.demowebapp.eve;

import org.weinschenker.demowebapp.cache.MyCache;
import org.weinschenker.demowebapp.dto.Characters;

public interface EveService {

	@MyCache(cacheName = "getCharacters")
	public Characters getCharacters(final String eveUserId);

	public boolean addCharacter(final Character character);

}