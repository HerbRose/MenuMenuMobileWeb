package com.veliasystems.menumenu.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;

public class MyImage extends Image {

	
	
	public MyImage(String url) {
	
		setUrl(url);
		setStyleName("image");
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("clicked: " + getUrl());
				System.out.println("clicked");
				
			}
		});
	}
	
	
}
