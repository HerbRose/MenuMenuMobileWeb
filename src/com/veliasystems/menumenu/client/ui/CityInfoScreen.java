package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class CityInfoScreen extends JQMPage {
	
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMButton backButton;
	JQMButton showRestaurant;
	JQMList restList;
	JQMPanel content;
	private String title;
	

	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	private void init(){
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		header = new JQMHeader(title);
		header.setBackButton(backButton);
		header.setFixed(true);
		add(header);
		
		content = new JQMPanel();
		
		restList = new JQMList();
		
				
				storeService.loadRestaurants(header.getText(), new AsyncCallback<List<Restaurant>>() {
					
					@Override
					public void onSuccess(List<Restaurant> result) {
						// TODO Auto-generated method stub
						if(result.size() > 0){
							
							addRestaurants(result);
							
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
	
		add(content);
		content.add(restList);
		
		 	footer = new JQMFooter();
		    addButton = new JQMButton(Customization.ADDRESTAURANT);
		    addButton.setWidth("100%");
		    addButton.setIcon(DataIcon.PLUS);
		    addButton.setIconPos(IconPos.TOP);
		    footer.add(addButton);
		    footer.setFixed(true);
		    footer.setWidth("100%");
		    add(footer);
		
	}
	
	public CityInfoScreen() {
		this.title = Customization.CITY;
		init();
	}
	
	public CityInfoScreen(String city){
		this.title = city;
		init();
		
	}
	
	
	
	private void addRestaurants(List<Restaurant> list){
		
		for(final Restaurant item: list){
			RestaurantImageView restaurantView;
			if(RestaurantController.restMapView.containsKey(item.getId())){
				restaurantView = RestaurantController.restMapView.get(item.getId());	
			}
			else{
				restaurantView = new RestaurantImageView(item);
				RestaurantController.restMapView.put(item.getId(), restaurantView);
				
			}
			
			restList.addItem(item.getName(), restaurantView);
		}
	}
}
