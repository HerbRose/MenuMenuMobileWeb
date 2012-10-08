package com.veliasystems.menumenu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.ui.Pages;
import com.veliasystems.menumenu.client.ui.RestaurantImageView;





public class MenuMenuMobileWeb implements EntryPoint {

	public static boolean loggedIn = false;
	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	public void onModuleLoad() {
	
		//JQMContext.changePage( new UploadRestaurantsScreen());

		
		String logged = Cookies.getCookie(R.loggedIn);
		if (logged!=null) loggedIn = true;
		
		
		if (loggedIn) {
			changePage();
		} else {
			JQMContext.changePage( Pages.PAGE_LOGIN );
		}
		
		//JQMContext.changePage( Pages.PAGE_UPLOAD );
		
				
	}
	
	private void changePage(){
		String lastPage = Cookies.getCookie(R.lastPage);
		
		if(lastPage!=null){
			Long lastPageId;
			
			lastPageId = Long.parseLong(lastPage);
			storeService.loadRestaurant(lastPageId, new AsyncCallback<Restaurant>() {
				
				@Override
				public void onSuccess(Restaurant result) {
					if(result != null){
						RestaurantImageView restaurantView = new RestaurantImageView(result);
						RestaurantController.restMapView.put(result.getId(), restaurantView);
						JQMContext.changePage(Pages.PAGE_HOME);
						JQMContext.changePage(restaurantView, Transition.SLIDE);
					}else{
						JQMContext.changePage(Pages.PAGE_HOME, Transition.SLIDE);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			JQMContext.changePage(Pages.PAGE_HOME, Transition.SLIDE);
		}
	}
}
