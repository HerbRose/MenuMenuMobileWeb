package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.ui.views.HomePageScreen;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MenuMenuMobileWeb implements EntryPoint {
	
	
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	
	public void onModuleLoad() {
	
		HomePageScreen homePage = new HomePageScreen();
		
		JQMContext.changePage(homePage);
		
	}
}
