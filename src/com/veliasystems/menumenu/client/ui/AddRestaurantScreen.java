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
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class AddRestaurantScreen extends JQMPage implements HasClickHandlers, IObserver{
	

	JQMHeader header;
	JQMButton backButton;
	JQMButton saveButton;
	JQMPanel content;
	
	Label nameLabel;
	Label cityLabel;
	Label adressLabel;
	Label mailRestaurant;
	Label phoneRestaurant;
	Label mailUser;
	Label phoneUser;
	Label nameUser;
	Label surnameUser;
	
	TextBox cityText = new TextBox();
	TextBox nameText = new TextBox();
	TextBox adressText = new TextBox();
	TextBox mailRestaurantTextBox = new TextBox();
	TextBox phoneRestaurantTextBox = new  TextBox();
	TextBox mailUserTextBox = new TextBox();
	TextBox phoneUserTextBox = new TextBox();
	TextBox nameUserTextBox = new TextBox();
	TextBox surnameUserTextBox = new TextBox();
	ListBox cityListBox = new ListBox();
	Label warning = new Label();
	
	String city;
	Restaurant restaurant;
	
	private boolean isToCity;
	private boolean loaded = false;
	
	private CityController cityController = CityController.getInstance();

	{
		this.addClickHandler( new ClickHandler() {
			
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
	
	
	void meClicked( ClickEvent event ) {
		
		int clickedX =event.getClientX();
		int clickedY= event.getClientY();	
		int  buttonX= (int) saveButton.getElement().getAbsoluteLeft();
		int  buttonY= (int) saveButton.getElement().getAbsoluteTop();		
		int buttonWidth = (int) saveButton.getElement().getClientWidth();
		int buttonHeight = (int) saveButton.getElement().getClientHeight();
		
		if(clickedX>= buttonX && (clickedX <= buttonX + buttonWidth) && clickedY >= buttonY && (clickedY <= buttonY + buttonHeight)){
		
			if(checkFields()){
			
				restaurant = new Restaurant();
				restaurant.setName(nameText.getText());
				
				restaurant.setAddress(adressText.getText());			
				int index = cityListBox.getSelectedIndex();			
				restaurant.setCity(cityListBox.getItemText(index));		
				RestaurantController.getInstance().saveRestaurant(restaurant);
				
			}
				
		}
	}
	
	private void init(boolean isToCity){
		
		this.isToCity = isToCity;
		
		setContentHeader();
		setButtons();
		
		content = new JQMPanel();
		
		header.setRightButton(saveButton);
		add(header);			
		
		//this.add(saveButton);
		
		cityLabel = new Label();
		cityLabel.setText(Customization.CITYONE + ":");
		
		content.add(cityLabel);	
		if(!isToCity){
			addCities(cityController.getCitiesList());
			
		}else{
			cityListBox.addItem(city);
			
		}
		content.add(cityListBox);
		setLabels();
		
	}	
	
	private void setLabels(){
		
		nameLabel = new Label();
		nameLabel.setText(Customization.RESTAURANTNAME + ":");
		content.add(nameLabel);	
		
		nameText.setTitle(Customization.RESTAURANTNAME);
		content.add(nameText);		
		
		adressLabel = new Label();
		adressLabel.setText(Customization.RESTAURANTADRESS + ":");
		content.add(adressLabel);
		
		adressText.setTitle(Customization.RESTAURANTADRESS);
		content.add(adressText);	
		
		mailRestaurant = new Label();
		mailRestaurant.setText(Customization.RESTAURANT_MAIL + ":");
		content.add(mailRestaurant);
		
		mailRestaurantTextBox.setTitle(Customization.RESTAURANT_MAIL);
		content.add(mailRestaurantTextBox);
		
		phoneRestaurant = new Label();
		phoneRestaurant.setText(Customization.RESTAURANT_PHONE + ":");
		content.add(phoneRestaurant);
		
		phoneRestaurantTextBox.setTitle(Customization.RESTAURANT_PHONE);
		content.add(phoneRestaurantTextBox);
		
		surnameUser = new Label();
		surnameUser.setText(Customization.USER_SURNAME +":");
		content.add(surnameUser);
		
		surnameUserTextBox.setTitle(Customization.USER_NAME);
		content.add(surnameUserTextBox);
		
		nameUser = new Label();
		nameUser.setText(Customization.USER_NAME + ":");
		content.add(nameUser);
		
		nameUserTextBox.setTitle(Customization.USER_NAME);
		content.add(nameUserTextBox);
		
		phoneUser = new Label();
		phoneUser.setText(Customization.USER_PHONE +":");
		content.add(phoneUser);
		
		phoneUserTextBox.setTitle(Customization.USER_PHONE);
		content.add(phoneUserTextBox);
		
		mailUser = new Label(Customization.USER_MAIL+":");
		content.add(mailUser);
		
		mailUserTextBox.setTitle(Customization.USER_MAIL);
		content.add(mailUserTextBox);
		add(content);	
	}
	

	public AddRestaurantScreen(String city){
		
		this.city = city;
		init(true);
	}

	
	
	public AddRestaurantScreen() {
		cityController.addObserver(this);
		init(false);
	
	}
	
	@Override
	protected void onPageShow() {
		
		if(!loaded){
			if(isToCity){
				backButton = new JQMButton(Customization.BACK, PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE );
			}else{
				backButton = new JQMButton(Customization.BACK,  PagesController.getPage(Pages.PAGE_RESTAURANT_LIST), Transition.SLIDE );
			}
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);
			
			backButton.getElement().setInnerHTML(span);
			backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");
			
			header.add(backButton);
			loaded = true;
		}
		
		nameText.setText("");
		adressText.setText("");
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	private void setContentHeader(){
		header = new JQMHeader(Customization.RESTAURANTS);
		header.setFixed(true);
		header.setText(Customization.RESTAURANTS);
	}
	
	private void setButtons( ){
		
		saveButton = new JQMButton(Customization.SAVERESTAURANT);		
		saveButton.setIcon(DataIcon.PLUS);
		saveButton.setInline();
		saveButton.setIconPos(IconPos.RIGHT);
		saveButton.setId("saveButton");
	}
	
	private boolean checkFields(){
		if(!nameText.getText().isEmpty() && !adressText.getText().isEmpty()){
			return true;
		}
		else{
			if(nameText.getText().isEmpty() && adressText.getText().isEmpty()){
				warning.setText(Customization.EMPTYBOTHDATA);
			}else if(nameText.getText().isEmpty()){
				warning.setText(Customization.EMPTYNAME);
			}else {
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
		for(City city: CityController.getInstance().getCitiesList()){
			cityListBox.addItem(city.getCity());
		}
	}
	
}
