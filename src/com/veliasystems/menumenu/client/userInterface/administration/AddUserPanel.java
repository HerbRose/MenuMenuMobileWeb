package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.Util;
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
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class AddUserPanel extends FlowPanel implements IManager, IObserver {

	
	private Label mailLabel;
	private Label roleLabel;
	private Label restaurantChosenLabel; 
	private Label citiesChosenLabel; 
	
	private TextBox inputEmailRestaurator = new TextBox();

	private JQMButton saveUserButton;
		
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private CityController cityController = CityController.getInstance();
	private UserController userController = UserController.getInstance();
	
	private MyListCombo roleListCombo = new MyListCombo(true);
	private MyListCombo restaurantListCombo = new MyListCombo(true);
	private MyListCombo citiesListCombo = new MyListCombo(true);
	
	private MyRestaurantInfoPanel container = new MyRestaurantInfoPanel();;

	/**
	 * status of synchronize data, if 0 - no new data, if 1 one ne data (city or restaurant), if 2 all required data synchronized 
	 */
	private int synchronizeStatus = 0;
	
	public AddUserPanel() {
		
		
		setStyleName("barPanel", true);
		show(false);
		
		mailLabel = new Label(Customization.INPUT_EMAIL);
		roleLabel = new Label(Customization.USER_ROLE);
		citiesChosenLabel = new Label(Customization.CHOOSE_CITIES);
		restaurantChosenLabel = new Label(Customization.CHOOSE_RESTAURANTS);

		roleListCombo.addMyClickHendler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!citiesListCombo.getCheckedList().isEmpty()){
					roleListCombo.selectItem((long) UserType.AGENT.userTypeValue(), true);
				}
				if(!restaurantListCombo.getCheckedList().isEmpty()){
					roleListCombo.selectItem((long) UserType.RESTAURATOR.userTypeValue(), true);
				}
				
			}
		});
		
		
		inputEmailRestaurator.addStyleName("myTextBox nameBox");
		inputEmailRestaurator.getElement().setAttribute("placeHolder", Customization.EMAIL_PLACEHOLDER);	
		
		saveUserButton = new JQMButton(Customization.SAVE);
		saveUserButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validData()) {
					User newUser = new User(inputEmailRestaurator.getValue().trim());
					
					if(roleListCombo.getCheckedList().isEmpty()){
						newUser.setRestaurator(true);
					}else{
						
						for (Long userType : roleListCombo.getCheckedList()) {
							if(userType == (long) UserType.ADMIN.userTypeValue()){
								newUser.setAdmin(true);
							}
							if(userType == (long) UserType.AGENT.userTypeValue()){
								newUser.setAgent(true);
							}
							if(userType == (long) UserType.RESTAURATOR.userTypeValue()){
								newUser.setRestaurator(true);
							}
						}
					}
					if(!citiesListCombo.getCheckedList().isEmpty()){
						newUser.setCitiesId(citiesListCombo.getCheckedList());
						newUser.setAgent(true);
					}
					if(!restaurantListCombo.getCheckedList().isEmpty()){
						newUser.setRestaurantsId(restaurantListCombo.getCheckedList());
						newUser.setRestaurator(true);
					}
					
					newUser.setAddedByUser(userController.getLoggedUser().getEmail());
					addUser(newUser);
				}
			}
		});

		container.setStyleName("containerPanelAddRestaurant", true);

		container.addItem(mailLabel, inputEmailRestaurator);
		container.addItem(roleLabel, roleListCombo);
		container.addItem(restaurantChosenLabel, restaurantListCombo);
		container.addItem(citiesChosenLabel, citiesListCombo);	
	
		container.add(saveUserButton);
		
		add(container);
	}

	private void addUser(User user) {
		userController.addUser(user);
	}

	private boolean validData() {
		boolean isCorrect = false;
		
		String msg = "";
		
		if ( inputEmailRestaurator.getValue().trim().equals("")
				|| !Util.isValidEmail(inputEmailRestaurator.getValue())) {
			msg += Customization.WRONG_EMAIL_ADDRESS + "\n";
		} else {
			isCorrect = true;
		}

		if(!msg.isEmpty()){
//			Window.alert(msg);
			PagesController.MY_POP_UP.showError(new Label(msg), new IMyAnswer() {
				
				@Override
				public void answer(Boolean answer) {
						
				}
			});
		}
		return isCorrect;
	}

	/**
	 * sets the right shadow around the widget
	 * @param isCorrect - if <b>true</b> sets green shadow, if <b>false</b> sets red shadow, if <b>null</b> hide all shadows
	 * @param widget - widget
	 */
	private void setValidDataStyle(Boolean isCorrect, Widget widget){
		if(widget == null) return;
		
		String correct = "greenShadow";
		String unCorrect = "redShadow";
		
		if(isCorrect == null){
			widget.setStyleName(correct, false);
			widget.setStyleName(unCorrect, false);
			return;
		}
		widget.setStyleName(correct, isCorrect);
		widget.setStyleName(unCorrect, !isCorrect);
	}
	
	@Override
	public void clearData() {
		inputEmailRestaurator.setValue("");

        setValidDataStyle(null, inputEmailRestaurator);
	}

	@Override
	public String getName() {
		return Customization.ADD_USER;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		synchronizeStatus = 0;
		if(isVisable){
			container.setWidth( JS.getElementOffsetWidth(getParent().getElement())-40 );
			
			cityController.refreshCities(this);
			restaurantController.refreshRestaurants(this);
			
		}
	}

	@Override
	public void onChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newData() {
		synchronizeStatus++;
		if(synchronizeStatus >= 2){
			
			User user = userController.getLoggedUser();
			
			roleListCombo.clear();
			restaurantListCombo.clear();
			citiesListCombo.clear();
			
			if(user.isAdmin()){
				roleListCombo.addListItem(roleListCombo.getNewCheckBoxItem(UserType.ADMIN.toString()), UserType.ADMIN.userTypeValue());
				roleListCombo.addListItem(roleListCombo.getNewCheckBoxItem(UserType.AGENT.toString()), UserType.AGENT.userTypeValue());
				roleListCombo.addListItem(roleListCombo.getNewCheckBoxItem(UserType.RESTAURATOR.toString()), UserType.RESTAURATOR.userTypeValue());
			}else{
				roleListCombo.addListItem(roleListCombo.getNewCheckBoxItem(UserType.RESTAURATOR.toString()), UserType.RESTAURATOR.userTypeValue());
				roleListCombo.selectItem(UserType.RESTAURATOR.userTypeValue());
				
			}
			
			
			List<Restaurant> restaurantList = restaurantController.getRestaurantsList();
			for (Restaurant item : restaurantList) {
				String nameToDisplay = item.getName() + " " + item.getAddress();
				restaurantListCombo.addListItem(restaurantListCombo.getNewCheckBoxItem(nameToDisplay), item.getId());
				
			}
			if(user.isAdmin()){
				List<City> citiesList = cityController.getCitiesList();
				for (City city : citiesList) {
					citiesListCombo.addListItem(citiesListCombo.getNewCheckBoxItem(city.getCity()), city.getId());
				}
			}
			PagesController.hideWaitPanel();
		}
	}


}
