package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.GetMethod;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.CookieNames;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyInfoPanelRow;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyUploadForm;

public class CityManagerPanel extends FlowPanel implements IManager, IObserver {

	//pola do copiowania danych pomiedzy miastami
	private TextBox cityIdFrom;
	private TextBox cityIdTo;
	private JQMButton send;
	// KONIEC - pola do copiowania danych pomiedzy miastami
	
	//pola do edycji miast
	private List<City> cities = new ArrayList<City>();
	private Map<Long, FlowPanel> citiesPanels = new HashMap<Long, FlowPanel>();

	//KONIEC - pola do edycji miast
	
	//Kontrolery
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	
	//KONIEC - Kontrolery

	private String osType = getUserAgent(); 
	

	public CityManagerPanel() {
		show(false);
		setStyleName("barPanel", true);
		
		
//		cityIdFrom = new TextBox();
//		cityIdFrom.addStyleName("properWidth");
//		setPlaceHolder(cityIdFrom, "doNotUseThis");
//		
//		cityIdTo = new TextBox();
//		cityIdTo.addStyleName("properWidth");
//		setPlaceHolder(cityIdTo, "doNotUseThis");
		
//		send = new JQMButton(Customization.SAVE);
//		send.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				String pass = Window.prompt(Customization.PASSWORD_PLACEHOLDER, "");
//				if(pass.equals("wdo67pla")){
//					cityController.copyAllDataFromCity(cityIdFrom.getText(), cityIdTo.getText());
//				}
//				
//			}
//
//		});
//		
	
		
//		add(cityIdFrom);
//		
//		add(cityIdTo);
//		
//		add(send);

	}
	
	private void setCitiesList(){
		
		clear();
		for (final City city : cities) {
			
			FlowPanel cityDetails = new FlowPanel(); //div na detale miasta
			cityDetails.setStyleName("cityTableDiv", true);
			cityDetails.getElement().setId("cityToEdit"+city.getId());
			
			FlowPanel cityListDiv = new FlowPanel(); //div ze strzalka nazwa dzielnicy i detale miasta
			cityListDiv.setStyleName("cityListDiv", true);
			
			FlowPanel cityHeaderDiv = new FlowPanel(); //div na strzalke i nazwe dzielnicy
			cityHeaderDiv.setStyleName("cityHeaderDiv", true);
			
			Image blackArrowImage = new Image("img/blackArrow.png"); //strzalka
			blackArrowImage.setStyleName("blackArrowImage", true);
			
			final ToggleButton arrowToggleButton = new ToggleButton(blackArrowImage);
			arrowToggleButton.setStyleName("arrowToggleButton", true);
			
			arrowToggleButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(arrowToggleButton.isDown()){
						showCityTable(citiesPanels.get(city.getId()), arrowToggleButton, true);
					}else {
						showCityTable(citiesPanels.get(city.getId()), arrowToggleButton, false);
					}
				}
			});

			
			Label cityNameLabel = new Label(city.getCity()); //nazwa dzielnicy
			cityNameLabel.setStyleName("cityNameLabel", true);
			
			citiesPanels.put(city.getId(), cityDetails);
			
			cityHeaderDiv.add(arrowToggleButton);
			cityHeaderDiv.add(cityNameLabel);
			cityListDiv.add(cityHeaderDiv);
			cityListDiv.add(cityDetails);
			add(cityListDiv);
		}
	}
	private void fillCityDetails(FlowPanel cityDetails, final City city){
		
		cityDetails.clear();
		
		
		
//		FlowPanel namePanel = new FlowPanel();
//		namePanel.setStyleName("namePanel", true);
		
//		FlowPanel visabilityPanel = new FlowPanel();
//		visabilityPanel.setStyleName("visabilityPanel", true);
//		
//		FlowPanel buttonsPanel = new FlowPanel();
//		buttonsPanel.setStyleName("buttonsPanel", true);
//		
//		FlowPanel imagePanel = new FlowPanel();
//		imagePanel.setStyleName("imagesPanel", true);
		
		MyRestaurantInfoPanel cityInfoPanel = new MyRestaurantInfoPanel();
		cityInfoPanel.setStyleName("containerPanelAddRestaurant", true);
		cityInfoPanel.setWidth(JS.getElementOffsetWidth(getParent().getElement())-20 );

		
		Label nameLabel = new Label(Customization.CITY_NAME);
		final TextBox nameTextBox = new TextBox();
		nameTextBox.getElement().setId("nameTextBox"+city.getId());
		nameTextBox.addStyleName("myTextBox nameBox");
		nameTextBox.setText(city.getCity());
		
		Label visabilityLabel = new Label(Customization.VISIBILITY);
		final Button isVisibleForProduction = new Button("");
		setVisabilityText(true, city.isVisable(true), isVisibleForProduction, city.getId());
		//isVisable.setValue(city.isVisable());
		
		isVisibleForProduction.setStyleName("backgroundNone borderNone", true);
		isVisibleForProduction.getElement().setId("isVisable"+city.getId());
		isVisibleForProduction.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setVisabilityText(true, !cities.get(cities.indexOf(city)).isVisable(true), isVisibleForProduction, city.getId());
				
			}
		});
		
		Label visiblityForTestLabel = new Label(Customization.VISIBILITY_FOR_TESTS);
		final Button isVisibleForTests = new Button("");
		setVisabilityText(false, city.isVisable(false), isVisibleForTests, city.getId());
		
		isVisibleForTests.setStyleName("backgroundNone borderNone", true);
		isVisibleForTests.getElement().setId("isVisibleForTests" + city.getId());
		isVisibleForTests.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setVisabilityText(false, !cities.get(cities.indexOf(city)).isVisable(false), isVisibleForTests, city.getId());
			}
		});

		Label districtImageLabel = new Label(Customization.DISTRICT_IMAGE);

		Button deleteButton = new Button(Customization.DELETE);
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm(Customization.ARE_YOU_SURE_WANT_DELETE)){
					cityController.deleteCity(city.getId());
				}
				
			}
		});
		
		Button saveButton = new Button(Customization.SAVE);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				city.setCity(nameTextBox.getText());
				if(validData(city)){
					cityController.saveCity(getMe(), city, false);
				}else{
					Window.alert(Customization.WRONG_DATA_ERROR);
				}
			}

			
		});

		Label countryLabel = new Label(Customization.COUNTRY);
		MyListCombo	countryListCombo = new MyListCombo(false);
		
		countryListCombo.addListItem(countryListCombo.getNewItem(Customization.POLAND), 1);
		countryListCombo.addListItem(countryListCombo.getNewItem(Customization.FRANCE), 2);
		
		
		countryListCombo.selectItem(1);
		
//		namePanel.add(nameLabel);
//		namePanel.add(nameTextBox);
//		
//		
//		visabilityPanel.add(visabilityLabel);
//		visabilityPanel.add(isVisibleForProduction);
//		visabilityPanel.add(visiblityForTestLabel);
//		visabilityPanel.add(isVisibleForTests);
		
//		imagePanel.add(districtImageLabel);
		
		
		
		final MyUploadForm myUploadForm = createUpload(city.getId());
		
		cityInfoPanel.addItem(nameLabel, nameTextBox);
		cityInfoPanel.addItem(visabilityLabel, isVisibleForProduction);
		cityInfoPanel.addItem(visiblityForTestLabel, isVisibleForTests);
		cityInfoPanel.addItem(countryLabel, countryListCombo);
		if(!city.getDistrictImageURL().isEmpty()){
			myUploadForm.getElement().getStyle().setDisplay(Display.NONE);
			add(myUploadForm);
			
			final Image image = createImage(city.getDistrictImageURL(), myUploadForm.getFileUpload() );
			final MyInfoPanelRow imageRow = cityInfoPanel.addItem(districtImageLabel, image);
			image.addLoadHandler(new LoadHandler() {
				
				@Override
				public void onLoad(LoadEvent event) {
					imageRow.setHeight(image.getHeight() + 20);
				}
			});
			
			
//			imagePanel.add(createImage(city.getDistrictImageURL(), myUploadForm.getFileUpload() ) );
			
		}else{
			cityInfoPanel.addItem(districtImageLabel,  myUploadForm);
//			imagePanel.add(myUploadForm);
		}

		cityInfoPanel.addItem(saveButton, deleteButton);

		cityDetails.add(cityInfoPanel);
//		buttonsPanel.add(saveButton);
//		buttonsPanel.add(deleteButton);
//		
//		cityDetails.add(namePanel);
//		cityDetails.add(visabilityPanel);
//		cityDetails.add(imagePanel);
//		cityDetails.add(buttonsPanel);
		
		showCityTable(cityDetails, null, false);
	}
	private void setVisabilityText(boolean isVisableForProduction, boolean isVisible, Button button, Long cityId){
		
		if(button == null) return;
		
		if(isVisible){
			button.setText(Customization.VISIBLE);
			
		}else{
			button.setText(Customization.HIDDEN);
		}
		
		
		button.setStyleName("greenText", isVisible);
		button.setStyleName("redText", !isVisible);
		
		
		
		for (City city : cities) {
			if(city.getId() == cityId){
				if(isVisableForProduction) {
					city.setVisable(isVisible, city.isVisable(false));
				}
				else{
					city.setVisable(city.isVisable(true), isVisible);
				}
			}
		}
	}
	private void fillCitiesList(){
		cities.clear();
		cityController.refreshCities(this);
	}
	public void showCityTable( Widget widget, ToggleButton arrowToggleButton, boolean isVisable) {
		
		if(isVisable){
			int height = getHeight(widget.getElement().getId()); 
			widget.setHeight(height+20+ "px");
		}else{
			widget.setHeight("0px");
		}
		
		
		if(arrowToggleButton != null){
			arrowToggleButton.setStyleName("arrowToggleButtonShow", isVisable);
			arrowToggleButton.setStyleName("arrowToggleButtonHide", !isVisable);
		}
		
	}
	private void setPlaceHolder(Widget element, String placeHolder){
		element.getElement().setAttribute("placeHolder", placeHolder);
	}
	
	private boolean validData(City city) {
		boolean isCorrect = true;

		String matcher =".*[^-]-.*[^-]";
		if(city.getCity().matches(matcher)) {
			for (City oldCity : cities) {
				if(oldCity.equals(city)){
					if (oldCity.getId() == city.getId()) {
						isCorrect = true;
					}else{
						isCorrect = false;
					}
				}
			}
		}else{
			isCorrect = false;
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
	
	@Override
	public void clearData() {
		fillCitiesList();
//		newData();
//		clear();
//		cityIdFrom.setValue("");
//		setValidDataStyle(null, cityIdFrom);
//		cityIdTo.setValue("");
//		setValidDataStyle(null, cityIdTo);
//		
//		for (City city : cities) {
//			fillCityDetails(citiesPanels.get(city.getId()), city);
//			
//		}
		
	}

	@Override
	public String getName() {
		return Customization.CITY_MANAGER;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		
		if(isVisable){
			PagesController.showWaitPanel();
			fillCitiesList();
			
		}
	}

	
	private MyUploadForm createUpload(long cityId) {
		FileUpload fileUpload= new FileUpload();
		MyUploadForm formPanel= new MyUploadForm(fileUpload, ImageType.CITY, cityId+"");
		formPanel.setBackPage(PagesController.getPage(Pages.PAGE_ADMINISTRATION));
		formPanel.setChangeHandler();
		return formPanel;
	}
	
	private Image createImage(String imageUrl, final FileUpload fileUpload){
		Image districtImage = new Image();
		districtImage.setUrl(imageUrl);

		districtImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clickOnInputFile(fileUpload.getElement());
				
			}
		});
		return districtImage;
	}

	private native int getHeight(String elementId)/*-{
		
		var children = $wnd.document.getElementById(elementId).childNodes;
		var length = children.length;
		var height = 0;
		for( i=0; i<=length-1; i++ ){
			height+= children[i].offsetHeight;
		}
		
		return height;
	}-*/;
	
	private native String getUserAgent()/*-{
		return navigator.userAgent;
	}-*/;
	
	private static native void clickOnInputFile(Element elem) /*-{
		elem.click();
	}-*/;

	@Override
	public void onChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newData() {
		cities.clear();
		cities.addAll( cityController.getCitiesList() );
		setCitiesList();
		for (City city : cities) {
			fillCityDetails(citiesPanels.get(city.getId()), city);
		}
		PagesController.hideWaitPanel();	
	}
	
	private IObserver getMe() {
		return this;
	}
}
