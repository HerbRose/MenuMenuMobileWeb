package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestInfoScreen extends JQMPage implements HasClickHandlers,
		IObserver {

	JQMHeader header;
	JQMFooter footer;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMList list;
	JQMButton backButton;
	JQMButton showButton;

	Label nameLabel = new Label();
	Label cityLabel = new Label();
	Label adressLabel = new Label();

	ListBox cityListBox = new ListBox();
	TextBox nameText = new TextBox();
	TextBox adressText = new TextBox();

	Label mailRestaurant = new Label();
	Label phoneRestaurant = new Label();
	Label mailUser = new Label();
	Label phoneUser = new Label();
	Label nameUser = new Label();

	TextBox mailRestaurantTextBox = new TextBox();
	TextBox phoneRestaurantTextBox = new TextBox();
	TextBox mailUserTextBox = new TextBox();
	TextBox phoneUserTextBox = new TextBox();
	TextBox nameUserTextBox = new TextBox();

	JQMPanel content;
	Label warning = new Label();

	private Restaurant restaurant;
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController
			.getInstance();
	private JQMPage back;
	private boolean loaded = false;

	public RestInfoScreen(Restaurant r, JQMPage back) {
		this.back = back;
		cityController.addObserver(this);
		setRestaurant(r);

		init();
	}

	public void setRestaurant(Restaurant r) {
		this.restaurant = r;
	}

	private void init() {
		header = new JQMHeader(restaurant.getName());

		header.setFixed(true);
		add(header);

		removeButton = new JQMButton(Customization.REMOVEPROFILE);
		removeButton.setIcon(DataIcon.DELETE);
		removeButton.setIconPos(IconPos.TOP);
		removeButton.setWidth("49%");

		saveButton = new JQMButton(Customization.SAVEPROFILE);
		saveButton.setIcon(DataIcon.STAR);
		saveButton.setIconPos(IconPos.TOP);
		saveButton.setWidth("49%");
		footer = new JQMFooter();

		footer.setFixed(true);
		footer.add(removeButton);
		footer.add(saveButton);

		add(footer);
		content = new JQMPanel();

		cityLabel.setText(Customization.CITYONE + ":");
		content.add(cityLabel);

		addCities(cityController.getCitiesList());

		content.add(cityListBox);
		nameLabel.setText(Customization.RESTAURANTNAME + ":");
		content.add(nameLabel);
		nameText.setTitle(Customization.RESTAURANTNAME);
		nameText.setText(restaurant.getName());
		content.add(nameText);
		adressLabel.setText(Customization.RESTAURANTADRESS + ":");
		content.add(adressLabel);
		adressText.setTitle(Customization.RESTAURANTADRESS);
		adressText.setText(restaurant.getAddress());
		content.add(adressText);

		mailRestaurant.setText(Customization.RESTAURANT_MAIL + ":");
		content.add(mailRestaurant);

		mailRestaurantTextBox.setText(restaurant.getMailRestaurant());
		content.add(mailRestaurantTextBox);

		phoneRestaurant.setText(Customization.RESTAURANT_PHONE + ":");
		content.add(phoneRestaurant);

		phoneRestaurantTextBox.setText(restaurant.getPhoneRestaurant());
		content.add(phoneRestaurantTextBox);

		nameUser.setText(Customization.USER_NAME + ":");
		content.add(nameUser);

		nameUserTextBox.setText(restaurant.getNameUser());
		content.add(nameUserTextBox);

		phoneUser.setText(Customization.USER_PHONE + ":");
		content.add(phoneUser);

		phoneUserTextBox.setText(restaurant.getPhoneUser());
		content.add(phoneUserTextBox);

		mailUser.setText(Customization.USER_MAIL + ":");
		content.add(mailUser);

		mailUserTextBox.setText(restaurant.getMailUser());
		content.add(mailUserTextBox);

		add(content);
	}

	{
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				meClicked(event);
			}
		});
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	private void meClicked(ClickEvent event) {

		if (isClicked(event, removeButton)) {

			RestaurantController.getInstance().deleteRestaurant(restaurant,
					RestInfoScreen.class.getName());
		}
		if (isClicked(event, saveButton)) {

			restaurant.setName(nameText.getText());
			restaurant.setAddress(adressText.getText());
			restaurant.setCity(cityListBox.getItemText(cityListBox
					.getSelectedIndex()));
			restaurant.setMailRestaurant(mailRestaurantTextBox.getText());
			restaurant.setPhoneRestaurant(phoneRestaurantTextBox.getText());
			restaurant.setMailUser(mailUserTextBox.getText());
			restaurant.setPhoneUser(phoneUserTextBox.getText());
			restaurant.setNameUser(nameUserTextBox.getText());
			if (validate()) {
			//	RestaurantController.getInstance().saveRestaurant(restaurant);
			}
		}

	}

	private boolean isClicked(ClickEvent event, JQMButton button) {

		int clickedX = event.getClientX();
		int clickedY = event.getClientY();

		int ButtonX = (int) button.getElement().getAbsoluteLeft();
		int ButtonY = (int) button.getElement().getAbsoluteTop();
		int ButtonWidth = (int) button.getElement().getClientWidth();
		int ButtonHeight = (int) button.getElement().getClientWidth();

		int ButtonStartX = ButtonX;
		int ButtonStopX = ButtonX + ButtonWidth;
		int ButtonStartY = ButtonY;
		int ButtonStopY = ButtonY + ButtonHeight;

		if (clickedX >= ButtonStartX && clickedX <= ButtonStopX
				&& clickedY >= ButtonStartY && clickedY <= ButtonStopY) {
			return true;
		}
		return false;
	}

	private boolean validate() {
		warning.setStyleName("warning");
		warning.setText("");
		add(warning);
		if (!nameText.getText().isEmpty() && !adressText.getText().isEmpty()) {
			if (restaurantExist()) {
				setValidDataStyle(false, nameText);
				setValidDataStyle(false, adressText);
				warning.setText(Customization.RESTAURANT_EXIST_ERROR);
				return false;
			} else {
				setValidDataStyle(true, nameText);
				setValidDataStyle(true, adressText);
			}
		}

		boolean isCorrect = true;

		if (nameText.getText().isEmpty() && adressText.getText().isEmpty()) {
			warning.setText(Customization.EMPTYBOTHDATA);
			setValidDataStyle(false, nameText);
			setValidDataStyle(false, adressText);
			isCorrect = false;
		} else {
			if (nameText.getText().isEmpty()) {
				warning.setText(Customization.EMPTYNAME);
				setValidDataStyle(false, nameText);
				isCorrect = false;
			} else {
				setValidDataStyle(true, nameText);
			}
			if (adressText.getText().isEmpty()) {
				warning.setText(warning.getText() + " \n"
						+ Customization.EMPTYADRESS);
				setValidDataStyle(false, adressText);
				isCorrect = false;
			} else {
				setValidDataStyle(true, adressText);
			}
		}
		if (!mailRestaurantTextBox.getText().isEmpty()) {
			if (!Util.isValidEmail(mailRestaurantTextBox.getText())) {
				setValidDataStyle(false, mailRestaurantTextBox);
				isCorrect = false;
			} else {
				setValidDataStyle(true, mailRestaurantTextBox);
			}
		} else {
			setValidDataStyle(null, mailRestaurantTextBox);
		}
		if (!phoneRestaurantTextBox.getText().isEmpty()) {
			if (!Util.isValidPhoneNumber(phoneRestaurantTextBox.getText())) {
				setValidDataStyle(false, phoneRestaurantTextBox);
				isCorrect = false;
			} else {
				setValidDataStyle(true, phoneRestaurantTextBox);
			}
		} else {
			setValidDataStyle(null, phoneRestaurantTextBox);
		}
		if (!mailUserTextBox.getText().isEmpty()) {
			if (!Util.isValidEmail(mailUserTextBox.getText())) {
				setValidDataStyle(false, mailUserTextBox);
				isCorrect = false;
			} else {
				setValidDataStyle(true, mailUserTextBox);
			}
		} else {
			setValidDataStyle(null, mailUserTextBox);
		}
		if (!phoneUserTextBox.getText().isEmpty()) {
			if (!Util.isValidPhoneNumber(phoneUserTextBox.getText())) {
				setValidDataStyle(false, phoneUserTextBox);
				isCorrect = false;
			} else {
				setValidDataStyle(true, phoneUserTextBox);
			}
		} else {
			setValidDataStyle(null, phoneUserTextBox);
		}
		return isCorrect;
	}

	/**
	 * 
	 * @return true if restaurant exist and it's not the same restaurant
	 */
	private boolean restaurantExist() {
		
		String cityName = cityListBox.getItemText(cityListBox
				.getSelectedIndex());
		List<Restaurant> restaurants = restaurantController
				.getRestaurantsInCity(cityName);

		for (Restaurant restaurant : restaurants) {
			String restaurationName = restaurant.getName().replaceAll(" ", "");
			String restaurantAddress = restaurant.getAddress().replaceAll(" ","");
			
			if(restaurationName.equalsIgnoreCase(nameText.getText().replaceAll(" ", "")) &&
					restaurantAddress.equalsIgnoreCase(adressText.getText().replace(" ", ""))){
				if(nameText.getText().replaceAll(" ", "").equalsIgnoreCase(this.restaurant.getName().replace(" ", "")) &&
						adressText.getText().replace(" ", "").equalsIgnoreCase(this.restaurant.getAddress().replace(" " , ""))){
					return false;
				}else{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * sets the right shadow around the widget
	 * 
	 * @param isCorrect
	 *            - if <b>true</b> sets green shadow, if <b>false</b> sets red
	 *            shadow, if <b>null</b> hide all shadows
	 * @param widget
	 *            - widget
	 */
	private void setValidDataStyle(Boolean isCorrect, Widget widget) {
		if (widget == null)
			return;

		String correct = "greenShadow";
		String unCorrect = "redShadow";

		if (isCorrect == null) {
			widget.setStyleName(correct, false);
			widget.setStyleName(unCorrect, false);
			return;
		}
		widget.setStyleName(correct, isCorrect);
		widget.setStyleName(unCorrect, !isCorrect);
	}

	@Override
	public void onChange() {
		refreshCityList();
	}

	private void refreshCityList() {
		cityListBox.clear();
		addCities(cityController.getCitiesList());
	}

	private void addCities(List<City> cities) {
		int k = 0;
		for (City city : CityController.getInstance().getCitiesList()) {
			cityListBox.addItem(city.getCity());
			if (city.getCity().equalsIgnoreCase(restaurant.getCity())) {
				cityListBox.setSelectedIndex(k);
			}
			k++;
		}
	}

	@Override
	protected void onPageShow() {
		super.onPageShow();

		if (!loaded) {
			backButton = new JQMButton(Customization.BACK, back,
					Transition.SLIDE);
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"
					+ Customization.BACK
					+ "</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);

			backButton.getElement().setInnerHTML(span);
			backButton
					.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");

			header.add(backButton);

			loaded = true;
		}

		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
}
