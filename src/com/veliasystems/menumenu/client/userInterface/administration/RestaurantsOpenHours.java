package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.Days;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyInfoPanelRow;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class RestaurantsOpenHours extends FlowPanel implements IManager, IObserver {
	
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	
	private Map<Long, FlowPanel> restaurantsPanels = new HashMap<Long, FlowPanel>();

	
	public RestaurantsOpenHours() {
		setStyleName("barPanel", true);
		show(false);
		

	}
	@Override
	public void clearData() {
		
	}

	@Override
	public String getName() {
		return Customization.RESTAURANT_OPEN_HOURS;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		
		if(isVisable){
			restaurantController.refreshRestaurants(this);
		}
		
	}
	@Override
	public void onChange() {
		
		
	}
	@Override
	public void newData() {
		restaurantList.clear();
		restaurantList.addAll(restaurantController.getRestaurantsList());
		setRestaurantsList();
		for(Restaurant restaurant: restaurantList){
			fillRestaurantsDetails(restaurantsPanels.get(restaurant.getId()), restaurant);
		}
	}
	
	private void setRestaurantsList(){
		clear();
		
		for(final Restaurant restaurant: restaurantList){
			FlowPanel restaurantDetails = new FlowPanel(); //div na detale miasta
			restaurantDetails.setStyleName("cityTableDiv", true);
			restaurantDetails.getElement().setId("cityToEdit"+restaurant.getId());
			
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
						showRestaurantTable(restaurantsPanels.get(restaurant.getId()), arrowToggleButton, true);
					}else {
						showRestaurantTable(restaurantsPanels.get(restaurant.getId()), arrowToggleButton, false);
					}
				}
			});

			
			Label cityNameLabel = new Label(restaurant.getName()); //nazwa dzielnicy
			cityNameLabel.setStyleName("cityNameLabel", true);
			
			restaurantsPanels.put(restaurant.getId(), restaurantDetails);
			
			cityHeaderDiv.add(arrowToggleButton);
			cityHeaderDiv.add(cityNameLabel);
			cityListDiv.add(cityHeaderDiv);
			cityListDiv.add(restaurantDetails);
			add(cityListDiv);
		}
		
	}
	
	private void showRestaurantTable( Widget widget, ToggleButton arrowToggleButton, boolean isVisable) {
		
		if(isVisable){
			int height = getHeight(widget.getElement().getId()); 
			widget.setHeight(height+20+ "px");
		}else{
			widget.setHeight("0px");
		}
		
		
		if(arrowToggleButton != null){
			arrowToggleButton.setStyleName("arrowToggleButtonShow", isVisable);
			arrowToggleButton.setStyleName("arrowToggleButtonHide", !isVisable);
		}
		
	}
	
	private void fillRestaurantsDetails(FlowPanel restaurantsDetails, final Restaurant restaurant){
		restaurantsDetails.clear();
		
		MyRestaurantInfoPanel restaurantInfoPanel = new MyRestaurantInfoPanel();
		restaurantInfoPanel.setStyleName("containerPanelAddRestaurant", true);
		restaurantInfoPanel.setWidth(JS.getElementOffsetWidth(getParent().getElement())-20 );
		
		
		Label nameLabel = new Label(Customization.NAME);
		TextBox nameTextBox = new TextBox();
//		nameTextBox.getElement().setId("nameTextBox"+restaurant.getId());
		nameTextBox.addStyleName("myTextBox nameBox");
		nameTextBox.setText(restaurant.getName());
		nameTextBox.setEnabled(false);
		
		Label monLabel = new Label(Days.MONDAY.dayName());
		final TextBox monTextBox = new TextBox();
		monTextBox.setEnabled(false);
		monTextBox.addStyleName("myTextBox nameBox openTextBox");
		
		final MyListCombo monCombo = new MyListCombo(false);
		monCombo.addStyleName("openCombo");
		
		monCombo.addListItem(monCombo.getNewItem("Poniedziałek Zamknięte"), 1);
		monCombo.addListItem(monCombo.getNewItem("Poniedziałek Otwarte"), 2);
		
		monCombo.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(monCombo.getSelectedOrder() == 1){
					monTextBox.setEnabled(false);
				}
				if(monCombo.getSelectedOrder() == 2){
					monTextBox.setEnabled(true);
				}
			}
		});
		
		FlowPanel monWrapper = new FlowPanel();
		FlowPanel tueWrapper = new FlowPanel();
		FlowPanel wedWrapper = new FlowPanel();
		FlowPanel thuWrapper = new FlowPanel();
		FlowPanel fridWrapper = new FlowPanel();
		FlowPanel satWrapper = new FlowPanel();
		FlowPanel sunWrapper = new FlowPanel();
		
		monWrapper.addStyleName("openWrapper");
		tueWrapper.addStyleName("openWrapper");
		thuWrapper.addStyleName("openWrapper");
		fridWrapper.addStyleName("openWrapper");
		satWrapper.addStyleName("openWrapper");
		sunWrapper.addStyleName("openWrapper");
		wedWrapper.addStyleName("openWrapper");
		
		
		
		
		monWrapper.add(monCombo);
		monWrapper.add(monTextBox);
		
		
		restaurantInfoPanel.addItem(nameLabel, nameTextBox);
		MyInfoPanelRow monRow = restaurantInfoPanel.addItem(monLabel, monWrapper);
//		monWrapper.setWidth(monRow.getElement().getStyle())
		restaurantsDetails.add(restaurantInfoPanel);
		showRestaurantTable(restaurantsDetails, null, false);
		
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
