package com.veliasystems.menumenu.client.controllers;

import java.util.HashMap;
import java.util.Map;

import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.ui.AddRestaurantScreen;
import com.veliasystems.menumenu.client.ui.CityListScreen;
import com.veliasystems.menumenu.client.ui.HomePageScreen;
import com.veliasystems.menumenu.client.ui.RestaurantsListScreen;

public class PagesController {

	private static Map<Pages, JQMPage> pagesMap = new HashMap<Pages, JQMPage>();
	
	public static JQMPage getPage(Pages page){
		if(pagesMap.containsKey(page)){
			return pagesMap.get(page);
		}else{
			JQMPage newPage = setMyPage(page);
			System.out.println(page);
			pagesMap.put(page, newPage);
			return newPage;
		}
	}
	
	/**
	 * set the page only if local page is null
	 * @param enumPage
	 * @return added page or null
	 */
	private static JQMPage setMyPage(Pages enumPage) {
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
			default:
				page = null;
				break;
			}
		return page;
	}
}

