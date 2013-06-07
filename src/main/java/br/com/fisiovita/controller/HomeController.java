package br.com.fisiovita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping(value="/")
	public String home() {
		return "empresa";
	}
	
	@RequestMapping(value = "/home")
	public String printWelcome() {
		return this.home();
	}

}
