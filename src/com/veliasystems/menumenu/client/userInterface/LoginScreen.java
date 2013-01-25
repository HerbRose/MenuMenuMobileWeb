package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.MenuMenuMobileWeb;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListItem;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;


public class LoginScreen extends MyPage{

//	JQMHeader header;
	private MyButton cancelButton;
	private MyButton okButton;
	
	private TextBox nameBox;
	private PasswordTextBox passwordBox;
	
	private Label nameLabel = new Label(Customization.LOGIN);
	private Label passwordLabel = new Label(Customization.PASSWORD);
	private Label wrongData;
	private MyListCombo languageCombo = new MyListCombo(true);
	
	private MyRestaurantInfoPanel namePanel = new MyRestaurantInfoPanel();
	private MyRestaurantInfoPanel passwordPanel = new MyRestaurantInfoPanel();
	private FlowPanel buttonPanel = new FlowPanel();
	private FlowPanel logoPanel = new FlowPanel();
	
	private FlowPanel footer = new FlowPanel();
	private FocusPanel polishFlag;
	private FocusPanel englishFlag;
	private FocusPanel frenchFlag;
	
	private boolean isWrongData = false;

	
	public LoginScreen(boolean isWrongLogin){
		super();
				
		nameBox = new TextBox();
		passwordBox = new PasswordTextBox();

	    okButton = new MyButton(Customization.OK);
	    okButton.setBackGroundImage("img/layout/okButton.png", true, false, "#919191");
	    okButton.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(nameBox.getValue().equals("") || passwordBox.getValue().equals("")){
					PagesController.MY_POP_UP.showError(new Label(Customization.LOGIN_ERROR), new IMyAnswer() {
						
				    	@Override
						public void answer(Boolean answer) {
						}
					});
				}else{
					Cookies.setCookie(R.LOGIN, nameBox.getValue());
					JQMContext.changePage(new LoadDataScreen(nameBox.getValue(), passwordBox.getValue()));
				}
			}
		});
	    cancelButton = new MyButton(Customization.CANCEL);
//	    cancelButton.getElement().getStyle().setProperty("margin","39px");
	    cancelButton.setBackGroundImage("img/layout/anulujButton.png", true, false, "#919191");
	    
	    
	    if(isWrongLogin){
	    	nameBox.addStyleName("redShadow");
	    	passwordBox.addStyleName("redShadow");
	    	wrongData = new Label(Customization.WRONG_LOGIN_DATA);
	    	add(wrongData);
	    	wrongData.addStyleName("warning");
	    	isWrongData = true;
	    }
	    
	    buttonPanel.getElement().setAttribute("style", "text-align:center");
	    buttonPanel.setStyleName("loginButtonPanel", true);
	    
	    namePanel.setStyleName("containerPanelLoginScreen", true);
	    passwordPanel.setStyleName("containerPanelLoginScreen", true);
	    
	    nameLabel.setStyleName("nameLabel", true);
	    nameBox.setStyleName("myTextBox nameBox", true);
	    
	    passwordLabel.setStyleName("passwordLabel", true);
	    passwordBox.setStyleName("myTextBox passwordBox", true);
	    
	    namePanel.addItem(nameLabel, nameBox);
	    
	    passwordPanel.addItem(passwordLabel , passwordBox);
	    
	    buttonPanel.add(cancelButton);
	    buttonPanel.add(okButton);
	    buttonPanel.setWidth("100%");
	    
	    logoPanel.add(new Image("img/layout/menuMenuLogo.png"));
	    logoPanel.setStyleName("logoPanel", true);
	    
	    languageCombo.setStyleName("languageCombo", true);
	    MyListItem listItem = languageCombo.getNewItem(R.ENGLISH_NAME);
	    listItem.setValue(R.ENGLISH_CODE);
	    listItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Cookies.setCookie(R.LANGUAGE, R.ENGLISH_CODE);
	        	changeLanguage(Cookies.getCookie(R.LANGUAGE));
			}
		});
	    languageCombo.addListItem(listItem, 0);
	    
	    listItem = languageCombo.getNewItem(R.FRENCH_NAME);
	    listItem.setValue(R.FRENCH_CODE);
	    listItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Cookies.setCookie(R.LANGUAGE, R.FRENCH_CODE);
	        	changeLanguage(Cookies.getCookie(R.LANGUAGE));
			}
		});
	    languageCombo.addListItem(listItem, 1);
	    
	    listItem = languageCombo.getNewItem(R.POLISH_NAME);
	    listItem.setValue(R.POLISH_CODE);
	    listItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Cookies.setCookie(R.LANGUAGE, R.POLISH_CODE);
	        	changeLanguage(Cookies.getCookie(R.LANGUAGE));
			}
		});
	    languageCombo.addListItem(listItem, 2);
	    
	    String language = getCurrentLocale();
	    
	    if(language.equals(R.FRENCH_CODE)) languageCombo.selectItem(1);
	    else if(language.equals(R.POLISH_CODE)) languageCombo.selectItem(2);
	    else languageCombo.selectItem(0);
	    
	    
	    getContentPanel().add(namePanel);
	    getContentPanel().add(passwordPanel);
	    //getContentPanel().add(languageCombo);
	    getContentPanel().add(buttonPanel);
	    getHeader().addImageHeader(logoPanel);
	  
	}
	
	
	private void setFooterWithFlags(){
		footer.addStyleName("footerWithFlags");
		
		polishFlag = new FocusPanel();
		frenchFlag = new FocusPanel();
		englishFlag = new FocusPanel();
		
		polishFlag.setStyleName("noFocus pointer polishFlag");
		frenchFlag.setStyleName("noFocus pointer frenchFlag");
		englishFlag.setStyleName("noFocus pointer englishFlag");
		
		Image plImage = new Image("img/layout/pl.png");
		Image frImage = new Image("img/layout/fr.png");
		Image enImage = new Image("img/layout/gb.png");
		
		plImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Cookies.setCookie(R.LANGUAGE, R.POLISH_CODE);
	        	changeLanguage(Cookies.getCookie(R.LANGUAGE));
			}
		});
		
		frImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Cookies.setCookie(R.LANGUAGE, R.FRENCH_CODE);
	        	changeLanguage(Cookies.getCookie(R.LANGUAGE));
			}
		});
		
		enImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Cookies.setCookie(R.LANGUAGE, R.ENGLISH_CODE);
	        	changeLanguage(Cookies.getCookie(R.LANGUAGE));
			}
		});
		
		polishFlag.add(plImage);
		frenchFlag.add(frImage);
		englishFlag.add(enImage);
		
		
		footer.add(polishFlag);
		footer.add(frenchFlag);
		footer.add(englishFlag);
		
		
		getContentPanel().add(footer);
	}

	
	
	@Override
	protected void onPageShow() {
		namePanel.setWidth( JS.getElementOffsetWidth(getElement())-40 );
		passwordPanel.setWidth( JS.getElementOffsetWidth(getElement())-40 );
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
		if(footer.getWidgetCount() == 0){
			setFooterWithFlags();
		}
		
		super.onPageShow();
	}
	

	public String getCurrentLocale(){	
	    String ret = Window.Location.getParameter("locale");
	    if (ret==null || ret.isEmpty()) ret = R.ENGLISH_CODE; // default
	    return ret;
	}
	
    private native void changeLanguage( String locale )
    /*-{
	    var currLocation = $wnd.location.toString().split("#");
	    currLocation = currLocation[0].split("?");
	    var currLocale = "";
	    if ( ! (locale == "EN" || locale == "en") )
	    {	    	
	      currLocale = "?locale=" + locale;
	     
	    }
	    var tmp = currLocation[0]+= currLocale ;
	    $wnd.location.replace(tmp);
	    console.log(locale);
     }-*/;
	
}
