package com.veliasystems.menumenu.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class MyImage extends FlowPanel {

	ImagesController imagesController;
	
	Image image = new Image();
	
	FlowPanel flowPanelButtons = new FlowPanel();
	
	JQMButton cropButton;
	JQMButton setMainButton;
	ImageBlob imgBlob;
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	public MyImage(String url, ImagesController imagesController, ImageBlob imageBlob) {
		
		imgBlob = imageBlob;
		image.setUrl(url);
		image.setStyleName("image");
		
		cropButton = new JQMButton("", new CropImage(imgBlob), Transition.SLIDE);
		cropButton.setIcon(DataIcon.GEAR);
		cropButton.setIconPos(IconPos.NOTEXT);
	
		flowPanelButtons.add(cropButton);
		setMainButton = new JQMButton("");
		setMainButton.setIcon(DataIcon.STAR);
		setMainButton.setIconPos(IconPos.NOTEXT);
		
		flowPanelButtons.add(setMainButton);
		
		setMainButton.addClickHandler(new ClickHandler() {
			
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
