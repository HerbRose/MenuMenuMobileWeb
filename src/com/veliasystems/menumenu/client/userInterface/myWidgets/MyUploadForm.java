package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageType;

public class MyUploadForm extends FormPanel {
	private VerticalPanel mainPanel = new VerticalPanel();
	private FileUpload fileUpload;
	private long restaurantId = -1;
	private ImageType imageType = null;

	private RestaurantController restaurantController = RestaurantController.getInstance();
	/**
	 * default settings:
	 * <ul>
	 * 	<li>FormPanel.ENCODING_MULTIPART</li>
	 * 	<li>FormPanel.METHOD_POST</li>
	 * </ul>
	 * @param fileUpload
	 * @param imageType
	 * @param restId
	 */
	public MyUploadForm(FileUpload fileUpload, ImageType imageType,
			String restId) {

		restaurantId = Long.parseLong(restId);
		this.imageType = imageType;
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
		
		this.fileUpload = fileUpload;
		mainPanel.add(fileUpload);
		fileUpload.setName("image");
		setWidget(mainPanel);
		
		addMySubmitCompleteHandler();

	}

	private void addMySubmitCompleteHandler(){
		addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				getMe().reset();
				
				String blobKey = getBlobKeyFromResult(event.getResults());
				if( blobKey == null){
					restaurantController.cropImage(restaurantId, imageType);
				}else{
					restaurantController.cropImage(restaurantId, imageType, blobKey);
				}
				
			}
		});
	}
	
	private MyUploadForm getMe(){
		return this;
	}
	
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}
	
	/**
	 * Return blobKey from result
	 * @param result 
	 * @return blobKey or null if not found
	 */
	public String getBlobKeyFromResult(String result){
		
		if(result != null && !result.isEmpty()){
		
			String[] splitQuotes = result.split("\"");
			String blobKeyFromResult = null;
			for (String string : splitQuotes) {
				if(string.indexOf("blob-key")>=0){
					blobKeyFromResult = string;
				}
			}
			
			if(blobKeyFromResult != null && !blobKeyFromResult.isEmpty()){
				String[] splitEqual = blobKeyFromResult.split("=");
				if(splitEqual.length > 1 && splitEqual[1].length()>0){
					return splitEqual[1];
				}
			}
		}
		
		return null;
	}
	
	 

}
