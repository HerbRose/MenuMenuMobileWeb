package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

public class AddCity extends MyPage{


	private TextBox nameCity;
	private Label warningLabel = new Label();
	private BackButton backButton;
	private MyButton saveButton;
	private FlowPanel wrapper;
	
	private CityController cityController = CityController.getInstance();
	
	private boolean loaded = false;
	
	public AddCity(){
		super(Customization.ADD_CITY);
		
		nameCity = new TextBox();
		nameCity.setStyleName("addCity");
		
		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_CITY_LIST), Transition.SLIDE);
			}
		});
		
		saveButton = new MyButton(Customization.SAVE);
		saveButton.setStyleName("rightButton", true);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(validate()){
					CityController.getInstance().saveCity(nameCity.getText());
				}			
			}
		});
		
		wrapper = new FlowPanel();
		wrapper.setStyleName("addCityWrapper");
		wrapper.add(nameCity);
		
		
		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(saveButton);
		getContentPanel().add(wrapper);
		
	}
	
	@Override
	protected void onPageShow() {
		warningLabel.setText("");		
		nameCity.setText("");
		Document.get().getElementById("load").setClassName(R.LOADED);
	}


	private boolean validate(){
		String matcher =".*[^-]-.*[^-]";
		if(nameCity.getText().matches(matcher)) {
			if(!cityExist(nameCity.getText())){
				return true;
			}else{
				showWarning(Customization.CITY_EXIST_ERROR);
				return false;
			}
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
		remove(warningLabel);
		warningLabel.setText(worning);
		warningLabel.setStyleName("warning");
		wrapper.add(warningLabel);
	}
}
