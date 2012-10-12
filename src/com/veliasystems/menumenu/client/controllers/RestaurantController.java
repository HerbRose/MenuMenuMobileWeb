package com.veliasystems.menumenu.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.ui.RestaurantImageView;



/**
 * @author mateusz
 *
 */
public class RestaurantController {

	private List<IObserver> observers = new ArrayList<IObserver>();
	private static final Logger log = Logger.getLogger(RestaurantController.class.getName());
	public static Map<Long, RestaurantImageView> restMapView = new HashMap<Long, RestaurantImageView>();
	private static RestaurantController instance = null ; //instance of controller
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	private Map<Long, Restaurant> restaurants = new HashMap<Long, Restaurant>();

	
	private RestaurantController() {
		
	}
	
	public static RestaurantController getInstance(){
		
		if(instance == null ){
			instance = new RestaurantController();
		}
		
		return instance;
	}

	
	/**
	 * add observer to list only if observer != null and list doesn't contains this observer.
	 * @param observer
	 */
	public void addObserver( IObserver observer){
		if(observer != null && !observers.contains(observer)){
			observers.add(observer);
		}
	}
	/**
	 * remove observer from list.
	 * @param observer
	 */
	public void removeObserver( IObserver observer ){
		if(observer != null){
			observers.remove(observer);
		}
	}
	
	private void notifyAllObservers(){
		for (IObserver observer : observers) {
			observer.onChange();
		}
	}
	
	private Map<Long, Restaurant> getRestaurants() {// it shuldn't by used
		return restaurants;
	}
	/**
	 * It should be called only once!!!
	 */
	public void setRestaurants(Map<Long, Restaurant> restaurants) {// it shuldn't by used
		this.restaurants = restaurants;
	}
	
	public List<Restaurant> getRestaurantsList() {
		List<Restaurant> restaurantsList = new ArrayList<Restaurant>();
		for (Long restaurantId : getRestaurantsKey()) {
			restaurantsList.add(restaurants.get(restaurantId));
		}
		return restaurantsList;
	}
	/**
	 * 
	 * @param restaurantId
	 * @return Restaurant or null
	 */
	public Restaurant getRestaurant(Long restaurantId){
		return restaurants.get(restaurantId);
	}
	
	/**
	 * 
	 * @param cityId - id of the city
	 * @return new ArrayList with chosen restaurants
	 */
	public List<Restaurant> getRestaurantsInCity(Long cityId){
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		for (Long restaurantid : getRestaurantsKey()) {
			Restaurant restaurant = this.restaurants.get(restaurantid);
			if(restaurant.getCityId() == cityId){
				restaurants.add(restaurant);
			}
		}
		
		return restaurants;
	}
	/**
	 * 
	 * @param cityName - name of the city
	 * @return new ArrayList with chosen restaurants
	 */
	public List<Restaurant> getRestaurantsInCity(String cityName){
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		for (Long restaurantid : getRestaurantsKey()) {
			Restaurant restaurant = this.restaurants.get(restaurantid);
			if(restaurant.getCity().equals(cityName)){
				restaurants.add(restaurant);
			}
		}
		
		return restaurants;
	}
	
	
	private Set<Long> getRestaurantsKey(){
		return restaurants.keySet();
	}
	
	public void saveRestaurant(Restaurant restaurant){
		
		final Restaurant restaurantToSave = restaurant;
		storeService.saveRestaurant(restaurantToSave, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				restaurants.put(restaurantToSave.getId(), restaurantToSave); //add/change restaurant in our list
			}
			@Override
			public void onFailure(Throwable caught) {	
			}
		});
	}
	
	public void deleteRestaurant(Restaurant restaurant){
		
		final Restaurant restaurantToDelete = restaurant;
		
		storeService.deleteRestaurant(restaurantToDelete, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				restaurants.remove(restaurantToDelete.getId());	 //removing restaurant from our list
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
