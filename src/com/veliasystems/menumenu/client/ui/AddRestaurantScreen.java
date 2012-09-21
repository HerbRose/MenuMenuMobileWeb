package com.veliasystems.menumenu.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.veliasystems.menumenu.client.StoreService;
import com.veliasystems.menumenu.client.StoreServiceAsync;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class AddRestaurantScreen extends JQMPage{
	

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
	
	Restaurant restaurant;
	
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);

//	{
//		this.addClickHandler( new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {			
//					meClicked(event);
//			}
//		});
//	}
		
//	@Override
//	public HandlerRegistration addClickHandler(ClickHandler handler) {
//		return addDomHandler(handler, ClickEvent.getType());
//	}
	
	
//	void meClicked( ClickEvent e ) {
//		e.stopPropagation();
//		
//		Object o = e.getSource();
//		
//		System.out.println(o.toString());
//		System.out.println(o.getClass().toString());
//		System.out.println(o.getClass().getName()); 
//		
//		if (o instanceof JQMButton) {
//			JQMButton button = (JQMButton) o;
//			if (button.getId().equalsIgnoreCase(saveButton.getId())) {
//				Window.alert("Save Button clicked");
//				return;
//			}
//		}
//			
//	}
	
	public AddRestaurantScreen() {
		
		setContentHeader();
		setButtons();
		
		content = new JQMPanel();
		
		//header.setRightButton(saveButton);
		header.setLeftButton(backButton);

		add(header);			
		this.add(saveButton);
		cityLabel = new Label();
		cityLabel.setText(Customization.CITYONE + ":");
		
		content.add(cityLabel);	
		
		//cityText.setTitle(Customization.CITYONE);	
		//content.add(cityText);
		
		
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
		this.add(content);	
		
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(checkFields()){
				restaurant = new Restaurant();
				restaurant.setName(nameText.getText());
				
				restaurant.setAddress(adressText.getText());
				
				storeService.saveRestaurant(restaurant, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						Window.alert("save succed");
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
		});
		
	
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
		
		//saveButton = new JQMButton(Customization.SAVERESTAURANT, new RestaurantSavedScreen(), Transition.SLIDE);
		
		saveButton = new JQMButton(Customization.SAVERESTAURANT);		
		saveButton.setIcon(DataIcon.PLUS);
		saveButton.setInline();
		saveButton.setIconPos(IconPos.RIGHT);
		saveButton.setId("saveButton");
	}
	
	private boolean checkFields(){
		
		if(!cityText.getText().isEmpty() && !nameText.getText().isEmpty() && !adressText.getText().isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	private void showWarning(){
		Label warning = new Label();
		warning.setText(Customization.SAVERRESTAURANTERROR);
//		warning.addStyleName("color");
//		warning.setStyleName("red");
		this.add(warning);
	}
	
}
