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
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestInfoScreen extends JQMPage implements HasClickHandlers, IObserver {

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
	Label surnameUser = new Label();
	
	TextBox mailRestaurantTextBox = new TextBox();
	TextBox phoneRestaurantTextBox = new  TextBox();
	TextBox mailUserTextBox = new TextBox();
	TextBox phoneUserTextBox = new TextBox();
	TextBox nameUserTextBox = new TextBox();
	TextBox surnameUserTextBox = new TextBox();

	JQMPanel content;
	Label warning = new Label();

	Restaurant restaurant;
	private CityController cityController = CityController.getInstance();
	private JQMPage back;
	private boolean loaded = false;

	public RestInfoScreen(Restaurant r , JQMPage back) {
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
		
		surnameUser.setText(Customization.USER_SURNAME +":");
		content.add(surnameUser);
		
		surnameUserTextBox.setText(restaurant.getSurnameUser());
		content.add(surnameUserTextBox);
		
		nameUser.setText(Customization.USER_NAME + ":");
		content.add(nameUser);
		
		nameUserTextBox.setText(restaurant.getNameUser());
		content.add(nameUserTextBox);
		
		phoneUser.setText(Customization.USER_PHONE +":");
		content.add(phoneUser);
		
		phoneUserTextBox.setText(restaurant.getPhoneUser());
		content.add(phoneUserTextBox);
		
		mailUser.setText(Customization.USER_MAIL+":");
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

			RestaurantController.getInstance().deleteRestaurant(restaurant);
		}
		if (isClicked(event, saveButton)) {

			restaurant.setName(nameText.getText());
			restaurant.setAddress(adressText.getText());
			restaurant.setCity(cityListBox.getItemText(cityListBox.getSelectedIndex()));
			restaurant.setMailRestaurant(mailRestaurantTextBox.getText());
			restaurant.setPhoneRestaurant(phoneRestaurantTextBox.getText());
			restaurant.setMailUser(mailUserTextBox.getText());
			restaurant.setPhoneUser(phoneUserTextBox.getText());
			restaurant.setNameUser(nameUserTextBox.getText());
			restaurant.setSurnameUser(surnameUserTextBox.getText());
			if (validate()) {
				
				RestaurantController.getInstance().saveRestaurant(restaurant);

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
		if (!nameText.getText().isEmpty() && !adressText.getText().isEmpty()) {
			return true;
		} else {
			if (nameText.getText().isEmpty() && adressText.getText().isEmpty()) {
				warning.setText(Customization.EMPTYBOTHDATA);
			} else if (nameText.getText().isEmpty()) {
				warning.setText(Customization.EMPTYNAME);
			} else {
				warning.setText(Customization.EMPTYADRESS);
			}
			warning.setStyleName("warning");
			add(warning);

		}
		return false;
	}
	
	@Override
	public void onChange() {
		refreshCityList();
	}
	private void refreshCityList(){
		cityListBox.clear();
		addCities(cityController.getCitiesList());
	}
	
	private void addCities(List<City> cities){
		int k=0;
		for(City city: CityController.getInstance().getCitiesList()){
			cityListBox.addItem(city.getCity());
			if(city.getCity().equalsIgnoreCase(restaurant.getCity())){
				cityListBox.setSelectedIndex(k);
			}
			k++;
		}
	}

	@Override
	protected void onPageShow() {
		super.onPageShow();
		
		if(!loaded){
			backButton = new JQMButton(Customization.BACK, back, Transition.SLIDE);
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);
			
			backButton.getElement().setInnerHTML(span);
			backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");
			
			header.add(backButton);
			
			loaded=true;
		}
		
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
}
