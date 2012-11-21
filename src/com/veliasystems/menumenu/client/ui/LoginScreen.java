package com.veliasystems.menumenu.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
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
import com.veliasystems.menumenu.client.translations.Messages;
import com.veliasystems.menumenu.client.ui.myWidgets.MyPage;


public class LoginScreen extends MyPage{

//	JQMHeader header;
	private Button okButton;
	private Button cancelButton;
	
	private TextBox nameBox;
	private PasswordTextBox passwordBox;
	
	private Label nameLabel = new Label(Customization.LOGIN);
	private Label passwordLabel = new Label(Customization.PASSWORD);
	private Label wrongData;
	private LanguageCombo languageCombo = new LanguageCombo();
	
	private FlowPanel namePanel = new FlowPanel();
	private FlowPanel passwordPanel = new FlowPanel();
	private FlowPanel buttonPanel = new FlowPanel();
	
	private boolean isWrongData = false;

	
	public LoginScreen(boolean isWrongLogin){
		super();
	//	header = new JQMHeader(translated.pleaseLogin());
	//	header.setFixed(true);
	//	header.setText(Customization.MAINTITLE);
	//	add(header);
	    
		nameBox = new TextBox();

		passwordBox = new PasswordTextBox();
		
		
		
	    okButton = new Button(Customization.OK);
	    
	    okButton.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(nameBox.getValue().equals("") || passwordBox.getValue().equals("")){
					Window.alert(Customization.LOGIN_ERROR);
				}else{
					Cookies.setCookie(R.LOGIN, nameBox.getValue());
					JQMContext.changePage(new LoadDataScreen(nameBox.getValue(), passwordBox.getValue()));
				}
			}
		});
	    cancelButton = new Button(Customization.CANCEL);
	    
	    
	    if(isWrongLogin){
	    	nameBox.addStyleName("redShadow");
	    	passwordBox.addStyleName("redShadow");
	    	wrongData = new Label(Customization.WRONG_LOGIN_DATA);
	    	add(wrongData);
	    	wrongData.addStyleName("warning");
	    	isWrongData = true;
	    }
	    
	    namePanel.setStyleName("namePanel", true);
	    passwordPanel.setStyleName("passwordPanel", true);
	    
	    nameLabel.setStyleName("myLabel nameLabel", true);
	    nameBox.setStyleName("myTextBox nameBox", true);
	    
	    passwordLabel.setStyleName("myLabel passwordLabel", true);
	    passwordBox.setStyleName("myTextBox passwordBox", true);
	    
	    namePanel.add(nameLabel);
	    namePanel.add(nameBox);
	    
	    passwordPanel.add(passwordLabel);
	    passwordPanel.add(passwordBox);
	    
	    buttonPanel.add(okButton);
	    buttonPanel.add(cancelButton);
	    buttonPanel.setWidth("100%");
	    
	    getContentPanel().add(namePanel);
	    getContentPanel().add(passwordPanel);
	    getContentPanel().add(languageCombo);
	    getContentPanel().add(buttonPanel);
	    
	}

	@Override
	protected void onPageShow() {
		
		String login = Cookies.getCookie(R.LOGIN);
		if (login != null && !login.equals("null")) {
			nameBox.setValue(Cookies.getCookie(R.LOGIN));
		}
		else{
			nameBox.setValue("");
		}
		
		passwordBox.setValue("");
	
		MenuMenuMobileWeb.loggedIn = false;
		Cookies.removeCookie(R.LOGGED_IN);
		
		Document.get().getElementById("load").setClassName(R.LOADED);
		
		if(!isWrongData){
			nameBox.removeStyleName("redShadow");
	    	passwordBox.removeStyleName("redShadow");	
		}
		super.onPageShow();
	}
	
}
