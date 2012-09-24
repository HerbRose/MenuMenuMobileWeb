package com.veliasystems.menumenu.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
	Restaurant getRestaurant(Restaurant r);
	void deleteRestaurant(Restaurant r);
	
}
