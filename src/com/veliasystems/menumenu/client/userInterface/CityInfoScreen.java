package com.veliasystems.menumenu.client.userInterface;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.userInterface.AddRestaurantScreen;
import com.veliasystems.menumenu.client.ui.RestaurantImageView;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListItem;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

public class CityInfoScreen extends MyPage{
	
	private BackButton backButton;
	private MyButton addButton;
	

	private RestaurantController restaurantController = RestaurantController.getInstance();
	private List<Restaurant> restaurants;
	
	private String cityName;

    public CityInfoScreen(String city){
    	super(city);
    	cityName = city;
    	backButton = new BackButton(Customization.BACK);
    	backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE);
			}
		});
    	
    	addButton = new MyButton("");
    	addButton.removeStyleName("borderButton");
		addButton.setStyleName("rightButton addButton", true);
    	addButton.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(new AddRestaurantScreen(cityName), Transition.SLIDE);	
			}
		});
    	
    	
    	getHeader().setLeftButton(backButton);
    	getHeader().setRightButton(addButton);
    
        
        restaurants = restaurantController.getRestaurantsInCity(city);        
        addRestaurants(restaurants);
        
//        add(restaurantList);
        
//             footer = new JQMFooter();
//            addButton = new JQMButton(Customization.ADDRESTAURANT, new AddRestaurantScreen(title), Transition.SLIDE);
//            addButton.setWidth("100%");
//            addButton.setIcon(DataIcon.PLUS);
//            addButton.setIconPos(IconPos.TOP);
//            footer.add(addButton);
//            footer.setFixed(true);
//            footer.setWidth("100%");
//            add(footer);
        
        
    }
	
	
	private void addRestaurants(List<Restaurant> list){
		
		java.util.Collections.sort(list, new Comparator<Restaurant>() {

			@Override
			public int compare(Restaurant o1, Restaurant o2) {
				 return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(Restaurant item: list){
			final RestaurantImageView restaurantView;
			if(RestaurantController.restMapView.containsKey(item.getId())){
				restaurantView = RestaurantController.restMapView.get(item.getId());
			}
			else{
				restaurantView = new RestaurantImageView(item, this);
				RestaurantController.restMapView.put(item.getId(), restaurantView);
			}
			
			final MyListItem restaurantlItem = new  MyListItem();
			restaurantlItem.setText(item.getName());
			restaurantlItem.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Document.get().getElementById("load").setClassName(R.LOADING);
					JQMContext.changePage(restaurantView);
				}
			});
			restaurantlItem.setStyleName("itemList", true);
			getContentPanel().add(restaurantlItem);
		}
	}


	@Override
	protected void onPageShow() {
		super.onPageShow();
		
		refreshRestaurantList();
		restaurantController.setLastOpenPage(this);
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	private void refreshRestaurantList() {
		getContentPanel().clear();
		addRestaurants(restaurantController.getRestaurantsInCity(cityName));
	}

}