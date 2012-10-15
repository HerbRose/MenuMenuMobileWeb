package com.veliasystems.menumenu.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.ui.AddRestaurantScreen;
import com.veliasystems.menumenu.client.ui.CityInfoScreen;
import com.veliasystems.menumenu.client.ui.CityListScreen;
import com.veliasystems.menumenu.client.ui.HomePageScreen;
import com.veliasystems.menumenu.client.ui.LoginScreen;
import com.veliasystems.menumenu.client.ui.Pages;
import com.veliasystems.menumenu.client.ui.RestaurantImageView;
import com.veliasystems.menumenu.client.ui.RestaurantSavedScreen;
import com.veliasystems.menumenu.client.ui.RestaurantsListScreen;





public class MenuMenuMobileWeb implements EntryPoint {

	public static boolean loggedIn = false;
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	private RestaurantController restaurantController = RestaurantController.getInstance();
	
	public void onModuleLoad() {
	
		//JQMContext.changePage( new UploadRestaurantsScreen());
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
				
//				Pages.PAGE_CITY_INFO = new CityInfoScreen();
				Pages.PAGE_CITY_LIST = new CityListScreen();
				
				Pages.PAGE_ADD_RESTAURANT = new AddRestaurantScreen();
				Pages.PAGE_SAVE_RESTAURANT = new RestaurantSavedScreen();
				Pages.PAGE_RESTAURANT_LIST = new RestaurantsListScreen();

				Pages.PAGE_HOME = new HomePageScreen();
				
				Pages.PAGE_LOGIN = new LoginScreen();
				
				
				String logged = Cookies.getCookie(R.loggedIn);
				if (logged!=null) loggedIn = true;
				
				if (loggedIn) {
					changePage();
				} else {
					JQMContext.changePage( Pages.PAGE_LOGIN );
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
//		System.out.println("MenuMenuMobileWeb");
//		String logged = Cookies.getCookie(R.loggedIn);
//		if (logged!=null) loggedIn = true;
//		
//		
//		if (loggedIn) {
//			System.out.println("loggedIn");
//			changePage();
//		} else {
//			JQMContext.changePage( Pages.PAGE_LOGIN );
//		}
		
		//JQMContext.changePage( Pages.PAGE_UPLOAD );
		
				
	}
	
	private void changePage(){
		String lastPage = Cookies.getCookie(R.lastPage);
		
		if(lastPage!=null){
			
			Long lastPageId;
			
			lastPageId = Long.parseLong(lastPage);
			Restaurant lastOpenRestaurant = RestaurantController.getInstance().getRestaurant(lastPageId);
			if(lastOpenRestaurant == null){
				JQMContext.changePage(Pages.PAGE_HOME);
			}else{
				String imageType = Cookies.getCookie(R.imageType);
				RestaurantImageView restaurantView = new RestaurantImageView(lastOpenRestaurant);
				RestaurantController.restMapView.put(lastOpenRestaurant.getId(), restaurantView);
				if(imageType != null){
					JQMContext.changePage(Pages.PAGE_HOME);
					
					//JQMContext.changePage(restaurantView, Transition.SLIDE);
					restaurantController.cropImageApple(lastPageId, ImageType.valueOf(imageType));
				}else{
					JQMContext.changePage(Pages.PAGE_HOME);
					JQMContext.changePage(restaurantView, Transition.SLIDE);
				}
				
			}
//			storeService.loadRestaurant(lastPageId, new AsyncCallback<Restaurant>() {
//				
//				@Override
//				public void onSuccess(Restaurant result) {
//					if(result != null){
//						RestaurantImageView restaurantView = new RestaurantImageView(result);
//						RestaurantController.restMapView.put(result.getId(), restaurantView);
//						JQMContext.changePage(Pages.PAGE_HOME);
//						JQMContext.changePage(restaurantView, Transition.SLIDE);
//					}else{
//						JQMContext.changePage(Pages.PAGE_HOME, Transition.SLIDE);
//					}
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
		}else{
			JQMContext.changePage(Pages.PAGE_HOME, Transition.SLIDE);
		}
	}
}
