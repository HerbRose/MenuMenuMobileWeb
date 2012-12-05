package com.veliasystems.menumenu.client.userInterface;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.ui.RestaurantImageView;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListItem;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;

public class RestaurantsListScreen extends MyPage implements IObserver {
	  
//	JQMHeader header;
//	JQMFooter footer;
//	JQMButton addButton;
//	JQMButton uploadButton;

//	JQMList restaurantList = new JQMList();
	
//	JQMButton backButton;
	
	private BackButton backButton;
	private MyButton addButton;

	private List<Restaurant> restaurants;
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private boolean loaded = false;
	
	public RestaurantsListScreen() {
		super(Customization.RESTAURANTS);
		
		
		//restaurantController.addObserver(this);

		backButton = new BackButton(Customization.BACK);
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_HOME), Transition.SLIDE);
			}
		});
		
		addButton = new  MyButton(Customization.ADDRESTAURANT);
		addButton.setStyleName("rightButton", true);
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				JQMContext.changePage(PagesController.getPage(Pages.PAGE_ADD_RESTAURANT));
			}
		});
		
		
		
		
		getHeader().setRightButton(addButton);
		getHeader().setLeftButton(backButton);
		restaurants = restaurantController.getRestaurantsList();
		addRestaurants(restaurants);
		
		
		//getContentPanel().add(backButton);
	    

	    //add(restaurantList);

//	    
//	    addButton = new JQMButton(Customization.ADDRESTAURANT, PagesController.getPage(Pages.PAGE_ADD_RESTAURANT) );
//	    addButton.setIcon(DataIcon.PLUS);
//	    addButton.setIconPos(IconPos.TOP);
//	    addButton.setTransition(Transition.SLIDE);
//	    addButton.setWidth("49%");
//	    addButton.setInline();
//	    uploadButton = new JQMButton(Customization.UPLOAD, new UploadRestaurantsScreen());
//	    uploadButton.setIcon(DataIcon.FORWARD);
//	    uploadButton.setIconPos(IconPos.TOP);
//	    uploadButton.setTransition(Transition.SLIDE);
//	    uploadButton.setWidth("49%");
//	    uploadButton.setInline();
//	        
//	    footer = new JQMFooter(addButton);
//	    footer.add(uploadButton);
//	    footer.setFixed(true);
   
	   // add(footer);
	        
	  }
	
	private void addRestaurants(List<Restaurant> list){
		
		java.util.Collections.sort(list, new Comparator<Restaurant>() {

			@Override
			public int compare(Restaurant o1, Restaurant o2) {
				 return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
			}
		});
		for(Restaurant item: list){
			final RestaurantImageView restaurantView;
			if(RestaurantController.restMapView.containsKey(item.getId())){
				restaurantView = RestaurantController.restMapView.get(item.getId());	
			}
			else{
				restaurantView = new RestaurantImageView(item, this);
				RestaurantController.restMapView.put(item.getId(), restaurantView);				
			}

			//this.restaurantList.addItem(item.getName(), restaurantView);
			final MyListItem restaurantItem = new MyListItem();
			restaurantItem.setText(item.getName());
			restaurantItem.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Document.get().getElementById("load").setClassName(R.LOADING);
					JQMContext.changePage(restaurantView);
				}
			});
			restaurantItem.setStyleName("itemList", true);
			getContentPanel().add(restaurantItem);

		}
	}
	private void showError(){
		Label label = new Label();
		label.setText(Customization.LOADERROR);
		this.add(label);
	}
	@Override
	protected void onPageShow() {
//		super.onPageShow();
//		if(Cookies.getCookie(R.LAST_PAGE) != null){
//			Cookies.removeCookie(R.LAST_PAGE);
//		}
//		
//		
//		if(!loaded){
//			
////			backButton = new JQMButton("" , PagesController.getPage(Pages.PAGE_HOME), Transition.SLIDE);
////			
////			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
////			backButton.setIcon(DataIcon.LEFT);
////			backButton.setIconPos(IconPos.LEFT);
////			
////			backButton.getElement().setInnerHTML(span);
////			backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");
////			
//		//	header.add(backButton);
//			loaded = true;
//		}
//		
//		restaurantController.setLastOpenPage(this);
//	
//		refreshRestaurantList();
//
//		Cookies.removeCookie(R.LAST_PAGE);
//		
		Document.get().getElementById("load").setClassName(R.LOADED);
	}

	@Override
	public void onChange() {
		
	}

	private void refreshRestaurantList() {
		
//		restaurantList.clear();
//		addRestaurants(restaurantController.getRestaurantsList());
//		restaurantList.refresh();
		
	}
	

}
