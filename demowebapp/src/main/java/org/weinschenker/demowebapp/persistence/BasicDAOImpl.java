/**
 * 
 */
package org.weinschenker.demowebapp.persistence;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.weinschenker.demowebapp.persistence.entities.Users;

/**
 * @author jawe1de
 * 
 */
public class BasicDAOImpl extends HibernateDaoSupport implements BasicDao {

	@PersistenceContext(unitName = "eve")
	private EntityManager entityManager;

	@Resource
	private SessionFactory sessionFactory;

	@PostConstruct
	public void init() {
		setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.weinschenker.demowebapp.persistence.BasicDao#setEntityManager(javax
	 * .persistence.EntityManager)
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.weinschenker.demowebapp.persistence.BasicDao#query(java.lang.String,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public List<Users> query(String queryString, final Object... params) {
		final Query query = entityManager.createQuery(queryString);
		query.setParameter("param", "true");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Users> getAllUsers() {
		final Query q = entityManager.createNamedQuery("users.all");
		return (List<Users>) q.getResultList();
	}
}