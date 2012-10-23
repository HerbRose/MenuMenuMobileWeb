package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EmailServiceAsync {

	void sendEmail(List<String> toAddr, String toName, String body,
			String subject, AsyncCallback<Void> callback);

}
