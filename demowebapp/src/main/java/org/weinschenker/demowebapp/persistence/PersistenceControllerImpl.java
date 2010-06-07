/**
 * 
 */
package org.weinschenker.demowebapp.persistence;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jawe1de
 * 
 */
@Controller
@Transactional
public class PersistenceControllerImpl implements PersistenceController {
	private BasicDao basicDAO;

	/* (non-Javadoc)
	 * @see org.weinschenker.demowebapp.persistence.PersistenceController#query(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public List query(String queryString, Object... params) {
		return basicDAO.query(queryString, params);
	}

	public void setBasicDAO(BasicDao basicDAO) {
		this.basicDAO = basicDAO;
	}

}
