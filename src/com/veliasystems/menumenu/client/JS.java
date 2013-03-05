package com.veliasystems.menumenu.client;

import com.google.gwt.dom.client.Element;

public class JS {

	public static native void clickOnInputFile(Element elem) /*-{
		elem.click();
	}-*/;

	public static native int getElementOffsetWidth(Element element)/*-{
		return element.offsetWidth;
	}-*/;
	
	public static native int getWidth(String elementId)/*-{
		return $wnd.document.getElementById(elementId).offsetWidth;
	}-*/;
	
	public static native int getActivePageWidth()/*-{
		var element = $wnd.document.getElementsByClassName("ui-page-active")[0];
		if( element == undefined){
			$wnd.console.log("JS::getActivePageWidth: not found active page");
			return 0;
		}
		
		return element.offsetWidth;
	}-*/;
	
	public static native int getActivePageHeight()/*-{
		var element = $wnd.document.getElementsByClassName("ui-page-active")[0];
		if( element == undefined){
			$wnd.console.log("JS::getActivePageHeight: not found active page");
			return 0;
		}
		
		return element.offsetHeight;
	}-*/;
	
	public static native Element getElement(String elementId)/*-{	
		return $wnd.document.getElementById(elementId);
	}-*/;
	
	public static native int getBodyWidth()/*-{	
		return $wnd.document.body.offsetWidth;
	}-*/;
	
	public static native void setMinWidth(String elementId, int minWidth)/*-{	
		var element = $wnd.document.getElementById(elementId);
		if( element == undefined){
			$wnd.console.log("JS::setMinWidth: not found element by id: " + elementId);
			return;
		}
		element.style.minWidth = minWidth + "px";
	}-*/;
	
	public static native void setMaxWidth(String elementId, int maxWidth)/*-{
		var element = $wnd.document.getElementById(elementId);
		if( element == undefined){
			$wnd.console.log("JS::setMaxWidth: not found element by id: " + elementId);
			return;
		}
		element.style.maxWidth = maxWidth + "px";
//		$wnd.console.log("JS::setMaxWidth: max width set on: " + maxWidth);
	}-*/;
	
	public static native void setMaxHeight(String elementId, int maxHeight)/*-{
		var element = $wnd.document.getElementById(elementId);
		if( element == undefined){
			$wnd.console.log("JS::setMaxHeight: not found element by id: " + elementId);
			return;
		}
		element.style.maxHeight = maxHeight + "px";
	}-*/;
	

	
	
	public static native void consolLog(String elementId)/*-{
		$wnd.console.log("JS::consolLog: " + elementId);
	}-*/;

	public static native String getUserAgent()/*-{
		return navigator.userAgent;
	}-*/;


}
