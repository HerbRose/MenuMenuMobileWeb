package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;


public interface StoreServiceAsync {
	
	void saveRestaurant(Restaurant r, AsyncCallback<Void> callback);

	void loadRestaurants(AsyncCallback<List<Restaurant>> callback);
	void loadRestaurants(String city, AsyncCallback<List<Restaurant>> callback);

	void loadCities(AsyncCallback<List<String>> callback);

	void deleteRestaurant(Restaurant r, AsyncCallback<Void> callback);

	void setMainImage(ImageBlob imageBlob, AsyncCallback<Void> callback);

	void fillWithData(AsyncCallback<Void> callback);

	void clearStore(AsyncCallback<Void> callback);

	void uploadRestaurants(String JSON, AsyncCallback<String> callback);

	void addCity(String cityName, AsyncCallback<Void> callback);

	void loadRestaurant(Long id, AsyncCallback<Restaurant> callback);

	void loadCitiesEntity(AsyncCallback<List<City>> callback);

	

	


}
