package com.veliasystems.menumenu.client.ui.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;

public class AddRestauratorPanel extends FlowPanel implements IManager {

	
	private Label mailLabel;
	private Label passwordLabe;
	private Label repeatPasswordLabe;
	private PasswordTextBox passwordRestaurator;
	private PasswordTextBox passwordRestaurator2;
	private TextBox inputEmailRestaurator;
	private MultiWordSuggestOracle restaurantSuggest;
	private SuggestBox restaurantSuggestBox;
	private JQMButton addRestaurantToRestaurantTextBox;
	private CellList<Restaurant> restaurantCellList;
	private ArrayList<Restaurant> addedRestauration;
	private SingleSelectionModel<Restaurant> selectionModelRestaurant;
	private JQMButton saveRestauratorButton;
	
	private Restaurant restaurant = null;
	
	private List<Long> restaurantsIdList = new ArrayList<Long>();
	
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private UserController userController = UserController.getInstance();

	public AddRestauratorPanel(Restaurant restaurant) {
		
		if(restaurant != null) this.restaurant=restaurant;
		
		setStyleName("barPanel", true);
		show(false);
		
		mailLabel = new Label(Customization.INPUT_EMAIL);
		passwordLabe = new Label(Customization.INPUT_PASSWORD);
		repeatPasswordLabe = new Label(Customization.REPEAT_PASSWORD);
		
		passwordRestaurator = new PasswordTextBox();
		passwordRestaurator.addStyleName("properWidth");
		passwordRestaurator.getElement().setAttribute("placeHolder",
				Customization.PASSWORD_PLACEHOLDER);
		passwordRestaurator2 = new PasswordTextBox();
		passwordRestaurator2.addStyleName("properWidth");
		passwordRestaurator2.getElement().setAttribute("placeHolder",
				Customization.REPEAT_PASSWORD_PLACEHOLDER);
		inputEmailRestaurator = new TextBox();
		inputEmailRestaurator.addStyleName("properWidth");
		inputEmailRestaurator.getElement().setAttribute("placeHolder",
				Customization.EMAIL_PLACEHOLDER);
		restaurantSuggest = new MultiWordSuggestOracle();
		restaurantSuggestBox = new SuggestBox(restaurantSuggest);
		restaurantSuggestBox.getElement().setAttribute("placeHolder",
				Customization.RESTAURANT_PLACEHOLDER);
		restaurantSuggestBox.addStyleName("properWidth");
		addRestaurantToRestaurantTextBox = new JQMButton("");
		addRestaurantToRestaurantTextBox.setIcon(DataIcon.PLUS);
		addRestaurantToRestaurantTextBox.setIconPos(IconPos.NOTEXT);
		addRestaurantToRestaurantTextBox.setStyleName("addButtonCity");
		addRestaurantToRestaurantTextBox
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						if (checkRestaurant(restaurantSuggestBox.getValue())) {
							setRestauransId(true);
							restaurantCellList
									.setRowData(addedRestauration);
							restaurantCellList
									.setRowCount(addedRestauration.size());
							restaurantCellList.redraw();
							restaurantSuggestBox.setText("");

						}

					}
				});


		restaurantCellList = new CellList<Restaurant>(
				new RestaurantCellClass());
		addedRestauration = new ArrayList<Restaurant>();
		selectionModelRestaurant = new SingleSelectionModel<Restaurant>();
		restaurantCellList.setSelectionModel(selectionModelRestaurant);
		selectionModelRestaurant
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						// TODO Auto-generated method stub
						Restaurant selected = selectionModelRestaurant
								.getSelectedObject();
						if (selected != null) {
							restaurantsIdList.remove(selected.getId());
							addedRestauration.remove(selected);
							selectionModelRestaurant.setSelected(
									selectionModelRestaurant
											.getSelectedObject(), false);
							restaurantCellList
									.setRowData(addedRestauration);
							restaurantCellList
									.setRowCount(addedRestauration.size());
							restaurantCellList.redraw();
						}
					}
				});
		restaurantCellList.setRowData(addedRestauration);
		restaurantCellList.setRowCount(addedRestauration.size());
		saveRestauratorButton = new JQMButton(Customization.SAVE);
		saveRestauratorButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validData()) {
					User restaurator = new User(inputEmailRestaurator
							.getValue().trim());
					restaurator.setPassword(passwordRestaurator.getValue()
							.trim());
					restaurator.setRestaurantsId(restaurantsIdList);
					addUser(restaurator);
				}
			}
		});

		add(mailLabel);
		add(inputEmailRestaurator);
		add(passwordLabe);
		add(passwordRestaurator);
		add(repeatPasswordLabe);
		add(passwordRestaurator2);
		add(restaurantSuggestBox);
		add(addRestaurantToRestaurantTextBox);
		add(restaurantCellList);// restaurantTextBox);
		add(saveRestauratorButton);
	}

	private void addUser(User user) {
		userController.addUser(user);
	}
	private boolean checkRestaurant(String restaurantName) {
		boolean is = false;
		int indexOfCity = restaurantName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1)
			return false;
		String restName = restaurantName.substring(0, indexOfCity - 1);
		List<Restaurant> restaurants = restaurantController
				.getRestaurantsList();
		List<String> restaurantNameList = new ArrayList<String>();
		for (Restaurant restaurant : restaurants) {
			restaurantNameList.add(restaurant.getName());
		}

		if (restaurantNameList.contains(restName)) {
			if (addedRestauration.isEmpty())
				return true;

			is = true;

			for (Restaurant restaurant : addedRestauration) {
				if (restaurant.getName().equals(restName))
					is = false;
			}
		}
		return is;
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
		if (addedRestauration.size() < 1) {
			setValidDataStyle(false, restaurantSuggestBox);
			isCorrect = false;
		} else {
			setValidDataStyle(true, restaurantSuggestBox);
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
	private void setRestauransId(boolean isAdded) {
		if (isAdded) {
			String fullRestName = restaurantSuggestBox.getValue();// selectionModelRestaurant.getSelectedObject();
			String restNameNoCity = getRestaurationName(fullRestName);
			if (restNameNoCity == null)
				return;
			String city = getCityName(fullRestName);
			if (city == null)
				return;
			String adress = getAddresName(fullRestName);
			if (adress == null)
				return;

			for (Restaurant restaurant : restaurantController
					.getRestaurantsList()) {
				if (restaurant.getName().equals(restNameNoCity)
						&& restaurant.getCity().equals(city)
						&& restaurant.getAddress().equals(adress)) {
					addedRestauration.add(restaurant);
					// addedRestauration.add(restaurantSuggestBox.getValue());
					restaurantsIdList.add(restaurant.getId());
				}
			}

		} else {
			String fullRestName = restaurantSuggestBox.getValue();// selectionModelRestaurant.getSelectedObject();
			String restNameNoCity = getRestaurationName(fullRestName);
			if (restNameNoCity == null)
				return;
			String city = getCityName(fullRestName);
			if (city == null)
				return;
			String adress = getAddresName(fullRestName);
			if (adress == null)
				return;

			for (Restaurant restaurant : restaurantController
					.getRestaurantsList()) {
				if (restaurant.getName().equals(restNameNoCity)
						&& restaurant.getCity().equals(city)
						&& restaurant.getAddress().equals(adress)) {
					addedRestauration.remove(selectionModelRestaurant
							.getSelectedObject());
					restaurantsIdList.remove(restaurant.getId());
				}
			}

		}

	}
	
	private String getRestaurationName(String fullRestName) {
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1)
			return null;
		return fullRestName.substring(0, indexOfCity - 1);
	}

	private String getCityName(String fullRestName) {
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1)
			return null;
		indexOfCity += Customization.CITY.length();
		int indexOfAdress = fullRestName.indexOf(Customization.ADRESS + ":",
				indexOfCity);
		if (indexOfAdress < 0)
			return null;
		return fullRestName.substring(indexOfCity + 3, indexOfAdress - 2);
	}

	private String getAddresName(String fullRestName) {
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1)
			return null;
		indexOfCity += Customization.CITY.length();
		int indexOfAdress = fullRestName.indexOf(Customization.ADRESS + ":",
				indexOfCity);
		if (indexOfAdress < 0)
			return null;
		indexOfAdress += Customization.ADRESS.length();

		return fullRestName.substring(indexOfAdress + 2,
				fullRestName.length() - 1);
	}
	
	@Override
	public void clearData() {
		inputEmailRestaurator.setValue("");
        passwordRestaurator.setValue("");
        passwordRestaurator2.setValue("");
        restaurantSuggestBox.setText("");

        setValidDataStyle(null, inputEmailRestaurator);
		setValidDataStyle(null, passwordRestaurator);
		setValidDataStyle(null, passwordRestaurator2);
		setValidDataStyle(null, restaurantSuggestBox);
        
        restaurantSuggest.clear();
        addedRestauration.clear();
        for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
            if (restaurant == this.restaurant) {
                addedRestauration.add(restaurant);
            }
            restaurantSuggest.add(restaurant.getName() + " ("
                    + Customization.CITYONE + ": " + restaurant.getCity()
                    + " ," + Customization.ADRESS + ": "
                    + restaurant.getAddress() + ")");
        }
        
        restaurantCellList.setRowData(addedRestauration);
        restaurantCellList.redraw();
	}

	@Override
	public String getName() {
		return Customization.ADD_RESTAURATOR;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
	}

}

class RestaurantCellClass extends AbstractCell<Restaurant> {

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			Restaurant restaurant, SafeHtmlBuilder sb) {
		sb.appendEscaped(restaurant.getName() + " (" + Customization.CITYONE
				+ ": " + restaurant.getCity() + " ," + Customization.ADRESS
				+ ": " + restaurant.getAddress() + ")");

	}

}
