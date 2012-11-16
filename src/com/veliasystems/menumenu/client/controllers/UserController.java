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
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
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
	private String loggedUser;
	
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
	
	public Map<String, User> getUsers() {
		return users;
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
		
		PagesController.showWaitPanel();
		
		storeService.addUser(user, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				users.put(userToAdd.getEmail(), userToAdd);
				notifyAllObservers();
				PagesController.hideWaitPanel();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				log.warning(caught.getMessage()); 
				PagesController.hideWaitPanel();
				Window.alert("Connection error. Please try again later");
				
			}
		});
	}
	
	public void changeUserData(User user){
		User userToChange = getLoggedUser();
		storeService.changeUserData(user, userToChange.getEmail(), new AsyncCallback<User>() {

			@Override
			public void onFailure(Throwable caught) {	
			}

			@Override
			public void onSuccess(User result) {
				if(result == null){
					
				}else{
					String login = result.getEmail();
					users.put(login, result);					
					Window.alert(Customization.CHANGE_OK);
				}
			}
		});
	}
	
	public boolean isValidPassword(String password){
		boolean isCorrect = false;
		
		List<User> userList = getUserList();
		String login = Cookies.getCookie(R.LOGIN);
		for (User user : userList) {
			if(user.getEmail().equals(login)){
				if(user.getPassword().equals(password)) isCorrect = true;
			}
		}
		
		return isCorrect;
	}
	
	public void setUsers(Map<String, User> users) {
		this.users = users;
	}
	
	private User getUser(String login){
		
		Set<String> key = users.keySet();
		
		for (String userLogin : key) {
			if(userLogin.equalsIgnoreCase(login)) return users.get(userLogin);
		}
		
		return null;
	}
	
	public void setUserType(String login) {
		
		
		Set<String> key = users.keySet();
		
		User logUser = null;
		
		for (String userLogin : key) {
			if(userLogin.equalsIgnoreCase(login)) logUser = users.get(userLogin);
		}
		loggedUser = logUser.getEmail();
		if(logUser.isAdmin()) userType = UserType.ADMIN;
		else if(logUser.getCitiesId() != null) userType = UserType.AGENT;
		else userType = UserType.RESTAURATOR;
	}
	public UserType getUserType(){
		return userType;
	}
	
	public User getLoggedUser() {
		return users.get(loggedUser);
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
		
		emailService.sendEmail(chosenEmailList, chosenEmailList.get(0), message, subject, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Window.alert(Customization.EMAIL_SEND);
				notifyAllObservers();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(Customization.CONNECTION_ERROR);
				
			}
		});
		
		PagesController.hideWaitPanel();
		
	}
	
	public void removeUser(String user){
		
		PagesController.showWaitPanel();
		storeService.removeUser(users.get(user), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
			}

			@Override
			public void onSuccess(String email) {
				if(!email.equalsIgnoreCase("null")) users.remove(email);
				notifyAllObservers();
				PagesController.hideWaitPanel();
			}
		});
	}
	
	
}
