package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.StoreService;
import com.veliasystems.menumenu.client.StoreServiceAsync;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestInfoScreen extends JQMPage {
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMList list;
	JQMButton backButton;
	JQMButton showButton;
	
	Label nameLabel = new Label();
	Label cityLabel = new Label();
	Label adressLabel = new Label();
	
	TextBox cityText = new TextBox();
	TextBox nameText = new TextBox();
	TextBox adressText = new TextBox();
	
	JQMPanel content;
	
	Restaurant restaurant = new Restaurant();
	


	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	private void init() {

		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header = new JQMHeader("[Restaurant]");
		header.setBackButton(backButton);
		header.setFixed(true);
		add(header);
		
		content = new JQMPanel();
		
		showButton = new JQMButton("show rest");
		showButton.setBack(false);
		showButton.setIcon(DataIcon.INFO);
		content.add(showButton);
		cityLabel.setText(Customization.CITYONE + ":");
		content.add(cityLabel);
		cityText.setTitle(Customization.CITYONE);
		content.add(cityText);
		nameLabel.setText(Customization.RESTAURANTNAME + ":");		
		content.add(nameLabel);
		nameText.setTitle(Customization.RESTAURANTNAME);
		content.add(nameText);
		adressLabel.setText(Customization.RESTAURANTADRESS + ":");	
		content.add(adressLabel);
		adressText.setTitle(Customization.RESTAURANTADRESS);
		content.add(adressText);
		
		showButton.addClickHandler(new ClickHandler() {
			
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				
				
				storeService.getRestaurant(restaurant, new AsyncCallback<Restaurant>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Restaurant result) {
						// TODO Auto-generated method stub
						cityText.setText(result.getCity());
						nameText.setText(result.getName());
						adressText.setText(result.getAddress());
					}
				});
			}
		});
	
		add(content);	
				
		removeButton = new JQMButton(Customization.REMOVEPROFILE);
		removeButton.setIcon(DataIcon.DELETE);
		removeButton.setIconPos(IconPos.TOP);
		
		removeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
					storeService.deleteRestaurant(restaurant, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(Void result) {
							// TODO Auto-generated method stub
							Window.Location.reload();
						}
					});
			}
		});
		
		add(removeButton);
	}
	
	
	public RestInfoScreen() {
	    init();
	  }
	
	
	public RestInfoScreen(Restaurant r) {
		init();
		setRestaurant(r);
	}
	
	
	public RestInfoScreen setRestaurant(Restaurant r) {

		header.setText(r.getName());
		this.restaurant = r;
		return this;
	}

}
