package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
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
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class RestaurantsListScreen extends JQMPage {
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMButton uploadButton;
	JQMList list = new JQMList();
	JQMButton backButton;
	
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	public RestaurantsListScreen() {
	    
		header = new JQMHeader(Customization.RESTAURANTS);
		header.setFixed(true);
		header.setText(Customization.RESTAURANTS);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header.setBackButton(backButton);
		add(header);
	    
	    storeService.loadRestaurants(new AsyncCallback<List<Restaurant>>() {
			
			@Override
			public void onSuccess(List<Restaurant> result) {
				// TODO Auto-generated method stub
				addRestaurants(result);			
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				showError();
			}
		});
	    
	    add(list);
	    
	    addButton = new JQMButton(Customization.ADDRESTAURANT, Pages.PAGE_ADD_RESTAURANT);
	    addButton.setIcon(DataIcon.PLUS);
	    addButton.setIconPos(IconPos.TOP);
	    addButton.setTransition(Transition.SLIDE);
	    addButton.setWidth("49%");
	    addButton.setInline();
	    uploadButton = new JQMButton(Customization.UPLOAD, new UploadRestaurantsScreen());
	    uploadButton.setIcon(DataIcon.FORWARD);
	    uploadButton.setIconPos(IconPos.TOP);
	    uploadButton.setTransition(Transition.SLIDE);
	    uploadButton.setWidth("49%");
	    uploadButton.setInline();
	        
	    footer = new JQMFooter(addButton);
	    footer.add(uploadButton);
	    footer.setFixed(true);
   
	    add(footer);
	        
	  }
	
	private void addRestaurants(List<Restaurant> list){
		
		for(final Restaurant item: list){
			RestaurantImageView restaurantView;
			if(RestaurantController.restMapView.containsKey(item.getId())){
				restaurantView = RestaurantController.restMapView.get(item.getId());	
			}
			else{
				restaurantView = new RestaurantImageView(item);
				RestaurantController.restMapView.put(item.getId(), restaurantView);
				
			}
			
			this.list.addItem(item.getName(), restaurantView);
		}
	}
	private void showError(){
		Label label = new Label();
		label.setText(Customization.LOADERROR);
		this.add(label);
	}
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		super.onPageShow();
		if(Cookies.getCookie(R.lastPage) != null){
			Cookies.removeCookie(R.lastPage);
		}
	}
}
