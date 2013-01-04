package com.veliasystems.menumenu.client.userInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
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
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.LoadedPageController;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyUploadForm;
public class SwipeView extends FlowPanel {

	private final BlobServiceAsync blobService = GWT.create(BlobService.class);

	private List<ImageBlob> imageBlobList;
	private String title = "";
	private String mainImageUrl = "";

	private CustomScrollPanel wrapper = new CustomScrollPanel();
	private FlowPanel scrollerContainer = new FlowPanel();
	private ImagesController imagesController = ImagesController.getInstance();

	private FlowPanel titleDiv = new FlowPanel();
	private FlowPanel cameraDiv = new FlowPanel();
	private FlowPanel cameraContainerDiv = new FlowPanel();

	private FileUpload fileUpload = new FileUpload();
	private MyUploadForm formPanel;
	private RestaurantImageView parent;

	private Restaurant restaurant;
	private ImageType imageType;
	private Image cameraImg = new Image("img/layout/plus.png");

	private LoadedPageController loadedPageController;

	private String osType = getUserAgent(); 
	private RestaurantController restaurantController = RestaurantController
			.getInstance();

	public SwipeView(Restaurant restaurant, ImageType imageType,
			RestaurantImageView parent) {

		loadedPageController = LoadedPageController.getInstance();
		this.parent = parent;
		this.restaurant = restaurant;
		this.imageType = imageType;
		fillData(imageType);

		wrapper.addStyleName("wrapper");
		
		setUpload();
		scrollerContainer.addStyleName("scroller");
		
		fillImages();
		wrapper.setWidget(scrollerContainer);
		add(wrapper);
		
		//setMyTitle(title);

		addStyleName("swipeView");
	}


	private void fillData(ImageType imageType) {
		switch (imageType) {
		case LOGO:
			imageBlobList = restaurant.getLogoImages();
			title = Customization.LOGO_PICTURE_TITLE;
			mainImageUrl = restaurant.getMainLogoImageString() != null ? restaurant
					.getMainLogoImageString() : "";
			break;
		case MENU:
			imageBlobList = restaurant.getMenuImages();
			title = Customization.MAENU_PICTURE_TITLE;
			mainImageUrl = restaurant.getMainMenuImageString() != null ? restaurant
					.getMainMenuImageString() : "";
			break;
		case PROFILE:
			imageBlobList = restaurant.getProfileImages();
			title = Customization.PROFILE_PICTURE_TITLE;
			mainImageUrl = restaurant.getMainProfileImageString() != null ? restaurant
					.getMainProfileImageString() : "";
			break;
		default:
			break;
		}
		if (imageBlobList == null) {
			this.imageBlobList = new ArrayList<ImageBlob>();
		}
		
		Collections.sort(this.imageBlobList, new MyComparator());

	}

	private void fillImages() {
		imagesController.clear(parent);
		int imageCounter = 30;
		for (ImageBlob imageBlob : imageBlobList) {
			addImage(imageBlob);
			imageCounter--;
		}
		if(imageBlobList.isEmpty()){
			loadedPageController.emptySwipe(getRestId());
		}
		for (; imageCounter >= 1;) {
			MyImage emptyImage = new MyImage(imageCounter, imageType);
			scrollerContainer.insert(emptyImage, 0);
			imageCounter--;
		}
		if (imageType == ImageType.MENU) {
			MyImage emptyBoard = new MyImage(imagesController, new Image(
					imagesController.getDefoultEmptyMenu().getImageUrl()),
					parent, imageType);
			scrollerContainer.insert(emptyBoard, 0);
		}
	//	wrapper.scrollToRight();
		
	}
	

	private void addImage(ImageBlob imageBlob) {
		MyImage newImage;
		loadedPageController.addImage(getRestId());
		
		newImage = new MyImage(imagesController, imageBlob, parent, imageType);

		newImage.image.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				//wrapper.scrollToRight();
				loadedPageController.removeImage(getRestId());
			}
		});
		newImage.image.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				loadedPageController.removeImage(getRestId());
			}
		});

		if (mainImageUrl.equals(imageBlob.getImageUrl())) {
			imagesController.selectImage(newImage);
//			scrollerContainer.insert(newImage, 0);
//			return;
		}
		scrollerContainer.add(newImage);
	}

	private void addEmptyCliche(int clicheNumber) {

	}

	private void setMyTitle(String title) {
//		titleDiv.addStyleName("titleDiv");
//		Element span = DOM.createSpan();
//		span.setInnerText(title);
//
//		titleDiv.getElement().insertFirst(span);
//		add(titleDiv);
	}

//	private void setAndroidUpload() {
//		formPanel.setVisible(true);
//		formPanel.addStyleName("androidForm");
//		cameraDiv.add(formPanel);
//
//	}


	private void setAppleUpload( boolean isOS6) {

		if(isOS6){
			setOtherUpload(true);
		}else{
			setOtherUpload(false);
			cameraImg.addClickHandler(new ClickHandler() {
	
				@Override
				public void onClick(ClickEvent event) {
					blobService.getBlobStoreUrl(getRestId(), getImageType(),
							new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {
									String callbackURL = R.HOST_URL + "picupCallback.html" ;
									Cookies.setCookie(R.IMAGE_TYPE_PICUP, imageType.name());
									Cookies.setCookie(R.LAST_PAGE_PICUP, restaurant.getId()+"");
									onUploadFormLoaded(fileUpload.getElement(), result, callbackURL, R.HOST_URL);
	
									clickOnInputFile(fileUpload.getElement());
	
								}
	
								@Override
								public void onFailure(Throwable caught) {
	
								}
							});
				}
	
			});
			
		}
	}

	private void setOtherUpload( boolean isVisable) {
		formPanel.setVisible(isVisable);
		formPanel.addStyleName("swipeViewForm");
		
		FlowPanel cameraDivRelative = new FlowPanel();
		cameraDivRelative.setStyleName("cameraDivRelative", true);
		cameraDivRelative.add(formPanel);
		
		cameraDivRelative.add(cameraImg);
		cameraDivRelative.addStyleName("cameraDiv");
		cameraDivRelative.getElement().setId(imageType.name()+restaurant.getId());
		
		
		cameraDiv.add(cameraDivRelative);
		
		FlowPanel cameraContainerDivRelative = new FlowPanel();
		cameraContainerDivRelative.setStyleName("cameraContainerDivRelative", true);
		cameraContainerDivRelative.add(cameraDiv);
		cameraContainerDiv.add(cameraContainerDivRelative);
		
	}

	private void setUpload() {

		formPanel = new MyUploadForm(fileUpload, imageType, restaurant.getId()
				+ "");
		formPanel.setVisible(false);
		
		if (osType.toLowerCase().indexOf("ipad") >= 0
		 || osType.toLowerCase().indexOf("iphone") >= 0) { // ipad or iphone
			setAppleUpload(osType.toLowerCase().indexOf("os 6")>=0);	
			
		} else{
			setOtherUpload(true);
		}

		
		fileUpload.setVisible(true);
		fileUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Document.get().getElementById("load").setClassName("loading");
				// clickOnInputFile(formPanel.getUploadButton().getElement());
				blobService.getBlobStoreUrl(getRestId(), getImageType(),
						new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {

								//result = "http://mymenumenu.appspot.com/fileUploadServer?token=a1b2c3";
								formPanel.setAction(result);
								
								formPanel.submit();
							}

							@Override
							public void onFailure(Throwable caught) {
								Document.get().getElementById("load")
										.setClassName("loaded");
								Window.alert("Problem with upload. Try again");

							}
						});
			}
		});

		cameraContainerDiv.addStyleName("cameraContainerDiv");
		add(cameraContainerDiv);
		//add(formPanel);

	}

	private static native void clickOnInputFile(Element elem) /*-{
		elem.click();

	}-*/;

	private static native void onUploadFormLoaded(Element fileUpload, String blobStoreUrl, String callbackURL, String cancelURL) /*-{
		window.name = "picup";
	
		var url = "fileupload2://new?postValues=&postFileParamName=multipart/form-data&shouldIncludeEXIFData=true&postURL="
				+ encodeURI(blobStoreUrl)
				+ "&callbackURL="
				+ encodeURI(callbackURL)
				+ "&returnServerResponse=false&isMultiselectForbidden=true&mediaTypesAllowed=image&cancelURL="
				+ encodeURI(cancelURL)
				+ "&returnStatus=false&minVersionRequired=2.1&callbackParamType=query";
	
		$wnd.Picup2.convertFileInput(fileUpload, {
			windowName : encodeURI('My Web App'),
			'purpose' : encodeURI(url)
		});
	
		window.open('', '_self', '');
		window.close();

	}-*/;

	public String getRestId() {
		return restaurant.getId() + "";
	}

	public ImageType getImageType() {
		return imageType;
	}

	public void checkChanges() {

		imagesController.clear(parent);
		scrollerContainer.clear();
		fillData(imageType);
		
		fillImages();
	}
	
	private native String getUserAgent()/*-{
		return navigator.userAgent;
	}-*/;
	
}

class MyComparator implements Comparator<ImageBlob> {
	public int compare(ImageBlob o1, ImageBlob o2) {
		if (o1.getTimeInMiliSec() > o2.getTimeInMiliSec()) {
			return -1;
		} else if (o1.getTimeInMiliSec() < o2.getTimeInMiliSec()) {
			return 1;
		}
		return 0;
	}

}
