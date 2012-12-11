package com.veliasystems.menumenu.client.userInterface;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListItem;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

public class RestaurantsListScreen extends MyPage implements IObserver {
	
	private BackButton backButton;
	private MyButton addButton;

	private List<Restaurant> restaurants;
	private RestaurantController restaurantController = RestaurantController.getInstance();
	
	public RestaurantsListScreen() {
		super(Customization.RESTAURANTS);

		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_HOME), Transition.SLIDE);
			}
		});
		
		addButton = new  MyButton("");
		addButton.removeStyleName("borderButton");
		addButton.setStyleName("rightButton addButton", true);
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_ADD_RESTAURANT), Transition.SLIDE);
			}
		});

		getHeader().setRightButton(addButton);
		getHeader().setLeftButton(backButton);
		restaurants = restaurantController.getRestaurantsList();
		addRestaurants(restaurants);
		
	
	        
	  }
	
	private void addRestaurants(List<Restaurant> list){
		
		java.util.Collections.sort(list, new Comparator<Restaurant>() {

			@Override
			public int compare(Restaurant o1, Restaurant o2) {
				 return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
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
			
			final MyListItem restaurantItem = new MyListItem();
			restaurantItem.setText(item.getName());
			restaurantItem.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Document.get().getElementById("load").setClassName(R.LOADING);
					JQMContext.changePage(restaurantView);
				}
			});
			restaurantItem.setStyleName("itemList", true);
			getContentPanel().add(restaurantItem);

		}
	}
	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		if(Cookies.getCookie(R.LAST_PAGE) != null){
			Cookies.removeCookie(R.LAST_PAGE);
		}
		
		restaurantController.setLastOpenPage(this);
	
		refreshRestaurantList();

		Cookies.removeCookie(R.LAST_PAGE);
		
		Document.get().getElementById("load").setClassName(R.LOADED);
	}

	@Override
	public void onChange() {
	}

	private void refreshRestaurantList() {
		getContentPanel().clear();
		addRestaurants(restaurantController.getRestaurantsList());	
	}
	

}
