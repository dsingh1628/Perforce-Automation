package com.yodlee.perforceAutomation.utilities;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.yodlee.perforceAutomation.Config.MailConfig;

public class MailUtility {

	public static void SendMail(String subject, String body) {

		System.out.println("inside send mail");
		Session session = MailConfig.CreateSession();
		String toMail = MailConfig.recipient;
		body = "<html><head></head><body>" + body + "</body></html>";
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress("#sender's mail id", "Perforce-Automation"));
			msg.setSubject(subject, "text/html; charset=utf-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail, false));
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html");

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is attachment
			messageBodyPart = new MimeBodyPart();
			String filename = "output.xlsx";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("perforce-automation-report.xlsx");
			multipart.addBodyPart(messageBodyPart);

			System.out.println("inside send mail22");
			// Send the complete message parts
			msg.setContent(multipart, "text/html");

			// Send message
			System.out.println("sending");
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with attachment!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
