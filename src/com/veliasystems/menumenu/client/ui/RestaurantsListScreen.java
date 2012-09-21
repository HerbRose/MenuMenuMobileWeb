package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.StoreService;
import com.veliasystems.menumenu.client.StoreServiceAsync;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestaurantsListScreen extends JQMPage {
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMList list;
	JQMButton backButton;
	
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	private List<Restaurant> restaurantList;
	
	public RestaurantsListScreen() {
	    
		header = new JQMHeader(Customization.RESTAURANTS);
		header.setFixed(true);
		header.setText(Customization.RESTAURANTS);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header.setBackButton(backButton);
		add(header);
	    
		list = new JQMList();
	    list.setInset(false);
	    
	    list.refresh();
	    
	    storeService.loadRestaurants(new AsyncCallback<List<Restaurant>>() {
			
			@Override
			public void onSuccess(List<Restaurant> result) {
				// TODO Auto-generated method stub
				restaurantList = new ArrayList<Restaurant>();
				restaurantList = result;
				addRestaurants(restaurantList);
				add(list);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				showError();
			}
		});
	    
	    addButton = new JQMButton(Customization.ADDRESTAURANT, Pages.PAGE_ADD_RESTAURANT);
	    addButton.setIcon(DataIcon.PLUS);
	    addButton.setIconPos(IconPos.TOP);
	    addButton.setTransition(Transition.SLIDE);
	    addButton.setWidth("100%");
	        
	    footer = new JQMFooter(addButton);
	    footer.setFixed(true);
   
	    add(footer);
	        
	    System.out.println(list.getTitle());
	  }
	
	private void addRestaurants(List<Restaurant> list){
		
		for(final Restaurant item: list){
			this.list.addItem(item.getName(), new RestInfoScreen(item.getName()));
		}
	}
	private void showError(){
		Label label = new Label();
		label.setText(Customization.LOADERROR);
		this.add(label);
	}
	}