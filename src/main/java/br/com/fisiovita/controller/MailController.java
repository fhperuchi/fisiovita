package br.com.fisiovita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.fisiovita.bean.Contato;
import br.com.fisiovita.mail.MailUtil;

@Controller
public class MailController {
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value="/enviarEmail")
	public String enviarEmail(Contato contato) throws Exception {
		MailUtil.getIntance().enviaEmailContato(contato, messageSource);
		return "contato";
	}
	
}
