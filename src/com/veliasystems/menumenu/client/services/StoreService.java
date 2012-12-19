package com.veliasystems.menumenu.client.services;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.taskqueue.Queue;
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
	/**
	 * 
	 * @param r - {@link Restaurant} to save in datastore
	 * @return {@link Restaurant} saved in datastore
	 */
	Restaurant saveRestaurant( Restaurant r );
	/**
	 * 
	 * @return List of all {@link City} in datastore
	 */
	List<String> loadCities();
	/**
	 * 
	 * @return List of all {@link Restaurant} in datastore
	 */
	List<Restaurant> loadRestaurants();
	/**
	 * 
	 * @param city - name of city
	 * @return List of {@link Restaurant} in given city
	 */
	List<Restaurant> loadRestaurants(String city);
	/**
	 * 
	 * @param r - {@link Restaurant} to delete
	 */
	void deleteRestaurant(Restaurant r);
	/**
	 * 
	 * @param imageBlob - image to set as main image
	 * @return Restaurant in which given {@link ImageBlob} was set
	 */
	Restaurant setMainImage(ImageBlob imageBlob);
	/**
	 *@deprecated
	 */
	void fillWithData();
	/**
	 * method to clear store
	 */
	void clearStore();
	/**
	 * 
	 * @param JSON - valid JSON String
	 * @return server result of action
	 */
	String uploadRestaurants(String JSON);
	/**
	 * 
	 * @param cityName - name of {@link City}
	 * @return {@link City} object
	 */
	City addCity(String cityName);
	/**
	 * 
	 * @param id - {@link Restaurant} id
	 * @return Single {@link Restaurant} object
	 */
	Restaurant loadRestaurant(Long id);
	/**
	 * 
	 * @return all {@link City} in datastore
	 */
	List<City> loadCitiesEntity();
	/**
	 * 
	 * @param login - {@link User} login
	 * @param password - {@link User} password
	 * @return Map of objects, like {@link Restaurant}, {@link City}
	 */
	Map<String, Object> getAllData(String login, String password);
	/**
	 * 
	 * @param user - {@link User} to add
	 */
	void addUser(User user);
	/**
	 * 
	 * @param user - {@link User} to change data
	 * @param oldEmail - old email
	 * @return {@link User} object after change data
	 */
	User changeUserData(User user, String oldEmail);
	/**
	 * 
	 * @return List of users
	 */
	List<User> getUsers();
	/**
	 * 
	 * @param login - User login
	 * @return Map of objects, like {@link Restaurant}, {@link City}
	 */
	Map<String, Object> getAllData(String login);
	/**
	 * 
	 * @param restaurants - List of restaurants
	 * @return Map of restaurants id as key and visibility as value
	 */
	Map<Long, Boolean> setVisibilityRestaurants(List<Restaurant> restaurants);
	/**
	 * 
	 * @param restaurants - List of {@link Restaurant} to save
	 */
	void saveRestaurants(List<Restaurant> restaurants);
	/**
	 * 
	 * @param restaurant - {@link Restaurant} object
	 * @param imageType - type of image, specified by {@link ImageType}
	 * @return {@link Restaurant} object
	 */
	Restaurant clearBoard(Restaurant restaurant, ImageType imageType);
	/**
	 * 
	 * @param user - {@link User} to delete
	 * @return Email of user who was deleted
	 */
	String removeUser(User user);
	/**
	 * 
	 * @param cityIdFrom - id of city from data must be copied
	 * @param cityIdTo - id of city to data will be copied
	 * @param email - email address, where notification will be send after copy data
	 * @return id's of {@link City}
	 * 
	 * <br/>
	 * This method create a {@link Queue} on server side. When {@link Queue} is done, notification is send to user
	 * <br/>
	 * <br/>
	 * This method can increase server consumption, in extreme cases can use all resources
	 */
	Map<String, String> copyCityData(String cityIdFrom, String cityIdTo, String email);
	/**
	 * 
	 * @param cityId - id of {@link City}
	 * @return null if {@link City} query return null, else id of {@link City}
	 */
	Long deleteRestaurants(long cityId);
	/**
	 * 
	 * @param city - {@link City} Object
	 * @return {@link City} object
	 */
	City saveCity(City city);
}
