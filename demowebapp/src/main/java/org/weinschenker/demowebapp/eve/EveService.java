package org.weinschenker.demowebapp.eve;

import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;
import org.weinschenker.demowebapp.dto.Characters;

public interface EveService {

	@Cacheable(modelId = "getCharactersModel")
	public Characters getCharacters(final String eveUserId,
			final String eveApiKey);

	@CacheFlush(modelId = "getCharactersFlushingModel")
	public boolean addCharacter(final Character character);

}