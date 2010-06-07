package org.weinschenker.demowebapp.persistence;

import java.util.List;

public interface PersistenceController {

	public List query(String queryString, Object... params);

}