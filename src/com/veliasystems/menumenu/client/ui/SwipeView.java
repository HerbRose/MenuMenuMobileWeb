package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
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
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.elements.JQMTextArea;
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
	private ImageBlob mainImage;
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
	
	public SwipeView(Restaurant restaurant, ImageType imageType) {
		//Window.alert(Navigator.getPlatform());
		switch (imageType) {
			case LOGO:
				imageUrlList = restaurant.getLogoImages();
				title = Customization.LOGO_PICTURE_TITLE;
				mainImage = restaurant.getMainLogoImage();
				break;
			case MENU:
				imageUrlList = restaurant.getMenuImages();
				title = Customization.MAENU_PICTURE_TITLE;
				mainImage = restaurant.getMainMenuImage();
				break;
			case PROFILE:
				imageUrlList = restaurant.getProfileImages();
				title = Customization.PROFILE_PICTURE_TITLE;
				mainImage = restaurant.getMainProfileImage();
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
			mainImageUrl = mainImage.getImageUrl();
		}
		wrapper.addStyleName("wrapper");
		
		setCameraImage();
		fillImages(mainImageUrl);
		setMyTitle(title);
		
		addStyleName("swipeView");
	}
	
	private void fillImages(String mainImageUrl){
		
		scrollerContainer.addStyleName("scroller");
		
		MyImage newImage;
		
		for (ImageBlob image : imageUrlList) {	
			
			newImage = new MyImage(image.getImageUrl(), imagesController);
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
	
	private void setCameraImage(){
		
		Image cameraImg = new Image("img/camera.png");
		cameraDiv.add( cameraImg );
		cameraDiv.addStyleName("cameraDiv");
		
		cameraContainerDiv.add(cameraDiv);
		cameraContainerDiv.addStyleName("cameraContainerDiv");
		
		fileUpload.setVisible(true);
		fileUpload.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				
				clickOnInputFile(formPanel.getUploadButton().getElement());
			}
		});
		
		formPanel = new MyUploadForm(fileUpload, imageType, restaurant.getId()+"");
		formPanel.setVisible(false);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.getMainPanel().add(fileUpload);
		formPanel.addSubmitCompleteHandler( new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				blobService.getLastUploadedImage(String.valueOf(restaurant.getId()), new AsyncCallback<ImageBlob>() {
					
					@Override
					public void onSuccess(ImageBlob result) {
						// TODO Auto-generated method stub
						CropImage cropImage = new CropImage(result);
						System.out.println(cropImage.getId());
						JQMContext.changePage(cropImage);
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
				formPanel.reset();
			}
		});
		
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

		add(cameraContainerDiv);
		add(formPanel);
		
	}
	
	private static native void clickOnInputFile(Element elem) /*-{
	elem.click();
	
}-*/;


private static native void onUploadFormLoaded(String windowName, Element fileUpload, String blobStoreUrl, String callbackURL) /*-{
	window.name = windowName;
	
	var url = "fileupload2://new?postValues=&postFileParamName=multipart/form-data&shouldIncludeEXIFData=true&postURL="+encodeURI(blobStoreUrl)+"&callbackURL="+encodeURI(callbackURL)+"&returnServerResponse=false&isMultiselectForbidden=true&mediaTypesAllowed=image";
	
	$wnd.Picup2.convertFileInput( fileUpload,  { windowName : encodeURI('My Web App'), 'purpose' : encodeURI(url) });
	
	window.open('', '_self', ''); 
	window.close();  
	
}-*/;


	public String getRestId() {
		return restaurant.getId()+"";
	}
	
	public ImageType getImageType() {
		return imageType;
	}
	
	
	
}

class ToAddInformation {
	public String REST_ID = ""; 
	public ImageType imgType;
	
}


class MyUploadForm extends FormPanel {
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private JQMButton uploadButton = new JQMButton("Upload");
	JQMTextArea textArea = new JQMTextArea();
	ImageType imageType;
	String restId;
	
	
	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	
	public MyUploadForm( FileUpload fileUpload, ImageType imageType, String restId ) {
			
		this.imageType = imageType;
		this.restId = restId;
		
		mainPanel.add(textArea);
		mainPanel.add(fileUpload);
		mainPanel.add(uploadButton);
		
		fileUpload.setName("image");
		
		uploadButton.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				blobService.getBlobStoreUrl( getRestId(), getImageType(), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						setAction(result);
						submit();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						textArea.setText(caught.getMessage());
					}
				});
				
			}
		});
		
		setWidget(mainPanel);
		
		
	}
	
	public String getRestId() {
		return restId;
	}
	
	public ImageType getImageType() {
		return imageType;
	}
	
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}
	public JQMButton getUploadButton() {
		return uploadButton;
	}
}
