package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class LastUploadedImages extends FlowPanel implements IManager, IObserver{
	
	private BlobServiceAsync blobService = GWT.create(BlobService.class);
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private ImagesController imagesController = ImagesController.getInstance();
	
	
	
	public LastUploadedImages() {
		show(false);
		
	}
	
	@Override
	public void clearData() {
	}

	@Override
	public String getName() {
		return Customization.LAST_UPLOADED;
	}


	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		
		
		
		if(isVisable){
			imagesController.getLast24hUploadedImages(this);
		}
	}

	@Override
	public void onChange() {
		
	}

	@Override
	public void newData() {
		List<ImageBlob> lastImages = imagesController.getLastUplaodedImages();

		clear();
		
		
		for (final ImageBlob imageBlob : lastImages) {
			
			Label nameRestaurant = new Label();
			Label dateUploaded = new Label();
			Label imageType = new Label();
			Image img = new Image(imageBlob.getImageUrl());
			Button deleteButton = new Button();
			final Button pubUnpubButton = new Button();
			
			final FlowPanel imgWrapper = new FlowPanel();
			imgWrapper.addStyleName("imgWrapper");
			nameRestaurant.addStyleName("lastUploadedName");
			dateUploaded.addStyleName("lastUploadedName");
			imageType.addStyleName("lastUploadedName");
			img.addStyleName("lastUploadedImage");
			deleteButton.addStyleName("lastUploadedDeleteButton");
			pubUnpubButton.addStyleName("lastUploadedDeleteButton");
			String restaurantName = restaurantController.getRestaurantName(Long.parseLong(imageBlob.getRestaurantId()));
			if(restaurantName == null){
				continue;
			}
			
			Long restId = Long.valueOf(imageBlob.getRestaurantId());
			final ImageType imageDeletedType = imageBlob.getImageType();
			
			final Restaurant restaurant = restaurantController.getRestaurant(restId);
			
			nameRestaurant.setText(Customization.RESTAURANTNAME + ": "+restaurantName);
			dateUploaded.setText(Customization.DATE_CREATED + ": " +imageBlob.getDateCreated().toString());
			imageType.setText(Customization.IMAGE_TYPE + ": "+imageBlob.getImageType().name());
			deleteButton.setText(Customization.DELETE);
			
			
			
			deleteButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
						PagesController.showWaitPanel();
						
						blobService.deleteBlob(imageBlob, false, new AsyncCallback<Boolean>() {
							
							@Override
							public void onSuccess(Boolean result) {
								if(result == true){
									remove(imgWrapper);
								}
								switch (imageDeletedType) {
								case LOGO:
									restaurant.setMainLogoImageString("");
									break;
								case MENU:
									restaurant.getMenuImages().remove(imageBlob);
									break;
								case PROFILE:
									restaurant.getProfileImages().remove(imageBlob);
									break;
								default:
									break;
								}
								PagesController.hideWaitPanel();
								
							}
							
							@Override
							public void onFailure(Throwable caught) {
								PagesController.hideWaitPanel();
							}
						});
				}
			});
			
			if(restaurant.isVisibleForApp()){
				pubUnpubButton.setText(Customization.PROFILE_PUBLISHED);
			}else{
				pubUnpubButton.setText(Customization.PROFILE_UNPUBLISHED);
			}
			
			pubUnpubButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					restaurant.setVisibleForApp(!restaurant.isVisibleForApp());
					
					restaurantController.saveRestaurant(restaurant, false);
					if(restaurant.isVisibleForApp()){
						pubUnpubButton.setText(Customization.PROFILE_PUBLISHED);
					}else{
						pubUnpubButton.setText(Customization.PROFILE_UNPUBLISHED);
					}
				}
			});
			
			imgWrapper.add(nameRestaurant);
			imgWrapper.add(dateUploaded);
			imgWrapper.add(imageType);
			imgWrapper.add(img);
			imgWrapper.add(deleteButton);
			imgWrapper.add(pubUnpubButton);
			add(imgWrapper);
			
		}
		
		PagesController.hideWaitPanel();
	}
	
	

}
