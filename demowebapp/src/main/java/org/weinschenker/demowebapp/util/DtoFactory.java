/**
 * 
 */
package org.weinschenker.demowebapp.util;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.weinschenker.demowebapp.dto.Character;
import org.weinschenker.demowebapp.dto.Characters;
import org.weinschenker.demowebapp.dto.ObjectFactory;
import org.weinschenker.demowebapp.eve.EveResponseConverter;

/**
 * @author jawe1de
 *
 */
@Component
public class DtoFactory {
	
	private static final Logger LOGGER = Logger.getLogger(EveResponseConverter.class);

	private ObjectFactory objectFactory = null;

	@PostConstruct
	public void init(){
		try {
			objectFactory = new ObjectFactory();
		} catch (JAXBException e) {
			LOGGER.error("Could not create objectFactory", e);
		}
	}
	
	public Character createCharacter(){
		try {
			return objectFactory.createCharacter();
		} catch (JAXBException e) {
			LOGGER.error("Could not create Character-instance", e);
			return null;
		}
	}
	public Characters createCharacters(){
		try {
			return objectFactory.createCharacters();
		} catch (JAXBException e) {
			LOGGER.error("Could not create Characters-instance", e);
			return null;
		}
	}

}
