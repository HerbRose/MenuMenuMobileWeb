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
	
	/**
	 * set the page only if local page is null
	 * @param enumPage
	 * @return the page or <code>new JQMPage()</code> if enumPage doesn't known
	 */
	public static JQMPage getPage(Pages enumPage){
		if(pagesMap.containsKey(enumPage)){
			return pagesMap.get(enumPage);
		}else{
			
			JQMPage newPage = Pages.setMyPage(enumPage);
			if(newPage != null){
				pagesMap.put(enumPage, newPage);
				return newPage;
			}
			return new JQMPage();
		}
	}

}

