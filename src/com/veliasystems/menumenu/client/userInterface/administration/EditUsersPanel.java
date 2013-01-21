package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
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
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class EditUsersPanel extends FlowPanel implements IManager, IObserver{
	
	
	private UserController userController = UserController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private CityController cityController = CityController.getInstance();
	
	private List<User> userList = new ArrayList<User>();
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	private List<City> cityList = new ArrayList<City>();

	
	private Map<String, FlowPanel> usersPanel = new HashMap<String, FlowPanel>();
	private MyRestaurantInfoPanel container;
	
	
	private int numberOfNewDataSuccess = 0;
	
	public EditUsersPanel() {

		setStyleName("barPanel", true);
		show(false);
		userController.addObserver(this);
	}
	
	private void removeUser(String userEmail){
		userController.removeUser(userEmail, this);
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
			numberOfNewDataSuccess = 0;
			PagesController.showWaitPanel();
			userController.getUsersFromServer(this);
			cityController.refreshCities(this);
			restaurantController.refreshRestaurants(this);
		}
	}
	
	private void setUsersList(){
		clear();
		for(final User user : userList){
			FlowPanel userDetails = new FlowPanel(); //div na detale miasta
			userDetails.setStyleName("cityTableDiv", true);
			userDetails.getElement().setId("userToEdit"+user.getEmail());
			
			FlowPanel userListDiv = new FlowPanel(); //div ze strzalka nazwa dzielnicy i detale miasta
			userListDiv.setStyleName("cityListDiv", true);
			
			FlowPanel userHeaderDiv = new FlowPanel(); //div na strzalke i nazwe uzytkownika
			userHeaderDiv.setStyleName("cityHeaderDiv", true);
			
			Image blackArrowImage = new Image("img/blackArrow.png"); //strzalka
			blackArrowImage.setStyleName("blackArrowImage", true);
			
			final ToggleButton arrowToggleButton = new ToggleButton(blackArrowImage);
			arrowToggleButton.setStyleName("arrowToggleButton", true);
			
			arrowToggleButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(arrowToggleButton.isDown()){
						showUserTable(usersPanel.get(user.getEmail()), arrowToggleButton, true);
					}else {
						showUserTable(usersPanel.get(user.getEmail()), arrowToggleButton, false);
					}
				}
			});
			
			
			Label userNameLabel = new Label(user.getEmail()); //nazwa uzytkownika
			userNameLabel.setStyleName("cityNameLabel", true);
			
			usersPanel.put(user.getEmail(), userDetails);
			
			userHeaderDiv.add(arrowToggleButton);
			userHeaderDiv.add(userNameLabel);
			userListDiv.add(userHeaderDiv);
			userListDiv.add(userDetails);
			add(userListDiv);
			
		}
	}

	public void showUserTable( final Widget widget, ToggleButton arrowToggleButton, boolean isVisable) {
		
		if(isVisable){
			int height = getHeight(widget.getElement().getId()); 
			widget.setHeight(height+20+ "px");
			Timer timer = new Timer() {
				
				@Override
				public void run() {
					widget.getElement().getStyle().setOverflow(Overflow.VISIBLE);
				}
			};
			timer.schedule(1000);
		}else{
			widget.setHeight("0px");
			widget.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		}
		
		
		if(arrowToggleButton != null){
			arrowToggleButton.setStyleName("arrowToggleButtonShow", isVisable);
			arrowToggleButton.setStyleName("arrowToggleButtonHide", !isVisable);
		}
		
	}
	
	private void fillUsersDetails(FlowPanel userDatails, final User user){
		
			Label emailLabel= new Label(Customization.USER_MAIL);
			TextBox emailTextBox;
			Label data = new Label(Customization.NAME);
			TextBox dataTextBox;
			 
			Label role = new Label(Customization.ROLE);
			final MyListCombo roleCombo = new MyListCombo(true);
			Label cities = new Label(Customization.CITY);
			final MyListCombo citiesCombo = new MyListCombo(true);
			Label restaurants = new Label(Customization.RESTAURANTS);
			final MyListCombo restaurantsCombo = new MyListCombo(true);
			 
			Button save = new Button();
			save.setText(Customization.SAVE);
			
			Button delete = new Button(); 
			delete.setText(Customization.DELETE);

		
			emailTextBox = new TextBox();
			emailTextBox.setStyleName("myTextBox nameBox");
			emailTextBox.setText(user.getEmail());
			emailTextBox.setEnabled(false);
			
			dataTextBox = new  TextBox();
			dataTextBox.setStyleName("myTextBox nameBox");
			String dataText = "";
			if(user.getName() != null && user.getSurname() != null){
				dataText = user.getName() + " " + user.getSurname();
			}
			dataTextBox.setText(dataText);
			dataTextBox.setEnabled(false);
			
			
			for (UserType userType : UserType.values()) {
				roleCombo.addListItem(roleCombo.getNewCheckBoxItem(userType.toString()), userType.userTypeValue());
			}
			if(user.isAdmin()){
				roleCombo.selectItem(UserType.ADMIN.ordinal());
			}
			if(user.isAgent()){
				roleCombo.selectItem(UserType.AGENT.ordinal());
			}
			if(user.isRestaurator()){
				roleCombo.selectItem(UserType.RESTAURATOR.ordinal());
			}
			
			
			for (City city : cityList) {
				citiesCombo.addListItem(citiesCombo.getNewCheckBoxItem(city.getCity() + " " + city.getCountry()), city.getId());
			}
			
			if(user.getCitiesId() != null){
				for (long cityId : user.getCitiesId()) {
					citiesCombo.selectItem(cityId);
				}
			}
			
			
			for (Restaurant restaurant : restaurantList) {
				restaurantsCombo.addListItem(restaurantsCombo.getNewCheckBoxItem(restaurant.getName() + " " + restaurant.getAddress()), restaurant.getId());
			}
			
			if(user.getRestaurantsId() != null){
				for (long restId : user.getRestaurantsId()) {
					restaurantsCombo.selectItem(restId);
				}
			}
			
			
			save.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(roleCombo.getCheckedList().contains((long) UserType.ADMIN.userTypeValue())){
						user.setAdmin(true);
					} else{
						user.setAdmin(false);
					}
					if(roleCombo.getCheckedList().contains((long) UserType.AGENT.userTypeValue())){
						user.setAgent(true);
						user.setCitiesId(citiesCombo.getCheckedList());
					}else{
						user.setAgent(false);
					}
					if(roleCombo.getCheckedList().contains((long) UserType.RESTAURATOR.userTypeValue())){
						user.setRestaurator(true);
						user.setRestaurantsId(restaurantsCombo.getCheckedList());
					} else{
						user.setRestaurator(false);
					}
				
					userController.saveUser(user);
				}
				
				
			});
			
			delete.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					removeUser(user.getEmail());
				}
			});
			
			container = new MyRestaurantInfoPanel();
			container.setStyleName("containerPanelAddRestaurant", true);

			container.addItem(emailLabel, emailTextBox);
			container.addItem(data, dataTextBox);
			container.addItem(role, roleCombo);
			container.addItem(cities, citiesCombo);
			container.addItem(restaurants, restaurantsCombo);
			container.addItem(save, delete);
			container.setWidth( JS.getElementOffsetWidth(getParent().getElement())-40 );
			userDatails.add(container);
	}
	
	
	@Override
	public void onChange() {
			fillContent();
	}

	@Override
	public void newData() {
		numberOfNewDataSuccess ++;
		if(numberOfNewDataSuccess == 3){
			fillContent();
		}
		
	}
	
	private void fillContent(){
		userList.clear();
		restaurantList.clear();
		cityList.clear();
		
		restaurantList = restaurantController.getRestaurantsList();
		cityList = cityController.getCitiesList();
		
		String userName = userController.getLoggedUser().getEmail();
		for (User user : userController.getUserList()) {
			if(!user.getEmail().equalsIgnoreCase(userName)) userList.add(user);
		}	
		setUsersList();
		for (User user : userList) {
			fillUsersDetails(usersPanel.get(user.getEmail()), user);
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


}
