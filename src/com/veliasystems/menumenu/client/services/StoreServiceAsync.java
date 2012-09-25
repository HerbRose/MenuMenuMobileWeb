package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.entities.Restaurant;


public interface StoreServiceAsync {
	
	void saveRestaurant(Restaurant r, AsyncCallback<Void> callback);

	void loadRestaurants(AsyncCallback<List<Restaurant>> callback);
	void loadRestaurants(String city, AsyncCallback<List<Restaurant>> callback);

	void loadCities(AsyncCallback<List<String>> callback);

	void deleteRestaurant(Restaurant r, AsyncCallback<Void> callback);

	void getRestaurant(Restaurant r, AsyncCallback<Restaurant> callback);


}
