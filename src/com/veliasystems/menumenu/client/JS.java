package com.veliasystems.menumenu.client;

import com.google.gwt.dom.client.Element;

public class JS {

	public static native void clickOnInputFile(Element elem) /*-{
		elem.click();
	}-*/;

	public static native int getElementOffsetWidth(Element element)/*-{
		$wnd.console.log(element.offsetWidth);
		return element.offsetWidth;
	}-*/;
}
