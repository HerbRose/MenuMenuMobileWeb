package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.ui.Pages;
import com.veliasystems.menumenu.client.ui.UploadRestaurantsScreen;





public class MenuMenuMobileWeb implements EntryPoint {

	public static boolean loggedIn = false;
	
	public void onModuleLoad() {
	
		//JQMContext.changePage( new UploadRestaurantsScreen());

		
		String logged = Cookies.getCookie(R.loggedIn);
		if (logged!=null) loggedIn = true;
		
		
		if (loggedIn) {
			JQMContext.changePage( Pages.PAGE_HOME );
		} else {
			JQMContext.changePage( Pages.PAGE_LOGIN );
		}
		
		//JQMContext.changePage( Pages.PAGE_UPLOAD );
		
				
	}
}
