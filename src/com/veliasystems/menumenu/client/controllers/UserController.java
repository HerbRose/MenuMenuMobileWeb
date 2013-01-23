package com.veliasystems.menumenu.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.EmailService;
import com.veliasystems.menumenu.client.services.EmailServiceAsync;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.userInterface.administration.EditDataPanel;
/**
 * 
 * Controller to all {@link User} operations
 *
 */
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
	/**
	 * 
	 * @return Single instance of {@link UserController}
	 */
	public static UserController getInstance(){
		if(instance == null){
			instance = new UserController();
		}
		return instance;
	}
	/**
	 * get all {@link User}'s list
	 * @return List of {@link User}'s
	 */
	
	public List<User> getUserList() {
		List<User> userList = new ArrayList<User>();
        
        Set<String> usersEmailSet = users.keySet();
        
        for (String email : usersEmailSet) {
                userList.add(users.get(email));
        }
        return userList;
	}
	
	public void getUsersFromServer(final IObserver iObserver){
		PagesController.showWaitPanel();
		storeService.getUsers(new AsyncCallback<List<User>>() {

			@Override
			public void onFailure(Throwable caught) {
		
			}

			@Override
			public void onSuccess(List<User> result) {
				users.clear();
				for (User user : result) {
					users.put(user.getEmail(), user);
				}
				iObserver.newData();
			}
		});
	}
	
	public List<String> getEmailAdresses(){
		List<String> emailList = new ArrayList<String>();
		
		for (User user : getUserList() ) {
			emailList.add(user.getEmail());
		}
		return emailList;
	}
	/**
	 * 
	 * @return Map of {@link User}'s  as value, email as key
	 */
	public Map<String, User> getUsers() {
		return users;
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
	/**
	 * Add {@link User} to server
	 * @param user - {@link User} object
	 */
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
				Window.alert(Customization.CONNECTION_ERROR);
				
			}
		});
	}
	/**
	 * Change user data's 
	 * @param user - {@link User} object
	 */
	public void changeUserData(User user, String oldPassword, String newPassword){
		storeService.changeUserData(user, oldPassword, newPassword, new AsyncCallback<User>() {

			@Override
			public void onFailure(Throwable caught) {	
			}

			@Override
			public void onSuccess(User result) {
				if(result == null){
					Window.alert("bad login or user not found");
				}else{
					String login = result.getEmail();
					users.put(login, result);					
					Window.alert(Customization.EMAIL_SEND);
				}
			}
		});
	}
	/**
	 * 
	 * @param password - {@link User} password
	 * @return true if valid, else false
	 */
//	public boolean isValidPassword(String password){
//		boolean isCorrect = false;
//		
//		List<User> userList = getUserList();
//		String login = Cookies.getCookie(R.LOGIN);
//		for (User user : userList) {
//			if(user.getEmail().equals(login)){
//				if(user.getPassword().equals(password)) isCorrect = true;
//			}
//		}
//		
//		return isCorrect;
//	}
	/**
	 * 
	 * @param users - Map of {@link User}'s as values, email's as keys
	 */
	public void setUsers(Map<String, User> users) {
		this.users = users;
	}
	/**
	 * @deprecated
	 * @param login
	 * @return
	 */
	private User getUser(String login){
		
		Set<String> key = users.keySet();
		
		for (String userLogin : key) {
			if(userLogin.equalsIgnoreCase(login)) return users.get(userLogin);
		}
		
		return null;
	}
	/**
	 * Set {@link User} type 
	 * @param login - {@link User} login
	 */
	public void setUserType(String login) {
		
		
		Set<String> key = users.keySet();
		
		User logUser = null;
		
		for (String userLogin : key) {
			if(userLogin.equalsIgnoreCase(login)) logUser = users.get(userLogin);
		}
		if(logUser == null){
			
			return;
		}
		loggedUser = logUser.getEmail();
		if(logUser.isAdmin()) userType = UserType.ADMIN;
		else if(logUser.getCitiesId() != null) userType = UserType.AGENT;
		else userType = UserType.RESTAURATOR;
	}
	/**
	 * 
	 * @return {@link UserType}
	 */
	public UserType getUserType(){
		return userType;
	}
	/**
	 * 
	 * @return logged {@link User}
	 */
	public User getLoggedUser() {
		return users.get(loggedUser);
	}
	/**
	 * 
	 * @param email - {@link User} login
	 * @return true if {@link User} is in store
	 */
	public boolean isUserInStor(String email){
		return users.containsKey(email);
	}
	/**
	 * 
	 * @param chosenEmailList - List of email's addresses 
	 * @param from - Sender email
	 * @param subject - Subject of message
	 * @param message - message to send
	 */
	public void sendMail(List<String> chosenEmailList, String from,
			String subject, String message) {
		
		String s = "";
		for (String string : chosenEmailList) {
			s+= string+ ", ";
		}
		PagesController.showWaitPanel();
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
	/**
	 * Remove {@link User} from server
	 * @param user - email of user
	 */
	public void removeUser(String user, final IObserver iObserver){
		
		PagesController.showWaitPanel();
		storeService.removeUser(users.get(user), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
			}

			@Override
			public void onSuccess(String email) {
				if(!email.equalsIgnoreCase("null")) users.remove(email);
				notifyObserver(iObserver, false);
				PagesController.hideWaitPanel();
			}
		});
	}
	
	
	public boolean isUserType(UserType userType){
		switch (userType) {
		case ADMIN:
			return getLoggedUser().isAdmin();
		case AGENT:
			return getLoggedUser().isAgent();
		case RESTAURATOR:
			return getLoggedUser().isRestaurator();
		}
		
		return false;
	}
	
	
	public void saveUser(User user){
		PagesController.showWaitPanel();
		storeService.saveUser(user, new AsyncCallback<User>() {
			
			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
				Window.alert(Customization.ERROR);	
			}

			@Override
			public void onSuccess(User result) {
				PagesController.hideWaitPanel();
				Window.alert(Customization.OK);
			}
		});
	}
	
	private void notifyObserver(IObserver iObserver, boolean isNewData){
		if(isNewData) iObserver.newData();
		else iObserver.onChange();
	}
	
	
	private static native void consoleLog(String message)/*-{
		console.log(message);
	}-*/;
	
}
