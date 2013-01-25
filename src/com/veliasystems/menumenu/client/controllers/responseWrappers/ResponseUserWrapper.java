package com.veliasystems.menumenu.client.controllers.responseWrappers;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.veliasystems.menumenu.client.entities.User;



public class ResponseUserWrapper implements IsSerializable {
	private User user;
	private List<Integer> errorCodes;
	
	public ResponseUserWrapper() {
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Integer> getErrorCodes() {
		return errorCodes;
	}
	public void setErrorCodes(List<Integer> errorCodes) {
		this.errorCodes = errorCodes;
	}
	
}
