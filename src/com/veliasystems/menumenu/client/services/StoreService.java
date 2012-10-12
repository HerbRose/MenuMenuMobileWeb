package com.veliasystems.menumenu.client.services;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("store")
public interface StoreService extends RemoteService {
	
void saveRestaurant( Restaurant r );
	
	List<String> loadCities();
	
	List<Restaurant> loadRestaurants();
	List<Restaurant> loadRestaurants(String city);
	
	void deleteRestaurant(Restaurant r);
	void setMainImage(ImageBlob imageBlob);

	void fillWithData();

	void clearStore();
	String uploadRestaurants(String JSON);
	City addCity(String cityName);

	Restaurant loadRestaurant(Long id);

	List<City> loadCitiesEntity();

	Map<String, Object> getAllData();
}
