package com.veliasystems.menumenu.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.userInterface.CityInfoScreen;

/**
 * Singleton.
 * @author mateusz
 * Controller to all cities operations
 */
public class CityController{

	private List<IObserver> observers = new ArrayList<IObserver>();
	public static Map<Long, CityInfoScreen> cityMapView = new HashMap<Long, CityInfoScreen>();
	private static final Logger log = Logger.getLogger(CityController.class.getName());
	private static CityController instance = null ; //instance of controller
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	private UserController userController = UserController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private Map<Long, City> cities = new HashMap<Long, City>(); //key is a cityId

	
	private CityController() {
	}
	
	public static CityController getInstance(){
		if(instance == null){
			
			instance = new CityController();
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
	
	private Map<Long, City> getCities() {// it shuldn't by used
		return cities;
	}
	/**
	 * It should be called only once!!!
	 */
	public void setCities(Map<Long, City> cities) {// it shuldn't by used
		this.cities = cities;
	}
	
	private Set<Long> getCitiesKey(){
		return cities.keySet();
	}
	/**
	 * 
	 * @return List of {@link City} 
	 */
	public List<City> getCitiesList(){
		List<City> citiesList = new ArrayList<City>();
		for (Long cityId : getCitiesKey()) {
			citiesList.add(cities.get(cityId));
		}
		return citiesList;
	}
	
	/**
	 *Saving city in datastore and put to local map with cities
	 * 
	 * @param cityName - name of {@link City}
	 */
	public void saveCity(final String cityName, final String country){
		
	
		storeService.addCity(cityName, country, new AsyncCallback<City>() {
			@Override
			public void onSuccess(City result) {
				if(result != null){
					cities.put(result.getId(), result);
					notifyAllObservers();
				}	
				History.back();
			}
			@Override
			public void onFailure(Throwable caught) {
				log.warning(caught.getMessage());
			}
		});
	}
	/**
	 * 
	 * @param cityId - id of {@link City}
	 * @return {@link City}
	 */
	public City getCity(Long cityId) {
		City city = cities.get(cityId);
		return city == null? new City() : city; 
	}
	/**
	 * Method to copy all data to another {@link City}
	 * 
	 * @param cityIdFrom - id of {@link City} - source of data
	 * @param cityIdTo - id of {@link City} - destination of copying data
	 */
	public void copyAllDataFromCity(String cityIdFrom, String cityIdTo){
		PagesController.showWaitPanel();
		storeService.copyCityData(cityIdFrom, cityIdTo, userController.getLoggedUser().getEmail() , new AsyncCallback<Map<String, String>>() {
			
			@Override
			public void onSuccess(Map<String, String> result) {
				
				if(result == null){
					Window.alert(Customization.WRONG_DATA_ERROR);
				}else{
					Window.alert(Customization.COPY_RESTAURANTS_IN_PROGRESS);
				}
				PagesController.hideWaitPanel();
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
				Window.alert(Customization.CONNECTION_ERROR);
			}
		});
	}
	/**
	 * Delete city from datastore
	 * @param id - id of {@link City}
	 */
	public void deleteCity(Long id) {
		PagesController.showWaitPanel();
		storeService.deleteRestaurants(id, new AsyncCallback<Long>() {
			
			@Override
			public void onSuccess(Long cityId) {
				List<Restaurant> restaurantsToDelete = restaurantController.getRestaurantsInCity(cityId);
				cities.remove(cityId);
				restaurantController.removeRestaurantLocally(restaurantsToDelete);
				PagesController.hideWaitPanel();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(Customization.CONNECTION_ERROR);
				PagesController.hideWaitPanel();
			}
		});
		
		
	}
	
	/**
	 * 
	 * @param cityName - name of {@link City}
	 * @return id fo {@link City}
	 */
	public long getCityId(String cityName){
		
		for (Long id : cities.keySet()) {
			if(cities.get(id).getCity().equals(cityName)){
				return id;
			}
		}
		return -1;
	}
	
	
//	private <T, E> T getKeyByValue(Map<T, E> map, String value) {
//	    for (Entry<T, E> entry : map.entrySet()) {
//	        if (value.equals(entry.getValue())) {
//	            return entry.getKey();
//	        }
//	    }
//	    return null;
//	}
	
	/**
	 * save edited {@link City}
	 * @param city - {@link City} to save
	 */
	public void saveCity(City city) {
		if(city == null) return;
		
		PagesController.showWaitPanel();
		storeService.saveCity(city, new AsyncCallback<City>() {
			
			@Override
			public void onSuccess(City editedCity) {
				cities.get(editedCity.getId()).setCity(editedCity.getCity());
				cities.get(editedCity.getId()).setVisable(editedCity.isVisable(true), editedCity.isVisable(false));
				notifyAllObservers();
				PagesController.hideWaitPanel();
				Window.alert(Customization.OK);
			}
			
			@Override
			public void onFailure(Throwable caught) {

				PagesController.hideWaitPanel();
				Window.alert(Customization.CONNECTION_ERROR);
			}
		});
		
	}

	public void refreshCities(final IObserver observer) {
		PagesController.showWaitPanel();
		
		storeService.getCitiesForUser(userController.getLoggedUser().getEmail(), new AsyncCallback<List<City>>() {
			
			@Override
			public void onSuccess(List<City> citiesList) {
				refreshCities(citiesList);
				notifyObserver(observer);
			}

			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
				Window.alert(Customization.CONNECTION_ERROR);
			}
		});
	}
	
	private void notifyObserver(IObserver observer){
		if(observer != null){
			observer.newData();
		}else{
			PagesController.hideWaitPanel();
		}
	}
	
	private void refreshCities(List<City> citiesList) {
		cities.clear();
		for (City city : citiesList) {
			cities.put(city.getId(), city);
		}
	}
}
