package com.veliasystems.menumenu.client.services;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.controllers.responseWrappers.ResponseSaveCityWrapper;
import com.veliasystems.menumenu.client.controllers.responseWrappers.ResponseSaveRestaurantWrapper;
import com.veliasystems.menumenu.client.controllers.responseWrappers.ResponseUserWrapper;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.entities.UserToAdd;

public interface StoreServiceAsync {

	void saveRestaurant(String userEmail, Restaurant r, long oldCityId,
			long newCityId,List<String> usersToAdd, AsyncCallback<ResponseSaveRestaurantWrapper> callback);

	void loadRestaurants(AsyncCallback<List<Restaurant>> callback);

	void loadRestaurants(String city, AsyncCallback<List<Restaurant>> callback);

	void loadCities(AsyncCallback<List<String>> callback);

	void deleteRestaurant(Restaurant r, AsyncCallback<Void> callback);

	void setMainImage(ImageBlob imageBlob, AsyncCallback<Restaurant> callback);

	void fillWithData(AsyncCallback<Void> callback);

	void clearStore(AsyncCallback<Void> callback);

	void uploadRestaurants(String JSON, AsyncCallback<String> callback);

	void addCity(String cityName, String country, AsyncCallback<City> callback);

	void loadRestaurant(Long id, AsyncCallback<Restaurant> callback);

	void loadCitiesEntity(AsyncCallback<List<City>> callback);

	void getAllData(String login, String password,
			AsyncCallback<Map<String, Object>> callback);

	void getAllData(String login, AsyncCallback<Map<String, Object>> callback);

	void addUser(User user, AsyncCallback<Void> callback);

	void getUsers(AsyncCallback<List<User>> callback);

	void setVisibilityRestaurants(List<Restaurant> restaurants,
			AsyncCallback<Map<Long, Boolean>> callback);

	void saveRestaurants(List<Restaurant> restaurants,
			AsyncCallback<Void> callback);

	void clearBoard(Restaurant restaurant, ImageType imageType,
			AsyncCallback<Restaurant> asyncCallback);

	void changeUserData(User user, String oldPassword, String newPassword,
			AsyncCallback<User> callback);

	void removeUser(User user, AsyncCallback<String> callback);

	void copyCityData(String cityIdFrom, String cityIdTo, String email,
			AsyncCallback<Map<String, String>> callback);

	void deleteRestaurants(long cityId, AsyncCallback<Long> callback);

	void saveCity(City city, AsyncCallback<ResponseSaveCityWrapper> callback);

	void addNewRestaurant(Restaurant restaurant, List<String> usersEmailToAdd,
			String emailAddingUser, AsyncCallback<List<Object>> callback);

	void saveRestaurant(Restaurant r, AsyncCallback<Restaurant> callback);

	void getCitiesForUser(String email, long lastCitySyncDate,
			AsyncCallback<Map<Long, List<City>>> callback);
	
	void saveUser(User user, AsyncCallback<User> callback);

	void getRestaurantsForUser(String email,
			AsyncCallback<List<Restaurant>> callback);
	
	void getRestaurantsForUser(String email, long cityId,
			long lastRestaurantSyncDate,
			AsyncCallback<Map<Long, List<Restaurant>>> callback);

	void confirmUser(User user, UserToAdd userToAdd,
			AsyncCallback<ResponseUserWrapper> callback);

	void addUserToTests(User user, AsyncCallback<Void> asyncCallback);
	
	void authorization(String login, AsyncCallback<User> callback);

	void authorization(String login, String password,
			AsyncCallback<User> asyncCallback);

	void getCitiesNameAndIdForUser(String email,
			AsyncCallback<Map<Long, String>> callback);

	void getRestaurantsNameAndIdForUser(String email,
			AsyncCallback<Map<Long, String>> callback);

}
