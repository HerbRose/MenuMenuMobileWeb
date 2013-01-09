package com.veliasystems.menumenu.server;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.services.EmailService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EmailServiceImpl extends RemoteServiceServlet implements
		EmailService {

	@Override
	public void sendEmail(List<String> toAddr, String toName, String body,
			String subject) {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {

			Message msg = new MimeMessage(session);

			String fromAddr = R.EMAIL_ADDRES;
			String fromName = "MenuMenu";
			
			msg.setFrom(new InternetAddress(fromAddr, fromName));
			
			for (String address : toAddr) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(address, ""));
			}

			msg.setSubject(subject);
			msg.setText(body);

			Transport.send(msg);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
