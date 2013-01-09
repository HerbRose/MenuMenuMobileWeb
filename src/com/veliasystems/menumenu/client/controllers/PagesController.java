package com.veliasystems.menumenu.client.controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.userInterface.CityInfoScreen;
import com.veliasystems.menumenu.client.userInterface.CityListScreen;

public class PagesController {

	private static Map<Pages, JQMPage> pagesMap = new HashMap<Pages, JQMPage>();
	public static int contentWidth = getBodyOffsetWidth();
	private static CityController cityController = CityController.getInstance();
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
	/**
	 * 
	 * @param cityId the {@link City} id
	 * @return if the city with the given id exists return {@link CityInfoScreen} else return {@link CityListScreen}
	 */
	public static JQMPage getCityInfoScreenPage(Long cityId){
		CityInfoScreen cityInfoScreen = CityController.cityMapView.get(cityId);
		
		if(cityInfoScreen == null){
			City city = cityController.getCity(cityId);
			if(city != null){
				cityInfoScreen = new CityInfoScreen(city);
				CityController.cityMapView.put(cityId, cityInfoScreen);
				return cityInfoScreen;
			}else{
				return getPage(Pages.PAGE_CITY_LIST);
			}
		}
		
		return cityInfoScreen;
	}
	
	/**
	 * get div of name <strong>"load"</strong> and sets display to none
	 */
	public static void showWaitPanel(){
		Document.get().getElementById("load").setClassName(R.LOADING);
	}
	/**
	 *  get div of name <strong>"load"</strong> and sets display to block
	 */
	public static void hideWaitPanel(){
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	private static native int getBodyOffsetWidth()/*-{
		return $wnd.document.body.offsetWidth;
	}-*/;

}

