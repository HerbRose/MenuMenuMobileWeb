package com.veliasystems.menumenu.client.controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.userInterface.CityInfoScreen;
import com.veliasystems.menumenu.client.userInterface.CityListScreen;
import com.veliasystems.menumenu.client.userInterface.RestaurantImageView;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp;
import com.veliasystems.menumenu.client.userInterface.myWidgets.TouchGetter;

public class PagesController {
	
	public static MyPopUp MY_POP_UP = new MyPopUp();
	private static Map<Pages, JQMPage> pagesMap = new HashMap<Pages, JQMPage>();
	public static int contentWidth = getBodyOffsetWidth();
	private static CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private CookieController cookieController = CookieController.getInstance();
	
	private static PagesController instance = null;
	
	private PagesController() {
		// TODO Auto-generated constructor stub
	}
	
	public static PagesController getInstance() {
		if(instance == null) instance = new PagesController();
		return instance;
	}
	
	public static TouchGetter TOUCH_GETTER = new TouchGetter();
	/**
	 * set the page only if local page is null
	 * @param enumPage
	 * @return the page or <code>new JQMPage()</code> if enumPage doesn't known
	 */
	public static JQMPage getPage(Pages enumPage){
		if(pagesMap.containsKey(enumPage)){
			return pagesMap.get(enumPage);
		}else{
			
			JQMPage newPage = Pages.getMyPage(enumPage);
			if(newPage != null){
				pagesMap.put(enumPage, newPage);
				return newPage;
			}
			return new JQMPage();
		}
	}
	/**
	 * 
	 * @param cityId the {@link City} id
	 * @return if the city with the given id exists return {@link CityInfoScreen} else return {@link CityListScreen}
	 */
	public static JQMPage getCityInfoScreenPage(Long cityId){
		CityInfoScreen cityInfoScreen = CityController.cityMapView.get(cityId);
		
		if(cityInfoScreen == null){
			City city = cityController.getCity(cityId);
			if(city != null){
				cityInfoScreen = new CityInfoScreen(city);
				CityController.cityMapView.put(cityId, cityInfoScreen);
				return cityInfoScreen;
			}else{
				return getPage(Pages.PAGE_CITY_LIST);
			}
		}
		
		return cityInfoScreen;
	}
	
	public static JQMPage getRestaurantImageView(long restaurantId, JQMPage back){
		RestaurantImageView restaurantView = RestaurantController.restMapView.get(restaurantId);
		
		if(restaurantView == null){
			Restaurant restaurant = RestaurantController.getInstance().getRestaurant(restaurantId);
			if(restaurant != null){
				restaurantView = new RestaurantImageView(restaurant, back);
				RestaurantController.restMapView.put(restaurantId, restaurantView);
				return restaurantView;
			}else{
				return getCityInfoScreenPage(restaurant.getCityId());
			}
		}
		return restaurantView;
			
	}
	/**
	 * get div of name <strong>"load"</strong> and sets display to none
	 */
	public static void showWaitPanel(){
		Document.get().getElementById("load").setClassName(R.LOADING);
	}
	/**
	 *  get div of name <strong>"load"</strong> and sets display to block
	 */
	public static void hideWaitPanel(){
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	public void changePageAfterLogin(){
		String restaurantIdString = cookieController.getCookie(CookieNames.RESTAURANT_ID);
		
		if(!restaurantIdString.isEmpty()){
			
			Long restaurantId;
			
			restaurantId = Long.parseLong(restaurantIdString);
			Restaurant lastOpenRestaurant = RestaurantController.getInstance().getRestaurant(restaurantId);
			
			if(cookieController.getCookie(CookieNames.IS_PICUP_USED).equals("true")){
				String imageType = cookieController.getCookie(CookieNames.IMAGE_TYPE);
				restaurantController.cropImageApple(restaurantId, ImageType.valueOf(imageType));
			}else if(lastOpenRestaurant == null ){
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_HOME));
			}else {
				RestaurantImageView restaurantView;
				
				if(RestaurantController.restMapView.get(restaurantId.longValue())!=null){
					restaurantView = RestaurantController.restMapView.get(restaurantId);
				}else{
					restaurantView = new RestaurantImageView(lastOpenRestaurant,PagesController.getPage(Pages.PAGE_HOME));
					RestaurantController.restMapView.put(lastOpenRestaurant.getId(), restaurantView);
				}
				
//				if(RestaurantController.restMapView.get(lastPageId.longValue())!=null){
//					restaurantView = RestaurantController.restMapView.get(lastPageId);
//				}
//				
				String imageType = cookieController.getCookie(CookieNames.IMAGE_TYPE); 
				
				if(imageType.isEmpty()){
					JQMContext.changePage(restaurantView, Transition.SLIDE);
				}else{
					restaurantController.cropImageApple(restaurantId, ImageType.valueOf(imageType));
				}
			}
		}else{
			JQMContext.changePage(PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE);
		}
	}
	
	
	private static native int getBodyOffsetWidth()/*-{
		return $wnd.document.body.offsetWidth;
	}-*/;
}

