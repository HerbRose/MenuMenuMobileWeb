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
	
	public static native Element getElement(String elementId)/*-{	
		return $wnd.document.getElementById(elementId);
	}-*/;
	
	public static native void consolLog(String elementId)/*-{
		$wnd.console.log(elementId);
	}-*/;
}
