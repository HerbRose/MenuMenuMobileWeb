package com.veliasystems.menumenu.client.controllers;

import com.google.gwt.user.client.Cookies;

public class CookieController {

	private static CookieController instance = null;
	
	private CookieController() { }
	
	public static CookieController getInstance() {
		if(instance == null) instance = new CookieController();
		
		return instance;
	}
	
	/**
	 * return value of the cookie or empty string (<code>""</code>) if cookie is not set
	 * @param cookieNames {@link CookieNames}
	 * @return return value of the cookie or empty string (<code>""</code>) if cookie is not set
	 */
	public String getCookie(CookieNames cookieNames){
		String coockieValue = Cookies.getCookie(cookieNames.name());
		if (coockieValue == null || coockieValue.equals("null") || coockieValue.isEmpty()){ //it's weird, but it seems to be working
			coockieValue = "";
		}
		return coockieValue;
	}
	public void setCookie(CookieNames cookieNames, String cookieValue){
		Cookies.setCookie(cookieNames.name(), cookieValue);
	}
	public void clearCookie(CookieNames cookieNames){
		Cookies.removeCookie(cookieNames.name());
	}
	
}