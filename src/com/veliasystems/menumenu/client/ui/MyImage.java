package com.veliasystems.menumenu.client.ui;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class MyImage extends FlowPanel {

	ImagesController imagesController;
	
	Image image = new Image();
	
	ImageBlob imgBlob;
	Label txtLabel;
	Label mainLAbel;
	String url;
	

	
	FlowPanel detailsPanel;
	HTML detailsContent;
	

	private  boolean mainImage = false;
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	private RestaurantImageView parent;
	
	public MyImage( ImagesController imagesController, ImageBlob imageBlob, RestaurantImageView parent) {
		this.url = imageBlob.getImageUrl();
		this.parent = parent;
		imgBlob = imageBlob;
		image.setUrl(url);
		image.setStyleName("image");
		
		image.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				Document.get().getElementById("load").setClassName("show");
				storeService.setMainImage(imgBlob, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						Document.get().getElementById("load").setClassName("hide");
						getImagesController().selectImage(getMe());
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
						
					}
				});
			}
		});
		
		detailsPanel = new FlowPanel();
		detailsPanel.setStyleName("details");
		
		String dateTimeFormat = DateTimeFormat.getShortDateFormat().format(imageBlob.getDateCreated(), null);

		
		detailsContent = new HTML(dateTimeFormat);
		
		detailsPanel.add(detailsContent);
		detailsContent.setStyleName("detailsContent");
		//flowPanelButtons.addStyleName("hiddenPanel");
		
		setStyleName("imagePanel");
		this.imagesController = imagesController;
		add(image);

	}
		

		//add(flowPanelButtons);
	}

	
	private MyImage getMe(){
		return this;
	}
	public ImagesController getImagesController(){
		return imagesController;
	}
	
	public void setSelected(){
		image.addStyleName("imageSelected");
		setMain(true);
	}
	
	public void unselectImage(){
		image.removeStyleName("imageSelected");
		setMain(false);
	}



	public String getUrl() {
		return url;
	}
	public RestaurantImageView getMyParent() {
		return parent;
	}
	
	public void setMain(Boolean isMain){
		mainImage = isMain;
	}
	public boolean isMainImage() {
		return mainImage;
	}
	
	
}
