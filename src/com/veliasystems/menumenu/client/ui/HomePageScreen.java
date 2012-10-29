package com.veliasystems.menumenu.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;

public class HomePageScreen extends JQMPage implements HasClickHandlers{

	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMList list;
	JQMButton logoutButton;

	public HomePageScreen() {

		header = new JQMHeader(Customization.APPNAME);
		header.setFixed(true);
		header.setText(Customization.MAINTITLE);
		add(header);

		logoutButton = new JQMButton(Customization.LOGOUT);
		logoutButton.setIcon(DataIcon.LEFT);
		logoutButton.setIconPos(IconPos.LEFT);

		header.setBackButton(logoutButton);

		list = new JQMList();
		list.setInset(false);
		
		list.addItem(Customization.CITY, PagesController.getPage(Pages.PAGE_CITY_LIST));
		list.addItem(Customization.RESTAURANTS, PagesController.getPage(Pages.PAGE_RESTAURANT_LIST));

		add(list);

		/*
		 * this.setWidth("640px"); this.setHeight("960px");
		 */

	}
	
	{
		this.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {			
					meClicked(event);
			}
		});
	}
	
	private void meClicked(ClickEvent event){
		if(isClicked(event, logoutButton)){
			if(Window.confirm(Customization.ARE_YOU_SURE_WANT_LOGOUT)){
				JQMContext.changePage(new LogoutScreen(), Transition.SLIDE);
			}
		}
	}
	
	private boolean isClicked(ClickEvent event, JQMButton button) {
		
		int clickedX = event.getClientX();
		int clickedY = event.getClientY();

		int ButtonX = (int) button.getElement().getAbsoluteLeft();
		int ButtonY = (int) button.getElement().getAbsoluteTop();
		int ButtonWidth = (int) button.getElement().getClientWidth();
		int ButtonHeight = (int) button.getElement().getClientHeight();

		int ButtonStartX = ButtonX;
		int ButtonStopX = ButtonX + ButtonWidth;
		int ButtonStartY = ButtonY;
		int ButtonStopY = ButtonY + ButtonHeight;
		if (clickedX >= ButtonStartX && clickedX <= ButtonStopX
				&& clickedY >= ButtonStartY && clickedY <= ButtonStopY) {
			return true;
		}
		return false;
	}

	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		super.onPageShow();
		Document.get().getElementById("load").setClassName(R.LOADED);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
}
