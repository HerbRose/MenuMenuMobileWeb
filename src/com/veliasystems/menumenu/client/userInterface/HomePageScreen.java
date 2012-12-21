package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListItem;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

public class HomePageScreen extends MyPage{

	private BackButton logoutButton;
	private MyListItem cityItem;
	private MyListItem restaurantItem;
	
	public HomePageScreen() {
			
		super(Customization.MAINTITLE);
		
		logoutButton = new BackButton(Customization.LOGOUT);
		logoutButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm(Customization.ARE_YOU_SURE_WANT_LOGOUT)){
					JQMContext.changePage(new LogoutScreen(), Transition.SLIDE);
				}
			}
		});
	
		
		cityItem = new MyListItem();
		cityItem.setStyleName("itemList", true);
		cityItem.setText(Customization.CITY);
		cityItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_CITY_LIST));
			}
		});
		
		
		restaurantItem = new MyListItem();
		restaurantItem.setStyleName("itemList", true);
		restaurantItem.setText(Customization.RESTAURANTS);
		restaurantItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_RESTAURANT_LIST));
			}
		});
		
		getHeader().setLeftButton(logoutButton);
		getContentPanel().add(cityItem);
		getContentPanel().add(restaurantItem);
		
	}
	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
}
