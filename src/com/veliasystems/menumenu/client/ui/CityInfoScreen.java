package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class CityInfoScreen extends JQMPage {
	
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMButton backButton;
	JQMButton showRestaurant;
	JQMList restaurantList = new JQMList();;
	JQMPanel content;
	private String title; //city name or Customization.CITY
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private List<Restaurant> restaurants;
	
    public CityInfoScreen(String city){
        restaurantList = new JQMList();
        
        title = city;
        
        backButton = new JQMButton(Customization.BACK);
        backButton.setBack(true);
        backButton.setIcon(DataIcon.LEFT);
        backButton.setIconPos(IconPos.LEFT);
        header = new JQMHeader(title);
        header.setBackButton(backButton);
        header.setFixed(true);
        add(header);
        
        restaurants = restaurantController.getRestaurantsInCity(title);        
        addRestaurants(restaurants);
        
        add(restaurantList);
        
             footer = new JQMFooter();
            addButton = new JQMButton(Customization.ADDRESTAURANT, new AddRestaurantScreen(title), Transition.SLIDE);
            addButton.setWidth("100%");
            addButton.setIcon(DataIcon.PLUS);
            addButton.setIconPos(IconPos.TOP);
            footer.add(addButton);
            footer.setFixed(true);
            footer.setWidth("100%");
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
			restaurantList.addItem(item.getName(), restaurantView);
		}
	}

	@Override
	protected void onPageShow() {
		super.onPageShow();
		List<Restaurant> newRestaurants = restaurantController.getRestaurantsInCity(title);
		
		if(restaurants.size() != newRestaurants.size() ){
			restaurants = newRestaurants;
			refreshRestaurantList();
		}
	}
	private void refreshRestaurantList() {
		restaurantList.clear();
		addRestaurants(restaurants);
		restaurantList.refresh();	
	}
}
