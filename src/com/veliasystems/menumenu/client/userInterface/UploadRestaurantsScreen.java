package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class UploadRestaurantsScreen extends JQMPage implements HasClickHandlers{
	
	JQMHeader header;
	JQMFooter footer;
	TextArea jsonArea;
	JQMButton backButton;
	JQMButton uploadButton;
	Label label = new Label(Customization.EMPTY_JSON);
	Image jsonImage;
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	private boolean loaded = false;
	
	
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
		if(isClicked(event, uploadButton)){
			
			String json = jsonArea.getText();
			
			if(validate()){
			
				storeService.uploadRestaurants(json, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						if(!result.equals(null)){
							jsonArea.setText(result);
						}
					}
					
				});
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
	
	public UploadRestaurantsScreen() {
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		
		header = new JQMHeader(Customization.UPLOAD);
		header.setFixed(true);
		add(header);
		
		jsonArea = new TextArea();
		jsonArea.setHeight("400px");
		jsonArea.setWidth("50%");
		
		jsonImage = new Image("img/goodJson.png");
		jsonImage.setStyleName("goodJson");
		add(jsonImage);
		add(jsonArea);
		
		
		footer = new JQMFooter();
		uploadButton = new JQMButton(Customization.UPLOAD);
		uploadButton.setWidth("100%");
		uploadButton.setIcon(DataIcon.FORWARD);
		uploadButton.setIconPos(IconPos.TOP);
		
		footer.setFixed(true);
		add(footer);
		footer.add(uploadButton);
	}
	
	private boolean validate(){
		
			if(!jsonArea.getText().isEmpty()){
				return true;
			}
			else{
				showWarning();
			}
		return false;
	}
	
	private void showWarning(){
		label.setStyleName("warning");
		add(label);
	}
	
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		super.onPageShow();
		
		if(!loaded){
			backButton = new JQMButton(Customization.BACK);
			backButton.setBack(true);
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);
			
			backButton.getElement().setInnerHTML(span);
			backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");
			
			header.add(backButton);
			
			loaded = true;
		}
		
		Document.get().getElementById("load").setClassName(R.LOADED);
	}

}
