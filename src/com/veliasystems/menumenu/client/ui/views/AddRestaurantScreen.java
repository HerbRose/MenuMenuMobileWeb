package com.veliasystems.menumenu.client.ui.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMContent;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;

public class AddRestaurantScreen extends JQMPage{
	
	JQMHeader header;
	JQMButton backButton;
	JQMButton saveButton;
	JQMContent content;
	
	

	public AddRestaurantScreen() {
		// TODO Auto-generated constructor stub
		header = new JQMHeader(Customization.RESTAURANTS);
		header.setFixed(true);
		header.setText(Customization.RESTAURANTS);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setInline();
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		saveButton = new JQMButton(Customization.SAVERESTAURANT);
		saveButton.setIcon(DataIcon.PLUS);
		saveButton.setInline();
		saveButton.setIconPos(IconPos.RIGHT);
		
		
		
		header.setLeftButton(backButton);
		
		saveButton.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Window.alert("uhhf");
			}
		});
		
		
		
		header.setRightButton(saveButton);
		
		
		
		add(header);
		
		
		
	}
	
	
}
