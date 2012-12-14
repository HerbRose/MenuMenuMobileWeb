package com.veliasystems.menumenu.client.userInterface;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.LoadedPageController;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.ui.SwipeView;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

public class RestaurantImageView extends MyPage {

	private VerticalPanel scrollerDiv;

	private Restaurant restaurant;
	private boolean loaded = false;
	private List<SwipeView> swipeViews = new ArrayList<SwipeView>();
	private RestaurantController restaurantController = RestaurantController
			.getInstance();

	private MyButton cancelButton = new MyButton(Customization.CANCEL);;
	private MyButton saveButton = new MyButton(Customization.SAVE);
	private BackButton backButton;
	private MyButton editButton;
	private MyButton adminPanel;

	private Label nameLabel;
	private Label adressLabel;
	private Label phoneRestaurant;
	private Label websiteLabel;
	private Label bossLabel;
	private Label addBoardText;
	private Label warning = new Label();

	private Image logoImage;
	private Label nameLabelInfo;
	private Label addressLabelInfo;
	private Label publishText;

	private TextBox nameText = new TextBox();
	private TextBox adressText = new TextBox();
	private TextBox phoneRestaurantTextBox = new TextBox();
	private TextBox websiteTextBox = new TextBox();
	private TextBox bossTextBox = new TextBox();

	private FlowPanel container;

	private FlowPanel infoContainer = new FlowPanel();;
	private FlowPanel logoImageWrapper = new FlowPanel();;
	private FlowPanel infoTextWrapper = new FlowPanel();;
	private FlowPanel swipeContainer = new FlowPanel();;
	private FlowPanel publishWrapper = new FlowPanel();;
	private FlowPanel publishImageWrapper = new FlowPanel();;
	private FlowPanel publishLabelWrapper = new FlowPanel();;
	private FlowPanel editPanel = new FlowPanel();
	private FlowPanel adminPanelWrapper = new FlowPanel();
	private FlowPanel nameWrapper = new FlowPanel();
	private FlowPanel addressWrapper = new FlowPanel();
	private FlowPanel phoneWrapper = new FlowPanel();
	private FlowPanel wwwWrapper = new FlowPanel();
	private FlowPanel bossWrapper = new FlowPanel();
	private FlowPanel addBoardWrapper = new FlowPanel();
	
	private Image publishImage;
	private Image logoEditImage = new Image();
	
	private FocusPanel adminLabel;
	private FocusPanel addBoard;
	private FocusPanel publish;

	private FileUpload fileLogoUpload = new FileUpload();
	private FormPanel formLogoUpload = new FormPanel();

	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	private String osType = getUserAgent();

	public RestaurantImageView(Restaurant r, JQMPage back) {
		super(r.getName());

		this.restaurant = r;

		infoContainer = new FlowPanel();
		infoContainer.addStyleName("infoContainer");

		logoImageWrapper = new FlowPanel();
		logoImageWrapper.addStyleName("logoImageWrapper");

		infoTextWrapper = new FlowPanel();
		infoTextWrapper.addStyleName("infoTextWrapper");

		swipeContainer = new FlowPanel();
		swipeContainer.addStyleName("swipeContainer");

		editPanel = new FlowPanel();
		editPanel.addStyleName("editPanel");

		adminPanelWrapper = new FlowPanel();
		adminPanelWrapper.addStyleName("adminWrapper");

		adminPanel = new MyButton("");
		adminPanel.removeStyleName("borderButton");
		adminPanel.addStyleName("addButton adminButton");
		adminPanel.getElement().getStyle().setHeight(50, Unit.PX);
		adminPanel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PagesController.showWaitPanel();
				JQMContext.changePage(
						PagesController.getPage(Pages.PAGE_RESTAURANT_MANAGER),
						Transition.SLIDE);
			}
		});
		adminLabel = new FocusPanel();
		adminLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PagesController.showWaitPanel();
				JQMContext.changePage(
						PagesController.getPage(Pages.PAGE_RESTAURANT_MANAGER),
						Transition.SLIDE);
			}
		});
		adminLabel.addStyleName("adminLabel noFocus");

		adminLabel.add(new Label(Customization.ADD_USER));

		adminPanelWrapper = new FlowPanel();
		adminPanelWrapper.addStyleName("adminWrapper");

		FlowPanel adminButtonDiv = new FlowPanel();
		adminButtonDiv.addStyleName("adminButtonDiv");
		adminButtonDiv.add(adminPanel);

		FlowPanel adminLabelDiv = new FlowPanel();
		adminLabelDiv.addStyleName("adminLabelDiv");
		adminLabelDiv.add(adminLabel);

		adminPanelWrapper.add(adminButtonDiv);
		adminButtonDiv.add(adminLabelDiv);

		logoImage = new Image();
		if (restaurant.getMainLogoImageString() != null) {
			logoImage.setUrl(restaurant.getMainLogoImageString());
		} else {
			//logoImage.setUrl("img/layout/logo.png");

		}
		logoImage.addStyleName("logoImage");

		adminPanelWrapper.getElement().getStyle().setDisplay(Display.NONE);

		nameLabelInfo = new Label(restaurant.getName());
		addressLabelInfo = new Label(restaurant.getAddress());

		infoTextWrapper.add(nameLabelInfo);
		infoTextWrapper.add(addressLabelInfo);

		logoImageWrapper.add(logoImage);

		infoContainer.add(logoImageWrapper);
		infoContainer.add(infoTextWrapper);

		scrollerDiv = new VerticalPanel();
		scrollerDiv.setStyleName("mainScroller");

		getContentPanel().add(editPanel);
		getContentPanel().add(infoContainer);
		getContentPanel().add(swipeContainer);
		getContentPanel().add(adminPanelWrapper);
	}

	@Override
	protected void onPageShow() {

		if (!loaded) {
			SwipeView swipeView = new SwipeView(restaurant, ImageType.PROFILE,
					this);
			swipeViews.add(swipeView);
			addToContent(swipeView);

			swipeView = new SwipeView(restaurant, ImageType.MENU, this);
			swipeViews.add(swipeView);
			addToContent(swipeView);

			swipeContainer.add(scrollerDiv);

			publishWrapper = new FlowPanel();
			publishWrapper.addStyleName("publishWrapper");

			publishImageWrapper = new FlowPanel();
			publishImageWrapper.addStyleName("publishImage");

			publishLabelWrapper = new FlowPanel();
			publishLabelWrapper.addStyleName("publishLabelWrapper");
			
			publishText = new Label();
			publishImage = new Image();
			
			publish = new FocusPanel();
			publish.addStyleName("noFocus");
			
			
			changeIcons();
			
			publishImage.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					restaurant.setVisibleForApp(!restaurant.isVisibleForApp());
					restaurantController.saveRestaurant(restaurant, false);
					changeIcons();
				}
			});
			
			publish.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					restaurant.setVisibleForApp(!restaurant.isVisibleForApp());
					restaurantController.saveRestaurant(restaurant, false);
					changeIcons();
				}
			});
			
			
			
			publishImageWrapper.add(publishImage);

			publishLabelWrapper.add(publishText);
			publish.add(publishLabelWrapper);
			publishWrapper.add(publish);
			publishWrapper.add(publishImageWrapper);
			
			getContentPanel().add(publishWrapper);

			loaded = true;
		}

		if (editPanel != null) {
			editPanel.getElement().getStyle().setDisplay(Display.NONE);
		}

		// setting buttons

		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PagesController.showWaitPanel();
				JQMContext.changePage(new CityInfoScreen(restaurant.getCity()),
						Transition.SLIDE);
			}
		});

		editButton = new MyButton(Customization.EDIT);
		editButton.setStyleName("rightButton saveButton", true);
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setEditable();
				changeButtons();
			}
		});

		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(editButton);

		if (loaded)
			checkChanges();

		setValidVisibility();
		
		logoImage.setUrl(restaurant.getMainLogoImageString()==null?"":restaurant.getMainLogoImageString());
		//PagesController.hideWaitPanel(); made on imageController

	};
	
	private void changeIcons(){
		if(restaurant.isVisibleForApp()){
			publishText.setText(Customization.PROFILE_PUBLISHED);
			publishImage.removeStyleName("rotatedPlus");
			publishImage.setUrl("img/layout/confirme.png");
		}else{
			publishText.setText(Customization.PROFILE_UNPUBLISHED);
			publishImage.setUrl("img/layout/plus.png");
			publishImage.setStyleName("rotatedPlus", true);
		}
	}

	private void changeButtons() {
		cancelButton.setStyleName("saveButton leftButton", true);
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getContentPanel().remove(warning);
				setProperButtons();
				setValidVisibility();
				showCamera();
			}
		});

		saveButton.setStyleName("rightButton saveButton", true);
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validate()) {
					restaurant.setName(nameText.getText());
					restaurant.setAddress(adressText.getText());
					restaurant.setPhoneRestaurant(phoneRestaurantTextBox
							.getText());
					PagesController.showWaitPanel();
					RestaurantController.getInstance().saveRestaurant(
							restaurant, false);
					setProperButtons();
					setValidVisibility();
					showCamera();
					nameLabelInfo.setText(restaurant.getName());
					addressLabelInfo.setText(restaurant.getAddress());

				}
			}
		});

		getHeader().setLeftButton(cancelButton);
		getHeader().setRightButton(saveButton);
	}

	private void setProperButtons() {
		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(editButton);
	}

	private void setValidVisibility() {
		editPanel.getElement().getStyle().setDisplay(Display.NONE);
		adminPanelWrapper.getElement().getStyle().setDisplay(Display.NONE);
		infoContainer.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		publishWrapper.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		setValidDataStyle(null, nameWrapper);
		setValidDataStyle(null, addressWrapper);
		setValidDataStyle(null, phoneWrapper);
		setValidDataStyle(null, wwwWrapper);
		setValidDataStyle(null, bossWrapper);
	}

	private boolean validate() {
		warning.setStyleName("warning");
		warning.setText("");
		getContentPanel().add(warning);
		if (!nameText.getText().isEmpty() && !adressText.getText().isEmpty()) {
			if (restaurantExist()) {
				setValidDataStyle(false, nameWrapper);
				setValidDataStyle(false, addressWrapper);
				warning.setText(Customization.RESTAURANT_EXIST_ERROR);
				return false;
			} else {
				setValidDataStyle(true, nameWrapper);
				setValidDataStyle(true, addressWrapper);
			}
		}

		boolean isCorrect = true;

		if (nameText.getText().isEmpty() && adressText.getText().isEmpty()) {
			warning.setText(Customization.EMPTYBOTHDATA);
			setValidDataStyle(false, nameWrapper);
			setValidDataStyle(false, addressWrapper);
			isCorrect = false;
		} else {
			if (nameText.getText().isEmpty()) {
				warning.setText(Customization.EMPTYNAME);
				setValidDataStyle(false, nameWrapper);
				isCorrect = false;
			} else {
				setValidDataStyle(true, nameWrapper);
			}
			if (adressText.getText().isEmpty()) {
				warning.setText(warning.getText() + " \n"
						+ Customization.EMPTYADRESS);
				setValidDataStyle(false, addressWrapper);
				isCorrect = false;
			} else {
				setValidDataStyle(true, addressWrapper);
			}
		}
		if (!phoneRestaurantTextBox.getText().isEmpty()) {
			if (!Util.isValidPhoneNumber(phoneRestaurantTextBox.getText())) {
				setValidDataStyle(false, phoneWrapper);
				isCorrect = false;
			} else {
				setValidDataStyle(true, phoneWrapper);
			}
		} else {
			setValidDataStyle(null, phoneWrapper);
		}
		return isCorrect;
	}

	private void setValidDataStyle(Boolean isCorrect, Widget widget) {
		if (widget == null)
			return;

		String correct = "greenShadow";
		String unCorrect = "redShadow";

		if (isCorrect == null) {
			widget.setStyleName(correct, false);
			widget.setStyleName(unCorrect, false);
			return;
		}
		widget.setStyleName(correct, isCorrect);
		widget.setStyleName(unCorrect, !isCorrect);
	}

	private boolean restaurantExist() {

		String cityName = restaurant.getCity();
		List<Restaurant> restaurants = restaurantController
				.getRestaurantsInCity(cityName);

		for (Restaurant restaurant : restaurants) {
			String restaurationName = restaurant.getName().replaceAll(" ", "");
			String restaurantAddress = restaurant.getAddress().replaceAll(" ",
					"");

			if (restaurationName.equalsIgnoreCase(nameText.getText()
					.replaceAll(" ", ""))
					&& restaurantAddress.equalsIgnoreCase(adressText.getText()
							.replace(" ", ""))) {
				if (nameText
						.getText()
						.replaceAll(" ", "")
						.equalsIgnoreCase(
								this.restaurant.getName().replace(" ", ""))
						&& adressText
								.getText()
								.replace(" ", "")
								.equalsIgnoreCase(
										this.restaurant.getAddress().replace(
												" ", ""))) {
					return false;
				} else {
					return true;
				}
			}
		}

		return false;
	}

	private void setEditable() {

		if (container == null) {
			container = new FlowPanel();
			container.addStyleName("containerPanelAddRestaurant");

			nameLabel = new Label();
			nameLabel.addStyleName("addRestaurantLabel myLabel arialBold ");
			nameLabel.setText(Customization.RESTAURANTNAME + ":");

			nameText.setTitle(Customization.RESTAURANTNAME);
			nameText.addStyleName("myTextBox nameBox arialBold");//addRestaurantInput
			

			adressLabel = new Label();
			adressLabel.setText(Customization.RESTAURANTADRESS + ":");
			adressLabel.addStyleName("addRestaurantLabel myLabel arialBold");

			adressText.setTitle(Customization.RESTAURANTADRESS);
			adressText.addStyleName("myTextBox nameBox arialBold");

			phoneRestaurant = new Label();
			phoneRestaurant.setText(Customization.RESTAURANT_PHONE + ":");
			phoneRestaurant.addStyleName("addRestaurantLabel myLabel arialBold");

			phoneRestaurantTextBox.setTitle(Customization.RESTAURANT_PHONE);
			phoneRestaurantTextBox.addStyleName("myTextBox nameBox arialBold");

			websiteLabel = new Label(Customization.WEBSITE_LABEL);
			websiteLabel.addStyleName("addRestaurantLabel myLabel arialBold");

			websiteTextBox.addStyleName("myTextBox nameBox arialBold");

			bossLabel = new Label(Customization.BOSS_LABEL);
			bossLabel.addStyleName("addRestaurantLabel myLabel arialBold");

			bossTextBox.addStyleName("myTextBox nameBox arialBold");

			
			nameWrapper.addStyleName("namePanel addWrapper");
			
			addressWrapper.addStyleName("namePanel addWrapper");
			
			phoneWrapper.addStyleName("namePanel addWrapper");
			
			wwwWrapper.addStyleName("namePanel addWrapper");
			
			bossWrapper.addStyleName("namePanel addWrapper");

			
			nameWrapper.add(nameLabel);
			nameWrapper.add(nameText);

			addressWrapper.add(adressLabel);
			addressWrapper.add(adressText);
	
			phoneWrapper.add(phoneRestaurant);
			phoneWrapper.add(phoneRestaurantTextBox);

			wwwWrapper.add(websiteLabel);
			wwwWrapper.add(websiteTextBox);

			bossWrapper.add(bossLabel);
			bossWrapper.add(bossTextBox);

			container.add(nameWrapper);
			container.add(addressWrapper);
			container.add(phoneWrapper);
			container.add(wwwWrapper);
			container.add(bossWrapper);

			FlowPanel addBoardWrap = new FlowPanel();
			addBoardWrap.addStyleName("addBoardWrap");
			
			addBoard = new FocusPanel();
			addBoard.addStyleName("addBoardWrapper noFocus");
			
			addBoardText = new Label(Customization.ADD_BOARD);
			addBoardText.addStyleName("addBoardLabel");
							
			
			// setting logo upload

			// formLogoUpload.setVisible(false);
			formLogoUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
			formLogoUpload.setMethod(FormPanel.METHOD_POST);
			formLogoUpload.addStyleName("formLogoUpload");

			fileLogoUpload.addStyleName("fileLogoUplaod");
			fileLogoUpload.setName("image");
			//VerticalPanel mainPanel = new VerticalPanel();
			//mainPanel.add(fileLogoUpload);
			formLogoUpload.add(fileLogoUpload);

			formLogoUpload
					.addSubmitCompleteHandler(new SubmitCompleteHandler() {

						@Override
						public void onSubmitComplete(SubmitCompleteEvent event) {
							PagesController.showWaitPanel();
							
							restaurantController.cropImage(restaurant.getId(),
									ImageType.LOGO);
							formLogoUpload.reset();
						}
					});

			if (osType.toLowerCase().indexOf("ipad") >= 0
					|| osType.toLowerCase().indexOf("iphone") >= 0) { // ipad or
																	// iphone
				setAppleUpload(osType.toLowerCase().indexOf("os 6") >= 0);

				// } else if (osType.toLowerCase().indexOf("armv") >= 0
				// || osType.toLowerCase().indexOf("android")>=0) { // android
				// setAndroidUpload();
			} 
//			else {
//				setOtherUpload();
//			}

			fileLogoUpload.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					PagesController.showWaitPanel();
					blobService.getBlobStoreUrl(restaurant.getId() + "",
							ImageType.LOGO, new AsyncCallback<String>() {

								@Override
								public void onSuccess(String result) {

									// result =
									// "http://mymenumenu.appspot.com/fileUploadServer?token=a1b2c3";
									formLogoUpload.setAction(result);

									formLogoUpload.submit();
								}

								@Override
								public void onFailure(Throwable caught) {
									PagesController.hideWaitPanel();
									Window.alert("Problem with upload. Try again");

								}
							});
				}
			});

			
			addBoardWrap.add(addBoardText);
			addBoardWrap.add(formLogoUpload);
			addBoard.add(addBoardWrap);
			
			
			if(restaurant.getMainLogoImageString() != null){
				addBoard.setHeight("50px"); //takie samo co w css dla logo image
				addBoard.setWidth("220px");
				addBoardWrap.remove(addBoardText);
				
				logoEditImage.addStyleName("logoEditImage");
				FlowPanel edit = new FlowPanel();
				
				edit.addStyleName("editImagePanel");
				Label editLabel = new Label(Customization.EDIT.toUpperCase());
				editLabel.addStyleName("editLabelImage");
				
				edit.add(editLabel);
				
				logoEditImage.setUrl(restaurant.getMainLogoImageString());
				addBoardWrap.add(logoEditImage);
				addBoardWrap.add(edit);
			}

			setData();
			editPanel.add(container);
			editPanel.add(addBoard);
		}

		hideCamera();
		
		if(restaurant.getMainLogoImageString() != null){
			logoEditImage.setUrl(restaurant.getMainLogoImageString());
		}
		
		
		nameText.setText(restaurant.getName());
		adressText.setText(restaurant.getAddress());
		phoneRestaurantTextBox.setText(restaurant.getPhoneRestaurant());
		websiteTextBox.setText("");
		bossTextBox.setText("");
		
		adminPanelWrapper.getElement().getStyle()
				.setDisplay(Display.BLOCK);
		editPanel.getElement().getStyle().setDisplay(Display.BLOCK);
		infoContainer.getElement().getStyle().setDisplay(Display.NONE);
		publishWrapper.getElement().getStyle().setDisplay(Display.NONE);

	}
	
	private void hideCamera(){
		Document.get().getElementById(ImageType.PROFILE.name()).addClassName("hidden");
		Document.get().getElementById(ImageType.MENU.name()).addClassName("hidden");
	}
	
	private void showCamera(){
		Document.get().getElementById(ImageType.PROFILE.name()).removeClassName("hidden");
		Document.get().getElementById(ImageType.MENU.name()).removeClassName("hidden");
	}

	private void setData() {
		nameText.setText(restaurant.getName());
		adressText.setText(restaurant.getAddress());
		phoneRestaurantTextBox.setText(restaurant.getPhoneRestaurant());

		// TODO
		// uzupelnianie o nowe pola: www, imie szefa
	}

	public void checkChanges() {
		LoadedPageController.getInstance().clear(restaurant.getId());
		for (SwipeView swipeView : swipeViews) {
			swipeView.checkChanges();
		}
	}

	private JQMPage getMe() {
		return this;
	}

	public void addToContent(Widget widget) {
		scrollerDiv.add(widget);
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	private void setAppleUpload(boolean isOS6) {

		if (!isOS6) {
			// setCameraImg();
			formLogoUpload.setStyleName("hidden", true);
			fileLogoUpload.setStyleName("hidden", true);
			formLogoUpload.getElement().getStyle().setPosition(Position.ABSOLUTE);
			formLogoUpload.getElement().getStyle().setTop(10000, Unit.PX); // aby nie kliknąć na niego ;/
			addBoard.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					blobService.getBlobStoreUrl(restaurant.getId() + "",
							ImageType.LOGO, new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {
									
									String callbackURL = R.HOST_URL + "picupCallback.html" ;
									Cookies.setCookie(R.IMAGE_TYPE_PICUP, ImageType.LOGO.name() );
									Cookies.setCookie(R.LAST_PAGE_PICUP, restaurant.getId()+"");
									onUploadFormLoaded(fileLogoUpload.getElement(), result, callbackURL, R.HOST_URL);

									clickOnInputFile(fileLogoUpload.getElement());

								}

								@Override
								public void onFailure(Throwable caught) {

								}
							});
				}

			});

		}
	}

//	private void setOtherUpload() {
//
//		addBoard.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				clickOnInputFile(fileLogoUpload.getElement());
//			}
//
//		});
//	}

	private native String getUserAgent()/*-{
		return navigator.userAgent;
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

	private static native void clickOnInputFile(Element elem) /*-{
		elem.click();
	}-*/;

}
