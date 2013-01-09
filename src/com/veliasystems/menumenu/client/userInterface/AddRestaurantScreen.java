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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.panel.JQMControlGroup;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.Pages;
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
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyUploadForm;

public class AddRestaurantScreen extends MyPage implements IObserver{
	
	
	private BackButton backButton;
	private MyButton saveButton;
	
	private Label nameLabel;
	private Label cityLabel;
	private Label adressLabel;
	private Label mailRestaurant;
	private Label phoneRestaurant;
	private Label mailUser;
	private Label phoneUser;
	private Label nameUser;
	private Label websiteLabel;
	private Label bossLabel;
	
	private TextBox nameText = new TextBox();
	private TextBox adressText = new TextBox();
	private TextBox mailRestaurantTextBox = new TextBox();
	private TextBox phoneRestaurantTextBox = new  TextBox();
	private TextBox mailUserTextBox = new TextBox();
	private TextBox phoneUserTextBox = new TextBox();
	private TextBox nameUserTextBox = new TextBox();
	private TextBox websiteTextBox = new  TextBox();
	private TextBox bossTextBox = new TextBox();
	
//	private ListBox cityListBox = new ListBox();
	private Label warning = new Label();
	
	private City city;
	private Restaurant restaurant = new Restaurant();;
	
	private boolean isToCity;
	private boolean loaded = false;
	private boolean isToDeleted = true;
	
	
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private UserController userController = UserController.getInstance();
	
	private FlowPanel container;
	
	private FlowPanel nameWrapper = new FlowPanel();
	private FlowPanel addressWrapper = new FlowPanel();
	private FlowPanel phoneWrapper = new FlowPanel();
	private FlowPanel wwwWrapper = new FlowPanel();
	private FlowPanel bossWrapper = new FlowPanel();
	private FlowPanel addBordWrapper = new FlowPanel();
	private FlowPanel addFlowPanel = new FlowPanel();
	private FlowPanel addBoardPanel = new FlowPanel();
	
//	private FlowPanel addUser;
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
	
	private void init(boolean isToCity){
		
		this.isToCity = isToCity;
		
		setButtons();
		
		addBoardPanel.setStyleName("deletePanel");
		
		cityLabel = new Label();
		cityLabel.setText(Customization.CITYONE + ":");
		
		
		if(!isToCity){
			addCities(cityController.getCitiesList());	
		}else{
//			cityListBox.addItem(city);		
		}
//		getContentPanel().add(cityListBox);
		
		cancelDeleteBoard.addStyleName("cancelDeleteProfile noFocus pointer");
		addBoardFromBottom.addStyleName("deletePanelWhiteBackground noFocus pointer");
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
				blobService.removeImageBlobByBlobKey(logoBlobString, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						
						PagesController.hideWaitPanel();
						Window.alert(Customization.CONNECTION_ERROR);
						//logoImage.setUrl("");
						//addBoardWrap.remove(logoEditImage);
					}

					@Override
					public void onSuccess(Void result) {
//						restaurant.setMainLogoImageString("");
//						restaurantController.saveRestaurant(restaurant, false);
						hideAddBoardPanel();
						addBordWrapper.clear();
						addBordWrapper.getElement().getStyle().clearWidth();
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
	
	private String parseURLtoBlobKey(String url){	
		String tab[] = url.split("/?");
		String tab2[] = url.split("=");		
		return tab2[1];
	}
	
	private void setLabels(){
		
		container = new FlowPanel();
		container.addStyleName("containerPanelAddRestaurant");
		
		
		nameLabel = new Label();
		nameLabel.addStyleName("addRestaurantLabel myLabel arialBold");
		nameLabel.setText(Customization.RESTAURANTNAME + ":");
		
		
		nameText.setTitle(Customization.RESTAURANTNAME);
		nameText.addStyleName("myTextBox nameBox arialBold");	
		
		adressLabel = new Label();
		adressLabel.setText(Customization.RESTAURANTADRESS + ":");
		adressLabel.addStyleName("addRestaurantLabel myLabel arialBold");
		
		adressText.setTitle(Customization.RESTAURANTADRESS);
		adressText.addStyleName("myTextBox nameBox arialBold");
		
		mailRestaurant = new Label();
		mailRestaurant.setText(Customization.RESTAURANT_MAIL + ":");
		mailRestaurant.addStyleName("addRestaurantLabel myLabel arialBold");
		
		mailRestaurantTextBox.setTitle(Customization.RESTAURANT_MAIL);
		
		
		phoneRestaurant = new Label();
		phoneRestaurant.setText(Customization.RESTAURANT_PHONE + ":");
		phoneRestaurant.addStyleName("addRestaurantLabel myLabel arialBold");
		
		phoneRestaurantTextBox.setTitle(Customization.RESTAURANT_PHONE);
		phoneRestaurantTextBox.addStyleName("myTextBox nameBox arialBold");

		
		nameUser = new Label();
		nameUser.setText(Customization.USER_NAME + ":");
		
		
		nameUserTextBox.setTitle(Customization.USER_NAME);
			
		phoneUser = new Label();
		phoneUser.setText(Customization.USER_PHONE +":");
		
		
		phoneUserTextBox.setTitle(Customization.USER_PHONE);
		
		
		mailUser = new Label(Customization.USER_MAIL+":");	
		mailUserTextBox.setTitle(Customization.USER_MAIL);
		
		
		websiteLabel = new Label(Customization.WEBSITE_LABEL);
		websiteLabel.addStyleName("addRestaurantLabel myLabel arialBold");
		
		websiteTextBox.addStyleName("myTextBox nameBox arialBold");
		
		bossLabel = new Label(Customization.BOSS_LABEL);
		bossLabel.addStyleName("addRestaurantLabel myLabel arialBold");
		
		bossTextBox.addStyleName("myTextBox nameBox arialBold");
		
		nameWrapper.addStyleName("addWrapper");
		
		
		addressWrapper.addStyleName("addWrapper");
		
		
		phoneWrapper.addStyleName("addWrapper");
	
		wwwWrapper.addStyleName("addWrapper");
		
		
		bossWrapper.addStyleName("addWrapper");

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
		

		addAddUserWidget();
		
		addBoardText = new Label(Customization.ADD_BOARD);
		addBoardText.addStyleName("addBoardLabel");
			
		addBordWrapper.addStyleName("addBoardWrapper");
		addBordWrapper.add(addBoardText);
		//addBoard.add(addBoardText);
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
	
	
	private void showAddBoardPanel(){
		addBoardPanel.getElement().getStyle().setHeight(225.0, Unit.PX);
		addBoardPanel.getElement().getStyle().setBottom(0, Unit.PX);
	}
	
	private void hideAddBoardPanel(){
		addBoardPanel.getElement().getStyle().setHeight(0.0, Unit.PX);
		addBoardPanel.getElement().getStyle().setBottom(0, Unit.PX);

	}

	public void addAddUserWidget(){
		
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
	
	public AddRestaurantScreen(City city){
		super(city.getCity());
		pageToDelete = this;
		cityController.addObserver(this);
		this.city = city;
		init(true);
	}

	public AddRestaurantScreen() {
		super(Customization.ADDRESTAURANT);
		cityController.addObserver(this);
		init(false);
	
	}
	
	@Override
	protected void onPageShow() {	
		hideAddBoardPanel();
		RestaurantController.getInstance().setLastOpenPage(this);
		isToDeleted = true;
		clearData();		
		
		if(!ImagesController.imageUrl.isEmpty()){
			restaurant.setMainLogoImageString(ImagesController.imageUrl);
			image.setUrl(ImagesController.imageUrl);
			
			addBordWrapper.clear();
			addBordWrapper.getElement().getStyle().setWidth(220d, Unit.PX);
			addBordWrapper.add(image);
			ImagesController.imageUrl = "";
		}
		
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	@Override
	protected void onPageHide() {
		super.onPageHide();
		if(isToDeleted){
			getElement().removeFromParent();
		}
	}
	
	private void clearData() {
//		nameText.setText("");
//		adressText.setText("");
//		mailRestaurantTextBox.setText("");
//		mailUserTextBox.setText("");
//		nameUserTextBox.setText("");
//		phoneRestaurantTextBox.setText("");
//		phoneUserTextBox.setText("");
//		warning.setText("");
		
		setValidDataStyle(null, nameText);
		setValidDataStyle(null, adressText);
		setValidDataStyle(null, mailRestaurantTextBox);
		setValidDataStyle(null, mailUserTextBox);
		setValidDataStyle(null, mailUserTextBox);
		setValidDataStyle(null, phoneRestaurantTextBox);
		setValidDataStyle(null, phoneUserTextBox);
	}

	
	private void setButtons( ){		
		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
//				if(isToCity){
//					Document.get().getElementById("load").setClassName(R.LOADING);
//					JQMContext.changePage(PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE);
//				}else{
//					Document.get().getElementById("load").setClassName(R.LOADING);
//					JQMContext.changePage(PagesController.getPage(Pages.PAGE_RESTAURANT_LIST), Transition.SLIDE);
//				}
				PagesController.showWaitPanel();
				JQMContext.changePage(new CityInfoScreen(city), Transition.SLIDE);
				
			
			}
		});
		
		saveButton = new MyButton(Customization.SAVE);
		saveButton.setStyleName("rightButton saveButton", true);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(validate()){
					
					restaurant.setName(nameText.getText());
					
					restaurant.setAddress(adressText.getText());			
//					int index = cityListBox.getSelectedIndex();			
//					restaurant.setCity(cityListBox.getItemText(index));	
					restaurant.setCity(city.getCity());
					long cityId = cityController.getCityId(city.getCity());
					restaurant.setCityId(cityId);
					//restaurant.setCityId(cityController.)
//					restaurant.setMailRestaurant(mailRestaurantTextBox.getText());
					restaurant.setPhoneRestaurant(phoneRestaurantTextBox.getText());
//					restaurant.setNameUser(nameUserTextBox.getText());
//					restaurant.setPhoneUser(phoneUserTextBox.getText());
//					restaurant.setMailUser(mailUserTextBox.getText());
					restaurant.setNameUser(bossTextBox.getText());
					List<String> usersEmailToAdd = new ArrayList<String>();
					if(!addUserTextBox.getText().isEmpty()){ //validation of email is checked in validate() method
						usersEmailToAdd.add(addUserTextBox.getText());
					}
						
					restaurantController.addNewRestaurant(restaurant, usersEmailToAdd);
//					JQMContext.changePage(new CityInfoScreen(city), Transition.SLIDE);
				}	
			}
		});
		
		

		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(saveButton);
		
	}
	
	private boolean validate(){
		warning.setStyleName("warning");
		warning.setText("");
		add(warning);
		if(!nameText.getText().isEmpty() && !adressText.getText().isEmpty()){
			if(restaurantExist()){
				setValidDataStyle(false, nameWrapper);
				setValidDataStyle(false, addressWrapper);
				warning.setText(Customization.RESTAURANT_EXIST_ERROR);
				return false;
			}else{
				setValidDataStyle(true, nameWrapper);
				setValidDataStyle(true, addressWrapper);
			}
		}
		
		boolean isCorrect = true;
		
		if(nameText.getText().isEmpty() && adressText.getText().isEmpty()){
			warning.setText(Customization.EMPTYBOTHDATA);
			setValidDataStyle(false, nameWrapper);
			setValidDataStyle(false, addressWrapper);
			isCorrect = false;
		}else{
			if(nameText.getText().isEmpty()){
				warning.setText(Customization.EMPTYNAME);
				setValidDataStyle(false, nameWrapper);
				isCorrect = false;
			}else{
				setValidDataStyle(true, nameWrapper);
			}
			if(adressText.getText().isEmpty()){
				warning.setText(warning.getText()+ " \n"+ Customization.EMPTYADRESS);
				setValidDataStyle(false, addressWrapper);
				isCorrect = false;
			}else{
				setValidDataStyle(true, addressWrapper);
			}
		}
		if(!mailRestaurantTextBox.getText().isEmpty() ){
			if(!Util.isValidEmail(mailRestaurantTextBox.getText())){
				setValidDataStyle(false, mailRestaurantTextBox);
				isCorrect = false;
			}else{
				setValidDataStyle(true, mailRestaurantTextBox);
			}
		}else{
			setValidDataStyle(null, mailRestaurantTextBox);
		}
		if(!phoneRestaurantTextBox.getText().isEmpty() ){
			if(!Util.isValidPhoneNumber(phoneRestaurantTextBox.getText())){
				setValidDataStyle(false, phoneWrapper);
				isCorrect = false;
			}else{
				setValidDataStyle(true, phoneWrapper);
			}
		}
		else{
			setValidDataStyle(null, phoneWrapper);
		}
		if(!mailUserTextBox.getText().isEmpty()){
			if(!Util.isValidEmail(mailUserTextBox.getText())){
				setValidDataStyle(false, mailUserTextBox);
				isCorrect = false;
			}else{
				setValidDataStyle(true, mailUserTextBox);
			}
		}else{
			setValidDataStyle(null, mailUserTextBox);
		}
		if(!phoneUserTextBox.getText().isEmpty()){
			if(!Util.isValidPhoneNumber(phoneUserTextBox.getText())){
				setValidDataStyle(false, phoneUserTextBox);
				isCorrect = false;
			}else{
				setValidDataStyle(true, phoneUserTextBox);
			}
		}else{
			setValidDataStyle(null, phoneUserTextBox);
		}
		
		if(!addUserTextBox.getText().isEmpty()){
			if( !Util.isValidEmail(addUserTextBox.getText()) ){
				isCorrect = false;
				setValidDataStyle(false, addUserTextBoxDiv);
			}else{
				setValidDataStyle(true, addUserTextBoxDiv);
			}
		}
		return isCorrect;
	}

	/**
	 * sets the right shadow around the widget
	 * @param isCorrect - if <b>true</b> sets green shadow, if <b>false</b> sets red shadow, if <b>null</b> hide all shadows
	 * @param widget - widget
	 */
	private void setValidDataStyle(Boolean isCorrect, Widget widget){
		if(widget == null) return;
		
		String correct = "greenShadow";
		String unCorrect = "redShadow";
		
		if(isCorrect == null){
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
	private boolean restaurantExist(){
//		String cityName = cityListBox.getItemText(cityListBox.getSelectedIndex());
		List<Restaurant> restaurants = restaurantController.getRestaurantsInCity(city.getId());
		
		for (Restaurant restaurant : restaurants) {
			if( (restaurant.getName().replaceAll(" ", "").equals(nameText.getText().replaceAll(" ", "")))  &&
			    (restaurant.getAddress().replaceAll(" ", "").equals(adressText.getText().replaceAll(" ", ""))) ){
				return true;
			}
		}
		
		return false;
	}
		
	@Override
	public void onChange() {
		refreshCityList();
	}
	private void refreshCityList(){
//		cityListBox.clear();
		addCities(cityController.getCitiesList());
	}
	
	private void addCities(List<City> cities){
		for(City city: CityController.getInstance().getCitiesList()){
//			cityListBox.addItem(city.getCity());
		}
	}
	
	private void setPlaceHolder(Widget element, String placeHolder){
		element.getElement().setAttribute("placeHolder", placeHolder);
	}

	
	private void setUpload() {
		formLogoUpload = new MyUploadForm(fileLogoUpload, ImageType.LOGO, restaurant.getId() + "");
		
		formLogoUpload.setVisible(false);
		formLogoUpload.setStyleName("formLogoUpload", true);
		formLogoUpload.getElement().getStyle().setDisplay(Display.NONE);
		formLogoUpload.setBackPage(this);
		
		
		final boolean isOSMobile = osType.toLowerCase().indexOf("ipad") >= 0 || osType.toLowerCase().indexOf("iphone") >= 0;
		final boolean isOS6 = osType.toLowerCase().indexOf("os 6")>=0;
		final boolean isAndroid = osType.toLowerCase().indexOf("android")>=0;
		
		if(isOSMobile && !isOS6){
			add(formLogoUpload);
			addBoardFromBottom.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					blobService.getBlobStoreUrl(restaurant.getId()+"", ImageType.LOGO,
							new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {
									String callbackURL = R.HOST_URL + "picupCallback.html" ;
									Cookies.setCookie(R.IMAGE_TYPE_PICUP, ImageType.LOGO.name());
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

		} else if(isAndroid){
			formLogoUpload.getElement().getStyle().setDisplay(Display.BLOCK);
			addFlowPanel.insert(formLogoUpload, 0);
		} else{
			add(formLogoUpload);
			addBoardFromBottom.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					clickOnInputFile(fileLogoUpload.getElement());
//					formLogoUpload.getElement().getStyle().setDisplay(Display.BLOCK);
//					formLogoUpload.getElement().getStyle().setPosition(Position.RELATIVE);
//					addBordWrapper.add(formLogoUpload);
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
