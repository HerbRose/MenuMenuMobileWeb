package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CookieController;
import com.veliasystems.menumenu.client.controllers.CookieNames;

public class LogoutScreen extends JQMPage{

	
	
	private CookieController cookieController = CookieController.getInstance();
	public LogoutScreen(){
		
		
	}
	
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		super.onPageShow();
		cookieController.clearCookie(CookieNames.LOGGED_IN);
		Window.Location.reload();
		
		//Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	
}
