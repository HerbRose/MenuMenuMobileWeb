package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;

public class CityManagerPanel extends FlowPanel implements IManager {

	//pola do copiowania danych pomiedzy miastami
	private TextBox cityIdFrom;
	private TextBox cityIdTo;
	private JQMButton send;
	// KONIEC - pola do copiowania danych pomiedzy miastami
	
	//pola do edycji miast
	private List<City> cities = new ArrayList<City>();
	private Map<Long, FlowPanel> citiesPanels = new HashMap<Long, FlowPanel>();
	//KONIEC - pola do edycji miast
	
	//Kontrolery
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	//KONIEC - Kontrolery
	
	public CityManagerPanel() {
		
		setStyleName("barPanel", true);
		show(false);
		
		cityIdFrom = new TextBox();
		cityIdFrom.addStyleName("properWidth");
		setPlaceHolder(cityIdFrom, "doNotUseThis");
		
		cityIdTo = new TextBox();
		cityIdTo.addStyleName("properWidth");
		setPlaceHolder(cityIdTo, "doNotUseThis");
		
		send = new JQMButton(Customization.SAVE);
		send.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String pass = Window.prompt(Customization.PASSWORD_PLACEHOLDER, "");
				if(pass.equals("wdo67pla")){
					cityController.copyAllDataFromCity(cityIdFrom.getText(), cityIdTo.getText());
				}
				
			}

		});
		
		setCitiesList();
		
		add(cityIdFrom);
		
		add(cityIdTo);
		
		add(send);

	}
	
	private void setCitiesList(){
		fillCitiesList();
		
		for (final City city : cities) {
			
			FlowPanel cityDetails = new FlowPanel(); //div na detale miasta
			cityDetails.setStyleName("cityTableDiv", true);
			cityDetails.getElement().setId("cityToEdit"+city.getId());
			
			FlowPanel cityListDiv = new FlowPanel(); //div ze strzalka nazwa dzielnicy i detale miasta
			cityListDiv.setStyleName("cityListDiv", true);
			
			FlowPanel cityHeaderDiv = new FlowPanel(); //div na strzalke i nazwe dzielnicy
			cityHeaderDiv.setStyleName("cityHeaderDiv", true);
			
			Image blackArrowImage = new Image("img/blackArrow.png"); //strzalka
			blackArrowImage.setStyleName("blackArrowImage", true);
			
			final ToggleButton arrowToggleButton = new ToggleButton(blackArrowImage);
			arrowToggleButton.setStyleName("arrowToggleButton", true);
			
			arrowToggleButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(arrowToggleButton.isDown()){
						showCityTable(citiesPanels.get(city.getId()), arrowToggleButton, true);
					}else {
						showCityTable(citiesPanels.get(city.getId()), arrowToggleButton, false);
					}
				}
			});

			
			Label cityNameLabel = new Label(city.getCity()); //nazwa dzielnicy
			cityNameLabel.setStyleName("cityNameLabel", true);
			
			citiesPanels.put(city.getId(), cityDetails);
			
			cityHeaderDiv.add(arrowToggleButton);
			cityHeaderDiv.add(cityNameLabel);
			cityListDiv.add(cityHeaderDiv);
			cityListDiv.add(cityDetails);
			add(cityListDiv);
		}
	}
	private void fillCityDetails(FlowPanel cityDetails, final City city){
		
		cityDetails.clear();
		FlowPanel namePanel = new FlowPanel();
		namePanel.setStyleName("namePanel", true);
		FlowPanel visabilityPanel = new FlowPanel();
		visabilityPanel.setStyleName("visabilityPanel", true);
		FlowPanel buttonsPanel = new FlowPanel();
		buttonsPanel.setStyleName("buttonsPanel", true);
		
		Label nameLabel = new Label(Customization.CITY_NAME);
		TextBox nameTextBox = new TextBox();
		nameTextBox.getElement().setId("nameTextBox"+city.getId());
		nameTextBox.setText(city.getCity());
		
		Label visabilityLabel = new Label(Customization.VISIBILITY);
		final Button isVisable = new Button("");
		setVisabilityText(city.isVisable(), isVisable, city.getId());
		//isVisable.setValue(city.isVisable());
		
		isVisable.getElement().setId("isVisable"+city.getId());
		isVisable.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setVisabilityText(!cities.get(cities.indexOf(city)).isVisable(), isVisable, city.getId());
				
			}
		});
		
		Button deleteButton = new Button(Customization.DELETE);
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm(Customization.ARE_YOU_SURE_WANT_DELETE)){
					cityController.deleteCity(city.getId());
				}
				
			}
		});
		
		Button saveButton = new Button(Customization.SAVE);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				cityController.saveCity(city);
			}
		});
		namePanel.add(nameLabel);
		namePanel.add(nameTextBox);
		visabilityPanel.add(visabilityLabel);
		visabilityPanel.add(isVisable);
		buttonsPanel.add(saveButton);
		buttonsPanel.add(deleteButton);
		
		cityDetails.add(namePanel);
		cityDetails.add(visabilityPanel);
		cityDetails.add(buttonsPanel);
		
		showCityTable(cityDetails, null, false);
	}
	private void setVisabilityText(boolean isVisable, Button button, Long cityId){
		
		if(button == null) return;
		
		if(isVisable){
			button.setText(Customization.VISIBLE);
			
		}else{
			button.setText(Customization.HIDDEN);
		}
	
		button.setStyleName("greenText", isVisable);
		button.setStyleName("redText", !isVisable);
		for (City city : cities) {
			if(city.getId() == cityId) city.setVisable(isVisable);
		}
	}
	private void fillCitiesList(){
		cities.clear();
		cities.addAll( cityController.getCitiesList() );
	}
	public void showCityTable( Widget widget, ToggleButton arrowToggleButton, boolean isVisable) {
		
		if(isVisable){
			int width = getHeight(widget.getElement().getId()); 
			widget.setHeight(width+"px");
		}else{
			widget.setHeight("0px");
		}
		
		
		if(arrowToggleButton != null){
			arrowToggleButton.setStyleName("arrowToggleButtonShow", isVisable);
			arrowToggleButton.setStyleName("arrowToggleButtonHide", !isVisable);
		}
		
	}
	private void setPlaceHolder(Widget element, String placeHolder){
		element.getElement().setAttribute("placeHolder", placeHolder);
	}
	
	private boolean validData() {
		boolean isCorrect = false;
		

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
		cityIdFrom.setValue("");
		setValidDataStyle(null, cityIdFrom);
		cityIdTo.setValue("");
		setValidDataStyle(null, cityIdTo);
		
		for (City city : cities) {
			fillCityDetails(citiesPanels.get(city.getId()), city);
			
		}
		
	}

	@Override
	public String getName() {
		return Customization.CITY_MANAGER;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
	}

	
	private native int getHeight(String elementId)/*-{
		
		var children = $wnd.document.getElementById(elementId).childNodes;
		var length = children.length;
		console.log(children);
		var height = 0;
		for( i=0; i<=length-1; i++ ){
			height+= children[i].offsetHeight;
			console.log(height);
		}
		
		return height;
	}-*/;
	
	
}
