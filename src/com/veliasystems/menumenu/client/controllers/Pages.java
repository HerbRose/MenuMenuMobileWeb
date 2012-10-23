package com.veliasystems.menumenu.client.controllers;

import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.ui.AddRestaurantScreen;
import com.veliasystems.menumenu.client.ui.CityListScreen;
import com.veliasystems.menumenu.client.ui.HomePageScreen;
import com.veliasystems.menumenu.client.ui.RestaurantsManagerScreen;
import com.veliasystems.menumenu.client.ui.RestaurantsListScreen;


public enum Pages {

	PAGE_CITY_LIST,	
	PAGE_RESTAURANT_LIST,
	PAGE_HOME,
	PAGE_ADD_RESTAURANT,
	PAGE_RESTAURANT_MANAGER;
	
	
	/**
	 * set the page only if local page is null
	 * @param enumPage
	 * @return added page or null
	 */
	public static JQMPage setMyPage(Pages enumPage) {
		 
		JQMPage page ;
			switch (enumPage) {
			case PAGE_CITY_LIST:
				page = new CityListScreen(); 
				break;
			case PAGE_HOME:
				page = new HomePageScreen();
				break;
			case PAGE_RESTAURANT_LIST:
				page = new RestaurantsListScreen();
				break;
			case PAGE_ADD_RESTAURANT:
				page = new AddRestaurantScreen();
				break;
			case PAGE_RESTAURANT_MANAGER:
				page = new RestaurantsManagerScreen();
				break;
			default:
				page = null;
				break;
			}
		return page;
	}

}
