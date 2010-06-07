package org.weinschenker.demowebapp.persistence;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.weinschenker.demowebapp.persistence.entities.Users;

@Controller
public interface BasicDao {

	public List<Users> query(String queryString, final Object... params);
	
	public List<Users> getAllUsers();
}