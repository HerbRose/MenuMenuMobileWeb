package com.veliasystems.menumenu.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class UserController {
	
	private List<IObserver> observers = new ArrayList<IObserver>();
	private static UserController instance  = null;
	private static final Logger log = Logger.getLogger(UserController.class.getName());
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	private Map<Long, User> users = new HashMap<Long, User>();
	
	private UserController() {
	}
	
	public static UserController getInstance(){
		if(instance == null){
			
			instance = new UserController();
		}
		
		return instance;
	}
	
	public void addObserver( IObserver observer){
		if(observer != null && !observers.contains(observer)){
			observers.add(observer);
		}
	}
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
	
	public void addUser(User user){
		storeService.addUser(user, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				notifyAllObservers();
				History.back();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				log.warning(caught.getMessage());
				Window.alert("error");
			}
		});
	}
	

	
	
	
}
