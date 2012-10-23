package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("email")
public interface EmailService extends RemoteService {

	void sendEmail(List<String> toAddr, String toName, String body,
			String subject);

}
