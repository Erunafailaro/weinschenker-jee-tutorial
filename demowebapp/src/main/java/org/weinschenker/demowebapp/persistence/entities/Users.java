package org.weinschenker.demowebapp.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "EVEONLINE.TABLE_USERS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "users.oneuser", query = "select u from Users u where u.name = :name and u.password = :password", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "users.all", query = "select u from Users u", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }) })
public class Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3609454439197102515L;

	protected Long id;
	protected String name;
	protected String password;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pasword
	 */
	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	/**
	 * @param pasword
	 *            the pasword to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
