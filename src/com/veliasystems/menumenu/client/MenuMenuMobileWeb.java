package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.ui.LoadDataScreen;
import com.veliasystems.menumenu.client.ui.Pages;

public class MenuMenuMobileWeb implements EntryPoint {

	public static boolean loggedIn = false;
	
	
	public void onModuleLoad() {
		
		String logged = Cookies.getCookie(R.LOGGED_IN);
		
		if (logged != null && !logged.equals("null")) loggedIn = true; //it's weird, but it seems to be working
		
		if (loggedIn) {
			JQMContext.changePage( new LoadDataScreen(logged) );
		} else {
			JQMContext.changePage( Pages.PAGE_LOGIN_OK );
		}
	
		
	}
	
	
}
