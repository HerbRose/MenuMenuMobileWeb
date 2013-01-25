package com.veliasystems.menumenu.client.userInterface;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.CookieController;
import com.veliasystems.menumenu.client.controllers.CookieNames;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyUploadForm;

public class AddRestaurantScreen extends MyPage implements IObserver {

	private BackButton backButton;
	private MyButton saveButton;

	private Label nameLabel;
	private Label cityLabel;
	private Label adressLabel;
	private Label mailRestaurant;
	private Label phoneRestaurantLabel;
	private Label mailUser;
	private Label phoneUser;
	private Label nameUser;
	private Label websiteLabel;
	private Label bossLabel;
	private Label districtLabel;
	
	private TextBox nameText = new TextBox();
	private TextBox adressText = new TextBox();
	private TextBox mailRestaurantTextBox = new TextBox();
	private TextBox phoneRestaurantTextBox = new TextBox();
	private TextBox mailUserTextBox = new TextBox();
	private TextBox phoneUserTextBox = new TextBox();
	private TextBox nameUserTextBox = new TextBox();
//	private TextBox websiteTextBox = new TextBox();
	private TextBox bossTextBox = new TextBox();
	
	private MyListCombo citiesListCombo = new MyListCombo(false);

	// private ListBox cityListBox = new ListBox();
	private Label warning = new Label();

	private City city;
	private Restaurant restaurant = new Restaurant();;

	private boolean isToCity;
	private boolean loaded = false;
	/**
	 * when is false, this page will not be removed in onPageHide() method.
	 * Needed when user adding board picture to not lose data written by user.
	 */
	private boolean isToDeleted = true;

	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController
			.getInstance();
	private UserController userController = UserController.getInstance();
	private CookieController cookieController = CookieController.getInstance();

	private MyRestaurantInfoPanel container;

	private FlowPanel nameWrapper = new FlowPanel();
	private FlowPanel addressWrapper = new FlowPanel();
	private FlowPanel phoneWrapper = new FlowPanel();
	private FlowPanel wwwWrapper = new FlowPanel();
	private FlowPanel bossWrapper = new FlowPanel();
	private FlowPanel addBordWrapper = new FlowPanel();
	private FlowPanel addFlowPanel = new FlowPanel();
	private FlowPanel addBoardPanel = new FlowPanel();

	// private FlowPanel addUser;
	private FlowPanel addUserMainPanel;
	private MyButton addUserImage;
	private TextBox addUserTextBox;
	private FlowPanel addUserTextBoxDiv;

	private FocusPanel addBoard = new FocusPanel();;
	private FocusPanel cancelDeleteBoard = new FocusPanel();
	private FocusPanel removeBoard = new FocusPanel();
	private FocusPanel addBoardFromBottom = new FocusPanel();

	private Label addBoardText;

	private FileUpload fileLogoUpload = new FileUpload();
	private MyUploadForm formLogoUpload;
	private String osType = R.USER_AGENT;

	private Image image = new Image();

	private JQMPage pageToDelete = null;

	private String logoBlobString = "";

	private final BlobServiceAsync blobService = GWT.create(BlobService.class);

	private void init(boolean isToCity) {

		this.isToCity = isToCity;

		setButtons();

		addBoardPanel.setStyleName("deletePanel");

		cityLabel = new Label();
		cityLabel.setText(Customization.CITYONE + ":");

		if (!isToCity) {
			addCities(cityController.getCitiesList());
		} else {
			// cityListBox.addItem(city);
		}
		// getContentPanel().add(cityListBox);

		cancelDeleteBoard.addStyleName("cancelDeleteProfile noFocus pointer");
		addBoardFromBottom
				.addStyleName("deletePanelWhiteBackground noFocus pointer");
		removeBoard.setStyleName("deletePanelWhiteBackground");

		addFlowPanel.setStyleName("addFlowPanel");
		addFlowPanel.add(new Label(Customization.ADD_BOARD));
		addBoardFromBottom.add(addFlowPanel);

		removeBoard.add(new Label(Customization.REMOVE_BOARD));

		cancelDeleteBoard.add(new Label(Customization.CANCEL));

		cancelDeleteBoard.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isToDeleted = true;
				hideAddBoardPanel();
			}
		});

		removeBoard.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PagesController.showWaitPanel();
				blobService.removeImageBlobByBlobKey(logoBlobString,
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {

								PagesController.hideWaitPanel();
//								Window.alert(Customization.CONNECTION_ERROR);
								PagesController.MY_POP_UP.showError(new Label(Customization.CONNECTION_ERROR), new IMyAnswer() {
									
									@Override
									public void answer(Boolean answer) {
										
									}
								});
								// logoImage.setUrl("");
								// addBoardWrap.remove(logoEditImage);
							}

							@Override
							public void onSuccess(Void result) {
								// restaurant.setMainLogoImageString("");
								// restaurantController.saveRestaurant(restaurant,
								// false);
								hideAddBoardPanel();
								addBordWrapper.clear();
								addBordWrapper.getElement().getStyle()
										.clearWidth();
								addBordWrapper.add(addBoardText);
								restaurant.setMainLogoImageString("");
								PagesController.hideWaitPanel();
							}
						});

			}
		});

		addBoardPanel.add(addBoardFromBottom);
		addBoardPanel.add(removeBoard);
		addBoardPanel.add(cancelDeleteBoard);

		setUpload();
		setLabels();

	}

	private String parseURLtoBlobKey(String url) {
		String tab[] = url.split("/?");
		String tab2[] = url.split("=");
		return tab2[1];
	}

	private void setLabels() {

		container = new MyRestaurantInfoPanel();
		
		container.addStyleName("containerPanelAddRestaurant");

		
		nameLabel = new Label(Customization.RESTAURANTNAME + ":");
		adressLabel = new Label(Customization.RESTAURANTADRESS + ":");
		phoneRestaurantLabel = new Label(Customization.RESTAURANT_PHONE + ":");
		districtLabel = new Label(Customization.DISTRICT + ":");
		bossLabel = new Label(Customization.BOSS_LABEL + ":");

		nameText.setTitle(Customization.RESTAURANTNAME);
		adressText.setTitle(Customization.RESTAURANTADRESS);
		phoneRestaurantTextBox.setTitle(Customization.RESTAURANT_PHONE);
		bossTextBox.setTitle(Customization.BOSS_LABEL);
		
		nameText.addStyleName("myTextBox nameBox");//addRestaurantInput
		adressText.addStyleName("myTextBox nameBox");
		phoneRestaurantTextBox.addStyleName("myTextBox nameBox");
		bossTextBox.addStyleName("myTextBox nameBox");
		
		for (City city : cityController.getCitiesList()) {
			citiesListCombo.addListItem(citiesListCombo.getNewItem(city.getCity()), city.getId());
		}
		
		citiesListCombo.selectItem(city.getId());
		
		container.addItem(nameLabel, nameText);
		container.addItem(adressLabel, adressText);
		container.addItem(phoneRestaurantLabel, phoneRestaurantTextBox);
		container.addItem(districtLabel, citiesListCombo);
		container.addItem(bossLabel, bossTextBox);

		addAddUserWidget();

		addBoardText = new Label(Customization.ADD_BOARD);
		addBoardText.addStyleName("addBoardLabel");

		addBordWrapper.addStyleName("addBoardWrapper");
		addBordWrapper.add(addBoardText);
		// addBoard.add(addBoardText);
		addBoard.add(addBordWrapper);

		addBoard.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isToDeleted = false;
				showAddBoardPanel();
			}
		});

		getContentPanel().add(container);
		getContentPanel().add(addBoard);
		getContentPanel().add(addUserMainPanel);
		getContentPanel().add(addBoardPanel);
	}

	private void showAddBoardPanel() {
		addBoardPanel.getElement().getStyle().setHeight(225.0, Unit.PX);
		addBoardPanel.getElement().getStyle().setBottom(0, Unit.PX);
	}

	private void hideAddBoardPanel() {
		addBoardPanel.getElement().getStyle().setHeight(0.0, Unit.PX);
		addBoardPanel.getElement().getStyle().setBottom(0, Unit.PX);

	}

	public void addAddUserWidget() {

		addUserMainPanel = new FlowPanel();
		addUserMainPanel.addStyleName("plusPanel");

		addUserImage = new MyButton("");
		addUserImage.removeStyleName("borderButton");
		addUserImage.addStyleName("addButton adminButton");
		addUserImage.getElement().getStyle().setHeight(50, Unit.PX);

		addUserTextBoxDiv = new FlowPanel();
		addUserTextBoxDiv.addStyleName("plusPanelRightDiv adminLabel noFocus");

		addUserTextBox = new TextBox();
		addUserTextBox.addStyleName("myTextBox noFocus");
		setPlaceHolder(addUserTextBox, Customization.ADD_USER);

		addUserTextBoxDiv.add(addUserTextBox);
		addUserMainPanel.add(addUserImage);
		addUserMainPanel.add(addUserTextBoxDiv);
	}

	public AddRestaurantScreen(City city) {
		super(city.getCity());
		pageToDelete = this;
		cityController.addObserver(this);
		this.city = city;
		init(true);
	}

	public AddRestaurantScreen() {
		super(Customization.ADDRESTAURANT);

		cityController.addObserver(this);
		long cityId = Long.parseLong(cookieController
				.getCookie(CookieNames.R_CITY_ID));

		city = cityController.getCity(cityId);
		if (city == null) {
//			Window.alert(Customization.ERROR);
			PagesController.MY_POP_UP.showError(new Label(Customization.ERROR), new IMyAnswer() {
				
				@Override
				public void answer(Boolean answer) {
				
				}
			});
			return;
		}
		init(true);

	}

	@Override
	protected void onPageShow() {
		hideAddBoardPanel();
		RestaurantController.getInstance().setLastOpenPage(this);
		container.setWidth( JS.getElementOffsetWidth(getElement())-20 );
		isToDeleted = true;
		clearData();

		if (!ImagesController.imageUrl.isEmpty()) {
			restaurant.setMainLogoImageString(ImagesController.imageUrl);
			image.setUrl(ImagesController.imageUrl);

			addBordWrapper.clear();
			addBordWrapper.getElement().getStyle().setWidth(220d, Unit.PX);
			addBordWrapper.add(image);
			ImagesController.imageUrl = "";
		}

		PagesController.hideWaitPanel();
	}

	@Override
	protected void onPageHide() {
		super.onPageHide();
		if (isToDeleted) {
			setCookiesData(true);

			getElement().removeFromParent();
		}
	}

	private void clearData() {
		if (cookieController.getCookie(CookieNames.IS_PICUP_USED).equals("true")) {
			nameText.setText(cookieController.getCookie(CookieNames.R_NAME));
			adressText.setText(cookieController.getCookie(CookieNames.R_ADDRESS));
			bossTextBox.setText(cookieController.getCookie(CookieNames.R_BOSS_NAME));
			phoneRestaurantTextBox.setText(cookieController.getCookie(CookieNames.R_PHONE));
			addUserTextBox.setText(cookieController.getCookie(CookieNames.R_USERS));
			long selectedCityId;
			try {
				selectedCityId = Long.parseLong(cookieController.getCookie(CookieNames.R_CITY_ID));
				citiesListCombo.selectItem(selectedCityId);
			} catch (Exception e) {
				citiesListCombo.selectItem(city.getId());
			}
			
		}

		warning.setText("");

		setValidDataStyle(null, nameText);
		setValidDataStyle(null, adressText);
		setValidDataStyle(null, mailRestaurantTextBox);
		setValidDataStyle(null, mailUserTextBox);
		setValidDataStyle(null, mailUserTextBox);
		setValidDataStyle(null, phoneRestaurantTextBox);
		setValidDataStyle(null, phoneUserTextBox);
	}

	private void setButtons() {
		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				PagesController.showWaitPanel();
				JQMContext.changePage(new CityInfoScreen(city),
						Transition.SLIDE);

			}
		});

		saveButton = new MyButton(Customization.SAVE);
		saveButton.setStyleName("rightButton saveButton", true);
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (validate()) {

					restaurant.setName(nameText.getText());

					restaurant.setAddress(adressText.getText());
					restaurant.setCityId(city.getId());
					restaurant.setPhoneRestaurant(phoneRestaurantTextBox
							.getText());
					restaurant.setNameUser(bossTextBox.getText());
					List<String> usersEmailToAdd = new ArrayList<String>();
					if (!addUserTextBox.getText().isEmpty()) { // validation of
																// email is
																// checked in
																// validate()
																// method
						usersEmailToAdd.add(addUserTextBox.getText());
					}

					restaurantController.addNewRestaurant(restaurant,
							usersEmailToAdd);
				}
			}
		});

		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(saveButton);

	}

	private boolean validate() {
		warning.setStyleName("warning");
		warning.setText("");
		add(warning);
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
		} else if (isRestaurant(nameText.getText(), adressText.getText(),
				city.getId())) {
			warning.setText(Customization.RESTAURANT_EXIST_ERROR);
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
		if (!mailRestaurantTextBox.getText().isEmpty()) {
			if (!Util.isValidEmail(mailRestaurantTextBox.getText())) {
				setValidDataStyle(false, mailRestaurantTextBox);
				isCorrect = false;
			} else {
				setValidDataStyle(true, mailRestaurantTextBox);
			}
		} else {
			setValidDataStyle(null, mailRestaurantTextBox);
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
		if (!mailUserTextBox.getText().isEmpty()) {
			if (!Util.isValidEmail(mailUserTextBox.getText())) {
				setValidDataStyle(false, mailUserTextBox);
				isCorrect = false;
			} else {
				setValidDataStyle(true, mailUserTextBox);
			}
		} else {
			setValidDataStyle(null, mailUserTextBox);
		}
		if (!phoneUserTextBox.getText().isEmpty()) {
			if (!Util.isValidPhoneNumber(phoneUserTextBox.getText())) {
				setValidDataStyle(false, phoneUserTextBox);
				isCorrect = false;
			} else {
				setValidDataStyle(true, phoneUserTextBox);
			}
		} else {
			setValidDataStyle(null, phoneUserTextBox);
		}

		if (!addUserTextBox.getText().isEmpty()) {
			if (!Util.isValidEmail(addUserTextBox.getText())) {
				isCorrect = false;
				setValidDataStyle(false, addUserTextBoxDiv);
				warning.setText(Customization.WRONG_EMAIL_ADDRESS);
			} else {
				setValidDataStyle(true, addUserTextBoxDiv);
			}
		}
		return isCorrect;
	}

	private boolean isRestaurant(String restaurantName,
			String restaurantAddress, Long resturantCityId) {

		List<Restaurant> restaurants = restaurantController
				.getRestaurantsInCity(resturantCityId);

		for (Restaurant restaurant : restaurants) {
			if (restaurant.getName().equalsIgnoreCase(restaurantName)
					&& restaurant.getAddress().equalsIgnoreCase(
							restaurantAddress)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * sets the right shadow around the widget
	 * 
	 * @param isCorrect
	 *            - if <b>true</b> sets green shadow, if <b>false</b> sets red
	 *            shadow, if <b>null</b> hide all shadows
	 * @param widget
	 *            - widget
	 */
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

	/**
	 * 
	 * @return true if restaurant exist
	 */
	private boolean restaurantExist() {
		// String cityName =
		// cityListBox.getItemText(cityListBox.getSelectedIndex());
		List<Restaurant> restaurants = restaurantController
				.getRestaurantsInCity(city.getId());

		for (Restaurant restaurant : restaurants) {
			if ((restaurant.getName().replaceAll(" ", "").equals(nameText
					.getText().replaceAll(" ", "")))
					&& (restaurant.getAddress().replaceAll(" ", "")
							.equals(adressText.getText().replaceAll(" ", "")))) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void onChange() {
		refreshCityList();
	}

	private void refreshCityList() {
		// cityListBox.clear();
		addCities(cityController.getCitiesList());
	}

	private void addCities(List<City> cities) {
		for (City city : CityController.getInstance().getCitiesList()) {
			// cityListBox.addItem(city.getCity());
		}
	}

	private void setPlaceHolder(Widget element, String placeHolder) {
		element.getElement().setAttribute("placeHolder", placeHolder);
	}

	private void setUpload() {
		formLogoUpload = new MyUploadForm(fileLogoUpload, ImageType.LOGO,
				restaurant.getId() + "");

		formLogoUpload.setVisible(false);
		formLogoUpload.setStyleName("formLogoUpload", true);
		formLogoUpload.getElement().getStyle().setDisplay(Display.NONE);
		formLogoUpload.setBackPage(this);

		final boolean isOSMobile = osType.toLowerCase().indexOf("ipad") >= 0
				|| osType.toLowerCase().indexOf("iphone") >= 0;
		final boolean isOS6 = osType.toLowerCase().indexOf("os 6") >= 0;
		final boolean isAndroid = osType.toLowerCase().indexOf("android") >= 0;

		if (isOSMobile && !isOS6) {
			add(formLogoUpload);
			addBoardFromBottom.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					blobService.getBlobStoreUrl(restaurant.getId() + "",
							ImageType.LOGO, new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {
									String callbackURL = R.HOST_URL
											+ "picupCallback.html";
									cookieController.setCookie(
											CookieNames.IMAGE_TYPE_PICUP,
											ImageType.LOGO.name());
									cookieController.setCookie(
											CookieNames.RESTAURANT_ID_PICUP,
											restaurant.getId() + "");

									setCookiesData(false);
									onUploadFormLoaded(
											fileLogoUpload.getElement(),
											result, callbackURL, R.HOST_URL);

									clickOnInputFile(fileLogoUpload
											.getElement());

								}

								@Override
								public void onFailure(Throwable caught) {

								}
							});
				}
			});

		} else if (isAndroid) {
			formLogoUpload.getElement().getStyle().setDisplay(Display.BLOCK);
			addFlowPanel.insert(formLogoUpload, 0);
		} else {
			add(formLogoUpload);
			addBoardFromBottom.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					clickOnInputFile(fileLogoUpload.getElement());
					// formLogoUpload.getElement().getStyle().setDisplay(Display.BLOCK);
					// formLogoUpload.getElement().getStyle().setPosition(Position.RELATIVE);
					// addBordWrapper.add(formLogoUpload);
				}
			});
		}

		fileLogoUpload.setVisible(true);
		fileLogoUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				PagesController.showWaitPanel();
				// clickOnInputFile(formPanel.getUploadButton().getElement());
				blobService.getBlobStoreUrl(restaurant.getId() + "",
						ImageType.LOGO, new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
								formLogoUpload.setAction(result);

								formLogoUpload.submit();
							}

							@Override
							public void onFailure(Throwable caught) {
								PagesController.hideWaitPanel();
//								Window.alert("Problem with upload. Try again");
								PagesController.MY_POP_UP.showError(new Label("Problem with upload. Try again"), new IMyAnswer() {
									
									@Override
									public void answer(Boolean answer) {
										
									}
								});
							}
						});
			}
		});

		add(addBoard);

	}

	/**
	 * Save given by user data in browser cookies. Cookies name are set using
	 * {@link CookieNames} enum. If <code>clear</code> is true the cookies will
	 * by removed.
	 * 
	 * @param clear
	 *            if true the cookies will by removed
	 */
	private void setCookiesData(boolean clear) {
		if (clear) {
			cookieController.clearCookie(CookieNames.R_NAME);
			cookieController.clearCookie(CookieNames.R_ADDRESS);
			cookieController.clearCookie(CookieNames.R_PHONE);
			cookieController.clearCookie(CookieNames.R_CITY_ID);
			cookieController.clearCookie(CookieNames.R_BOSS_NAME);
			cookieController.clearCookie(CookieNames.R_USERS);

			
			cookieController.clearCookie(CookieNames.IS_PICUP_USED);
		} else {
			cookieController.setCookie(CookieNames.R_NAME, nameText.getText());
			cookieController.setCookie(CookieNames.R_ADDRESS, adressText.getText());
			cookieController.setCookie(CookieNames.R_PHONE, phoneRestaurantTextBox.getText());
			cookieController.setCookie(CookieNames.R_CITY_ID, citiesListCombo.getSelectedOrder()+"");
			cookieController.setCookie(CookieNames.R_BOSS_NAME, bossTextBox.getText());
			cookieController.setCookie(CookieNames.R_USERS, addUserTextBox.getText());
			
			cookieController.setCookie(CookieNames.IS_PICUP_USED, "true");
		}
	}

	private static native void onUploadFormLoaded(Element fileUpload,
			String blobStoreUrl, String callbackURL, String cancelURL) /*-{
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

	@Override
	public void newData() {
		// TODO Auto-generated method stub
		
	}
}
