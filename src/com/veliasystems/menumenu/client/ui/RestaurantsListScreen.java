package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
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
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class RestaurantsListScreen extends JQMPage {
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
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
	    addButton.setWidth("100%");
	        
	    footer = new JQMFooter(addButton);
	    footer.setFixed(true);
   
	    add(footer);
	        
	  }
	
	private void addRestaurants(List<Restaurant> list){
		
		for(final Restaurant item: list){
		
			RestaurantImageView restaurantView = new RestaurantImageView(item);
			restaurantView.addToContent(new SwipeView("img/article1.jpg", item.getMainImages(), Customization.MAIN_PICTURE ) );
			restaurantView.addToContent(new SwipeView("img/article2.jpg", item.getMainImages(), Customization.PROFILE_PICTURE ) );
			restaurantView.addToContent(new SwipeView("img/article3.jpg", item.getMainImages(), Customization.CARD ) );
//			
//			Image image = new Image("http://www.hauser.lu/uploads/images/SAT_et_TNT/HD/zdf-hd.jpg");
//			CropImage imageToCrop = new CropImage(image);
			this.list.addItem(item.getName(), restaurantView);
		}
	}
	private void showError(){
		Label label = new Label();
		label.setText(Customization.LOADERROR);
		this.add(label);
	}
}
