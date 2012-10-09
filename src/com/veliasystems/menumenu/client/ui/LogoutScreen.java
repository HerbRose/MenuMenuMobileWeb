package com.veliasystems.menumenu.client.ui;

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
		
		if(Cookies.getCookie(R.loggedIn) != null){
			Cookies.removeCookie(R.loggedIn);
			Window.Location.reload();
		}
	}
	
	
}
