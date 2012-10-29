package com.veliasystems.menumenu.client.ui;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.dom.client.Document;
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
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class CityInfoScreen extends JQMPage{
	
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
	private boolean loaded = false;
	

    public CityInfoScreen(String city){
        restaurantList = new JQMList();
        
        title = city;
        
        
        header = new JQMHeader(title);
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
		
		java.util.Collections.sort(list, new Comparator<Restaurant>() {

			@Override
			public int compare(Restaurant o1, Restaurant o2) {
				// TODO Auto-generated method stub
				 return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(Restaurant item: list){
			RestaurantImageView restaurantView;
			if(RestaurantController.restMapView.containsKey(item.getId())){
				restaurantView = RestaurantController.restMapView.get(item.getId());
			}
			else{
				restaurantView = new RestaurantImageView(item, this);
				RestaurantController.restMapView.put(item.getId(), restaurantView);
			}

			restaurantList.addItem(item.getName(), restaurantView);

		}
	}


	@Override
	protected void onPageShow() {
		super.onPageShow();
		
		
		if(!loaded){
			backButton = new JQMButton(Customization.BACK, PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE);
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);
			
			backButton.getElement().setInnerHTML(span);
			backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");
			
			header.add(backButton);
	        loaded = true;
		}
		
		refreshRestaurantList();
		
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	private void refreshRestaurantList() {
		restaurantList.clear();
		addRestaurants(restaurantController.getRestaurantsInCity(title));
		restaurantList.refresh();	
	}

}
