package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.EntryPoint;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.ui.Pages;


class HomePage extends JQMPage {
	
	public HomePage() {
		add( new JQMButton("Hello") );
	}
}


public class MenuMenuMobileWeb implements EntryPoint {
	
	public void onModuleLoad() {
	
		JQMContext.changePage( Pages.PAGE_HOME );
		
		//JQMContext.changePage( Pages.PAGE_UPLOAD );
		
				
	}
}
