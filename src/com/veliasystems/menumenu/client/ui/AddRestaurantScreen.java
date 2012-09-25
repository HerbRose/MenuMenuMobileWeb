package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class AddRestaurantScreen extends JQMPage implements HasClickHandlers{
	

	JQMHeader header;
	JQMButton backButton;
	JQMButton saveButton;
	JQMPanel content;
	
	Label nameLabel;
	Label cityLabel;
	Label adressLabel;
	
	TextBox cityText = new TextBox();
	TextBox nameText = new TextBox();
	TextBox adressText = new TextBox();
	
	ListBox cityList = new ListBox();
	Label warning = new Label();
	
	Restaurant restaurant;
	
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);

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
		
//		if(x == clickedX && y == clickedY){
//		Window.alert("clicked: " + x + " " + y);
//		}
		
		
		int buttonWidth = (int) saveButton.getElement().getClientWidth();
		int buttonHeight = (int) saveButton.getElement().getClientHeight();
		
		//Window.alert("button x: " + buttonX + " " + buttonY + "size of button: " + buttonWidth + " " + buttonHeight);
		
		if(clickedX>= buttonX && (clickedX <= buttonX + buttonWidth) && clickedY >= buttonY && (clickedY <= buttonY + buttonHeight)){
			
			if(checkFields()){
//				
				restaurant = new Restaurant();
				restaurant.setName(nameText.getText());
				
				restaurant.setAddress(adressText.getText());			
				int index = cityList.getSelectedIndex();			
				restaurant.setCity(cityList.getItemText(index));				
				storeService.saveRestaurant(restaurant, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						System.out.println(restaurant.getName() + ' ' + restaurant.getCity() + ' ' + restaurant.getAddress());
						com.google.gwt.user.client.Window.Location.reload();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
			else{
				showWarning();
			}
				
		}
	}
			

	
	public AddRestaurantScreen() {
		
		setContentHeader();
		setButtons();
		
		content = new JQMPanel();
		
		header.setLeftButton(backButton);
		header.setRightButton(saveButton);
		add(header);			
		
		//this.add(saveButton);
		
		cityLabel = new Label();
		cityLabel.setText(Customization.CITYONE + ":");
		
		content.add(cityLabel);	
		
		storeService.loadCities(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(List<String> result) {
				// TODO Auto-generated method stub
				for(String item: result){
					cityList.addItem(item);
					content.add(cityList);
				}
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
				add(content);	
			}
		});
			
		
		
//		saveButton.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				// TODO Auto-generated method stub
//				if(checkFields()){
//					
//					restaurant = new Restaurant();
//					restaurant.setName(nameText.getText());
//					
//					restaurant.setAddress(adressText.getText());			
//					int index = cityList.getSelectedIndex();			
//					restaurant.setCity(cityList.getItemText(index));				
//					storeService.saveRestaurant(restaurant, new AsyncCallback<Void>() {
//						
//						@Override
//						public void onSuccess(Void result) {
//							// TODO Auto-generated method stub
//							System.out.println(restaurant.getName() + ' ' + restaurant.getCity() + ' ' + restaurant.getAddress());
//							com.google.gwt.user.client.Window.Location.reload();
//						}
//						
//						@Override
//						public void onFailure(Throwable caught) {
//							// TODO Auto-generated method stub
//							
//						}
//					});
//					
//				}
//				else{
//					showWarning();
//				}
//			}
//		});
		
	
	}
	
	private void setContentHeader(){
		header = new JQMHeader(Customization.RESTAURANTS);
		header.setFixed(true);
		header.setText(Customization.RESTAURANTS);
	}
	
	private void setButtons(){
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setInline();
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		saveButton = new JQMButton(Customization.SAVERESTAURANT);		
		saveButton.setIcon(DataIcon.PLUS);
		saveButton.setInline();
		saveButton.setIconPos(IconPos.RIGHT);
		saveButton.setId("saveButton");
	}
	
	private boolean checkFields(){
		
		if(!nameText.getText().isEmpty() && !adressText.getText().isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	private void showWarning(){
		
		warning.setText(Customization.SAVERRESTAURANTERROR);
//		warning.addStyleName("color");
//		warning.setStyleName("red");
		this.add(warning);
	}
	
}
