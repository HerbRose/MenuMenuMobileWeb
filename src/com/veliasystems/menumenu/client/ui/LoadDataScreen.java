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
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class LoadDataScreen extends JQMPage {
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private UserController userController = UserController.getInstance();
	private CityController cityController = CityController.getInstance();
	
	private String email;
	
	public LoadDataScreen(String login){
		email = login;
		Document.get().getElementById("load").setClassName(R.LOADING);
		
		storeService.getAllData(login, new AsyncCallback<Map<String,Object>>() {
			
			@Override
			public void onSuccess(Map<String, Object> result) {
				if(result == null){
					JQMContext.changePage(com.veliasystems.menumenu.client.ui.Pages.PAGE_LOGIN_WRONG);
				}
				else{
					setData(result);
					changePage();	
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(Customization.CONNECTION_ERROR);
				Cookies.removeCookie(R.LOADED);
				Cookies.removeCookie(R.LAST_PAGE);
				JQMContext.changePage(com.veliasystems.menumenu.client.ui.Pages.PAGE_LOGIN_WRONG);
				
			}
		});
		
	}
	
	public LoadDataScreen(String login, String password) {
		this.email = login;
		Document.get().getElementById("load").setClassName(R.LOADING);
		storeService.getAllData(login, password, new AsyncCallback<Map<String,Object>>() {
			
			@Override
			public void onSuccess(Map<String, Object> result) {
				if(result == null){
					JQMContext.changePage(com.veliasystems.menumenu.client.ui.Pages.PAGE_LOGIN_WRONG);
					
				}
				else{
					setData(result);
					Cookies.setCookie(R.LOGGED_IN, email);
					changePage();	
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void setData(Map<String, Object> data){
		List<Restaurant> restaurants = (List<Restaurant>) data.get("Restaurants") ;
		Map<Long, Restaurant> restaurantsFromServer = new HashMap<Long, Restaurant>();
		for (Restaurant restaurant : restaurants) {
			restaurantsFromServer.put(restaurant.getId(), restaurant);
		}
		
		List<City> cities = (List<City>) data.get("Cities") ;
		Map<Long, City> citiesFromServer = new HashMap<Long, City>();
		for (City city : cities) {
			citiesFromServer.put(city.getId(), city);
		}
		List<User> users = (List<User>) data.get("Users");
		Map<String, User> usersFromServer = new HashMap<String, User>();
		for (User user : users) {
			usersFromServer.put(user.getEmail(), user);
		}
		userController.setUsers(usersFromServer);
		userController.setUserType(email);
		restaurantController.setRestaurants(restaurantsFromServer);
		cityController.setCities(citiesFromServer);
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
				RestaurantImageView restaurantView;
				
				if(RestaurantController.restMapView.get(lastPageId.longValue())!=null){
					restaurantView = RestaurantController.restMapView.get(lastPageId);
				}else{
					restaurantView = new RestaurantImageView(lastOpenRestaurant,PagesController.getPage(Pages.PAGE_HOME));
				}
				
				if(RestaurantController.restMapView.get(lastPageId.longValue())!=null){
					restaurantView = RestaurantController.restMapView.get(lastPageId);
				}
				
				String imageType = Cookies.getCookie(R.IMAGE_TYPE);
				
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
