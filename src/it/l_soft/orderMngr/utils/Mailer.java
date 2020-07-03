package it.l_soft.orderMngr.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import it.l_soft.orderMngr.rest.ApplicationProperties;

public class Mailer
{
	static ApplicationProperties ap = ApplicationProperties.getInstance();

	public static void sendMail(String body)
	{
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", ap.getMailSmtpHost());
		props.put("mail.smtp.port", "25");

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ap.getMailUser(), ap.getMailPassword());
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(ap.getMailFrom()));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(ap.getMailTo()));
			message.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse(ap.getMailCC()));

			// Set Subject: header field
			message.setSubject("Prese wedi Italia");

			// Send the actual HTML message, as big as you like
			message.setContent(body, "text/html");

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
