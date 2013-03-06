package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class MyUploadForm extends FormPanel {
	private VerticalPanel mainPanel = new VerticalPanel();
	private FileUpload fileUpload;
	private long restaurantId = -1;
	private ImageType imageType = null;
	private JQMPage backPage = null;
	private final String imageMimeType = "image/*";
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
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
		this.fileUpload.getElement().setPropertyString("accept",imageMimeType);
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
					restaurantController.cropImage(restaurantId, imageType, backPage);
				}else{
					restaurantController.cropImage(restaurantId, imageType, backPage, blobKey);
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
	
	 public void setBackPage(JQMPage backPage) {
		this.backPage = backPage;
	}
	
	public FileUpload getFileUpload() {
		return fileUpload;
	}
	 
	public void setChangeHandler(){
		 fileUpload.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					PagesController.showWaitPanel();
					// clickOnInputFile(formPanel.getUploadButton().getElement());
					blobService.getBlobStoreUrl(restaurantId+"", imageType,
							new AsyncCallback<String>() {

								@Override
								public void onSuccess(String result) {
									setAction(result);
									submit();
								}

								@Override
								public void onFailure(Throwable caught) {
									PagesController.hideWaitPanel();
									Window.alert("Problem with upload. Try again");

								}
							});
				}
			});
	 }

}
