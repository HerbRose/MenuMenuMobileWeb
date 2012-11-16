package com.veliasystems.menumenu.client.services;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("store")
public interface StoreService extends RemoteService {
	
	Restaurant saveRestaurant( Restaurant r );
	
	List<String> loadCities();
	
	List<Restaurant> loadRestaurants();
	List<Restaurant> loadRestaurants(String city);
	
	void deleteRestaurant(Restaurant r);
	Restaurant setMainImage(ImageBlob imageBlob);

	void fillWithData();

	void clearStore();
	String uploadRestaurants(String JSON);
	City addCity(String cityName);

	Restaurant loadRestaurant(Long id);

	List<City> loadCitiesEntity();

	Map<String, Object> getAllData(String login, String password);

	void addUser(User user);
	User changeUserData(User user, String oldEmail);

	List<User> getUsers();

	Map<String, Object> getAllData(String login);

	Map<Long, Boolean> setVisibilityRestaurants(List<Restaurant> restaurants);

	void saveRestaurants(List<Restaurant> restaurants);

	Restaurant clearBoard(Restaurant restaurant, ImageType imageType);

	String removeUser(User user);

	Map<String, String> copyCityData(String cityIdFrom, String cityIdTo, String email);
}
