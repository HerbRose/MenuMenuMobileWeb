package com.veliasystems.menumenu.client.userInterface;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.CookieController;
import com.veliasystems.menumenu.client.controllers.CookieNames;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;

public class LoadDataScreen extends JQMPage {
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private UserController userController = UserController.getInstance();
	private CityController cityController = CityController.getInstance();
	private ImagesController imagesController = ImagesController.getInstance();
	private CookieController cookieController = CookieController.getInstance();
	
	private String email;
	
	public LoadDataScreen(String login){
		email = login;
		PagesController.showWaitPanel();
		
//		storeService.getAllData(login, new AsyncCallback<Map<String,Object>>() {
//			
//			@Override
//			public void onSuccess(Map<String, Object> result) {
//				if(result == null){
//					JQMContext.changePage(com.veliasystems.menumenu.client.userInterface.Pages.PAGE_LOGIN_WRONG);
//				}
//				else{
//					setData(result);
//					changePage();	
//				}
//				
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				PagesController.MY_POP_UP.showError(new Label(Customization.CONNECTION_ERROR), new  IMyAnswer() {
//					
//					@Override
//					public void answer(Boolean answer) {
//						
//					}
//				});
//				Cookies.removeCookie(R.LOADED);
//				cookieController.clearCookie(CookieNames.RESTAURANT_ID);
//				JQMContext.changePage(com.veliasystems.menumenu.client.userInterface.Pages.PAGE_LOGIN_WRONG);
//				
//			}
//		});
		
		storeService.authorization(login, new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User user) {
				if(user == null){
					JQMContext.changePage(com.veliasystems.menumenu.client.userInterface.Pages.PAGE_LOGIN_WRONG);
				}
				else{
					long date = new Date().getTime();
					date += 1000*60*60*24*3; //three days
					cookieController.setCookie(CookieNames.LOGGED_IN, email, date);
					changePage();	
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				PagesController.MY_POP_UP.showError(new Label(Customization.CONNECTION_ERROR), new  IMyAnswer() {
					
					@Override
					public void answer(Boolean answer) {
						
					}
				});
				Cookies.removeCookie(R.LOADED);
				cookieController.clearCookie(CookieNames.RESTAURANT_ID);
				JQMContext.changePage(com.veliasystems.menumenu.client.userInterface.Pages.PAGE_LOGIN_WRONG);
				
			}
			
		});
		
		
	}
	
	public LoadDataScreen(String login, String password) {
		this.email = login;
		PagesController.showWaitPanel();
//		storeService.getAllData(login, password, new AsyncCallback<Map<String,Object>>() {
//			
//			@Override
//			public void onSuccess(Map<String, Object> result) {
//				if(result == null){
//					JQMContext.changePage(com.veliasystems.menumenu.client.userInterface.Pages.PAGE_LOGIN_WRONG);
//					
//				}
//				else{
//					setData(result);
//					Cookies.setCookie(R.LOGGED_IN, email);
//					changePage();	
//				}
//				
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		storeService.authorization(login, new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User user) {
				if(user == null){
					JQMContext.changePage(com.veliasystems.menumenu.client.userInterface.Pages.PAGE_LOGIN_WRONG);
				}
				else{
					userController.setLoggedUser(user);
					long date = new Date().getTime();
					date += 1000*60*60*24*3; //three days
					cookieController.setCookie(CookieNames.LOGGED_IN, email, date);
					changePage();	
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				PagesController.MY_POP_UP.showError(new Label(Customization.CONNECTION_ERROR), new  IMyAnswer() {
					
					@Override
					public void answer(Boolean answer) {
						
					}
				});
				Cookies.removeCookie(R.LOADED);
				cookieController.clearCookie(CookieNames.RESTAURANT_ID);
				JQMContext.changePage(com.veliasystems.menumenu.client.userInterface.Pages.PAGE_LOGIN_WRONG);
				
			}
			
		});
		
	}
	
//	private void setData(Map<String, Object> data){
//		List<Restaurant> restaurants = (List<Restaurant>) data.get("Restaurants") ;
//		Map<Long, Restaurant> restaurantsFromServer = new HashMap<Long, Restaurant>();
//		for (Restaurant restaurant : restaurants) {
//			restaurantsFromServer.put(restaurant.getId(), restaurant);
//		}
//		List<City> cities = (List<City>) data.get("Cities") ;
//		Map<Long, City> citiesFromServer = new HashMap<Long, City>();
//		for (City city : cities) {
//			citiesFromServer.put(city.getId(), city);
//		}
//		List<User> users = (List<User>) data.get("Users");
//		Map<String, User> usersFromServer = new HashMap<String, User>();
//		for (User user : users) {
//			usersFromServer.put(user.getEmail(), user);
//		}
//		List<ImageBlob> defaultEmptyProfile = (List<ImageBlob>) data.get("DefaultEmptyProfile");
//		Map<String, ImageBlob> defoultEmptyProfilImageBlobMap = new HashMap<String, ImageBlob>();
//		for (ImageBlob imageBlob : defaultEmptyProfile) {
//			defoultEmptyProfilImageBlobMap.put(imageBlob.getId(), imageBlob);
//		}
//		
//		
//		userController.setUsers(usersFromServer);
//		
//		userController.setUserType(email);
////		restaurantController.setRestaurants(restaurantsFromServer);
////		cityController.setCities(citiesFromServer);
//		imagesController.setDefoultEmptyMenuImageBlobMap(defoultEmptyProfilImageBlobMap);
//
//	}
	
	private static native void consoleLog(String message)/*-{
		console.log(message);
	}-*/;
	
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		
	}
	
	private void changePage(){
		String restaurantIdString = cookieController.getCookie(CookieNames.RESTAURANT_ID);
		
		if(!restaurantIdString.isEmpty()){
			
			Long restaurantId;
			
			restaurantId = Long.parseLong(restaurantIdString);
			Restaurant lastOpenRestaurant = RestaurantController.getInstance().getRestaurant(restaurantId);
			
			if(cookieController.getCookie(CookieNames.IS_PICUP_USED).equals("true")){
				String imageType = cookieController.getCookie(CookieNames.IMAGE_TYPE);
				restaurantController.cropImageApple(restaurantId, ImageType.valueOf(imageType));
			}else if(lastOpenRestaurant == null ){
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_HOME));
			}else {
				RestaurantImageView restaurantView;
				
				if(RestaurantController.restMapView.get(restaurantId.longValue())!=null){
					restaurantView = RestaurantController.restMapView.get(restaurantId);
				}else{
					restaurantView = new RestaurantImageView(lastOpenRestaurant,PagesController.getPage(Pages.PAGE_HOME));
					RestaurantController.restMapView.put(lastOpenRestaurant.getId(), restaurantView);
				}
				
//				if(RestaurantController.restMapView.get(lastPageId.longValue())!=null){
//					restaurantView = RestaurantController.restMapView.get(lastPageId);
//				}
//				
				String imageType = cookieController.getCookie(CookieNames.IMAGE_TYPE); 
				
				if(imageType.isEmpty()){
					JQMContext.changePage(restaurantView, Transition.SLIDE);
				}else{
					restaurantController.cropImageApple(restaurantId, ImageType.valueOf(imageType));
				}
			}
		}else{
			JQMContext.changePage(PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE);
		}
	}
	
}
