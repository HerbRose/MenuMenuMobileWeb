package com.veliasystems.menumenu.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.veliasystems.menumenu.client.controllers.ImagesController;

public class MyImage extends Image {

	ImagesController imagesController;
	
	public MyImage(String url, ImagesController imagesController) {
	
		setUrl(url);
		setStyleName("image");
		this.imagesController = imagesController;
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				getImagesController().selectImage(getMe());
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
