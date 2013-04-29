package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListItem;

/**
 * 
 * @author jakub
 *	new panel, read - only
 */
public class UsersPanel extends FlowPanel implements IManager, IObserver{
	
	private UserController userController = UserController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private CityController cityController = CityController.getInstance();
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	private List<City> cityList = new ArrayList<City>();
	
	private int numberOfNewDataSuccess = 0;
	
	public UsersPanel() {
		setStyleName("barPanel", true);
		show(false);
		userController.addObserver(this);
	}
	
	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return Customization.USERS_PANEL;
	}

	@Override
	public void show(boolean isVisable) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void onChange() {
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
		clear();
		
		String userName = userController.getLoggedUser().getEmail();
		restaurantList = restaurantController.getRestaurantsList();
		cityList = cityController.getCitiesList();
		
		for (final User user : userController.getUserList()) {
			if(!user.getEmail().equalsIgnoreCase(userName)){
				
				MyListItem userItem = new MyListItem();
				userItem.setText(user.getEmail());
				userItem.setStyleName("itemList noFocus", true);
				userItem.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						showDialogBox(user);
					}
				});
				add(userItem);
			}
		}
		PagesController.hideWaitPanel();
	}
	
	private void showDialogBox(User user){
		final DialogBox userInfo = new DialogBox();
		Button closeButton = new Button();
		
		FlowPanel infoPanel = new FlowPanel();
		FlowPanel topPanel = new FlowPanel();
		FlowPanel bottomPanel = new FlowPanel();
		FlowPanel nameWrapper = new FlowPanel();
		FlowPanel roleWrapper = new FlowPanel();
		FlowPanel restaurantsWrapper = new FlowPanel();
		FlowPanel citiesWrapper = new FlowPanel();
		FlowPanel restaurants = new FlowPanel();
		FlowPanel cities = new FlowPanel();
		
		Label nameLabel = new Label(Customization.NAME);
		Label roleLabel = new Label(Customization.ROLE);
		Label restaurantsLabel = new Label(Customization.RESTAURANTS + " : ");
		Label citiesLabel = new Label(Customization.CITY + " : " );
		Label emaiLabelTop = new  Label();
		
		infoPanel.setStyleName("userInfoPanel");
		topPanel.setStyleName("usersTopPanel");
		closeButton.setStyleName("usersCloseButton");
		emaiLabelTop.setStyleName("emailLabelTop");
		
		
		bottomPanel.setStyleName("usersBottomPanel");
		nameWrapper.setStyleName("userPanelSingleWrapper");
		roleWrapper.setStyleName("userPanelSingleWrapper");
		restaurantsWrapper.setStyleName("userPanelSingleWrapper");
		citiesWrapper.setStyleName("userPanelSingleWrapper");
		restaurants.setStyleName("userDetails");
		cities.setStyleName("userDetails");
		
		closeButton.setText(Customization.CANCEL);	
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				userInfo.hide();
			}
		});
		
		emaiLabelTop.setText(user.getEmail());
		
		if(user.getName() != null){
			nameLabel.setText(nameLabel.getText() + " : " + user.getName());
		}
		
		if(user.isAdmin()){
			roleLabel.setText(roleLabel.getText() + " : " + Customization.ADMIN );
			
		}  else {
			String role = "";
			if(user.isAgent()){
				role += Customization.AGENT + " ";
				fillCitiesForUser(user, cities);	
			}
			if(user.isRestaurator()){
				fillRestaurantsForUser(user, restaurants);
				role += Customization.RESTAURATOR;
			}
			
			roleLabel.setText(roleLabel.getText() + " : " + role);
		}
		
		topPanel.add(closeButton);
		topPanel.add(emaiLabelTop);
		
		
		nameWrapper.add(nameLabel);
		roleWrapper.add(roleLabel);
		restaurantsWrapper.add(restaurantsLabel);
		restaurantsWrapper.add(restaurants);
		citiesWrapper.add(citiesLabel);
		citiesWrapper.add(cities);
		
		bottomPanel.add(nameWrapper);
		bottomPanel.add(roleWrapper);
		bottomPanel.add(restaurantsWrapper);
		bottomPanel.add(citiesWrapper);
				
		
		infoPanel.add(topPanel);
		infoPanel.add(bottomPanel);
		
		userInfo.add(infoPanel);
		userInfo.center();
		userInfo.show();
	}
	
	private void fillRestaurantsForUser(User user, FlowPanel whereAdd){
		if(user.getRestaurantsId() == null) return;
		for (Restaurant restaurant : restaurantList) {
			if(user.getRestaurantsId().contains(restaurant.getId())){
				MyListItem myListItem = new MyListItem(restaurant.getName());
				myListItem.setStyleName("itemList noFocus userDetailsItem", true);
				whereAdd.add(myListItem);
			}
			
		}
	}
	
	private void fillCitiesForUser(User user, FlowPanel whereAdd){
		if(user.getCitiesId() == null) return;
		for (City city : cityList) {
			if(user.getCitiesId().contains(city.getId())){
				MyListItem myListItem = new MyListItem(city.getCity());
				myListItem.setStyleName("itemList noFocus userDetailsItem", true);
				whereAdd.add(myListItem);
			}
			
		}
	}
}
