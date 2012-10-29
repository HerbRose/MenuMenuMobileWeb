package com.veliasystems.menumenu.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.ui.CropImage;
import com.veliasystems.menumenu.client.ui.RestaurantImageView;



/**
 * @author mateusz
 *
 */
public class RestaurantController {

	private List<IObserver> observers = new ArrayList<IObserver>();
	private static final Logger log = Logger.getLogger(RestaurantController.class.getName());
	public static Map<Long, RestaurantImageView> restMapView = new HashMap<Long, RestaurantImageView>();
	private static RestaurantController instance = null ; //instance of controller
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	private BlobServiceAsync blobService = GWT.create(BlobService.class); 
	
	private boolean fromCityView = true;
	private JQMPage lastOpenPage = null;
	
	private Map<Long, Restaurant> restaurants = new HashMap<Long, Restaurant>();

	private RestaurantController() {
		
	}
	
	public void setFromCityView(boolean from) {
		this.fromCityView = from;
	}
	public boolean isFromCityView() {
		return fromCityView;
	}
	
	public void setLastOpenPage(JQMPage lastOpenPage) {
		this.lastOpenPage = lastOpenPage;
	}
	
	public JQMPage getLastOpenPage() {
		return lastOpenPage;
	}
	
	public static RestaurantController getInstance(){
		
		if(instance == null ){
			instance = new RestaurantController();
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
	
	private Map<Long, Restaurant> getRestaurants() {// it shuldn't by used
		return restaurants;
	}
	/**
	 * It should be called only once!!!
	 */
	public void setRestaurants(Map<Long, Restaurant> restaurants) {
		this.restaurants = restaurants;
	}
	
	public List<Restaurant> getRestaurantsList() {
		List<Restaurant> restaurantsList = new ArrayList<Restaurant>();
		for (Long restaurantId : getRestaurantsKey()) {
			restaurantsList.add(restaurants.get(restaurantId));
		}
		
		return restaurantsList;
	}
	/**
	 * 
	 * @param restaurantId
	 * @return Restaurant or null
	 */
	public Restaurant getRestaurant(Long restaurantId){
		return restaurants.get(restaurantId);
	}
	
	/**
	 * 
	 * @param cityId - id of the city
	 * @return new ArrayList with chosen restaurants
	 */
	public List<Restaurant> getRestaurantsInCity(Long cityId){
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		for (Long restaurantid : getRestaurantsKey()) {
			Restaurant restaurant = this.restaurants.get(restaurantid);
			if(restaurant.getCityId() == cityId){
				restaurants.add(restaurant);
			}
		}
		
		return restaurants;
	}
	/**
	 * 
	 * @param cityName - name of the city
	 * @return new ArrayList with chosen restaurants
	 */
	public List<Restaurant> getRestaurantsInCity(String cityName){
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		
		for (Long restaurantid : getRestaurantsKey()) {
			Restaurant restaurant = this.restaurants.get(restaurantid);
			if(restaurant.getCity().equals(cityName)){
				restaurants.add(restaurant);
			}
		}
		return restaurants;
	}
	
	
	private Set<Long> getRestaurantsKey(){
		return restaurants.keySet();
	}
	
	public void saveRestaurant(Restaurant restaurant){
		final Restaurant restaurantToSave = restaurant;
		
		storeService.saveRestaurant(restaurantToSave, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				
				restaurantToSave.setLogoImages(new ArrayList<ImageBlob>());
				restaurantToSave.setProfileImages(new ArrayList<ImageBlob>());
				restaurantToSave.setMenuImages(new ArrayList<ImageBlob>());
				
				restaurants.put(restaurantToSave.getId(), restaurantToSave); //add/change restaurant in our list
				
				Document.get().getElementById("load").setClassName(R.LOADED);
				notifyAllObservers();
				History.back();
			}
			@Override
			public void onFailure(Throwable caught) {	
			}
		});
	}
	
	public void deleteRestaurant(Restaurant restaurant){
		
		final Restaurant restaurantToDelete = restaurant;
		
		storeService.deleteRestaurant(restaurantToDelete, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				restaurants.remove(restaurantToDelete.getId());	 //removing restaurant from our list
				historyGoBack(2);
				notifyAllObservers();
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void cropImage(long restaurantId, ImageType imageType){
		
		
		final long myRestaurantId = restaurantId;
		final ImageType myImageType = imageType;
		final List<ImageBlob> oldImages = getImagesList(imageType, restaurantId);
		Cookies.removeCookie(R.IMAGE_TYPE);
		
		
		blobService.getImagesByType(myRestaurantId, myImageType, new AsyncCallback<List<ImageBlob>>() {
			@Override
			public void onSuccess(List<ImageBlob> result) {
				for (ImageBlob imageBlob : result) {
					boolean isIn = false;
					if(oldImages != null){
						for (ImageBlob oldImageBlob : oldImages) {
							if(oldImageBlob.getBlobKey().equals(imageBlob.getBlobKey())){
								isIn = true;
								
							}
						}
					}
					if (!isIn && imageBlob != null) {
						JQMContext.changePage(new CropImage(imageBlob, myRestaurantId), Transition.SLIDE);
					}
				}
				
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	
	public void cropImageApple(long restaurantId, ImageType imageType){
		
		final long myRestaurantId = restaurantId;
		final ImageType myImageType = imageType;
		
		Cookies.removeCookie(R.IMAGE_TYPE);
		
		blobService.getLastUploadedImage(restaurantId, myImageType, new AsyncCallback<ImageBlob>() {
			@Override
			public void onSuccess(ImageBlob result) {
				JQMContext.changePage(new CropImage(result, myRestaurantId), Transition.SLIDE);
				
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public List<ImageBlob> getImagesList(ImageType imageType, Long restaurantId){
		
		List<ImageBlob> oldImages;
		
		switch (imageType) {
		case LOGO:
			oldImages = restaurants.get(restaurantId).getLogoImages();
			break;
		case MENU:
			oldImages = restaurants.get(restaurantId).getMenuImages();
			break;
		case PROFILE:
			oldImages = restaurants.get(restaurantId).getProfileImages();
			break;
		default:
			oldImages = new ArrayList<ImageBlob>();
		}
		
		return oldImages;
	}
	
	public void afterCrop(Long restaurantId, ImageType imageType) {
		
		final long myRestaurantId = restaurantId;
		final ImageType myImageType = imageType;
		final List<ImageBlob> oldImages = getImagesList(imageType, restaurantId);
		
		blobService.getImagesByType(myRestaurantId, myImageType, new AsyncCallback<List<ImageBlob>>() {
			@Override
			public void onSuccess(List<ImageBlob> result) {
				for (ImageBlob imageBlob : result) {
					boolean isIn = false;
					if(oldImages != null){
						for (ImageBlob oldImageBlob : oldImages) {
							if(oldImageBlob.getBlobKey().equals(imageBlob.getBlobKey())){
								isIn = true;
							}
						}
					}
					if (!isIn) {
						List<ImageBlob> imagList = getImagesList(myImageType, myRestaurantId);
						if(imagList == null){
							imagList = new ArrayList<ImageBlob>();
						}
						imagList.add(imageBlob);
					}
				}
				JQMContext.changePage(restMapView.get(myRestaurantId));
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
	}
	
	public void setRestaurantsVisable(Map<Long,Boolean> restaurantMap){
		List<Restaurant> restaurantsToChange = new ArrayList<Restaurant>();
		
		Set<Long> restaurantKey = restaurantMap.keySet();
		for (Long restaurantId : restaurantKey) {
			if(restaurants.containsKey(restaurantId)){
				if(restaurantMap.get(restaurantId) != restaurants.get(restaurantId).isVisibleForApp()){
					restaurantsToChange.add(restaurants.get(restaurantId));
				}
			}
		}
		
		
		storeService.setVisibilityRestaurants(restaurantsToChange, new AsyncCallback<Map<Long, Boolean>>() {
			@Override
			public void onSuccess(Map<Long, Boolean> result) {
				Set<Long> changedRestaurantIds = result.keySet();
				for (Long changedRestaurantId : changedRestaurantIds) {
					restaurants.get(changedRestaurantId).setVisibleForApp(result.get(changedRestaurantId));
				}
				History.back();
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(Customization.CONNECTION_ERROR);	
				
			}
		});
		
		
	}

	public void saveRestaurants(List<Restaurant> restaurantsToSave) {
		final List<Restaurant> restaurantsSentToServer = restaurantsToSave;
		
		storeService.saveRestaurants(restaurantsSentToServer, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				for (Restaurant restaurant : restaurantsSentToServer) {
					restaurants.put(restaurant.getId(), restaurant);
				}
				Window.alert("Done");	
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(Customization.CONNECTION_ERROR);	
			}
		});
		
	}
	
	
	private static native void historyGoBack(int howMany) /*-{
		history.go(-howMany);
	}-*/;

	public void setEmptyBoard(Restaurant restaurant) {
		
		storeService.clearBoard(restaurant, new AsyncCallback<Restaurant>() {
			
			@Override
			public void onSuccess(Restaurant result) {
				restaurants.put(result.getId(), result);
				Document.get().getElementById("load").setClassName("hide");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Document.get().getElementById("load").setClassName("hide");
				Window.alert(Customization.CONNECTION_ERROR);
			}
		});
		
	}

	public void setMainImage(ImageBlob myImgBlob) {
		
		final ImageBlob imageBlob = myImgBlob;
		
		
		storeService.setMainImage(imageBlob, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				
				Long restaurantId = Long.parseLong(imageBlob.getRestaurantId());
				
				switch(imageBlob.getImageType()){
				case PROFILE:
					restaurants.get(restaurantId).setMainProfileImageString(imageBlob.getImageUrl());
					break;
				case LOGO:
					restaurants.get(restaurantId).setMainLogoImageString(imageBlob.getImageUrl());
					break;
				case MENU:
					restaurants.get(restaurantId).setMainMenuImageString(imageBlob.getImageUrl());
					break;
				}
				restMapView.get(restaurantId).checkChanges();
				Document.get().getElementById("load").setClassName("hide");
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
				
			}
		});
		
	}

}
