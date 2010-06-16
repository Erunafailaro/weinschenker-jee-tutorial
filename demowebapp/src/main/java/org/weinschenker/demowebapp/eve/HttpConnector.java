package org.weinschenker.demowebapp.eve;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Controller
public class HttpConnector {

	private static final Logger LOGGER = Logger.getLogger(HttpConnector.class);

	@Resource
	private String eveApiHost;
	@Resource
	private String eveApiChars;

	public Document getCharacters(final String eveUserId, final String eveApiKey) {
		final Map<String, String> params = createParameterMap(eveUserId,
				eveApiKey);
		final Document response = getDocument(eveApiChars, params);
		return response;
	}

	/**
	 * Send query to EVE and return response as String. If anything fails,
	 * return empty String.
	 * 
	 * @param apiPath
	 * @param params
	 * @return
	 */
	private Document getDocument(final String apiPath,
			final Map<String, String> params) {
		final URI uri = getUri(eveApiHost, apiPath, params);
		if (uri == null) {
			return null;
		}
		final HttpClient client = new HttpClient();
		final GetMethod get = new GetMethod(uri.toString());
		try {
			final int resultcode = client.executeMethod(get);
			LOGGER.debug("executed " + uri.toString() + " - result: " +
					resultcode);
			if (resultcode != HttpStatus.SC_OK) {
				return null;
			}
			final BufferedInputStream bis = new BufferedInputStream(get
					.getResponseBodyAsStream());
			return getDocumentFromStream(bis);
		} catch (IOException e) {
			LOGGER.debug("IOException", e);
		} finally {
			get.releaseConnection();
		}
		return null;
	}

	/**
	 * Construct the URI for the request to the eve-api.
	 * 
	 * @param host
	 * @param path
	 * @param params
	 * @return
	 */
	private URI getUri(final String host, final String path,
			final Map<String, String> params) {
		try {
			final StringBuffer uriString = new StringBuffer();
			uriString.append(host.trim());
			uriString.append(path.trim());
			String delim = "?";
			for (final Iterator<String> i = params.keySet().iterator(); i
					.hasNext();) {
				final String eachKey = i.next();
				uriString.append(delim + eachKey + "=");
				uriString.append(params.get(eachKey));
				delim = "&";
			}
			final URI uri = new URI(uriString.toString());
			return uri;
		} catch (URISyntaxException e) {
			LOGGER.error("Could not create URI", e);
		}
		return null;
	}

	/**
	 * Create a basic map containing userId and apiKey.
	 * 
	 * @return
	 */
	private Map<String, String> createParameterMap(final String eveUserId,
			final String eveApiKey) {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("userID", eveUserId);
		params.put("apiKey", eveApiKey);
		return params;
	}

	/**
	 * Parse {@link Document} from {@link Reader}.
	 * 
	 * @param reader
	 * @return
	 */
	private Document getDocumentFromStream(final InputStream inputStream) {
		final DocumentBuilderFactory fact = DocumentBuilderFactory
				.newInstance();
		fact.setValidating(false);
		try {
			final DocumentBuilder db = fact.newDocumentBuilder();
			db.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(final String publicId,
						final String systemId) {
					ByteArrayInputStream bais = new ByteArrayInputStream(""
							.getBytes());
					LOGGER.debug("resolveEntity:" + publicId + "|"
							+ systemId);
					return new InputSource(bais);
				}
			});
			final InputSource is = new InputSource(inputStream);
			final Document document = db.parse(is);
			return document;
		} catch (ParserConfigurationException e) {
			LOGGER.debug("ParserConfigurationException", e);
		} catch (SAXException e) {
			LOGGER.debug("SAXException", e);
		} catch (IOException e) {
			LOGGER.debug("IOException", e);
		}
		return null;
	}
}
