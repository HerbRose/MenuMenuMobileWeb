package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("email")
public interface EmailService extends RemoteService {
	/**
	 * 
	 * @param toAddr - List of email's addresses
	 * @param toName - sender email address
	 * @param body - content of message
	 * @param subject - subject of messagge
	 */
	void sendEmail(List<String> toAddr, String toName, String body,
			String subject);

}
