package com.veliasystems.menumenu.client.ui;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
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
	private RestaurantController restaurantController = RestaurantController.getInstance();
	
	public MyImage( ImagesController imagesController, Image image, RestaurantImageView myParent) {
		this.image = image;
		this.parent = myParent;
		setStyleName("imagePanel");
		
		image.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				Document.get().getElementById("load").setClassName("show");
				
				restaurantController.setEmptyBoard(parent.getRestaurant());
				
			}
		});
		this.imagesController = imagesController;
		
	}
	
	public MyImage( ImagesController imagesController, ImageBlob imageBlob, RestaurantImageView parent) {
		this.url = imageBlob.getImageUrl();
		this.parent = parent;
		imgBlob = imageBlob;
		setMyImage(url);
		
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
		add(detailsPanel);
		setStyleName("imagePanel");
		this.imagesController = imagesController;
		add(image);

	}
		
	
	private void setMyImage(String url){
		image.setUrl(url);
		image.setStyleName("image");
	}
		//add(flowPanelButtons);
	

	
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
