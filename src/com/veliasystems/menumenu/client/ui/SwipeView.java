package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CustomScrollPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class SwipeView extends FlowPanel {
	
	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	
	private List<ImageBlob> imageUrlList;
	private String mainImage;
	private String title = "";
	private String mainImageUrl = "";
	
	private CustomScrollPanel wrapper = new CustomScrollPanel();
	private FlowPanel scrollerContainer = new FlowPanel();
	private ImagesController imagesController = new ImagesController();
	
	private FlowPanel titleDiv = new FlowPanel();
	private FlowPanel cameraDiv = new FlowPanel();
	private FlowPanel cameraContainerDiv = new FlowPanel();
	
	private FileUpload fileUpload = new FileUpload();
	private MyUploadForm formPanel;
	
	private Restaurant restaurant;
	private ImageType imageType;
	private Image cameraImg = new Image("img/camera.png");
	
	private String osType =Navigator.getPlatform();
	
	public SwipeView(Restaurant restaurant, ImageType imageType) {
		
		switch (imageType) {
			case LOGO:
				imageUrlList = restaurant.getLogoImages();
				title = Customization.LOGO_PICTURE_TITLE;
				mainImage = restaurant.getMainLogoImageString();
				break;
			case MENU:
				imageUrlList = restaurant.getMenuImages();
				title = Customization.MAENU_PICTURE_TITLE;
				mainImage = restaurant.getMainMenuImageString();
				break;
			case PROFILE:
				imageUrlList = restaurant.getProfileImages();
				title = Customization.PROFILE_PICTURE_TITLE;
				mainImage = restaurant.getMainProfileImageString();
				break;
			default:
				break;
		}
		
		this.restaurant = restaurant;
		this.imageType = imageType;
		
		if(imageUrlList == null){
			this.imageUrlList = new ArrayList<ImageBlob>();
		}
		if(mainImage != null){
			mainImageUrl = mainImage;
		}
		wrapper.addStyleName("wrapper");
		
		setUpload();
		fillImages(mainImageUrl);
		setMyTitle(title);
		
		addStyleName("swipeView");
	}
	
	private void fillImages(String mainImageUrl){
		
		scrollerContainer.addStyleName("scroller");
		
		MyImage newImage;
		
		for (ImageBlob image : imageUrlList) {	
			
			newImage = new MyImage(image.getImageUrl(), imagesController, image);
			
			if(mainImageUrl.equals(image.getImageUrl())){
				imagesController.selectImage(newImage);
				scrollerContainer.insert(newImage, 0);
				continue;
			}
			scrollerContainer.add(newImage);
		}

		
		
		//add(scrollerDiv);
		wrapper.setWidget(scrollerContainer);
		add(wrapper);
	}
	
	private void setMyTitle( String title ) {
		titleDiv.addStyleName("titleDiv");
		Element span = DOM.createSpan();
		span.setInnerText(title);
		
		titleDiv.getElement().insertFirst(span);
		add(titleDiv);
	}
	
	private void setAndroidUpload(){
		formPanel.setVisible(true);
		formPanel.addStyleName("androidForm");
		cameraContainerDiv.add(formPanel);
	}
	
	private void setCameraImg(){
		cameraDiv.add( cameraImg );
		cameraDiv.addStyleName("cameraDiv");
		
		cameraContainerDiv.add(cameraDiv);
	}
	
	private void setAppleUpload(){
		
		setCameraImg();
		cameraImg.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				blobService.getBlobStoreUrl( getRestId(), getImageType(), new AsyncCallback<String>() {		
						@Override
						public void onSuccess(String result) {
							String callbackURL = "http://mymenumenu.appspot.com";
							
							onUploadFormLoaded(restaurant.getName() + "_" + imageType, fileUpload.getElement(), result, callbackURL);
							
							clickOnInputFile(fileUpload.getElement());
							
						}
						@Override
						public void onFailure(Throwable caught) {
							
						}
				});
			}
			
		});
	}
	private void setOtherUpload(){
		setCameraImg();
		
		cameraImg.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clickOnInputFile(fileUpload.getElement());
			}
			
		});
	}
	private void setUpload(){
		
		formPanel = new MyUploadForm(fileUpload, imageType, restaurant.getId()+"");
		formPanel.setVisible(false);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.getMainPanel().add(fileUpload);
		formPanel.addSubmitCompleteHandler( new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				formPanel.reset();
				
				
				Window.Location.reload();
				
				//Document.get().getElementById("loadDisplay").setId("load");
			}
		});
		
		
		
		if( osType.toLowerCase().indexOf("ipad") >=0 || osType.toLowerCase().indexOf("ipho")>=0 ){ //ipad or iphone
			setAppleUpload();
		}else if(osType.toLowerCase().indexOf("armv") >= 0){ //android
			setAndroidUpload();
		}else{
			setOtherUpload();
		}
		
		
		
		fileUpload.setVisible(true);
		fileUpload.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				//clickOnInputFile(formPanel.getUploadButton().getElement());
				blobService.getBlobStoreUrl( getRestId(), getImageType(), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						Document.get().getElementById("load").setId("loadDisplay");
						formPanel.setAction(result);
						formPanel.submit();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Problem with upload. Try again");
					}
				});
				
			}
		});
		
		
		
		cameraContainerDiv.addStyleName("cameraContainerDiv");
		add(cameraContainerDiv);
		add(formPanel);
		
	}
	private static native void clickOnInputFile(Element elem) /*-{
		elem.click();
		
	}-*/;
	


	private static native void onUploadFormLoaded(String windowName, Element fileUpload, String blobStoreUrl, String callbackURL) /*-{
		window.name = windowName;
		
		var url = "fileupload2://new?postValues=&postFileParamName=multipart/form-data&shouldIncludeEXIFData=true&postURL="+encodeURI(blobStoreUrl)+"&callbackURL="+encodeURI(callbackURL)+"&returnServerResponse=false&isMultiselectForbidden=true&mediaTypesAllowed=image";
		
		window.open('', '_self', ''); 
		window.close();  
		
		$wnd.Picup2.convertFileInput( fileUpload,  { windowName : encodeURI('My Web App'), 'purpose' : encodeURI(url) });
		
		
	}-*/;

	
	public String getRestId() {
		return restaurant.getId()+"";
	}
	
	public ImageType getImageType() {
		return imageType;
	}
	
	
	
}

class MyUploadForm extends FormPanel {
	private VerticalPanel mainPanel = new VerticalPanel();
	
	FileUpload fileUpload ;
	
	public MyUploadForm( FileUpload fileUpload, ImageType imageType, String restId ) {
			
		this.fileUpload = fileUpload;
		mainPanel.add(fileUpload);
		fileUpload.setName("image");
		setWidget(mainPanel);
		
	}
	
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}

	public FileUpload getFileUpload() {
		return fileUpload;
	}
	
}