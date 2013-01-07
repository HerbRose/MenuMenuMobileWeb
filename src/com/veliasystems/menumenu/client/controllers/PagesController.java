package com.veliasystems.menumenu.client.controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.R;

public class PagesController {

	private static Map<Pages, JQMPage> pagesMap = new HashMap<Pages, JQMPage>();
	public static int contentWidth = getBodyOffsetWidth();
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

