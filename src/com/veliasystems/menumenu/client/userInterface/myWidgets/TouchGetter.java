package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.veliasystems.menumenu.client.R;

public class TouchGetter extends FocusPanel{

	
	private static HandlerRegistration handler;

	public TouchGetter() {
		setStyleName("hide");
		getElement().setId("touchGetter");
	}
	
	public interface HidePanels{
		void hidePanels();
	}
	
	public void addClickHandler(final HidePanels hidePanels){
		handler = this.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hidePanels.hidePanels();
				hideTouchGetter();
			}
		});

	}
	//should be called every time when is used to popup
	public static void removeMyClickHandler(){
		if(handler != null){
			handler.removeHandler();
		}
		
		
	}
	private void hideTouchGetter(){
		Document.get().getElementById("touchGetter").setClassName(R.HIDE);
	}
	public static void showTouchGetter(boolean isShow){
		if(isShow) {
			Document.get().getElementById("touchGetter").setClassName(R.SHOW);
		}else{
			Document.get().getElementById("touchGetter").setClassName(R.HIDE);
		}
	}
	
}
