package com.veliasystems.menumenu.client.controllers;

import java.util.HashMap;
import java.util.Map;

import com.veliasystems.menumenu.client.entities.Restaurant;
/**
 * 
 * Controller to page operations
 *
 */

public class LoadedPageController {

	
	private Map<String, Integer> loadController;
	private boolean isEmptyList = false; //jeżeli nie ma zdjec na liscie pole jest ustawiane na true
	private static LoadedPageController instance = null;
	/**
	 * 
	 * @return Single instance of {@link LoadedPageController}
	 */
	public static LoadedPageController getInstance(){
		if(instance == null){
			instance = new LoadedPageController();
		}
		return instance;
	}
	
	private LoadedPageController() {
		loadController = new HashMap<String, Integer>();
	}
	/**
	 * 
	 * @param restaurantId - id of {@link Restaurant}
	 */
	public void addImage(String restaurantId){
		PagesController.showWaitPanel();
		Integer count = loadController.get(restaurantId);
		
		if(count != null){
			loadController.put(restaurantId, ++count);
		}else{
			loadController.put(restaurantId, 1);
		}
		
	}
	/**
	 * 
	 * @param restaurantId - id of {@link Restaurant}
	 */
	public void removeImage(String restaurantId){
		Integer count = loadController.get(restaurantId);
		
		if(count != null ){
			
			loadController.put(restaurantId, --count);
			
			if (isLast(restaurantId)) {
				PagesController.hideWaitPanel();
			}	
		}
	}
	
	private boolean isLast(String restaurantId){
		
		return loadController.get(restaurantId) <= 0;
	}

	public void clear(long id) {
		loadController.remove(id+"");
		isEmptyList = true;
	}
	/**
	 * Hides wait panel
	 * @param restId - id of {@link Restaurant}
	 */
	public void emptySwipe(String restId) {
		if(isEmptyList){
			PagesController.hideWaitPanel();
			isEmptyList = false ;
		}else isEmptyList = true;
	}
	
}
