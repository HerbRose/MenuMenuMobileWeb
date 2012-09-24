package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.StoreService;
import com.veliasystems.menumenu.client.StoreServiceAsync;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class CityInfoScreen extends JQMPage {
	
	JQMHeader header;
	JQMFooter footer;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMList list = new JQMList();
	JQMButton backButton;
	JQMButton showRestaurant;
	
	private List<Restaurant> restaurantList;
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	private void init(){
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		header = new JQMHeader("[Restaurant]");
		header.setBackButton(backButton);
		header.setFixed(true);
		add(header);
		
		showRestaurant = new JQMButton("show rest");
		showRestaurant.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			
				
				storeService.loadRestaurants(header.getText(), new AsyncCallback<List<Restaurant>>() {
					
					@Override
					public void onSuccess(List<Restaurant> result) {
						// TODO Auto-generated method stub
						if(result.size() > 0){
//							list.clear();
//							
//							restaurantList = new ArrayList<Restaurant>();
//							restaurantList = result;
//							
//							list = (JQMList) restaurantList;
//							
//							add(list);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
		});
		
		add(showRestaurant);
	}
	
	public CityInfoScreen() {
		init();
	}
	
	public CityInfoScreen(String city){
		init();
		setHeaderText(city);
	}
	
	public void setHeaderText(String text){
		header.setText(text);
	}
}
