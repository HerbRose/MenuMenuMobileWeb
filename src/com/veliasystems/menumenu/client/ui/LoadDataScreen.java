package com.veliasystems.menumenu.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class LoadDataScreen extends JQMPage {
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	private RestaurantController restaurantController = RestaurantController.getInstance();
	
	public LoadDataScreen() {
		Document.get().getElementById("load").setClassName(R.LOADING);
		
		storeService.getAllData(new AsyncCallback<Map<String,Object>>() {
			
			@Override
			public void onSuccess(Map<String, Object> result) {
				List<Restaurant> restaurants = (List<Restaurant>) result.get("Restaurants") ;
				Map<Long, Restaurant> restaurantsFromServer = new HashMap<Long, Restaurant>();
				for (Restaurant restaurant : restaurants) {
					restaurantsFromServer.put(restaurant.getId(), restaurant);
				}
				
				List<City> cities = (List<City>) result.get("Cities") ;
				Map<Long, City> citiesFromServer = new HashMap<Long, City>();
				for (City city : cities) {
					citiesFromServer.put(city.getId(), city);
				}
				
				restaurantController.setRestaurants(restaurantsFromServer);
				CityController.getInstance().setCities(citiesFromServer);
//				Pages.PAGE_CITY_LIST = new CityListScreen();
				
//				Pages.PAGE_SAVE_RESTAURANT = new RestaurantSavedScreen();
//				Pages.PAGE_RESTAURANT_LIST = new RestaurantsListScreen();
//				Pages.PAGE_HOME = new HomePageScreen();
//				Pages.PAGE_ADD_RESTAURANT = new AddRestaurantScreen();
//				Pages.PAGE_LOGIN = new LoginScreen();
				
				changePage();	
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		
	}
	
	private void changePage(){
		String lastPage = Cookies.getCookie(R.LAST_PAGE);
		
		if(lastPage!=null){
			
			Long lastPageId;
			
			lastPageId = Long.parseLong(lastPage);
			Restaurant lastOpenRestaurant = RestaurantController.getInstance().getRestaurant(lastPageId);
			if(lastOpenRestaurant == null){
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_HOME));
			}else{
				
				RestaurantImageView restaurantView = new RestaurantImageView(lastOpenRestaurant,PagesController.getPage(Pages.PAGE_HOME));
				RestaurantController.restMapView.put(lastOpenRestaurant.getId(), restaurantView);
				
				String imageType = Cookies.getCookie(R.IMAGE_TYPE);
				
				//JQMContext.changePage(PagesController.getPage(Pages.PAGE_HOME));
				if(imageType == null){
					JQMContext.changePage(restaurantView, Transition.SLIDE);
				}else{
					restaurantController.cropImageApple(lastPageId, ImageType.valueOf(imageType));
				}
				
				
			}
		}else{
			JQMContext.changePage(PagesController.getPage(Pages.PAGE_HOME), Transition.SLIDE);
		}
	}
	
}
