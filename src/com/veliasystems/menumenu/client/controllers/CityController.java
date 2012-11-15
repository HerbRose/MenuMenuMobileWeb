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
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.ui.CityInfoScreen;

/**
 * Singleton.
 * @author mateusz
 *
 */
public class CityController {

	private List<IObserver> observers = new ArrayList<IObserver>();
	public static Map<Long, CityInfoScreen> cityMapView = new HashMap<Long, CityInfoScreen>();
	private static final Logger log = Logger.getLogger(CityController.class.getName());
	private static CityController instance = null ; //instance of controller
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	private UserController userController = UserController.getInstance();
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
	public List<City> getCitiesList(){
		List<City> citiesList = new ArrayList<City>();
		for (Long cityId : getCitiesKey()) {
			citiesList.add(cities.get(cityId));
		}
		return citiesList;
	}
	public void saveCity(String cityName){
		
		log.info("Saving city: " + cityName);
		
		final String cityNameToSave = cityName;
		storeService.addCity(cityNameToSave, new AsyncCallback<City>() {
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

	public City getCity(Long cityId) {
		return cities.get(cityId);
	}
	
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
	
}
