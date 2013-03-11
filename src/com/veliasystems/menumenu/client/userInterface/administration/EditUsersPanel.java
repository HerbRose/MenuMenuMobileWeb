package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.controllers.UserType;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo.IMyChangeHendler;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class EditUsersPanel extends FlowPanel implements IManager, IObserver{
	
	
	private UserController userController = UserController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private CityController cityController = CityController.getInstance();
	
	private List<User> userList = new ArrayList<User>();
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	private List<City> cityList = new ArrayList<City>();
	
	private MyRestaurantInfoPanel contentPanel = new MyRestaurantInfoPanel();
	
	private MyListCombo userListCombo = new MyListCombo(false);
	private TextBox nameTextBox = new TextBox();
	private MyListCombo roleListCombo = new MyListCombo(true);
	private MyListCombo cityListCombo = new MyListCombo(true);
	private MyListCombo restaurantListCombo = new MyListCombo(true);
	
	private Label userLabel = new Label(Customization.USER_MAIL);
	private Label nameLabel = new Label(Customization.NAME);
	private Label roleLabel = new Label(Customization.ROLE);
	private Label cityLabel = new Label(Customization.CITY);	
	private Label restaurantLabel = new Label(Customization.RESTAURANTS);
	private Button saveButton = new Button(Customization.SAVE);
	private Button deleteButton = new Button(Customization.DELETE);
	
	private User user;	
	private int numberOfNewDataSuccess = 0;
	
	public EditUsersPanel() {

		setStyleName("barPanel", true);
		show(false);
		userController.addObserver(this);
		nameTextBox.addStyleName("myTextBox nameBox");
		nameTextBox.setEnabled(false);
		contentPanel.setStyleName("containerPanelAddRestaurant", true);
		userListCombo.addMyChangeHendler(new IMyChangeHendler() {
			
			@Override
			public void onChange() {
				showUserDetails( (int) userListCombo.getSelectedOrder());
			}
		});
		
		
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(user==null) return;
				if(roleListCombo.getCheckedList().isEmpty()){
					user.setRestaurator(true);
					user.setAgent(false);
					user.setAdmin(false);
					user.setCitiesId(null);
				} else {					
					if(roleListCombo.getCheckedList().contains((long) UserType.ADMIN.userTypeValue())){
						user.setAdmin(true);
					} else{
						user.setAdmin(false);
					}
					if(roleListCombo.getCheckedList().contains((long) UserType.AGENT.userTypeValue())){
						user.setAgent(true);
					}else{
						user.setAgent(false);
					}
					if(roleListCombo.getCheckedList().contains((long) UserType.RESTAURATOR.userTypeValue())){
						user.setRestaurator(true);
					} else{
						user.setRestaurator(false);
					}
					
				}
				
				if(!cityListCombo.getCheckedList().isEmpty()){
					user.setCitiesId(cityListCombo.getCheckedList());
					user.setAgent(true);
				} else {
					user.setCitiesId(null);
				}
				if(!restaurantListCombo.getCheckedList().isEmpty()){
					user.setRestaurantsId(restaurantListCombo.getCheckedList());
					user.setRestaurator(true);
				} else {
					user.setRestaurantsId(null);
				}
				
				userController.saveUser(user, getMe());
			}
		});
		
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(user==null) return;
				removeUser(user.getEmail());
			}
		});
		
	}
	
	private void showUserDetails(int id){
		user = userList.get(id);
		nameTextBox.setText("");
		
		
		roleListCombo.clearSelection();
		cityListCombo.clearSelection();
		restaurantListCombo.clearSelection();
		
		roleListCombo.getCheckedList().clear();
		cityListCombo.getCheckedList().clear();
		restaurantListCombo.getCheckedList().clear();
		
		if(user.getName() != null){
			nameTextBox.setText(user.getName() + " ");
		} 
		
		if(user.getSurname() != null){
			nameTextBox.setText(nameTextBox.getText() + user.getSurname());
		}
		
		if(user.isAdmin()){
			roleListCombo.selectItem(UserType.ADMIN.userTypeValue());
		} 
		
		if(user.isAgent()){
			roleListCombo.selectItem(UserType.AGENT.userTypeValue());	
		}
		
		if(user.isRestaurator()){
			roleListCombo.selectItem(UserType.RESTAURATOR.userTypeValue());
		}	
		
		if(user.getRestaurantsId()!=null){
			for (Long restaurantId : user.getRestaurantsId()) {
				restaurantListCombo.selectItem(restaurantId);
			}
		}	
		
		if(user.getCitiesId()!=null){
			for (Long cityId : user.getCitiesId()) {
				cityListCombo.selectItem(cityId);
			}
		}
	}
	
	private void removeUser(String userEmail){
		userController.removeUser(userEmail, getMe());
	}
	
	@Override
	public void clearData() {
	}

	@Override
	public String getName() {
		return Customization.USERS;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);	
	
		if(isVisable){
			clear();
			contentPanel.clear();
			nameTextBox.setText("");
			roleListCombo.clearSelection();
			cityListCombo.clearSelection();
			restaurantListCombo.clearSelection();
			
			numberOfNewDataSuccess = 0;
			PagesController.showWaitPanel();
			userController.getUsersFromServer(this);
			cityController.refreshCities(this);
			restaurantController.refreshRestaurants(this);
		
			contentPanel.setWidth( JS.getElementOffsetWidth(getParent().getElement())-40 );
			
			contentPanel.addItem(userLabel, userListCombo);
			contentPanel.addItem(nameLabel, nameTextBox);
			contentPanel.addItem(roleLabel, roleListCombo);
			contentPanel.addItem(cityLabel, cityListCombo);
			contentPanel.addItem(restaurantLabel, restaurantListCombo);
			
			contentPanel.addItem(saveButton, deleteButton);
			
			add(contentPanel);
		}
	}
	
	@Override
	public void onChange() {
			PagesController.showWaitPanel();
			cityListCombo.clear();
			restaurantListCombo.clear();
			roleListCombo.clear();
			userListCombo.clear();
			userListCombo.clearSelection();
			userList.clear();
			user = null;
			fillContent();
		
			
//			userController.getUsersFromServer(this);
//			cityController.refreshCities(this);
//			restaurantController.refreshRestaurants(this);
		
	}

	@Override
	public void newData() {
		
		numberOfNewDataSuccess ++;
		if(numberOfNewDataSuccess == 3){
			numberOfNewDataSuccess = 0;
			fillContent();
		}
		
	}
	
	private void fillContent(){
		
		
//		userList.clear();
//		restaurantList.clear();
//		cityList.clear();
//		roleListCombo.clear();
		
		
		restaurantList = restaurantController.getRestaurantsList();
		cityList = cityController.getCitiesList();
		
//		userListCombo.clear();
		
		roleListCombo.addListItem(roleListCombo.getNewCheckBoxItem(UserType.ADMIN.toString()), UserType.ADMIN.userTypeValue());
		roleListCombo.addListItem(roleListCombo.getNewCheckBoxItem(UserType.AGENT.toString()), UserType.AGENT.userTypeValue());
		roleListCombo.addListItem(roleListCombo.getNewCheckBoxItem(UserType.RESTAURATOR.toString()), UserType.RESTAURATOR.userTypeValue());
		
		
		String userName = userController.getLoggedUser().getEmail();
		for (User user : userController.getUserList()) {
			if(!user.getEmail().equalsIgnoreCase(userName)) userList.add(user);
		}	
		
		
		for (User user : userList) {
			userListCombo.addListItem(userListCombo.getNewItem(user.getEmail()) , userList.indexOf(user));
	
		}
		
		for (Restaurant item : restaurantList) {
			String nameToDisplay = item.getName() + " " + item.getAddress();
			restaurantListCombo.addListItem(restaurantListCombo.getNewCheckBoxItem(nameToDisplay), item.getId());
			
		}
		
		for (City city : cityList) {
			cityListCombo.addListItem(cityListCombo.getNewCheckBoxItem(city.getCity()), city.getId());
		}
		
		PagesController.hideWaitPanel();
	}
	
	
	private native int getHeight(String elementId)/*-{
		
		var children = $wnd.document.getElementById(elementId).childNodes;
		var length = children.length;
		var height = 0;
		for( i=0; i<=length-1; i++ ){
			height+= children[i].offsetHeight;
		}
		
		return height;
	}-*/;

	
	private IObserver getMe(){
		return this;
	}

}
