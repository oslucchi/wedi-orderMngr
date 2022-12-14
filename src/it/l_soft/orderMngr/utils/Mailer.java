package it.l_soft.orderMngr.utils;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import it.l_soft.orderMngr.rest.ApplicationProperties;
import it.l_soft.orderMngr.rest.LanguageResources;
import it.l_soft.orderMngr.rest.dbUtils.Orders;

public class Mailer
{
	static final Logger log = Logger.getLogger(Mailer.class);
	static ApplicationProperties ap = ApplicationProperties.getInstance();

	public static void sendMail(String body)
	{
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", ap.getMailSmtpHost());
		props.put("mail.smtp.port", "25");

		log.debug("Connecting to mail server '" + ap.getMailSmtpHost() + "' on port 25");
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
			log.trace("Ready to send message via Transport.send");
			Transport.send(message);

			log.trace("Sent message successfully....");

		} 
		catch (MessagingException e) 
		{
			log.debug("Exception " + e.getMessage(), e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void sendChangeStatusEmail(String to, Orders order)
	{
		String orderRef = (order.getCustomerOrderRef() == null ? order.getOrderRef() : order.getCustomerOrderRef());

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", ap.getMailSmtpHost());
		props.put("mail.smtp.port", "25");
		log.debug("Connecting to mail server '" + ap.getMailSmtpHost() + "' on port 25");

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
			if ((ap.getStatusMailFrom() != null) && (ap.getStatusMailFrom().compareTo("") != 0))
			{
				message.setFrom(new InternetAddress(ap.getStatusMailFrom()));
			}

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			String cc = "ordini@wedi.it";
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));

			// Set Subject: header field
			message.setSubject("Stato lavorazione vostro ordine " + orderRef);
			
			// Send the actual HTML message, as big as you like
//			message.setContent(body, "text/html");
			String body = LanguageResources.getResource("statusChangeMail." + order.getStatus() + ".body");
			body = body.replaceAll("\\$ORDREF\\$", orderRef);
			if (order.getForwarder().compareTo("CLI") == 0)
			{
				body = body.replaceAll("\\$CLIPICK\\$", LanguageResources.getResource("statusChangeMail.CON.clipick"));
			}
			else
			{
				body = body.replaceAll("\\$CLIPICK\\$", "");
			}
			
			Calendar firstPickup = Calendar.getInstance();
			Calendar lastPickup = Calendar.getInstance();
	        firstPickup.setTime(new Date());

	        int dayShift = 0;
	        switch(firstPickup.get(Calendar.DAY_OF_WEEK))
	        {
	        case Calendar.THURSDAY:
	        	dayShift += 1;
	        case Calendar.FRIDAY:
	        	dayShift += 1;
	        case Calendar.SATURDAY:
	        	dayShift += 1;
	        case Calendar.SUNDAY:
	        	dayShift += 1;
	        case Calendar.MONDAY:
	        	dayShift += 1;
	        	firstPickup.add(Calendar.DATE, dayShift);
	        	break;

	        case Calendar.TUESDAY:
	        	dayShift += 1;
	        case Calendar.WEDNESDAY:
	        	dayShift += 1;
	        	firstPickup.add(Calendar.DATE, dayShift);
	        	break;
	        }

	        lastPickup.setTime(firstPickup.getTime());
	        switch(firstPickup.get(Calendar.DAY_OF_WEEK))
	        {
	        case Calendar.TUESDAY:
	        	lastPickup.add(Calendar.DATE, 9);
	        	break;

	        case Calendar.THURSDAY:
	        	lastPickup.add(Calendar.DATE, 11);
	        	break;
	        }

	        // convert calendar to date
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM", Locale.ITALIAN);
			body = body.replaceAll("\\$FIRSTPICKUP\\$", sdf.format(firstPickup.getTime()));
			body = body.replaceAll("\\$LASTPICKUP\\$", sdf.format(lastPickup.getTime()));
			body = body.replaceAll("??", "&agrave;");
			body = body.replaceAll("??", "&egrave;");
			body = body.replaceAll("??", "&eacute;");
			body = body.replaceAll("??", "&igrave;");
			body = body.replaceAll("??", "&ugrave;");
			body = body.replaceAll("??", "&ograve;");
			
			message.setContent(body, "text/html");

			// Send message
			log.trace("Ready to send message via Transport.send");
			Transport.send(message);

			log.trace("Sent message successfully....");
		} 
		catch (Exception e) 
		{
			log.debug("Exception " + e.getMessage(), e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
