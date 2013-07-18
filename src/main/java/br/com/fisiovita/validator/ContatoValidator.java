package br.com.fisiovita.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.fisiovita.model.Contato;
import br.com.fisiovita.util.Constantes;

@Component
public class ContatoValidator implements Validator {

	@Autowired
	private MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		return Contato.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		Contato contato = (Contato) object;
		String mensagem = null;
		if (contato.getNome().trim().equals("")) {
			String[] args = {"Nome"};
			mensagem = messageSource.getMessage("campo.obrigatorio", args, Constantes.LOCALE_PTBR);
			errors.rejectValue("nome", "nomeObrigatorio", mensagem);
		}
		if (contato.getEmail().trim().equals("")) {
			String[] args = {"E-mail"};
			mensagem = messageSource.getMessage("campo.obrigatorio", args, Constantes.LOCALE_PTBR);
			errors.rejectValue("email", "emailObrigatorio", mensagem);
		} else if (!contato.getEmail().trim().matches(Constantes.REGEX_EMAIL)) {
			mensagem = messageSource.getMessage("mail.contato.email.invalido", null, Constantes.LOCALE_PTBR);
			errors.rejectValue("email", "emailInvalido", mensagem);
		}
	}

}
