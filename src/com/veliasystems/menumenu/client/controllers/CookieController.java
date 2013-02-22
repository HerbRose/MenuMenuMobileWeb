package com.veliasystems.menumenu.client.controllers;

import java.util.Date;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Label;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;

public class CookieController {

	private static CookieController instance = null;
	
	private CookieController() { 
		
		if(!isCookieEnabled()){
			
			Label warningLabel = new Label(Customization.NO_COOKIE_SUPPORT_WARNING);
			
			PagesController.MY_POP_UP.showWarning(warningLabel , new IMyAnswer() {
				
				@Override
				public void answer(Boolean answer) {
					// TODO Auto-generated method stub
					
				}
			} );
		}
		
	}
	
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
	public void setCookie(CookieNames cookieNames, String cookieValue, long expiryTime){
		Cookies.setCookie(cookieNames.name(), cookieValue, new Date(expiryTime));
	}
	public void clearCookie(CookieNames cookieNames){
		Cookies.removeCookie(cookieNames.name());
	}
	public boolean isCookieEnabled(){
		return Cookies.isCookieEnabled();
	}
	
}