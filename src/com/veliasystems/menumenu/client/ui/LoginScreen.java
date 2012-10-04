package com.veliasystems.menumenu.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.elements.JQMPassword;
import com.sksamuel.jqm4gwt.form.elements.JQMText;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.MenuMenuMobileWeb;
import com.veliasystems.menumenu.client.R;

public class LoginScreen extends JQMPage{

	JQMHeader header;
	JQMFooter footer;
	JQMButton okButton;
	JQMButton cancelButton;
	JQMText nameBox;
	JQMPassword passwordBox;
	
	
	public LoginScreen(){
		header = new JQMHeader("Please login");
		header.setFixed(true);
		header.setText(Customization.MAINTITLE);
		add(header);
	    
		nameBox = new JQMText("name", "Login:");
		add(nameBox);
		
		passwordBox = new JQMPassword("passwd", "Password:");
		add(passwordBox);
		
		HorizontalPanel hp = new HorizontalPanel();
	    okButton = new JQMButton("OK");
	    
	    okButton.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				System.out.println(nameBox.getTitle());
				String text = nameBox.getValue();
				if (text.equalsIgnoreCase("agnieszka")) {
					MenuMenuMobileWeb.loggedIn = true;
					Cookies.setCookie(R.loggedIn, "YES");
					JQMContext.changePage(Pages.PAGE_HOME);
				}
				else Window.alert("Wrong username or password.");
			}
		});
	    
	    hp.add(okButton);
	    cancelButton = new JQMButton("Cancel");
	    hp.add(cancelButton);
	    hp.setWidth("100%");
	    
	    add(hp);
	    
	}

	@Override
	protected void onPageShow() {
		nameBox.setValue("");
		passwordBox.setValue("");
		
		MenuMenuMobileWeb.loggedIn = false;
		Cookies.removeCookie(R.loggedIn);
		
		super.onPageShow();
	}
	
}
