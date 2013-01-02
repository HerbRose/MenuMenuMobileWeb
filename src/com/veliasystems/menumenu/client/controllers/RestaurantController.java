package com.veliasystems.menumenu.client.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
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
import com.veliasystems.menumenu.client.ui.RestInfoScreen;
import com.veliasystems.menumenu.client.ui.administration.RestaurantsManagerPanel;
import com.veliasystems.menumenu.client.userInterface.CityInfoScreen;
import com.veliasystems.menumenu.client.userInterface.RestaurantImageView;



/**
 * @author mateusz
 * Controller to all operations on {@link Restaurant} 
 */
public class RestaurantController {

	private List<IObserver> observers = new ArrayList<IObserver>();
	private static final Logger log = Logger.getLogger(RestaurantController.class.getName());
	public static Map<Long, RestaurantImageView> restMapView = new HashMap<Long, RestaurantImageView>();
	private static RestaurantController instance = null ; //instance of controller
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	private BlobServiceAsync blobService = GWT.create(BlobService.class); 
	
	//private boolean fromCityView = true;
	private JQMPage lastOpenPage = null;
	
	private Map<Long, Restaurant> restaurants = new HashMap<Long, Restaurant>();

	private RestaurantController() {
		
	}
//	
//	public void setFromCityView(boolean from) {
//		this.fromCityView = from;
//	}
//	public boolean isFromCityView() {
//		return fromCityView;
//	}
	/**
	 * Sets given page to last visited
	 * @param lastOpenPage - {@link JQMPage} 
	 */
	public void setLastOpenPage(JQMPage lastOpenPage) {
		this.lastOpenPage = lastOpenPage;
	}
	/**
	 * Return last visited page
	 * @return {@link JQMPage}
	 */
	public JQMPage getLastOpenPage() {
		return lastOpenPage;
	}
	/**
	 * 
	 * @return Single instance of {@link RestaurantController}
	 */
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
	/**
	 * Return list of {@link Restaurant} from local datastore
	 * @return List of {@link Restaurant}
	 */
	public List<Restaurant> getRestaurantsList() {
		List<Restaurant> restaurantsList = new ArrayList<Restaurant>();
		for (Long restaurantId : getRestaurantsKey()) {
			restaurantsList.add(restaurants.get(restaurantId));
		}
		
		return restaurantsList;
	}
	/**
	 * 
	 * @param id - id of {@link Restaurant}
	 * @return name of  given {@link Restaurant}
	 */
	public String getRestaurantName(long id){
		return restaurants.get(id).getName();
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
	/**
	 * 
	 * @param restaurant - {@link Restaurant} to save
	 * @param isBack - boolean value, should we return to {@link CityInfoScreen} or not
	 */
	public void saveRestaurant(Restaurant restaurant, final boolean isBack){
		PagesController.showWaitPanel();
		storeService.saveRestaurant(restaurant, new AsyncCallback<Restaurant>() {
			@Override
			public void onSuccess(Restaurant result) {
				if(result.getLogoImages() == null){
					result.setLogoImages(new ArrayList<ImageBlob>());
				}
				if(result.getMenuImages() == null){
					result.setMenuImages(new ArrayList<ImageBlob>());
				}
				if(result.getProfileImages() == null){
					result.setProfileImages(new ArrayList<ImageBlob>());
				}	
				restaurants.put(result.getId(), result); //add/change restaurant in our list
				PagesController.hideWaitPanel();
				notifyAllObservers();
				
				if(isBack){
					Window.alert(Customization.RESTAURANTSAVED);
					JQMContext.changePage(CityController.cityMapView.get(result.getCityId()) , Transition.SLIDE);
					
				}
					
			}
			@Override
			public void onFailure(Throwable caught) {	
				Window.alert(Customization.CONNECTION_ERROR);
			}
		});
		
	}
	/**
	 * 
	 * @param restaurant - {@link Restaurant} to delete
	 * @param page - source of action
	 */
	public void deleteRestaurant(Restaurant restaurant, String page){
		
		final Restaurant restaurantToDelete = restaurant;
		final String lastPage = page;
		storeService.deleteRestaurant(restaurantToDelete, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				restaurants.remove(restaurantToDelete.getId());	 //removing restaurant from our list
				if(lastPage.equalsIgnoreCase(RestaurantsManagerPanel.class.getName())){
					
				}
				if(lastPage.equalsIgnoreCase(RestInfoScreen.class.getName())){
					historyGoBack(2);
				}
				notifyAllObservers();
			}
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}
	/**
	 * Remove {@link Restaurant} only locally
	 * @param restaurantId - id of {@link Restaurant}
	 */
	public void removeRestaurantLocally(Long restaurantId){
		
		restaurants.remove(restaurantId);
		
	}
	/**
	 * Remove list of {@link Restaurant}'s only locally
	 * @param restaurantIds - List of {@link Restaurant}
	 */
	public void removeRestaurantLocally(List<Restaurant> restaurantIds){
		
		for (Restaurant restaurant : restaurantIds) {
			removeRestaurantLocally(restaurant.getId());
		}
	}
	/**
	 * Change Page to {@link com.veliasystems.menumenu.client.userInterface.CropImage} 
	 * @param restaurantId - id of {@link Restaurant}
	 * @param imageType - type of image, specified in {@link ImageType}
	 * @param blobKey - {@link BlobKey} in {@link String} format
	 */
	public void cropImage(long restaurantId, ImageType imageType, String blobKey){
		if(Cookies.getCookie(R.IMAGE_TYPE) != null){
			Cookies.removeCookie(R.IMAGE_TYPE);
		}
		ImageBlob imageBlob = new ImageBlob(restaurantId+"", blobKey, new Date(), imageType);
		JQMContext.changePage(new com.veliasystems.menumenu.client.userInterface.CropImage(imageBlob), Transition.SLIDE);
	}
	/**
	 * Should not be used at all, but if {@link BlobKey} is null this method is called
	 * @param restaurantId - id of {@link Restaurant}
	 * @param imageType - type of Image, specified of {@link ImageType}
	 */
	public void cropImage(long restaurantId, ImageType imageType){
		
		if(Cookies.getCookie(R.IMAGE_TYPE) != null){
			Cookies.removeCookie(R.IMAGE_TYPE);
		}
		
		blobService.getLastUploadedImage(restaurantId, imageType, new AsyncCallback<ImageBlob>() {
			@Override
			public void onSuccess(ImageBlob imageBlob) {
				log.info("getLastUploadedImage onSuccess: " + imageBlob == null?"null":imageBlob.getImageUrl());
				if(imageBlob == null) Window.alert(Customization.CONNECTION_ERROR);
				else JQMContext.changePage(new com.veliasystems.menumenu.client.userInterface.CropImage(imageBlob), Transition.SLIDE);
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(Customization.CONNECTION_ERROR);
			}
		});
//		blobService.getImagesByType(myRestaurantId, myImageType, new AsyncCallback<List<ImageBlob>>() {
//			@Override
//			public void onSuccess(List<ImageBlob> result) {
//				for (ImageBlob imageBlob : result) {
//					boolean isIn = false;
//					if(oldImages != null){
//						for (ImageBlob oldImageBlob : oldImages) {
//							if(oldImageBlob.getBlobKey().equals(imageBlob.getBlobKey())){
//								isIn = true;
//								
//							}
//						}
//					}
//					if (!isIn && imageBlob != null) {
//						JQMContext.changePage(new CropImage(imageBlob, myRestaurantId), Transition.SLIDE);
//					}
//				}
//				
//			}
//			@Override
//			public void onFailure(Throwable caught) {
//			}
//		});
	}
	/**
	 * Crop image on Apple devices
	 * @param restaurantId - id of {@link Restaurant}
	 * @param imageType - type of Image, specified in {@link ImageType}
	 */
	public void cropImageApple(long restaurantId, ImageType imageType){
		
		final long myRestaurantId = restaurantId;
		final ImageType myImageType = imageType;
		
		Cookies.removeCookie(R.IMAGE_TYPE);
		
		blobService.getLastUploadedImage(restaurantId, myImageType, new AsyncCallback<ImageBlob>() {
			@Override
			public void onSuccess(ImageBlob result) {
				if(result == null) Window.alert(Customization.CONNECTION_ERROR);
				else JQMContext.changePage(new com.veliasystems.menumenu.client.userInterface.CropImage(result), Transition.SLIDE);
				
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(Customization.CONNECTION_ERROR);
			}
		});
	}
	/**
	 * Return list with {@link ImageBlob}'s by given type and {@link Restaurant} id
	 * @param imageType - type of image, specified in {@link ImageType}
	 * @param restaurantId - id of {@link Restaurant}
	 * @return List of {@link ImageBlob}
	 */
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
	
	private void setNewImageList(ImageBlob newImageBlob){
		
	}
	
	/**
	 * Remove (if exist) old {@link ImageBlob} from the image list (list is dependent of ImageTape).<br>
	 * Add new {@link ImageBlob} to the list (list is dependent of ImageTape).<br>
	 * If {@link ImageBlob#getImageType()} of <code> newImageBlob</code> is equal to {@link ImageType#LOGO} the image is set as main.
	 * @param newImageBlob - new {@link ImageBlob}
	 * @param oldImageBlob - old {@link ImageBlob}
	 */
	public void afterCrop(ImageBlob newImageBlob, ImageBlob oldImageBlob) {
		
		if(newImageBlob == null){
			Window.alert(Customization.CONNECTION_ERROR);
			return;
		}
		
		List<ImageBlob> imageBlobs = getImagesList(newImageBlob.getImageType(), Long.parseLong(newImageBlob.getRestaurantId()));
		if(imageBlobs == null){
			imageBlobs = new ArrayList<ImageBlob>();
			
		}
		imageBlobs.add(newImageBlob);

		if(newImageBlob.getImageType() == ImageType.LOGO){ //jezeli logo to ustawiamy jako glowne
			storeService.setMainImage(newImageBlob, new AsyncCallback<Restaurant>() {
				
				@Override
				public void onSuccess(Restaurant restaurant) {
					
					restaurants.get(restaurant.getId()).setMainLogoImageString(restaurant.getMainLogoImageString());
					JQMContext.changePage(restMapView.get(restaurant.getId()), Transition.SLIDE);
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					PagesController.hideWaitPanel();
					Window.alert(Customization.CONNECTION_ERROR);
				}
			});
		}else{ //jezeli nie logo to ustawiamy jako glowne
			if(oldImageBlob != null){ // dla pickup'a, usuwanie starego zdjecia (wymagany byl restart po uploudzie wiec na liscie jest zdjecie sprzed cropa)
				ImageBlob imageBlobToDelete = null;
				for (ImageBlob imageBlob : imageBlobs) {
					if(imageBlob.getBlobKey().equals(oldImageBlob.getBlobKey())) {
						imageBlobToDelete = imageBlob;
						break;
					}
				}
				imageBlobs.remove(imageBlobToDelete);
			}
			JQMContext.changePage(restMapView.get(Long.parseLong(newImageBlob.getRestaurantId())), Transition.SLIDE);
		}
		
		
	}
	/**
	 * 
	 * @param restaurantMap - Map of id's {@link Restaurant}'s and {@link Boolean} value of visibility for mobile device
	 */
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
	/**
	 * Save list of {@link Restaurant}'s to datastore
	 * @param restaurantsToSave - List of {@link Restaurant}, which must be saved
	 */
	public void saveRestaurants(List<Restaurant> restaurantsToSave) {
		final List<Restaurant> restaurantsSentToServer = restaurantsToSave;
		
		if(restaurantsSentToServer == null || restaurantsSentToServer.isEmpty()) return;
		
		PagesController.showWaitPanel();
		storeService.saveRestaurants(restaurantsSentToServer, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				for (Restaurant restaurant : restaurantsSentToServer) {
					restaurants.put(restaurant.getId(), restaurant);
				}
				PagesController.hideWaitPanel();
				Window.alert("Done");	
			}
			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
				Window.alert(Customization.CONNECTION_ERROR);	
			}
		});
		
	}
	
	
	private static native void historyGoBack(int howMany) /*-{
		history.go(-howMany);
	}-*/;
	/**
	 * Set empty image
	 * @param restaurant - {@link Restaurant} object
	 * @param imageType - type of Image, specified in {@link ImageType}
	 */
	public void setEmptyBoard(Restaurant restaurant, ImageType imageType) {
		
		storeService.clearBoard(restaurant, imageType, new AsyncCallback<Restaurant>() {
			
			@Override
			public void onSuccess(Restaurant restaurant) {
				restaurants.get(restaurant.getId()).setMainMenuImageString(restaurant.getMainMenuImageString());
//				restaurants.get(restaurant.getId()).setMainLogoImageString(restaurant.getMainLogoImageString());
//				restaurants.get(restaurant.getId()).setMainProfileImageString(restaurant.getMainProfileImageString());
				
				restMapView.get(restaurant.getId()).checkChanges();
				PagesController.hideWaitPanel();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
				Window.alert(Customization.CONNECTION_ERROR);
			}
		});
		
	}
	/**
	 * Set {@link ImageBlob} as main image
	 * @param imgBlob - {@link ImageBlob}
	 */
	public void setMainImage(ImageBlob imgBlob) {
		
		final ImageType imageType = imgBlob.getImageType();
		
		PagesController.showWaitPanel();
		storeService.setMainImage(imgBlob, new AsyncCallback<Restaurant>() {
			
			@Override
			public void onSuccess(Restaurant restaurant) {
				
				switch(imageType){
				case PROFILE:
					restaurants.get(restaurant.getId()).setMainProfileImageString(restaurant.getMainProfileImageString());
					break;
				case LOGO:
					restaurants.get(restaurant.getId()).setMainLogoImageString(restaurant.getMainLogoImageString());
					break;
				case MENU:
					restaurants.get(restaurant.getId()).setClearBoard(restaurant.isClearBoard());
					restaurants.get(restaurant.getId()).setMainMenuImageString(restaurant.getMainMenuImageString());
					break;
				}
				
				restMapView.get(restaurant.getId()).checkChanges();
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
