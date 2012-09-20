package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.ui.HomePageScreen;


public class MenuMenuMobileWeb implements EntryPoint {
	
	public void onModuleLoad() {
	
		HomePageScreen homePage = new HomePageScreen();
		
		JQMContext.changePage(homePage);
		
	}
}
