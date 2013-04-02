package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

public class WelcomeMobilePage extends MyPage{

	private String osType = R.USER_AGENT;
	private FlowPanel logoPanel = new FlowPanel();
	
	private FlowPanel mobileNotification = new FlowPanel();
	private FlowPanel noThanksWrapper = new FlowPanel();
	private FlowPanel getAppWrapper = new FlowPanel();
	
	
	
	private FocusPanel noThanksPanel = new FocusPanel();
	private FocusPanel getApp = new FocusPanel();
	
	private Label messageMobile = new Label(Customization.GET_MOBILE_APP);
	
	
	public WelcomeMobilePage() {
		super();
		
		 logoPanel.add(new Image("img/layout/menuMenuLogo.png"));
		 logoPanel.setStyleName("logoPanel", true);
		 getHeader().addImageHeader(logoPanel);  
		 
		showMobileOptions();
	}
	
	
	private void showMobileOptions(){
		
		
		final boolean isOSMobile = osType.toLowerCase().indexOf("ipad") >= 0 || osType.toLowerCase().indexOf("iphone") >= 0;
		final boolean isAndroid = osType.toLowerCase().indexOf("android") >= 0;
		
		mobileNotification.setStyleName("mobileNotification");
		messageMobile.addStyleName("messageMobile");
		noThanksPanel.setStyleName("noThanksPanel noFocus pointer");
		getApp.setStyleName("getApp noFocus pointer");
		
		
		
		noThanksWrapper.add(new Label(Customization.NO_THANKS));
		getAppWrapper.add(new Label(Customization.GET_APP));
		
	
		
		noThanksPanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				goToLoginScreen();
			}
		});
		
		getApp.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isAndroid){
					Window.Location.replace("http://www.android.com/");
				} else if(isOSMobile){
					Window.Location.replace("http://www.apple.com/pl/");
				}
			}
		});
		
		noThanksPanel.add(noThanksWrapper);
		getApp.add(getAppWrapper);
		
		
		mobileNotification.add(messageMobile);
		mobileNotification.add(noThanksPanel);
		mobileNotification.add(getApp);
		
		
		add(mobileNotification);
		
	}
	
	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		PagesController.hideWaitPanel();
	}
	private void goToLoginScreen(){
		PagesController.showWaitPanel();
//		JQMContext.changePage(Pages.PAGE_LOGIN_OK, Transition.SLIDE);
		JQMContext.changePage(PagesController.getPage(Pages.PAGE_LOGIN_CORRECT));
	}
	
	@Override
	protected void onPageHide() {
		super.onPageHide();
		removeFromParent();
	} 
}
