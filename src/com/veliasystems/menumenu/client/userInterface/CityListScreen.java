package com.veliasystems.menumenu.client.userInterface;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.ui.AddCity;
import com.veliasystems.menumenu.client.userInterface.CityInfoScreen;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListItem;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;




public class CityListScreen extends MyPage implements IObserver{
	
	
	private BackButton backButton;
	private MyButton addButton;
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private List<City> cityList;

	public CityListScreen() {
		super(Customization.CITY);
		
		cityController.addObserver(this);
		
		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_HOME), Transition.SLIDE);
			}
		});
		
		addButton = new  MyButton(Customization.ADD_CITY);
		addButton.setStyleName("rightButton", true);
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(new AddCity(), Transition.SLIDE);
			}
		});

	    cityList = CityController.getInstance().getCitiesList();
	    addCities(cityList);
	    
	    getHeader().setLeftButton(backButton);
	    getHeader().setRightButton(addButton);
	   
	  	    
//	    addUser = new JQMButton(Customization.ADD_USER, PagesController.getPage(Pages.PAGE_RESTAURANT_MANAGER), Transition.SLIDE);
//	    addUser.setWidth("49%");
//	    addUser.setIcon(DataIcon.PLUS);
//	    addUser.setIconPos(IconPos.TOP);
//	    addUser.setInline();
	  
	
	}
	
	private void addCities(List<City> cities){
		
		java.util.Collections.sort(cities, new Comparator<City>() {

			@Override
			public int compare(City o1, City o2) {
				 return o1.getCity().toLowerCase().compareTo(o2.getCity().toLowerCase());
			}
		});
			
		for(City city: cities){
			final CityInfoScreen cityInfoScreen;
			if(CityController.cityMapView.containsKey(city.getId())){
				cityInfoScreen = CityController.cityMapView.get(city.getId()) ;
			}else{
				cityInfoScreen = new CityInfoScreen(city.getCity());
				CityController.cityMapView.put(city.getId(), cityInfoScreen);
			}
			
			final MyListItem cityItem = new MyListItem();
			cityItem.setText(city.getCity());
			cityItem.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Document.get().getElementById("load").setClassName(R.LOADING);
					JQMContext.changePage(cityInfoScreen);
				}
			});
			cityItem.setStyleName("itemList", true);
			getContentPanel().add(cityItem);
		}
	}
	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		restaurantController.setLastOpenPage(this);
		restaurantController.setLastOpenPage(this);
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
		getContentPanel().clear();
		cityList = cityController.getCitiesList();
	    addCities(cityList);
	}
}
