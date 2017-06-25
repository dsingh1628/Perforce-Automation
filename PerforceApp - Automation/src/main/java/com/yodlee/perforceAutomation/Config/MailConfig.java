package com.yodlee.perforceAutomation.Config;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class MailConfig {

	private static Properties props = new Properties();
	static {

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "#host name");
		props.put("mail.smtp.port", "#portNumber");

	}

	public static String recipient = "#recipient mail ids";

	public static Session CreateSession() {
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("#username", "#password");
			}
		});
		return session;

	}

}
