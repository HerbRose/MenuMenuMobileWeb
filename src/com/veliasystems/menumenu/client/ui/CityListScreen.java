package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;




public class CityListScreen extends JQMPage implements IObserver{
	
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMList list;
	JQMButton backButton;
	JQMButton addUser;
	
	Label label = new Label();
	CityController cityController = CityController.getInstance();
	private List<City> cityList;
	private boolean loaded = false;
	private RestaurantController restaurantController = RestaurantController.getInstance();
	
	public CityListScreen() {
		
		cityController.addObserver(this);
		
		header = new JQMHeader(Customization.CITY);
		header.setFixed(true);
		header.setText(Customization.CITY);
	
		add(header);
		
		list = new JQMList();
	    list.setInset(false);

	    cityList = CityController.getInstance().getCitiesList();
	    addCities(cityList);
	    add(list);
	    
	    footer = new JQMFooter();
	    footer.setFixed(true);
	    footer.setWidth("100%");
	    add(footer);
	   
	    addButton = new JQMButton(Customization.ADD_CITY, new AddCity(), Transition.SLIDE);
	    addButton.setWidth("49%");
	    addButton.setIcon(DataIcon.PLUS);
	    addButton.setIconPos(IconPos.TOP);
	    addButton.setInline();
	    footer.add(addButton);
	    
	    addUser = new JQMButton(Customization.ADD_USER, new AddUsersScreen(this), Transition.SLIDE);
	    addUser.setWidth("49%");
	    addUser.setIcon(DataIcon.PLUS);
	    addUser.setIconPos(IconPos.TOP);
	    addUser.setInline();
	    footer.add(addUser);
	        
	}
	
	private void addCities(List<City> cities){
		CityInfoScreen cityInfoScreen;
		for(City city: cities){
			if(CityController.cityMapView.containsKey(city.getId())){
				cityInfoScreen = CityController.cityMapView.get(city.getId()) ;
			}else{
				cityInfoScreen = new CityInfoScreen(city.getCity());
				CityController.cityMapView.put(city.getId(), cityInfoScreen);
			}
			list.addItem(city.getCity(), cityInfoScreen);
		}
	}
	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		
		if( !loaded){
			backButton = new JQMButton(Customization.BACK, PagesController.getPage(Pages.PAGE_HOME), Transition.SLIDE);
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);
			
			backButton.getElement().setInnerHTML(span);
			backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");
			
			header.add(backButton);
			loaded = true;
		}
		
		restaurantController.setFromCityView(true);
		
		if(Cookies.getCookie(R.LAST_PAGE) != null){
			Cookies.removeCookie(R.LAST_PAGE);
		}
		Document.get().getElementById("load").setClassName(R.LOADED);
	}

	@Override
	public void onChange() {
		refreshCityList();
	}
	private void refreshCityList(){
		list.clear();
		cityList = cityController.getCitiesList();
	    addCities(cityList);
		list.refresh();
	}
}
