package com.veliasystems.menumenu.client.userInterface;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListItem;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;




public class CityListScreen extends MyPage implements IObserver{
	
	
	private MyButton addButton;
	private MyButton adminPanel = new MyButton("");
	private BackButton logoutButton;
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private UserController userController = UserController.getInstance();
	private List<City> cityList;

	private FlowPanel adminPanelWrapper = new FlowPanel();
	private FocusPanel adminLabel = new FocusPanel();
	
	public CityListScreen() {
		super(Customization.CITY);
		
		cityController.addObserver(this);
		
		logoutButton = new BackButton(Customization.LOGOUT);
		logoutButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm(Customization.ARE_YOU_SURE_WANT_LOGOUT)){
					JQMContext.changePage(new LogoutScreen(), Transition.SLIDE);
				}
			}
		});
		
		addButton = new  MyButton("");
		addButton.removeStyleName("borderButton");
		addButton.setStyleName("rightButton addButton", true);
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(new AddCity(), Transition.SLIDE);
			}
		});

		
	    cityList = CityController.getInstance().getCitiesList();
	    addCities(cityList);
	    	       
	    getHeader().setLeftButton(logoutButton);
	    getHeader().setRightButton(addButton);
	    
	    if(userController.getLoggedUser().isAdmin()){
	    	
	    	adminPanel.removeStyleName("borderButton");
	    	adminPanel.addStyleName("addButton adminButton");
	    	adminPanel.getElement().getStyle().setHeight(50, Unit.PX);
	 	    adminPanel.addClickHandler(new ClickHandler() {
	 			
	 			@Override
	 			public void onClick(ClickEvent event) {
	 				Document.get().getElementById("load").setClassName(R.LOADING);
	 				JQMContext.changePage(PagesController.getPage(Pages.PAGE_RESTAURANT_MANAGER), Transition.SLIDE);	
	 			}
	 		});
	    	adminLabel.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Document.get().getElementById("load").setClassName(R.LOADING);
					JQMContext.changePage(PagesController.getPage(Pages.PAGE_RESTAURANT_MANAGER), Transition.SLIDE);	
				}
			});
	    	adminLabel.addStyleName("adminLabel noFocus");
	    	
	    	adminLabel.add(new Label(Customization.ADMIN_PANEL));
	 	    
	 	    
	    	
	    	adminPanelWrapper.addStyleName("adminWrapper");

	    	adminPanelWrapper.add(adminPanel);
	    	adminPanelWrapper.add(adminLabel);
	    	getContentPanel().add(adminPanelWrapper);
	    }  

	 
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
					PagesController.showWaitPanel();
					JQMContext.changePage(cityInfoScreen);
				}
			});
			cityItem.setStyleName("itemList noFocus", true);
			getContentPanel().add(cityItem);
		}
	}
	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		restaurantController.setLastOpenPage(this);
		if(Cookies.getCookie(R.LAST_PAGE) != null){
			Cookies.removeCookie(R.LAST_PAGE);
		}
		
		if(userController.getLoggedUser().isAdmin()){
			getContentPanel().add(adminPanelWrapper);
		}
		PagesController.contentWidth = getOffsetWidth(getContent().getElement());
		PagesController.hideWaitPanel();
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
	
	private native int getOffsetWidth(Element element)/*-{
		$wnd.console.log(element);
		return element.offsetWidth;
	}-*/;
}
