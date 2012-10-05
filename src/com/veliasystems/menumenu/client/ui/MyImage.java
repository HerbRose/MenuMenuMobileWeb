package com.veliasystems.menumenu.client.ui;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
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
	
	
	FlowPanel flowPanelButton;
	
	Image cropImage;
	Image setMainImage;
	ImageBlob imgBlob;
	Label txtLabel;
	Label mainLAbel;
	String url;
	
	Image deleteImage;
	
	private  boolean mainImage = false;
	
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	private BlobServiceAsync blobService = GWT.create(BlobService.class);
	private RestaurantImageView parent;
	
	public MyImage( ImagesController imagesController, ImageBlob imageBlob, RestaurantImageView parent) {
		this.url = imageBlob.getImageUrl();
		this.parent = parent;
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
				
				storeService.setMainImage(imgBlob, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						getImagesController().selectImage(getMe());
						removeDomItems();
						// TODO Auto-generated method stub
						//Window.Location.reload();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
		//flowPanelButtons.addStyleName("hiddenPanel");

		deleteImage = new Image("img/delete.png");
		deleteImage.setStyleName("toolButton");
		
		deleteImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				if(!mainImage){
					blobService.deleteBlob(imgBlob, new AsyncCallback<Boolean>() {
	
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}
	
						@Override
						public void onSuccess(Boolean result) {
							// TODO Auto-generated method stub
							Window.Location.reload();
						}
					});
				}

			}
		});
		
			
		setStyleName("imagePanel");
		this.imagesController = imagesController;
		image.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				FlowPanel panel = new FlowPanel();
				panel.getElement().setId("imagePanel");
				Image tmpImg = new Image(getUrl());
				tmpImg.getElement().setId("imageClicked");
				FlowPanel flowPanelButtons = new FlowPanel();
				flowPanelButtons.getElement().setId("tooltip");
				flowPanelButtons.add(addToolButon(cropImage, "toolButtonCointainer", Customization.CROP));
				flowPanelButtons.add(addToolButon(setMainImage, "toolButtonCointainer", Customization.SET_AS_MAIN));
				if(!isMainImage()){
					flowPanelButtons.add(addToolButon(deleteImage, "toolButtonCointainer", Customization.DELETE_IMAGE));
				}
				tmpImg.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						removeDomItems();
						//getImagesController().showFlowPanel(getMe());
					}
				});
				
				//panel.add(tmpImg);
				
				getMyParent().add(panel);
				getMyParent().add(tmpImg);
				getMyParent().add(flowPanelButtons);

				//Document.get().getElementById("imagePanel").setClassName("show");
				
				
			}
		});
		
		
		add(image);
		//add(flowPanelButtons);
	}
	
	private FlowPanel addToolButon( Image image, String styleName, String labelText){
		flowPanelButton = new FlowPanel();
		flowPanelButton.add(image);
		flowPanelButton.addStyleName(styleName);
		txtLabel = new Label();
		txtLabel.setText(labelText);
		txtLabel.setStyleName("txtLabel");
		flowPanelButton.add(txtLabel);
		return flowPanelButton;
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
	public void removeDomItems(){
		Document.get().getElementById("imagePanel").removeFromParent();
		Document.get().getElementById("imageClicked").removeFromParent();
		Document.get().getElementById("tooltip").removeFromParent();
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
