package org.weinschenker.demowebapp.eve;

import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;
import org.weinschenker.demowebapp.dto.Characters;

public interface EveService {

	public Characters getCharacters(final String eveUserId);

	public boolean addCharacter(final Character character);

}