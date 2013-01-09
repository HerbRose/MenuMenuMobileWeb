package com.veliasystems.menumenu.client.controllers;

import com.google.gwt.user.client.Cookies;

public class CookieController {

	private CookieController instance = null;
	
	private CookieController() { }
	
	public CookieController getInstance() {
		if(instance == null) instance = new CookieController();
		
		return instance;
	}
	
	public String getCookie(CookieNames cookieNames){
		return Cookies.getCookie(cookieNames.name());
	}
	public void setCookie(CookieNames cookieNames, String cookieValue){
		Cookies.setCookie(cookieNames.name(), cookieValue);
	}
	
}