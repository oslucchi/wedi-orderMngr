package it.l_soft.orderMngr.utils;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import it.l_soft.orderMngr.rest.ApplicationProperties;
import it.l_soft.orderMngr.rest.LanguageResources;
import it.l_soft.orderMngr.rest.dbUtils.Orders;

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

	public static void sendChangeStatusEmail(String to, Orders order)
	{
		String orderRef = (order.getCustomerOrderRef() == null ? order.getOrderRef() : order.getCustomerOrderRef());

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
			message.setFrom(new InternetAddress("noreply@wedi.it"));
			// message.setFrom(new InternetAddress(ap.getMailFrom()));

			// Set To: header field of the header.
			to="antonio.masi@wedi.it";
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			String cc = "osvaldo.lucchini@wedi.it";
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
			body = body.replaceAll("à", "&agrave;");
			body = body.replaceAll("è", "&egrave;");
			body = body.replaceAll("é", "&eacute;");
			body = body.replaceAll("ì", "&igrave;");
			body = body.replaceAll("ù", "&ugrave;");
			body = body.replaceAll("ò", "&ograve;");
			
			message.setContent(body, "text/html");

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
