package com.veliasystems.menumenu.client.controllers;

public class CookieController {

	private CookieController instance = null;
	
	private CookieController() { }
	
	public CookieController getInstance() {
		if(instance == null) instance = new CookieController();
		
		return instance;
	}
	
}
