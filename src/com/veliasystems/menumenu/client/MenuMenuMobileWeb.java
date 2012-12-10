package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.ui.LoadDataScreen;
import com.veliasystems.menumenu.client.ui.Pages;
//import com.veliasystems.menumenu.client.userInterface.LoadDataScreen;
//import com.veliasystems.menumenu.client.userInterface.Pages;

public class MenuMenuMobileWeb implements EntryPoint {

	public static boolean loggedIn = false;
	
	
	public void onModuleLoad() {
		
		String logged = Cookies.getCookie(R.LOGGED_IN);
		
		if (logged != null && !logged.equals("null")) loggedIn = true; //it's weird, but it seems to be working
		
		if (loggedIn) {
			if(isMobile()) JQMContext.changePage( new LoadDataScreen(logged) );
			else {
				R.isMobile = false;
				JQMContext.changePage( new com.veliasystems.menumenu.client.userInterface.LoadDataScreen(logged) );
			}
		} else {
			if(isMobile()) JQMContext.changePage( Pages.PAGE_LOGIN_OK );
			else {
				R.isMobile = false;
				JQMContext.changePage( new com.veliasystems.menumenu.client.userInterface.LoginScreen(false) );
			}
		}
	
		
	}
	
	private native boolean isMobile()/*-{
		var isMobile = {
    		    Android: function() {
    		        return navigator.userAgent.indexOf("Android") >0 ;
    		    },
    		    BlackBerry: function() {
    		        return navigator.userAgent.indexOf("BlackBerry") >0 ;
    		    },
    		    iOS: function() {
    		        return navigator.userAgent.indexOf("iPhone") > 0 || navigator.userAgent.indexOf("iPod") > 0;  
    		    },
    		    Opera: function() {
    		        return navigator.userAgent.indexOf("Opera Mini") >0;
    		    },
    		    Windows: function() {
    		        return navigator.userAgent.indexOf("IEMobile") >0;
    		    },
    		    any: function() {
    		    	return (isMobile.BlackBerry()|| isMobile.Opera() || isMobile.Windows())
    		        //return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    		    }
    	};

    	return isMobile.any();
	}-*/;
}
