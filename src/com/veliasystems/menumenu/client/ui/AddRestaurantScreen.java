package com.veliasystems.menumenu.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMContent;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;

public class AddRestaurantScreen extends JQMPage implements HasClickHandlers{
	
	JQMHeader header;
	JQMButton backButton;
	JQMButton saveButton;
	JQMContent content;
	

	{
		this.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				meClicked(event);
			}
		});
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
	
	
	void meClicked( ClickEvent e ) {
		e.stopPropagation();
		
		Object o = e.getSource();
		
		System.out.println(o.toString());
		System.out.println(o.getClass().toString());
		System.out.println(o.getClass().getName()); 
		
		if (o instanceof JQMButton) {
			JQMButton button = (JQMButton) o;
			if (button.getId().equalsIgnoreCase(saveButton.getId())) {
				Window.alert("Save Button clicked");
				return;
			}
		}
		
		Window.alert("Me Clicked");
	}
	

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
		
		saveButton = new JQMButton(Customization.SAVERESTAURANT, new RestaurantSavedScreen());
		saveButton.setIcon(DataIcon.PLUS);
		saveButton.setInline();
		saveButton.setIconPos(IconPos.RIGHT);
		saveButton.setId("saveButton");
		
		
		header.setLeftButton(backButton);
		header.setRightButton(saveButton);
		
		
		add(header);
		
		
		
	}

	
}
