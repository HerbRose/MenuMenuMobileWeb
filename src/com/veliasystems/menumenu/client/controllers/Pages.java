package com.veliasystems.menumenu.client.controllers;


import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.userInterface.AddRestaurantScreen;
import com.veliasystems.menumenu.client.userInterface.CityListScreen;
import com.veliasystems.menumenu.client.userInterface.RestaurantsListScreen;
import com.veliasystems.menumenu.client.userInterface.RestaurantsManagerScreen;

/**
 * All {@link JQMPage}s all are singletons
 * <br/>
 * Contains {@link JQMPage}:
 * <ul>
 * <li>PAGE_CITY_LIST</li>
 * <li>PAGE_RESTAURANT_LIST</li>
 * <li>PAGE_HOME</li>
 * <li>PAGE_ADD_RESTAURANT</li>
 * <li>PAGE_RESTAURANT_MANAGER</li>
 * </ul>
 *
 */

public enum Pages {
	
	PAGE_CITY_LIST,	
	PAGE_RESTAURANT_LIST,
	PAGE_HOME,
	PAGE_ADD_RESTAURANT,
	/**
	 * @deprecated
	 * use {@link Pages#PAGE_ADMINISTRATION}
	 */
	PAGE_RESTAURANT_MANAGER,
	PAGE_ADMINISTRATION;
	
	
	/**
	 *
	 * @param enumPage
	 * @return added page or null
	 */
	public static JQMPage getMyPage(Pages enumPage) {
		 
		JQMPage page ;
			switch (enumPage) {
			case PAGE_CITY_LIST:
					page = new CityListScreen();
				break;
			case PAGE_HOME:
					page = new CityListScreen();// HomePageScreen();
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
			case PAGE_ADMINISTRATION:
					page = new RestaurantsManagerScreen();
				break;
			default:
				page = null;
				break;
			}
		return page;
	}

}
