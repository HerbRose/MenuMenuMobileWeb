package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.R;

public class LogoutScreen extends JQMPage{

	
	
	
	public LogoutScreen(){
		
		
	}
	
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		super.onPageShow();
		
		if(Cookies.getCookie(R.LOGGED_IN) != null){
			Cookies.removeCookie(R.LOGGED_IN);
			Window.Location.reload();
		}
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	
}
