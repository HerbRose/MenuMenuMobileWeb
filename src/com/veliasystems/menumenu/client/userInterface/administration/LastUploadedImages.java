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
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class LastUploadedImages extends FlowPanel implements IManager{
	
	private BlobServiceAsync blobService = GWT.create(BlobService.class);
	private RestaurantController restaurantController = RestaurantController.getInstance();
	
	public LastUploadedImages() {
		blobService.getLast24hImages(new AsyncCallback<List<ImageBlob>>() {
			
			@Override
			public void onSuccess(List<ImageBlob> result) {
				for (final ImageBlob imageBlob : result) {
					
					final FlowPanel imgWrapper = new FlowPanel();
					Label nameRestaurant = new Label();
					Image img = new Image(imageBlob.getImageUrl());
					Button deleteButton = new Button();
					
					
					imgWrapper.addStyleName("imgWrapper");
					nameRestaurant.addStyleName("lastUploadedName");
					img.addStyleName("lastUploadedImage");
					deleteButton.addStyleName("lastUploadedDeleteButton");
					
					String restaurantName = restaurantController.getRestaurantName(Long.parseLong(imageBlob.getRestaurantId()));
					if(restaurantName == null){
						continue;
					}
					nameRestaurant.setText(restaurantName);
			
					deleteButton.setText(Customization.DELETE);
					deleteButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							PagesController.showWaitPanel();
							
								blobService.deleteBlob(imageBlob, new AsyncCallback<Boolean>() {
									
									@Override
									public void onSuccess(Boolean result) {
										if(result == true){
											remove(imgWrapper);
										}
										PagesController.hideWaitPanel();
									}
									
									@Override
									public void onFailure(Throwable caught) {
									
									}
								});
						}
					});
					
					imgWrapper.add(nameRestaurant);
					imgWrapper.add(img);
					imgWrapper.add(deleteButton);
					
					add(imgWrapper);
					
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("fail");
			}
		});	
	}
	
	private void fillImages(){
		
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
	}

}
