package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.user.client.Cookies;
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
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestaurantsListScreen extends JQMPage implements IObserver {
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMButton uploadButton;

	JQMList restaurantList = new JQMList();

	JQMButton backButton;
	

	private List<Restaurant> restaurants;
	private RestaurantController restaurantController = RestaurantController.getInstance();

	public RestaurantsListScreen() {
		
		restaurantController.addObserver(this);
		
		header = new JQMHeader(Customization.RESTAURANTS);
		header.setFixed(true);
		header.setText(Customization.RESTAURANTS);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header.setBackButton(backButton);
		add(header);
	    	    
	    addRestaurants(restaurantController.getRestaurantsList());

	    addRestaurants(restaurantController.getRestaurantsList());
		restaurants = restaurantController.getRestaurantsList();
	    addRestaurants(restaurants);
	    
	    add(restaurantList);

	    
	    addButton = new JQMButton(Customization.ADDRESTAURANT, Pages.PAGE_ADD_RESTAURANT);
	    addButton.setIcon(DataIcon.PLUS);
	    addButton.setIconPos(IconPos.TOP);
	    addButton.setTransition(Transition.SLIDE);
	    addButton.setWidth("49%");
	    addButton.setInline();
	    uploadButton = new JQMButton(Customization.UPLOAD, new UploadRestaurantsScreen());
	    uploadButton.setIcon(DataIcon.FORWARD);
	    uploadButton.setIconPos(IconPos.TOP);
	    uploadButton.setTransition(Transition.SLIDE);
	    uploadButton.setWidth("49%");
	    uploadButton.setInline();
	        
	    footer = new JQMFooter(addButton);
	    footer.add(uploadButton);
	    footer.setFixed(true);
   
	    add(footer);
	        
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
			

			this.restaurantList.addItem(item.getName(), restaurantView);

		}
	}
	private void showError(){
		Label label = new Label();
		label.setText(Customization.LOADERROR);
		this.add(label);
	}
	@Override
	protected void onPageShow() {
		super.onPageShow();
		if(Cookies.getCookie(R.lastPage) != null){
			Cookies.removeCookie(R.lastPage);
		}
		List<Restaurant> newRestaurants = restaurantController.getRestaurantsList();
		
		if(restaurants.size() != newRestaurants.size() ){
			restaurants = newRestaurants;
			refreshRestaurantList();
		}
	}

	@Override
	public void onChange() {
		
	}

	private void refreshRestaurantList() {
		
		restaurantList.clear();
		addRestaurants(restaurants);
		restaurantList.refresh();
		
	}
	

}
