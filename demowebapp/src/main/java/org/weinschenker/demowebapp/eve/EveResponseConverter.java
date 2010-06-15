/**
 * 
 */
package org.weinschenker.demowebapp.eve;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.weinschenker.demowebapp.dto.Character;
import org.weinschenker.demowebapp.dto.Characters;


/**
 * @author jawe1de
 * 
 */
@Component
public class EveResponseConverter {

	private static final Logger LOGGER = Logger.getLogger(EveResponseConverter.class);

	public Characters getCharacters(final Document chars) {
		final Characters result = new Characters();
		final NodeList nodeList = getNodeList(chars, "//row");
		if (nodeList == null) {
			return result;
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Character eachNewChar = new Character();
			final Node eachNode = nodeList.item(i);
			final String eachName = getAttributeFromNode(eachNode, "@name");
			final String eachCharId = getAttributeFromNode(eachNode, "@characterID");
			final String eachCorporationName = getAttributeFromNode(eachNode, "@corporationName");
			final String eachCorporationID = getAttributeFromNode(eachNode, "@corporationID");
			eachNewChar.setName(eachName);
			eachNewChar.setCharacterID(eachCharId);
			eachNewChar.setCorporationName(eachCorporationName);
			eachNewChar.setCorporationID(eachCorporationID);
			result.getCharacterList().add(eachNewChar);
		}
		return result;
	}

	/**
	 * Get a list of nodes from the {@link Document} specified by the xPath-expresion.
	 * @param doc
	 * @param xPath
	 * @return
	 */
	private NodeList getNodeList(final Document doc, final String xPath){
		if (doc == null || "".equals(StringUtils.trimToEmpty(xPath))){
			return null;
		}
		try {
			final NodeList nodeList = XPathAPI.selectNodeList(doc, xPath);
			return nodeList;
		} catch (TransformerException e) {
			LOGGER.debug("TransformerException", e);
		}
		return null;
	}
	
	/**
	 * Get the attribute-value from this node.
	 * @param node
	 * @param xPath
	 * @return
	 */
	private String getAttributeFromNode(final Node node, final String xPath){
		if (node == null || "".equals(StringUtils.trimToEmpty(xPath))){
			return "";
		}
		try {
			XObject object = XPathAPI.eval(node, xPath);
			return object.str();
		} catch (TransformerException e) {
			LOGGER.debug("TransformerException", e);
		}
		return "";
	}
	
}
