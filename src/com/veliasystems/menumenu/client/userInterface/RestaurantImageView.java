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
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
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
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.CookieController;
import com.veliasystems.menumenu.client.controllers.CookieNames;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.LoadedPageController;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.controllers.UserType;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyUploadForm;

public class RestaurantImageView extends MyPage {

	private VerticalPanel scrollerDiv;

	private Restaurant restaurant;
	private boolean loaded = false;
	private List<SwipeView> swipeViews = new ArrayList<SwipeView>();
	
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private CityController cityController = CityController.getInstance();
	private CookieController cookieController = CookieController.getInstance();
	private UserController userController = UserController.getInstance();
	private ImagesController imagesController = ImagesController.getInstance();

	private MyButton cancelButton = new MyButton(Customization.CANCEL);;
	private MyButton saveButton = new MyButton(Customization.SAVE);
	private BackButton backButton;
	private MyButton editButton;
	private MyButton adminPanel;
	private MyRestaurantInfoPanel container;
	private MyListCombo citiesListCombo = new MyListCombo(false);
	
	private Label nameLabel;
	private Label adressLabel;
	private Label phoneRestaurantLabel;
	private Label districtLabel;
	private Label bossLabel;
	private Label addBoardText;
	private Label warning = new Label();
	private Label deleteLabel = new Label(Customization.REMOVEPROFILE);
	private Label cancel = new Label(Customization.CANCEL);
	private Label deleteImage = new Label(Customization.DELETE);

	private Image logoImage;
	private Label nameLabelInfo;
	private Label addressLabelInfo;
	private Label publishText;

	private TextBox nameText = new TextBox();
	private TextBox adressText = new TextBox();
	private TextBox phoneRestaurantTextBox = new TextBox();
	private TextBox districtTextBox = new TextBox();
	private TextBox bossTextBox = new TextBox();

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
	private FlowPanel addBoardWrap = new FlowPanel();
	private FlowPanel deletePanel = new FlowPanel();
	private FlowPanel addFlowPanel = new FlowPanel();

	private Image publishImage;
	private Image logoEditImage;
	
	private FocusPanel adminLabel;
	private FocusPanel addBoard;
	private FocusPanel publish;
	private FocusPanel deleteProfile = new FocusPanel();
	private FocusPanel deleteProfileConfirmed = new FocusPanel();
	private FocusPanel cancelDeleteProfile = new FocusPanel();
	private FocusPanel removeBoard = new FocusPanel();
	private FocusPanel addBoardFromBottom = new FocusPanel();

	private FileUpload fileLogoUpload = new FileUpload();
	private MyUploadForm formLogoUpload;

	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	private String osType = R.USER_AGENT;

	private boolean isEdit = false;
	private boolean isDeleteImage = false;
	private boolean isDeleteBoard = false;
	
	private ImageBlob imageBlobClickedToDelete;

	public RestaurantImageView(Restaurant r, JQMPage back) {
		super(r.getName());
		
		this.restaurant = r;
		
		deleteProfileConfirmed.addStyleName("deleteProfileConfirmed noFocus pointer");
		cancelDeleteProfile.addStyleName("cancelDeleteProfile noFocus pointer");
		
		deletePanel.add(deleteProfileConfirmed);
		deletePanel.add(cancelDeleteProfile);
		
		deleteProfile.add(deleteLabel);
		deletePanel.addStyleName("deletePanel");
		
		setDefaultDeleteBehavior();

		getContentPanel().add(deletePanel);
		deleteProfile.addStyleName("noFocus deleteProfile pointer");
		
		
		infoContainer = new FlowPanel();
		infoContainer.addStyleName("infoContainer");

		logoImageWrapper = new FlowPanel();
		logoImageWrapper.addStyleName("logoImageWrapper");

		infoTextWrapper = new FlowPanel();
		infoTextWrapper.addStyleName("infoTextWrapper");

		swipeContainer = new FlowPanel();
		swipeContainer.addStyleName("swipeContainer");

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
		logoImage.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				logoImage.getElement().getStyle().setHeight(60, Unit.PX);
			}
		});
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
	
	private void setDefautDeleteContent(){
		isDeleteImage = false;
		isDeleteBoard = false;
		deleteProfileConfirmed.clear();
		cancelDeleteProfile.clear();
		deleteProfileConfirmed.add(new Label(Customization.REMOVEPROFILE));
		deleteProfileConfirmed.setStyleName("deleteProfileConfirmed");
		deletePanel.remove(addBoardFromBottom);
		
		cancelDeleteProfile.add(cancel);
	}
	private void setDefaultDeleteBehavior() {
		setDefautDeleteContent();
		deleteProfile.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				deletePanel.getElement().getStyle().setHeight(160.0, Unit.PX);
				deletePanel.getElement().getStyle().setBottom(0, Unit.PX);	
			}
		});
		cancelDeleteProfile.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				deletePanel.getElement().getStyle().setHeight(0.00, Unit.PX);
				isDeleteImage = false;
				setDefautDeleteContent();
			}
		});
		
		deleteProfileConfirmed.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
					if(!isDeleteImage && !isDeleteBoard){
						restaurantController.deleteRestaurant(restaurant, RestaurantImageView.class.getName());
					}
					else if(isDeleteImage && !isDeleteBoard){
						PagesController.showWaitPanel();
						blobService.deleteBlob(imageBlobClickedToDelete, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {	
							}
							@Override
							public void onSuccess(Boolean result) {
								if(result){
									switch(imageBlobClickedToDelete.getImageType()){
									case MENU:
											restaurant.getMenuImages().remove(imageBlobClickedToDelete);
										break;
									case PROFILE:
											restaurant.getProfileImages().remove(imageBlobClickedToDelete);
										break;
									}
							
									setDefautDeleteContent();
									deletePanel.getElement().getStyle().setHeight(0.00, Unit.PX);
									checkChanges();	
									changeVisibility("myImageEditPanel" + restaurant.getId(), true);
									PagesController.hideWaitPanel();
								}
							}
						
						});
					} else if(isDeleteBoard && !isDeleteImage){
						
						
						if(restaurant.getMainLogoImageString().isEmpty() || restaurant.getMainLogoImageString() == null) return;
						PagesController.showWaitPanel();
						blobService.removeImageBlobByBlobKey(parseURLtoBlobKey(restaurant.getMainLogoImageString()), new AsyncCallback<Void>() {

							public void onFailure(Throwable caught) {
								PagesController.hideWaitPanel();
							}

							@Override
							public void onSuccess(Void result) {
								logoImage.setUrl("");
								addBoardWrap.remove(logoEditImage);
								PagesController.hideWaitPanel();
							}
						});
						
						
					}
					
			}
		});
		
		
		
	}
	
	
	private String parseURLtoBlobKey(String url){	
		String tab[] = url.split("/?");
		String tab2[] = url.split("=");		
		return tab2[1];
	}
	
	@Override
	protected void onPageHide() {
		super.onPageHide();
		
		
	}
	
	@Override
	protected void onPageShow() {

		if (!loaded) {
			SwipeView swipeView;
			
			if(userController.isUserType(UserType.ADMIN) || userController.isUserType(UserType.AGENT)){
				swipeView = new SwipeView(restaurant, ImageType.PROFILE,
				this);
				swipeViews.add(swipeView);
				addToContent(swipeView);
			}

			swipeView = new SwipeView(restaurant, ImageType.MENU, this);
			swipeViews.add(swipeView);
			addToContent(swipeView);

			swipeContainer.add(scrollerDiv);

			publishWrapper = new FlowPanel();
			publishWrapper.addStyleName("publishWrapper");

			publishImageWrapper = new FlowPanel();
			publishImageWrapper.addStyleName("publishImage");

			publishLabelWrapper = new FlowPanel();
//			publishLabelWrapper.addStyleName("");
			
			publishText = new Label();
			publishImage = new Image();
			logoEditImage = new Image();
			logoEditImage.addErrorHandler(new ErrorHandler() {
				@Override
				public void onError(ErrorEvent event) {
					logoEditImage.getElement().getStyle().setHeight(60, Unit.PX);
				}
			});
			publish = new FocusPanel();
			publish.addStyleName("publishLabelWrapper noFocus");
			
			
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
					//TODO not save, what if are connection error
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
//			editPanel.getElement().getStyle().setDisplay(Display.NONE);
			editPanel.getElement().getStyle().setHeight(0d, Unit.PX);
			
		}
		if(deletePanel != null)
		// setting buttons

		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PagesController.showWaitPanel();

				JQMContext.changePage(PagesController.getCityInfoScreenPage(restaurant.getCityId()), Transition.SLIDE);
			}
		});

		editButton = new MyButton(Customization.EDIT);
		editButton.setStyleName("rightButton saveButton", true);
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				setEditable();
				
				changeButtons();
				changeVisibility("myImageEditPanel"+restaurant.getId(), true);
			}
		});
		
		getHeader().setTitle(restaurant.getName());
		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(editButton);

		if (loaded)
			checkChanges();
//		changeVisibility("myImageEditPanel"+restaurant.getId(), false);
		setValidVisibility();
		showCamera();
		logoImage.setUrl(restaurant.getMainLogoImageString()==null?"":restaurant.getMainLogoImageString());

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
				setDefautDeleteContent();
				getContentPanel().remove(warning);
				hideDeletePanelForImages();
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
					restaurant.setNameUser(bossTextBox.getText());
					PagesController.showWaitPanel();
					RestaurantController.getInstance().saveRestaurant(
							restaurant, false);
					setProperButtons();
					setValidVisibility();
					showCamera();
					nameLabelInfo.setText(restaurant.getName());
					addressLabelInfo.setText(restaurant.getAddress());
					getHeader().setTitle(restaurant.getName());

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
		changeVisibility("myImageEditPanel"+restaurant.getId(), false);
		editPanel.getElement().getStyle().setHeight(0d, Unit.PX);
		adminPanelWrapper.getElement().getStyle().setDisplay(Display.NONE);
		infoContainer.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		publishWrapper.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		setValidDataStyle(null, nameWrapper);
		setValidDataStyle(null, addressWrapper);
		setValidDataStyle(null, phoneWrapper);
		setValidDataStyle(null, wwwWrapper);
		setValidDataStyle(null, bossWrapper);

		hideDeletePanelForImages();
		isEdit = false;
	
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

		String cityName = cityController.getCity(restaurant.getCityId()).getCity();
		List<Restaurant> restaurants = restaurantController
				.getRestaurantsInCity(restaurant.getCityId());

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

	private void setEditable(){

		if (container == null) {
			
			container = new MyRestaurantInfoPanel();
			container.setWidth( getBodyOffsetWidth(getElement())-20 );
			container.setStyleName("containerPanelAddRestaurant", true);
			//container.addStyleName("containerPanelAddRestaurant");

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
			
			citiesListCombo.selectItem(restaurant.getCityId());
			
			container.addItem(nameLabel, nameText);
			container.addItem(adressLabel, adressText);
			container.addItem(phoneRestaurantLabel, phoneRestaurantTextBox);
			container.addItem(districtLabel, citiesListCombo);
			container.addItem(bossLabel, bossTextBox);

			addBoardWrap.addStyleName("addBoardWrap");
			
			addBoard = new FocusPanel();
			addBoard.addStyleName("addBoardWrapper noFocus");
			
			addBoardText = new Label(Customization.ADD_BOARD);
			addBoardText.addStyleName("addBoardLabel");


			addBoardWrap.add(addBoardText);
			setUpload();
			addBoard.add(addBoardWrap);
			
		
			if(restaurant.getMainLogoImageString() != null){
				setMainLogoBoard();
			}
			setData();		
			editPanel.add(container);
			editPanel.add(addBoard);
				
		}
		container.setWidth( getBodyOffsetWidth(getElement())-20 );
		
		getContentPanel().add(deleteProfile);
		
		if(restaurant.getMainLogoImageString() != null){
			setMainLogoBoard();
		}
		
		nameText.setText(restaurant.getName());
		adressText.setText(restaurant.getAddress());
		phoneRestaurantTextBox.setText(restaurant.getPhoneRestaurant());
		districtTextBox.setText("");
		bossTextBox.setText(restaurant.getNameUser());
		
		adminPanelWrapper.getElement().getStyle()
				.setDisplay(Display.BLOCK);
//		editPanel.getElement().getStyle().setDisplay(Display.BLOCK);
		editPanel.getElement().getStyle().setHeight(410d, Unit.PX);
		infoContainer.getElement().getStyle().setDisplay(Display.NONE);
		publishWrapper.getElement().getStyle().setDisplay(Display.NONE);
		isEdit = true;
		hideCamera();

	}
	
	private void setMainLogoBoard(){
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
		changeVisibility("myImageEditPanel"+restaurant.getId(), false);
		
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

	private void setUpload() {
		formLogoUpload = new MyUploadForm(fileLogoUpload, ImageType.LOGO, restaurant.getId() + "");
		
		formLogoUpload.setVisible(false);
		formLogoUpload.setStyleName("formLogoUpload", true);
		formLogoUpload.getElement().getStyle().setDisplay(Display.NONE);
		
		
		final boolean isOSMobile = osType.toLowerCase().indexOf("ipad") >= 0 || osType.toLowerCase().indexOf("iphone") >= 0;
		final boolean isOS6 = osType.toLowerCase().indexOf("os 6")>=0;
		final boolean isAndroid = osType.toLowerCase().indexOf("android")>=0;

		addFlowPanel.setStyleName("addFlowPanel");
		addFlowPanel.add(new Label(Customization.ADD_BOARD));
		addBoard.addClickHandler(new ClickHandler() {
			
				@Override
				public void onClick(ClickEvent event) {

				isDeleteBoard = true;
				deletePanel.getElement().getStyle().setHeight(225.00, Unit.PX);
				deletePanel.getElement().getStyle().setBottom(0, Unit.PX);

				addBoardFromBottom.clear();
				//addFlowPanel.clear();
				
				
				addBoardFromBottom.add(addFlowPanel);
				addBoardFromBottom.addStyleName("deletePanelWhiteBackground noFocus pointer");
				
				deletePanel.insert(addBoardFromBottom, 0);
				
				deleteProfileConfirmed.clear();
				deleteProfileConfirmed.add(new Label(Customization.REMOVE_BOARD));	
				deleteProfileConfirmed.setStyleName("deletePanelWhiteBackground noFocus pointer");

			}
		});
		
		if(isOSMobile && !isOS6){
			addBoardFromBottom.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					blobService.getBlobStoreUrl(restaurant.getId()+"", ImageType.LOGO,
							new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {
									String callbackURL = R.HOST_URL + "picupCallback.html" ;
									
									cookieController.setCookie(CookieNames.IMAGE_TYPE_PICUP, ImageType.LOGO.name());
									cookieController.setCookie(CookieNames.RESTAURANT_ID_PICUP, restaurant.getId()+"");
									
									onUploadFormLoaded(fileLogoUpload.getElement(), result, callbackURL, R.HOST_URL);
	
									clickOnInputFile(fileLogoUpload.getElement());
	
								}
	
								@Override
								public void onFailure(Throwable caught) {
	
								}
							});
				}
			});

		} else if(isAndroid){
			formLogoUpload.getElement().getStyle().setDisplay(Display.BLOCK);
			addFlowPanel.insert(formLogoUpload, 0);
		} else{
			addBoardFromBottom.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					clickOnInputFile(fileLogoUpload.getElement());
					formLogoUpload.getElement().getStyle().setDisplay(Display.BLOCK);
					formLogoUpload.getElement().getStyle().setPosition(Position.RELATIVE);
					addFlowPanel.add(formLogoUpload);
					add(formLogoUpload);
					deletePanel.getElement().getStyle().setHeight(0.00, Unit.PX);
					setDefautDeleteContent();
				}
			});
		}
				
		fileLogoUpload.setVisible(true);
		fileLogoUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				PagesController.showWaitPanel();
				// clickOnInputFile(formPanel.getUploadButton().getElement());
				blobService.getBlobStoreUrl(restaurant.getId()+"", ImageType.LOGO,
						new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
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
		
		add(addBoard);
		
	}

	
	private void hideCamera(){
		if(userController.isUserType(UserType.ADMIN) || userController.isUserType(UserType.AGENT)){
			Document.get().getElementById(ImageType.PROFILE.name()+restaurant.getId()).addClassName("hidden");
		}
		
		Document.get().getElementById(ImageType.MENU.name()+restaurant.getId()).addClassName("hidden");
	}
	
	private void showCamera(){
		if(userController.isUserType(UserType.ADMIN) || userController.isUserType(UserType.AGENT)){
			Document.get().getElementById(ImageType.PROFILE.name()+restaurant.getId()).removeClassName("hidden");
		}
		
		Document.get().getElementById(ImageType.MENU.name()+restaurant.getId()).removeClassName("hidden");
	}
	
	public void showDeletePanelForImages(ImageBlob imageBlob){
		imageBlobClickedToDelete = imageBlob;
		deletePanel.getElement().getStyle().setHeight(160.00, Unit.PX);
		deletePanel.getElement().getStyle().setBottom(0, Unit.PX);
		
		deleteProfileConfirmed.clear();
		deleteProfileConfirmed.setStyleName("deletePanelWhiteBackground");
		
		deleteProfileConfirmed.add(new Label(Customization.DELETE_IMAGE));	
		isDeleteImage = true;
	}
	
	private void hideDeletePanelForImages(){
		deletePanel.getElement().getStyle().setHeight(0.00, Unit.PX);
		setDefautDeleteContent();
		getContentPanel().remove(deleteProfile);
	}
	
	public boolean isEdit() {
		return isEdit;
	}
	
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	
	

	private native void changeVisibility(String className, boolean show)/*-{
		var elements = 	$wnd.document.getElementsByClassName(className);
		if(elements === 'undefined') return;
		for(var i=0; i<elements.length; i++){
			if(show) elements[i].style.display = 'block';
			else elements[i].style.display = 'none';
		}
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
	
	private static native int getBodyOffsetWidth(Element element)/*-{
		return element.offsetWidth;
	}-*/;

}
