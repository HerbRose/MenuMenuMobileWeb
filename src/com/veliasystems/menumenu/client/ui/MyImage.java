package com.veliasystems.menumenu.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class MyImage extends Image {

	ImagesController imagesController;
	
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	public MyImage(String url, ImagesController imagesController, ImageBlob imageBlob) {
	
		final ImageBlob imgBlob = imageBlob;
		setUrl(url);
		setStyleName("image");
		this.imagesController = imagesController;
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				getImagesController().selectImage(getMe());
				storeService.setMainImage(imgBlob, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						System.out.println("main image set");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("fail");
					}
				});
			}
		});
	}
	
	private MyImage getMe(){
		return this;
	}
	public ImagesController getImagesController(){
		return imagesController;
	}
	
	public void setSelected(){
		addStyleName("imageSelected");
	}
	
	public void unselect(){
		removeStyleName("imageSelected");
	}
	
}
