package com.veliasystems.menumenu.client.userInterface;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

public class AddRestaurantScreen extends MyPage implements IObserver{
	
	
	private BackButton backButton;
	private MyButton saveButton;
	
	private Label nameLabel;
	private Label cityLabel;
	private Label adressLabel;
	private Label mailRestaurant;
	private Label phoneRestaurant;
	private Label mailUser;
	private Label phoneUser;
	private Label nameUser;
	
	private TextBox nameText = new TextBox();
	private TextBox adressText = new TextBox();
	private TextBox mailRestaurantTextBox = new TextBox();
	private TextBox phoneRestaurantTextBox = new  TextBox();
	private TextBox mailUserTextBox = new TextBox();
	private TextBox phoneUserTextBox = new TextBox();
	private TextBox nameUserTextBox = new TextBox();

	private ListBox cityListBox = new ListBox();
	private Label warning = new Label();
	
	private String city;
	private Restaurant restaurant;
	
	private boolean isToCity;
	private boolean loaded = false;
	
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();



	
	private void init(boolean isToCity){
		
		this.isToCity = isToCity;
		
		setButtons();

		
		cityLabel = new Label();
		cityLabel.setText(Customization.CITYONE + ":");
		
		
		if(!isToCity){
			addCities(cityController.getCitiesList());
			
		}else{
			cityListBox.addItem(city);
			
		}
		getContentPanel().add(cityListBox);
		setLabels();
		
	}	
	
	private void setLabels(){
		
		nameLabel = new Label();
		nameLabel.setText(Customization.RESTAURANTNAME + ":");
		getContentPanel().add(nameLabel);	
		
		nameText.setTitle(Customization.RESTAURANTNAME);
		getContentPanel().add(nameText);		
		
		adressLabel = new Label();
		adressLabel.setText(Customization.RESTAURANTADRESS + ":");
		getContentPanel().add(adressLabel);
		
		adressText.setTitle(Customization.RESTAURANTADRESS);
		getContentPanel().add(adressText);	
		
		mailRestaurant = new Label();
		mailRestaurant.setText(Customization.RESTAURANT_MAIL + ":");
		getContentPanel().add(mailRestaurant);
		
		mailRestaurantTextBox.setTitle(Customization.RESTAURANT_MAIL);
		getContentPanel().add(mailRestaurantTextBox);
		
		phoneRestaurant = new Label();
		phoneRestaurant.setText(Customization.RESTAURANT_PHONE + ":");
		getContentPanel().add(phoneRestaurant);
		
		phoneRestaurantTextBox.setTitle(Customization.RESTAURANT_PHONE);
		getContentPanel().add(phoneRestaurantTextBox);

		
		nameUser = new Label();
		nameUser.setText(Customization.USER_NAME + ":");
		getContentPanel().add(nameUser);
		
		nameUserTextBox.setTitle(Customization.USER_NAME);
		getContentPanel().add(nameUserTextBox);
		
		phoneUser = new Label();
		phoneUser.setText(Customization.USER_PHONE +":");
		getContentPanel().add(phoneUser);
		
		phoneUserTextBox.setTitle(Customization.USER_PHONE);
		getContentPanel().add(phoneUserTextBox);
		
		mailUser = new Label(Customization.USER_MAIL+":");
		getContentPanel().add(mailUser);
		
		mailUserTextBox.setTitle(Customization.USER_MAIL);
		getContentPanel().add(mailUserTextBox);

	}
	

	public AddRestaurantScreen(String city){
		super(city);
		cityController.addObserver(this);
		this.city = city;
		init(true);
	}

	
	
	public AddRestaurantScreen() {
		super(Customization.ADDRESTAURANT);
		cityController.addObserver(this);
		init(false);
	
	}
	
	@Override
	protected void onPageShow() {	
		clearData();		
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	private void clearData() {
		nameText.setText("");
		adressText.setText("");
		mailRestaurantTextBox.setText("");
		mailUserTextBox.setText("");
		nameUserTextBox.setText("");
		phoneRestaurantTextBox.setText("");
		phoneUserTextBox.setText("");
		warning.setText("");
		
		setValidDataStyle(null, nameText);
		setValidDataStyle(null, adressText);
		setValidDataStyle(null, mailRestaurantTextBox);
		setValidDataStyle(null, mailUserTextBox);
		setValidDataStyle(null, mailUserTextBox);
		setValidDataStyle(null, phoneRestaurantTextBox);
		setValidDataStyle(null, phoneUserTextBox);
	}

	
	private void setButtons( ){		
		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isToCity){
					Document.get().getElementById("load").setClassName(R.LOADING);
					JQMContext.changePage(PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE);
				}else{
					Document.get().getElementById("load").setClassName(R.LOADING);
					JQMContext.changePage(PagesController.getPage(Pages.PAGE_RESTAURANT_LIST), Transition.SLIDE);
				}
			
			}
		});
		
		saveButton = new MyButton(Customization.SAVE);
		saveButton.setStyleName("rightButton", true);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(validate()){
					restaurant = new Restaurant();
					restaurant.setName(nameText.getText());
					
					restaurant.setAddress(adressText.getText());			
					int index = cityListBox.getSelectedIndex();			
					restaurant.setCity(cityListBox.getItemText(index));	
					restaurant.setMailRestaurant(mailRestaurantTextBox.getText());
					restaurant.setPhoneRestaurant(phoneRestaurantTextBox.getText());
					restaurant.setNameUser(nameUserTextBox.getText());
					restaurant.setPhoneUser(phoneUserTextBox.getText());
					restaurant.setMailUser(mailUserTextBox.getText());
					
					Document.get().getElementById("load").setClassName(R.LOADING);
					restaurantController.saveRestaurant(restaurant);
				}	
			}
		});

		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(saveButton);
		
	}
	
	private boolean validate(){
		warning.setStyleName("warning");
		warning.setText("");
		add(warning);
		if(!nameText.getText().isEmpty() && !adressText.getText().isEmpty()){
			if(restaurantExist()){
				setValidDataStyle(false, nameText);
				setValidDataStyle(false, adressText);
				warning.setText(Customization.RESTAURANT_EXIST_ERROR);
				return false;
			}else{
				setValidDataStyle(true, nameText);
				setValidDataStyle(true, adressText);
			}
		}
		
		boolean isCorrect = true;
		
		if(nameText.getText().isEmpty() && adressText.getText().isEmpty()){
			warning.setText(Customization.EMPTYBOTHDATA);
			setValidDataStyle(false, nameText);
			setValidDataStyle(false, adressText);
			isCorrect = false;
		}else{
			if(nameText.getText().isEmpty()){
				warning.setText(Customization.EMPTYNAME);
				setValidDataStyle(false, nameText);
				isCorrect = false;
			}else{
				setValidDataStyle(true, nameText);
			}
			if(adressText.getText().isEmpty()){
				warning.setText(warning.getText()+ " \n"+ Customization.EMPTYADRESS);
				setValidDataStyle(false, adressText);
				isCorrect = false;
			}else{
				setValidDataStyle(true, adressText);
			}
		}
		if(!mailRestaurantTextBox.getText().isEmpty() ){
			if(!Util.isValidEmail(mailRestaurantTextBox.getText())){
				setValidDataStyle(false, mailRestaurantTextBox);
				isCorrect = false;
			}else{
				setValidDataStyle(true, mailRestaurantTextBox);
			}
		}else{
			setValidDataStyle(null, mailRestaurantTextBox);
		}
		if(!phoneRestaurantTextBox.getText().isEmpty() ){
			if(!Util.isValidPhoneNumber(phoneRestaurantTextBox.getText())){
				setValidDataStyle(false, phoneRestaurantTextBox);
				isCorrect = false;
			}else{
				setValidDataStyle(true, phoneRestaurantTextBox);
			}
		}
		else{
			setValidDataStyle(null, phoneRestaurantTextBox);
		}
		if(!mailUserTextBox.getText().isEmpty()){
			if(!Util.isValidEmail(mailUserTextBox.getText())){
				setValidDataStyle(false, mailUserTextBox);
				isCorrect = false;
			}else{
				setValidDataStyle(true, mailUserTextBox);
			}
		}else{
			setValidDataStyle(null, mailUserTextBox);
		}
		if(!phoneUserTextBox.getText().isEmpty()){
			if(!Util.isValidPhoneNumber(phoneUserTextBox.getText())){
				setValidDataStyle(false, phoneUserTextBox);
				isCorrect = false;
			}else{
				setValidDataStyle(true, phoneUserTextBox);
			}
		}else{
			setValidDataStyle(null, phoneUserTextBox);
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
	
	/**
	 * 
	 * @return true if restaurant exist
	 */
	private boolean restaurantExist(){
		String cityName = cityListBox.getItemText(cityListBox.getSelectedIndex());
		List<Restaurant> restaurants = restaurantController.getRestaurantsInCity(cityName);
		
		for (Restaurant restaurant : restaurants) {
			if( (restaurant.getName().replaceAll(" ", "").equals(nameText.getText().replaceAll(" ", "")))  &&
			    (restaurant.getAddress().replaceAll(" ", "").equals(adressText.getText().replaceAll(" ", ""))) ){
				return true;
			}
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
		for(City city: CityController.getInstance().getCitiesList()){
			cityListBox.addItem(city.getCity());
		}
	}
	
}
