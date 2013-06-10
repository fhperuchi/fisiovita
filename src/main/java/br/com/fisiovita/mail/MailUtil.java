package br.com.fisiovita.mail;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.springframework.context.MessageSource;

import br.com.fisiovita.bean.Contato;
import br.com.fisiovita.util.Constantes;

public final class MailUtil {

	private static final Logger LOGGER = Logger.getLogger(MailUtil.class.getName());

	private static MailUtil mailUtil;

	public static synchronized MailUtil getIntance() {
		if (mailUtil == null) {
			mailUtil = new MailUtil();
		}
		return mailUtil;
	}

	/**
	 * Monta e envia o email de lembrar minha senha, recebe o usuario e o messageSource.
	 * 
	 * @param Contato
	 * @param messageSource
	 * @return A função retorna o texto "SUCCESS" ou uma mensagem de erro
	 * @throws Exception
	 */
	public String enviaEmailContato(Contato contato, MessageSource messageSource) throws Exception {
		try {
			String[] campoInteresse = contato.getInteresse().split(";");
			String interesse = campoInteresse[0];
			String emailFisioVita = campoInteresse[1];

			Message messageFisioVita = new MimeMessage(Session.getDefaultInstance(new Properties()));
			messageFisioVita.setFrom(new InternetAddress(
					messageSource.getMessage("mail.email.remetente", null, Constantes.LOCALE_PTBR), 
					messageSource.getMessage("mail.nome.remetente", null, Constantes.LOCALE_PTBR)));

			if (emailFisioVita.indexOf(",") < 0) {
				messageFisioVita.addRecipient(Message.RecipientType.TO, new InternetAddress(emailFisioVita));
			} else {
				String[] emails = emailFisioVita.split(",");
				for (String email : emails) {
					messageFisioVita.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
				}
			}

			InternetAddress[] replyTo = { new InternetAddress(contato.getEmail(), contato.getNome()) };

			messageFisioVita.setReplyTo(replyTo);
			
			messageFisioVita.setSubject(MimeUtility.encodeText(messageSource.getMessage("mail.contato.assunto", null, Constantes.LOCALE_PTBR),
					Constantes.UTF_8, "B"));
			Multipart multiPart = new MimeMultipart();

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			String[] args = { interesse, contato.getNome(), contato.getEmail(), contato.getTelefone(), contato.getMensagem() };
			mimeBodyPart.setContent(messageSource.getMessage("mail.contato.corpo", args, Constantes.LOCALE_PTBR), "text/html");
			multiPart.addBodyPart(mimeBodyPart);

			messageFisioVita.setContent(multiPart);

			Transport.send(messageFisioVita);
			return "SUCCESS";
		} catch (Exception e) {
			LOGGER.severe("Erro ao enviar email: " + e.getMessage());
			throw e;
		}

	}

}
