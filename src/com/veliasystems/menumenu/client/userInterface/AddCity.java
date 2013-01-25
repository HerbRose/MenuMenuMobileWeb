package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class AddCity extends MyPage implements IObserver{

	private TextBox nameCity;
	private BackButton backButton;
	private MyButton saveButton;
	private MyListCombo countryListCombo;
	
	private MyRestaurantInfoPanel container;
	
	private CityController cityController = CityController.getInstance();
	
	public AddCity(){
		super(Customization.ADD_CITY);

		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				PagesController.showWaitPanel();
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE);
			}
		});
		
		saveButton = new MyButton(Customization.SAVE);
		saveButton.setStyleName("rightButton saveButton", true);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(validate()){
					String country = "";
					if(countryListCombo.getSelectedOrder() == 1) country = "Poland";
					if(countryListCombo.getSelectedOrder() == 2) country = "France";
					City city = new City();
					city.setCity(nameCity.getText());
					city.setCountry(country);
					
					CityController.getInstance().saveCity(getMe(),city, true);
				}			
			}
		});
		container = new MyRestaurantInfoPanel();
		
		container.setStyleName("containerPanelAddRestaurant", true);
		
		Label cityName = new Label(Customization.NAME);
		nameCity = new TextBox();
		nameCity.addStyleName("myTextBox nameBox");
		
		Label countryLabel = new Label(Customization.COUNTRY);
		countryListCombo = new MyListCombo(false);
		
		countryListCombo.addListItem(countryListCombo.getNewItem(Customization.POLAND), 1);
		countryListCombo.addListItem(countryListCombo.getNewItem(Customization.FRANCE), 2);
		
		
		countryListCombo.selectItem(1);
		
		
		container.addItem(cityName, nameCity);
		container.addItem(countryLabel, countryListCombo);
			
		add(container);
		
		
		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(saveButton);
	}
	
	@Override
	protected void onPageShow() {
		nameCity.setText("");
		container.setWidth( JS.getElementOffsetWidth(getElement())-20 );
		PagesController.hideWaitPanel();
	}


	private boolean validate(){
		String matcher =".*[^-]-.*[^-]";
		if(nameCity.getText().matches(matcher)) {
			return true;
		}
		showWarning(Customization.WRONG_CITY_NAME);
		return false;
	}
	
	private boolean cityExist(String cityName){
		
		String[] newCityName = cityName.split("-");
		
		for (City city : cityController.getCitiesList()) {
			if (cityName.equalsIgnoreCase(city.getCity())) {
				return true;
			}
			String[] cityNameFromServer = city.getCity().split("-");
			
			if( (cityNameFromServer[0].replace(" ", "")).equalsIgnoreCase(newCityName[0].replace(" ", "")) &&  (cityNameFromServer[1].replace(" ", "")).equalsIgnoreCase(newCityName[1].replace(" ", "")) ){
				return true;
			}
			
		}
		return false;
	}
	
	private void showWarning( String worning){
//		Window.alert(worning);
		PagesController.MY_POP_UP.showWarning(new Label(worning), new IMyAnswer() {
			
			@Override
			public void answer(Boolean answer) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newData() {
		// TODO Auto-generated method stub
		
	}
	
	private AddCity getMe(){
		return this;
	}
}
