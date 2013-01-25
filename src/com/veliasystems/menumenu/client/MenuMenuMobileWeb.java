package com.veliasystems.menumenu.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootPanel;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.controllers.CookieController;
import com.veliasystems.menumenu.client.controllers.CookieNames;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.userInterface.LoadDataScreen;
import com.veliasystems.menumenu.client.userInterface.NewUserPage;
import com.veliasystems.menumenu.client.userInterface.Pages;
import com.veliasystems.menumenu.client.userInterface.WelcomeMobilePage;


public class MenuMenuMobileWeb implements EntryPoint {

	public static boolean loggedIn = false;
	private String osType = R.USER_AGENT;
	

	private CookieController cookieController = CookieController.getInstance();
	
	public void onModuleLoad() {
		
		String newUser = cookieController.getCookie(CookieNames.NEW_USER_EMAIL);
		if(newUser != null && !newUser.equals("null")){ //it's weird, but it seems to be working
			JQMContext.changePage(new NewUserPage());
			return;
		}
		
		String logged = Cookies.getCookie(R.LOGGED_IN);
		RootPanel.get().insert(PagesController.MY_POP_UP, 0);
		final boolean isOSMobile = osType.toLowerCase().indexOf("ipad") >= 0
				|| osType.toLowerCase().indexOf("iphone") >= 0;
		final boolean isAndroid = osType.toLowerCase().indexOf("android") >= 0;
		
		if (logged != null && !logged.equals("null")) loggedIn = true; //it's weird, but it seems to be working
		
		if (loggedIn) {
			if(isMobile()) JQMContext.changePage( new LoadDataScreen(logged) );
			else {
				R.isMobile = false;
				JQMContext.changePage( new LoadDataScreen(logged) );
			}
		} else {
			if(isOSMobile || isAndroid) {
				JQMContext.changePage( new WelcomeMobilePage());
			}
			else {
				R.isMobile = false;
				JQMContext.changePage( Pages.PAGE_LOGIN_OK );
			}
		}
		
		
		
		PagesController.hideWaitPanel();
		
		
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
    		    	//return (isMobile.BlackBerry()|| isMobile.Opera() || isMobile.Windows())
    		        //return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    		        return false;
    		    }
    	};

    	return isMobile.any();
    	
	}-*/;
}
