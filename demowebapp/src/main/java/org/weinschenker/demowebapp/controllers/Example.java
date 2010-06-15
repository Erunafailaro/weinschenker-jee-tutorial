package org.weinschenker.demowebapp.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.weinschenker.demowebapp.eve.EveServiceImpl;
import org.weinschenker.demowebapp.persistence.BasicDao;
import org.weinschenker.demowebapp.persistence.entities.Users;

@Controller
public class Example {

	private static final Logger LOGGER = Logger.getLogger(Example.class);

	@Resource
	private EveServiceImpl eveServiceImpl;
	@Resource
	private String eveUserId;
	@Resource
	private BasicDao basicDao;
	@Resource
	private String eveApiKey;

	@RequestMapping("/index")
	public String show(@RequestParam(value = "who", required = false) final String who,
			final HttpServletRequest request, final HttpServletResponse response, final Model model) throws Exception {
		model.addAttribute("who", who);
		model.addAttribute("chars", eveServiceImpl.getCharacters(eveUserId, eveApiKey).getCharacterList());
		final List<Users> users = basicDao.getAllUsers();
		model.addAttribute("users", users);

		return "main";
	}

	@PostConstruct
	public void init() {
		LOGGER.debug("Created controller Example");
	}

}
