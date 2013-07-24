package br.com.fisiovita.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.fisiovita.mail.MailUtil;
import br.com.fisiovita.model.Contato;
import br.com.fisiovita.repository.ContatoRepository;
import br.com.fisiovita.util.Constantes;
import br.com.fisiovita.validator.ContatoValidator;

@Controller
public class ContactController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ContatoValidator contatoValidator;
	
	@Autowired
	private ContatoRepository contatoRepository;

	@RequestMapping(value = "/enviarEmail", method = RequestMethod.POST)
	@ResponseBody
	public String enviarEmail(@Valid Contato contato, BindingResult bindingResult) throws Exception {
		String mensagem = "";
		contatoValidator.validate(contato, bindingResult);
		if (bindingResult.getErrorCount() == 0) {
			contatoRepository.put(contato);
			MailUtil.getIntance().enviaEmailContato(contato, messageSource);
			mensagem = messageSource.getMessage("mail.contato.email.enviado.sucesso", null, Constantes.LOCALE_PTBR);
		} else {
			int i =1;
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				mensagem += fieldError.getDefaultMessage();
				if (i < bindingResult.getErrorCount()) {
					mensagem += "<br/>";
				}
				i++;
			}
		}
		return mensagem;
	}

}
