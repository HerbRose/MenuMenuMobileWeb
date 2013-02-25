package com.veliasystems.menumenu.client.userInterface.administration;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;

public class UploadRestaurantsPanel extends FlowPanel implements IManager, IObserver{
	
	
	private TextArea jsonArea;
	private Button saveButton;
	private Image jsonImage;
	private FlowPanel wrapper = new FlowPanel();
	
	private int numberOfNewDataSuccess = 0;
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	
	public UploadRestaurantsPanel() {
		
		jsonArea = new TextArea();
		saveButton = new Button(Customization.SAVE);
		
		setStyleName("barPanel", true);
		show(false);
		
		
	}
	
	@Override
	public void clearData() {
	}

	@Override
	public String getName() {
		return Customization.UPLOAD;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		if(isVisable){
			setContent();
		}
	}
	
	private void setContent(){
		wrapper.clear();
		clear();
		wrapper.setStyleName("uploadWrapper");
		
		
		jsonArea.setStyleName("jsonArea");
		saveButton.setStyleName("uploadSaveButton");
		
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!jsonArea.getText().isEmpty()){
					PagesController.showWaitPanel();
					storeService.uploadRestaurants(jsonArea.getText(), new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							checkWhenHideSpinner();	
						}
						
						@Override
						public void onFailure(Throwable caught) {
							PagesController.hideWaitPanel();
						}
					});
					
					
				}else{
					PagesController.MY_POP_UP.showWarning(new Label(Customization.EMPTY_JSON), new IMyAnswer() {
						
						@Override
						public void answer(Boolean answer) {
							
						}
					});
				}
			}
		});
		
		jsonImage = new Image("img/goodJsonExample.png");
		jsonImage.setStyleName("goodJson");
		
		wrapper.add(jsonImage);
		wrapper.add(jsonArea);
		wrapper.add(saveButton);
		
		add(wrapper);
	}

	
	private void checkWhenHideSpinner(){
		numberOfNewDataSuccess = 0;
		PagesController.showWaitPanel();
		CityController.getInstance().refreshCitiesAndNotifyAll(this);
		RestaurantController.getInstance().refreshRestaurantsAndNotifyAll(this);
	}

	@Override
	public void onChange() {
	
	}

	@Override
	public void newData() {
		numberOfNewDataSuccess ++;
		if(numberOfNewDataSuccess == 2){
			PagesController.hideWaitPanel();
		}
	}
}
