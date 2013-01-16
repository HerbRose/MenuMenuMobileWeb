package com.veliasystems.menumenu.client.services;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.controllers.ResponseSaveWrapper;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;


public interface StoreServiceAsync {
	
	void saveRestaurant( String userEmail , Restaurant r, long oldCityId, long newCityId, AsyncCallback<ResponseSaveWrapper> callback);

	void loadRestaurants(AsyncCallback<List<Restaurant>> callback);
	void loadRestaurants(String city, AsyncCallback<List<Restaurant>> callback);

	void loadCities(AsyncCallback<List<String>> callback);

	void deleteRestaurant(Restaurant r, AsyncCallback<Void> callback);

	void setMainImage(ImageBlob imageBlob, AsyncCallback<Restaurant> callback);

	void fillWithData(AsyncCallback<Void> callback);

	void clearStore(AsyncCallback<Void> callback);

	void uploadRestaurants(String JSON, AsyncCallback<String> callback);

	void addCity(String cityName, AsyncCallback<City> callback);

	void loadRestaurant(Long id, AsyncCallback<Restaurant> callback);

	void loadCitiesEntity(AsyncCallback<List<City>> callback);

	void getAllData(String login, String password,AsyncCallback<Map<String, Object>> callback);
	void getAllData(String login, AsyncCallback<Map<String, Object>> callback);

	void addUser(User user, AsyncCallback<Void> callback);

	void getUsers(AsyncCallback<List<User>> callback);

	void setVisibilityRestaurants(List<Restaurant> restaurants,
			AsyncCallback<Map<Long, Boolean>> callback);

	void saveRestaurants(List<Restaurant> restaurants,
			AsyncCallback<Void> callback);

	void clearBoard(Restaurant restaurant, ImageType imageType,
			AsyncCallback<Restaurant> asyncCallback);

	void changeUserData(User user, String oldPassword, String newPassword, AsyncCallback<User> callback);

	void removeUser(User user, AsyncCallback<String> callback);

	void copyCityData(String cityIdFrom, String cityIdTo, String email,
			AsyncCallback<Map<String, String>> callback);

	void deleteRestaurants(long cityId, AsyncCallback<Long> callback);

	void saveCity(City city, AsyncCallback<City> callback);

	void addNewRestaurant(Restaurant restaurant, List<String> usersEmailToAdd,
			String emailAddingUser, AsyncCallback<List<Object>> callback);

	void saveRestaurant(Restaurant r, AsyncCallback<Restaurant> callback);


	

	


}
