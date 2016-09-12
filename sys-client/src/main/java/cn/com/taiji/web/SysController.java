package cn.com.taiji.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.taiji.LoginService;

@Controller
public class SysController {
	private static final Logger log = LoggerFactory
			.getLogger(SysController.class);
@Autowired
	LoginService LoginService;

	@RequestMapping(value = "aaaaa", method = RequestMethod.GET)
	@ResponseBody
	public String index(HttpSession sc) {
		log.info(" {} ", sc.getServletContext().getRealPath("/"));
		return "sys";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String a(Model model) {
	
	model.addAttribute("name", 	LoginService.getToken("admin", "12345"));
		return "a";
	}

}