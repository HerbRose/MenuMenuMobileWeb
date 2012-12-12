package com.veliasystems.menumenu.client.userInterface;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
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

	private FlowPanel infoContainer;
	private FlowPanel logoImageWrapper;
	private FlowPanel infoTextWrapper;
	private FlowPanel swipeContainer;
	private FlowPanel publishWrapper;
	private FlowPanel publishImage;
	private FlowPanel publishLabelWrapper;
	private FlowPanel editPanel;
	private FlowPanel adminPanelWrapper;

	private FocusPanel adminLabel;
	private FocusPanel addBoard;

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

			publishImage = new FlowPanel();
			publishImage.addStyleName("publishImage");

			publishLabelWrapper = new FlowPanel();
			publishLabelWrapper.addStyleName("publishLabelWrapper");

			publishText = new Label(Customization.PROFILE_PUBLISHED);

			Image img = new Image("img/layout/plus.png");
			publishImage.add(img);

			publishLabelWrapper.add(publishText);

			publishWrapper.add(publishImage);
			publishWrapper.add(publishLabelWrapper);

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

		PagesController.hideWaitPanel();

	};

	private void changeButtons() {
		cancelButton.setStyleName("saveButton leftButton", true);
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getContentPanel().remove(warning);
				setProperButtons();
				setValidVisibility();
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
							restaurant);
					setProperButtons();
					setValidVisibility();

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
	}

	private boolean validate() {
		warning.setStyleName("warning");
		warning.setText("");
		getContentPanel().add(warning);
		if (!nameText.getText().isEmpty() && !adressText.getText().isEmpty()) {
			if (restaurantExist()) {
				setValidDataStyle(false, nameText);
				setValidDataStyle(false, adressText);
				warning.setText(Customization.RESTAURANT_EXIST_ERROR);
				return false;
			} else {
				setValidDataStyle(true, nameText);
				setValidDataStyle(true, adressText);
			}
		}

		boolean isCorrect = true;

		if (nameText.getText().isEmpty() && adressText.getText().isEmpty()) {
			warning.setText(Customization.EMPTYBOTHDATA);
			setValidDataStyle(false, nameText);
			setValidDataStyle(false, adressText);
			isCorrect = false;
		} else {
			if (nameText.getText().isEmpty()) {
				warning.setText(Customization.EMPTYNAME);
				setValidDataStyle(false, nameText);
				isCorrect = false;
			} else {
				setValidDataStyle(true, nameText);
			}
			if (adressText.getText().isEmpty()) {
				warning.setText(warning.getText() + " \n"
						+ Customization.EMPTYADRESS);
				setValidDataStyle(false, adressText);
				isCorrect = false;
			} else {
				setValidDataStyle(true, adressText);
			}
		}
		if (!phoneRestaurantTextBox.getText().isEmpty()) {
			if (!Util.isValidPhoneNumber(phoneRestaurantTextBox.getText())) {
				setValidDataStyle(false, phoneRestaurantTextBox);
				isCorrect = false;
			} else {
				setValidDataStyle(true, phoneRestaurantTextBox);
			}
		} else {
			setValidDataStyle(null, phoneRestaurantTextBox);
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
			nameLabel.addStyleName("addRestaurantLabel");
			nameLabel.setText(Customization.RESTAURANTNAME + ":");

			nameText.setTitle(Customization.RESTAURANTNAME);
			nameText.addStyleName("addRestaurantInput");

			adressLabel = new Label();
			adressLabel.setText(Customization.RESTAURANTADRESS + ":");
			adressLabel.addStyleName("addRestaurantLabel");

			adressText.setTitle(Customization.RESTAURANTADRESS);
			adressText.addStyleName("addRestaurantInput");

			phoneRestaurant = new Label();
			phoneRestaurant.setText(Customization.RESTAURANT_PHONE + ":");
			phoneRestaurant.addStyleName("addRestaurantLabel");

			phoneRestaurantTextBox.setTitle(Customization.RESTAURANT_PHONE);
			phoneRestaurantTextBox.addStyleName("addRestaurantInput");

			websiteLabel = new Label(Customization.WEBSITE_LABEL);
			websiteLabel.addStyleName("addRestaurantLabel");

			websiteTextBox.addStyleName("addRestaurantInput");

			bossLabel = new Label(Customization.BOSS_LABEL);
			bossLabel.addStyleName("addRestaurantLabel");

			bossTextBox.addStyleName("addRestaurantInput");

			FlowPanel nameWrapper = new FlowPanel();
			nameWrapper.addStyleName("addWrapper");

			FlowPanel addressWrapper = new FlowPanel();
			addressWrapper.addStyleName("addWrapper");

			FlowPanel phoneWrapper = new FlowPanel();
			phoneWrapper.addStyleName("addWrapper");

			FlowPanel wwwWrapper = new FlowPanel();
			wwwWrapper.addStyleName("addWrapper");

			FlowPanel bossWrapper = new FlowPanel();
			bossWrapper.addStyleName("addWrapper");

			FlowPanel nameDiv = new FlowPanel();
			nameDiv.addStyleName("addRestaurantLabelWrapper");

			FlowPanel nameInput = new FlowPanel();
			nameInput.addStyleName("addRestaurantInputWrapper");

			FlowPanel addressDiv = new FlowPanel();
			addressDiv.addStyleName("addRestaurantLabelWrapper");

			FlowPanel addressInput = new FlowPanel();
			addressInput.addStyleName("addRestaurantInputWrapper");

			FlowPanel phoneDiv = new FlowPanel();
			phoneDiv.addStyleName("addRestaurantLabelWrapper");

			FlowPanel phoneInput = new FlowPanel();
			phoneInput.addStyleName("addRestaurantInputWrapper");

			FlowPanel wwwDiv = new FlowPanel();
			wwwDiv.addStyleName("addRestaurantLabelWrapper");

			FlowPanel wwwInput = new FlowPanel();
			wwwInput.addStyleName("addRestaurantInputWrapper");

			FlowPanel bossDiv = new FlowPanel();
			bossDiv.addStyleName("addRestaurantLabelWrapper");

			FlowPanel bossInput = new FlowPanel();
			bossInput.addStyleName("addRestaurantInputWrapper");

			nameDiv.add(nameLabel);
			nameInput.add(nameText);
			nameWrapper.add(nameDiv);
			nameWrapper.add(nameInput);

			addressDiv.add(adressLabel);
			addressInput.add(adressText);
			addressWrapper.add(addressDiv);
			addressWrapper.add(addressInput);

			phoneDiv.add(phoneRestaurant);
			phoneInput.add(phoneRestaurantTextBox);
			phoneWrapper.add(phoneDiv);
			phoneWrapper.add(phoneInput);

			wwwDiv.add(websiteLabel);
			wwwInput.add(websiteTextBox);
			wwwWrapper.add(wwwDiv);
			wwwWrapper.add(wwwInput);

			bossDiv.add(bossLabel);
			bossInput.add(bossTextBox);
			bossWrapper.add(bossDiv);
			bossWrapper.add(bossInput);

			container.add(nameWrapper);
			container.add(addressWrapper);
			container.add(phoneWrapper);
			container.add(wwwWrapper);
			container.add(bossWrapper);

			
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
					|| osType.toLowerCase().indexOf("ipho") >= 0) { // ipad or
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

			

			

			// addBoard.add(addBoardText);

			addBoard.add(formLogoUpload);
			// addBoard.addClickHandler(new ClickHandler() {
			//
			// @Override
			// public void onClick(ClickEvent event) {
			//
			// }
			// });

			setData();
			editPanel.add(container);
			editPanel.add(addBoard);
		}

		adminPanelWrapper.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		editPanel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		infoContainer.getElement().getStyle().setDisplay(Display.NONE);
		publishWrapper.getElement().getStyle().setDisplay(Display.NONE);

	}

	private void setData() {
		nameText.setText(restaurant.getName());
		adressText.setText(restaurant.getAddress());
		phoneRestaurantTextBox.setText(restaurant.getPhoneRestaurant());

		// TO DO
		// uzupelnianie o nowe pola: www, imie szefa
	}

	public void checkChanges() {
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

		if (isOS6) {
			//setOtherUpload();
		} else {
			// setCameraImg();
			formLogoUpload.setVisible(false);
			
			addBoard.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					blobService.getBlobStoreUrl(restaurant.getId() + "",
							ImageType.LOGO, new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {
									String callbackURL = R.HOST_URL;

									onUploadFormLoaded(restaurant.getName()
											+ "_" + ImageType.LOGO,
											fileLogoUpload.getElement(),
											result, callbackURL);

									Cookies.setCookie(R.IMAGE_TYPE,
											ImageType.LOGO.name());

									clickOnInputFile(fileLogoUpload
											.getElement());

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

	private static native void onUploadFormLoaded(String windowName,
			Element fileUpload, String blobStoreUrl, String callbackURL) /*-{
		window.name = windowName;

		var url = "fileupload2://new?postValues=&postFileParamName=multipart/form-data&shouldIncludeEXIFData=true&postURL="
				+ encodeURI(blobStoreUrl)
				+ "&callbackURL="
				+ encodeURI(callbackURL)
				+ "&returnServerResponse=false&isMultiselectForbidden=true&mediaTypesAllowed=image&cancelURL="
				+ encodeURI(callbackURL)
				+ "&returnStatus=false&minVersionRequired=2.1";

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
