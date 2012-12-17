package com.veliasystems.menumenu.client.userInterface;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.panel.JQMControlGroup;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

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
	
	private String city;
	private Restaurant restaurant;
	
	private boolean isToCity;
	private boolean loaded = false;
	
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private UserController userController = UserController.getInstance();
	
	private FlowPanel container;
	
	private FlowPanel nameWrapper = new FlowPanel();
	private FlowPanel addressWrapper = new FlowPanel();
	private FlowPanel phoneWrapper = new FlowPanel();
	private FlowPanel wwwWrapper = new FlowPanel();
	private FlowPanel bossWrapper = new FlowPanel();
	
	private FocusPanel adminLabel;
	private FlowPanel adminPanelWrapper;
	private MyButton adminPanel;
	
	private FlowPanel addBoard;
	private Label addBoardText;
	
	
	private JQMPage pageToDelete = null;
	
	private void init(boolean isToCity){
		
		this.isToCity = isToCity;
		
		setButtons();

		
		cityLabel = new Label();
		cityLabel.setText(Customization.CITYONE + ":");
		
		
		if(!isToCity){
			addCities(cityController.getCitiesList());
			
		}else{
//			cityListBox.addItem(city);
			
		}
//		getContentPanel().add(cityListBox);
		setLabels();
		
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
		
		
		
		  if(userController.getLoggedUser().isAdmin()){
		    	
		    	adminPanel = new MyButton("");
		    	adminPanel.removeStyleName("borderButton");
		    	adminPanel.addStyleName("addButton adminButton");
		    	adminPanel.getElement().getStyle().setHeight(50, Unit.PX);
		 	    adminPanel.addClickHandler(new ClickHandler() {
		 			
		 			@Override
		 			public void onClick(ClickEvent event) {
//		 				PagesController.showWaitPanel();
//		 				JQMContext.changePage(PagesController.getPage(Pages.PAGE_RESTAURANT_MANAGER), Transition.SLIDE);	
		 			}
		 		});
		    	adminLabel = new FocusPanel();
		    	adminLabel.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
//						PagesController.showWaitPanel();
//						JQMContext.changePage(PagesController.getPage(Pages.PAGE_RESTAURANT_MANAGER), Transition.SLIDE);	
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
		    
		    }

		addBoard = new FlowPanel();
		addBoard.addStyleName("addBoardWrapper");
		
		addBoardText = new Label(Customization.ADD_BOARD);
		addBoardText.addStyleName("addBoardLabel");
		
		addBoard.add(addBoardText);
		
		getContentPanel().add(container);
		getContentPanel().add(addBoard);
		getContentPanel().add(adminPanelWrapper);
	}
	

	public AddRestaurantScreen(String city){
		super(city);
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
		RestaurantController.getInstance().setLastOpenPage(this);
		clearData();		
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	@Override
	protected void onPageHide() {
		super.onPageHide();
		getElement().removeFromParent();
	}
	
	private void clearData() {
		nameText.setText("");
		adressText.setText("");
		mailRestaurantTextBox.setText("");
		mailUserTextBox.setText("");
		nameUserTextBox.setText("");
		phoneRestaurantTextBox.setText("");
		phoneUserTextBox.setText("");
		warning.setText("");
		
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
					restaurant = new Restaurant();
					restaurant.setName(nameText.getText());
					
					restaurant.setAddress(adressText.getText());			
//					int index = cityListBox.getSelectedIndex();			
//					restaurant.setCity(cityListBox.getItemText(index));	
					restaurant.setCity(city);
					long cityId = cityController.getCityId(city);
					restaurant.setCityId(cityId);
					//restaurant.setCityId(cityController.)
//					restaurant.setMailRestaurant(mailRestaurantTextBox.getText());
					restaurant.setPhoneRestaurant(phoneRestaurantTextBox.getText());
//					restaurant.setNameUser(nameUserTextBox.getText());
//					restaurant.setPhoneUser(phoneUserTextBox.getText());
//					restaurant.setMailUser(mailUserTextBox.getText());
					
					restaurantController.saveRestaurant(restaurant, true);
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
		List<Restaurant> restaurants = restaurantController.getRestaurantsInCity(city);
		
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
	
}
