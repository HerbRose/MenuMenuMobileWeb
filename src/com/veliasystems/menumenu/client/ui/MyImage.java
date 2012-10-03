package com.veliasystems.menumenu.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class MyImage extends FlowPanel {

	ImagesController imagesController;
	
	Image image = new Image();
	
	FlowPanel flowPanelButtons = new FlowPanel();
	FlowPanel flowPanelButton;
	
	Image cropImage;
	Image setMainImage;
	ImageBlob imgBlob;
	Label txtLabel;
	Label mainLAbel;
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	public MyImage(String url, ImagesController imagesController, ImageBlob imageBlob) {
		
		imgBlob = imageBlob;
		image.setUrl(url);
		image.setStyleName("image");
		
		cropImage = new Image("img/crop.png");
		cropImage.setStyleName("toolButton");
		cropImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				JQMContext.changePage(new CropImage(imgBlob));
			}
		});
		
		setMainImage = new Image("img/apply.png");
		
		setMainImage.setStyleName("toolButton");
		
		setMainImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				getImagesController().selectImage(getMe());
				storeService.setMainImage(imgBlob, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
		flowPanelButtons.addStyleName("hiddenPanel");
		
		addToolButon(cropImage, "toolButtonCointainer", Customization.CROP);
		addToolButon(setMainImage, "toolButtonCointainer", Customization.SET_AS_MAIN);
			
		setStyleName("imagePanel");
		this.imagesController = imagesController;
		image.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getImagesController().showFlowPanel(getMe());
			}
		});
		
		
		add(image);
		add(flowPanelButtons);
	}
	
	private void addToolButon( Image image, String styleName, String labelText){
		flowPanelButton = new FlowPanel();
		flowPanelButton.add(image);
		flowPanelButton.addStyleName(styleName);
		txtLabel = new Label();
		txtLabel.setText(labelText);
		txtLabel.setStyleName("txtLabel");
		flowPanelButton.add(txtLabel);
		flowPanelButtons.add(flowPanelButton);
	}
	
	private MyImage getMe(){
		return this;
	}
	public ImagesController getImagesController(){
		return imagesController;
	}
	
	public void setSelected(){
		image.addStyleName("imageSelected");
	}
	
	public void unselectImage(){
		image.removeStyleName("imageSelected");
		
	}
	
	public void hideFlowPanelButtons(){	
		flowPanelButtons.setStyleName("hiddenPanel");
		
	}
	
	public void showFLowPanelButtons(){
		flowPanelButtons.setStyleName("flowPanelButtonsVisible");
	}
	
	
}
