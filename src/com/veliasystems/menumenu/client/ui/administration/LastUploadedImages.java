package com.veliasystems.menumenu.client.ui.administration;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class LastUploadedImages extends FlowPanel implements IManager{
	
	private BlobServiceAsync blobService = GWT.create(BlobService.class);
	
	public LastUploadedImages() {
		blobService.getLast24hImages(new AsyncCallback<List<ImageBlob>>() {
			
			@Override
			public void onSuccess(List<ImageBlob> result) {
				for (final ImageBlob imageBlob : result) {
					final FlowPanel imgWrapper = new FlowPanel();
					imgWrapper.addStyleName("imgWrapper");
					
					
					Image img = new Image(imageBlob.getImageUrl());
					img.addStyleName("lastUploadedImage");
					
					Button deleteButton = new Button();
					deleteButton.addStyleName("lastUploadedDeleteButton");
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
					
					
					imgWrapper.add(img);
					imgWrapper.add(deleteButton);
					
					add(imgWrapper);
					
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("fail");
			}
		});	
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
