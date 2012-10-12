package com.veliasystems.menumenu.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class AddCity extends JQMPage implements HasClickHandlers{

//	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	JQMHeader header;
	JQMButton backButton;
	JQMButton saveButton;
	TextBox nameCity;
	Label warningLabel = new Label();
	
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
	
	private void meClicked(ClickEvent event){
		if(isClicked(event, saveButton)){
			if(validate()){
				CityController.getInstance().saveCity(nameCity.getText());
//				storeService.addCity(nameCity.getText(), new AsyncCallback<Void>() {
//					
//					@Override
//					public void onSuccess(Void result) {
//						// TODO Auto-generated method stub
//							Window.Location.reload();
//					}
//					
//					@Override
//					public void onFailure(Throwable caught) {
//						// TODO Auto-generated method stub
//						
//					}
//				} );
			}
		}
	}
	
	public AddCity() {
		// TODO Auto-generated constructor stub
		init();
	}
	
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		nameCity.setText("");
	}
	private void init(){
		header = new  JQMHeader(Customization.ADD_CITY);
		header.setFixed(true);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		backButton.setBack(true);
		header.add(backButton);
		
		saveButton = new JQMButton(Customization.SAVE);
		saveButton.setIcon(DataIcon.PLUS);
		saveButton.setIconPos(IconPos.RIGHT);
		header.add(saveButton);
		add(header);
		
		nameCity = new TextBox();
		nameCity.setStyleName("addCity");
		add(nameCity);
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
	
	private boolean validate(){
		if(nameCity.getText().contains("-")){
			
			return true;
		}
		showWarning();
		return false;
	}
	
	private void showWarning(){
		warningLabel.setText(Customization.WRONG_CITY_NAME);
		warningLabel.setStyleName("warning");
		add(warningLabel);
	}
}
