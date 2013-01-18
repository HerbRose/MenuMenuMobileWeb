package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class AddRestauratorPanel extends FlowPanel implements IManager {

	
	private Label mailLabel;
	private Label passwordLabe;
	private Label repeatPasswordLabe;
	private PasswordTextBox passwordRestaurator;
	private PasswordTextBox passwordRestaurator2;
	private TextBox inputEmailRestaurator;

	private JQMButton saveRestauratorButton;
		
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private UserController userController = UserController.getInstance();

	
	
	private MyListCombo restaurantListCombo = new MyListCombo(true);
	
	private MyRestaurantInfoPanel container;

	public AddRestauratorPanel() {
		
		
		setStyleName("barPanel", true);
		show(false);
		
		mailLabel = new Label(Customization.INPUT_EMAIL);
		passwordLabe = new Label(Customization.INPUT_PASSWORD);
		repeatPasswordLabe = new Label(Customization.REPEAT_PASSWORD);
		
		passwordRestaurator = new PasswordTextBox();
		passwordRestaurator.addStyleName("myTextBox nameBox");
		passwordRestaurator.getElement().setAttribute("placeHolder",
				Customization.PASSWORD_PLACEHOLDER);
		passwordRestaurator2 = new PasswordTextBox();
		passwordRestaurator2.addStyleName("myTextBox nameBox");
		passwordRestaurator2.getElement().setAttribute("placeHolder",
				Customization.REPEAT_PASSWORD_PLACEHOLDER);
		inputEmailRestaurator = new TextBox();
		inputEmailRestaurator.addStyleName("myTextBox nameBox");
		inputEmailRestaurator.getElement().setAttribute("placeHolder",
				Customization.EMAIL_PLACEHOLDER);
		
		
		
		saveRestauratorButton = new JQMButton(Customization.SAVE);
		saveRestauratorButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validData()) {
					User restaurator = new User(inputEmailRestaurator
							.getValue().trim());
					restaurator.setPassword(passwordRestaurator.getValue()
							.trim());
					restaurator.setRestaurator(true);
					restaurator.setRestaurantsId(restaurantListCombo.getCheckedList());
					addUser(restaurator);
				}
			}
		});

		container = new MyRestaurantInfoPanel();
		container.setStyleName("containerPanelAddRestaurant", true);
		
		
		Label chosenLabel = new Label(Customization.CHOSEN);
		List<Restaurant> restaurantList = restaurantController.getRestaurantsList();
		

		for (Restaurant item : restaurantList) {
			String nameToDisplay = item.getName() + " " + item.getAddress();
			restaurantListCombo.addListItem(restaurantListCombo.getNewCheckBoxItem(nameToDisplay), item.getId());
			
		}


		container.addItem(mailLabel, inputEmailRestaurator);
		container.addItem(passwordLabe, passwordRestaurator);
		container.addItem(repeatPasswordLabe, passwordRestaurator2);

		
		container.addItem(chosenLabel, restaurantListCombo);
		
	
		container.add(saveRestauratorButton);
		
		add(container);
	}

	private void addUser(User user) {
		userController.addUser(user);
	}

	private boolean validData() {
		boolean isCorrect = false;
		if (userController.isUserInStor(inputEmailRestaurator.getValue()
				.trim())
				|| inputEmailRestaurator.getValue().trim().equals("")
				|| !Util.isValidEmail(inputEmailRestaurator.getValue())) {
			setValidDataStyle(false, inputEmailRestaurator);
		} else {
			setValidDataStyle(true, inputEmailRestaurator);
			isCorrect = true;
		}
		if (passwordRestaurator.getValue().trim().equals("")
				|| passwordRestaurator2.getValue().trim().equals("")
				|| !passwordRestaurator.getValue().equals(
						passwordRestaurator2.getValue())) {
			setValidDataStyle(false, passwordRestaurator);
			setValidDataStyle(false, passwordRestaurator2);
			isCorrect = false;
		} else {
			setValidDataStyle(true, passwordRestaurator);
			setValidDataStyle(true, passwordRestaurator2);
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
        passwordRestaurator.setValue("");
        passwordRestaurator2.setValue("");

        setValidDataStyle(null, inputEmailRestaurator);
		setValidDataStyle(null, passwordRestaurator);
		setValidDataStyle(null, passwordRestaurator2);
        
      
	}

	@Override
	public String getName() {
		return Customization.ADD_RESTAURATOR;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		if(isVisable){
			container.setWidth( JS.getElementOffsetWidth(getParent().getElement())-40 );
		}
	}


}
