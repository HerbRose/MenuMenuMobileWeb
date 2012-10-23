package com.veliasystems.menumenu.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.EmailService;
import com.veliasystems.menumenu.client.services.EmailServiceAsync;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class UserController {
	
	private List<IObserver> observers = new ArrayList<IObserver>();
	private static UserController instance  = null;
	private static final Logger log = Logger.getLogger(UserController.class.getName());
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	private final EmailServiceAsync emailService = GWT.create(EmailService.class);
	private Map<String, User> users = new HashMap<String, User>();
	private UserType userType;
	private User loggedUser;
	
	private UserController() {
	}
	public static UserController getInstance(){
		if(instance == null){
			instance = new UserController();
		}
		return instance;
	}
	
	public List<User> getUserList(){
		List<User> userList = new ArrayList<User>();
		
		Set<String> usersEmailSet = users.keySet();
		
		for (String email : usersEmailSet) {
			userList.add(users.get(email));
		}
		return userList;
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
		
		final User userToAdd = user;
		storeService.addUser(user, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				users.put(userToAdd.getEmail(), userToAdd);
				notifyAllObservers();
				History.back();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				log.warning(caught.getMessage());
				Window.alert("Connection error. Please try again later");
			}
		});
	}
	
	public void setUsers(Map<String, User> users) {
		this.users = users;
	}
	
	public void setUserType(String login) {
		loggedUser = users.get(login);
		
		if(loggedUser.isAdmin()) userType = UserType.ADMIN;
		else if(loggedUser.getCitiesId() != null) userType = UserType.AGENT;
		else userType = UserType.RESTAURATOR;
	}
	public UserType getUserType(){
		return userType;
	}
	
	public User getLoggedUser() {
		return loggedUser;
	}
	public boolean isUserInStor(String email){
		return users.containsKey(email);
	}
	public void sendMail(List<String> chosenEmailList, String from,
			String subject, String message) {
		
		String s = "";
		for (String string : chosenEmailList) {
			s+= string+ ", ";
		}
		
		Window.alert(s + "\n, from: " + from + "\n, subject: " + subject+"\n, message: " + message);
		
		emailService.sendEmail(chosenEmailList, chosenEmailList.get(0), message, subject, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				Window.alert("Done");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(Customization.CONNECTION_ERROR);
				
			}
		});
		
	}
	
	
}
