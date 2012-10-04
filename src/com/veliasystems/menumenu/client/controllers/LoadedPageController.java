package com.veliasystems.menumenu.client.controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;


public class LoadedPageController {

	
	private Map<String, Integer> loadController;
	private static LoadedPageController instance = null;
	
	public static LoadedPageController getInstance(){
		if(instance == null){
			instance = new LoadedPageController();
		}
		return instance;
	}
	
	private LoadedPageController() {
		loadController = new HashMap<String, Integer>();
	}
	
	public void addImage(String restaurantId){
		
		if(isRestaurant(restaurantId)){
			loadController.put(restaurantId, loadController.get(restaurantId)+1);
		}else{
			loadController.put(restaurantId, 1);
		}
		
	}
	
	public void removeImage(String restaurantId){
		
		if(isRestaurant(restaurantId)){
			
			loadController.put(restaurantId, loadController.get(restaurantId)-1);
			
			if (isLast(restaurantId)) {
				Document.get().getElementById("load").setClassName("loaded");
			}	
		}
	}
	
	private boolean isRestaurant(String restaurantId){
		return loadController.containsKey(restaurantId);
	}
	private boolean isLast(String restaurantId){
		
		return loadController.get(restaurantId) <= 0;
	}
	
}
