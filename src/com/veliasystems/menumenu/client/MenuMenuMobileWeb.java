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
		//System.out.println(loggedIn + logged);
		if (logged != null){ loggedIn = true;}
		
		//System.out.println(loggedIn);
		if (loggedIn) {
			//System.out.println("page nie login");
			JQMContext.changePage( new LoadDataScreen(logged) );
		} else {
			//System.out.println("page login");
			JQMContext.changePage( Pages.PAGE_LOGIN );
		}
	
		
	}
	
	
}
