package com.veliasystems.menumenu.client.userInterface.myWidgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.PagesController;

enum PopUpType{
	ERROR (Customization.POP_UP_ERROR_HEADER, R.ERROR_IMAGE_URL, Customization.OK, ""),
	CONFIRM (Customization.POP_UP_CONFIRM_HEADER, R.CONFIRM_IMAGE_URL, Customization.OK, Customization.CANCEL),
	SUCCESS (Customization.POP_UP_SUCCESS_HEADER, R.SUCCESS_IMAGE_URL, Customization.OK, ""),
	WARNING (Customization.POP_UP_WARNING_HEADER, R.WARNING_IMAGE_URL, Customization.OK, Customization.CANCEL);
	
	private final String name;
	private final String imageSrc;
	private final String leftButtonText;
	private final String rightButtonText;
	
	PopUpType(String name, String imageSrc, String leftButtonText, String rightButtonText){
		this.name = name;
		this.imageSrc = imageSrc;
		this.leftButtonText = leftButtonText;
		this.rightButtonText = rightButtonText;
	}
	
	public String enumName(){
		return name;
	}
	public String imageSrc(){
		return imageSrc;
	}
	public String leftButtonText(){
		return leftButtonText;
	}
	public String rightButtonText(){
		return rightButtonText;
	}
}



public class MyPopUp extends FlowPanel{

	public interface IMyAnswer{
		
		public void answer(Boolean answer);
	}
	private FlowPanel headerPanel;
	private FlowPanel contentPanel;
	private FlowPanel footerPanel;
	
	private MyButton confirmButtonPanel;
	private MyButton cancelButtonPanel;
	
	private TouchGetter touchGetter = PagesController.TOUCH_GETTER;
	
	public MyPopUp() {
		getElement().setId("myPopUp");
		headerPanel = new FlowPanel();
		contentPanel = new FlowPanel();
		footerPanel = new FlowPanel();
		
		headerPanel.getElement().setId("myPopUpHeaderPanel");
		contentPanel.getElement().setId("myPopUpContentPanel");
		footerPanel.getElement().setId("myPopUpFooterPanel");
		
		add(headerPanel);
		add(contentPanel);
		add(footerPanel);
		
		clearMe();
	}

	private void clearMe() {
		headerPanel.clear();
		contentPanel.clear();
		footerPanel.clear();
		
		setStyleName("hide", true);
		setStyleName("show", false);
	}

	public void setPopUpLook(PopUpType error, List<Widget> footerContent) {
		headerPanel.add(new Label(error.enumName()));
		headerPanel.add(new Image(error.imageSrc()));
		
		for (Widget widget : footerContent) {
			footerPanel.add(widget);
		}
		
		setStyleName("show", true);
		setStyleName("hide", false);
		JS.setMaxWidth(getElement().getId(), JS.getActivePageWidth()-40);
		JS.setMaxHeight(getElement().getId(), JS.getActivePageHeight()-200);
		JS.setMaxHeight(contentPanel.getElement().getId(), JS.getActivePageHeight()-200-134);
		
		int windowWidth = JS.getBodyWidth();
		int activePageWidth = JS.getActivePageWidth();
		int myWidth = JS.getElementOffsetWidth(getElement());
		if(windowWidth > activePageWidth){ //probably PC (or tablet)
			getElement().getStyle().setLeft( (windowWidth - myWidth)/2, Unit.PX) ;
		}else{//probably phone
			getElement().getStyle().setLeft( (activePageWidth - myWidth)/2, Unit.PX) ;
		}
		
		getElement().getStyle().setTop(100, Unit.PX) ;
		getElement().getStyle().setZIndex(9999);
		
		TouchGetter.removeMyClickHandler();
		TouchGetter.showTouchGetter(true);
		
	}
	
	public void showError( Widget content, IMyAnswer answer){
		clearMe();		
		
		createConfirmButton( answer);
		
		List<Widget> footerWidgetList = new ArrayList<Widget>();
		footerWidgetList.add(confirmButtonPanel);
		
		contentPanel.add(content);
		setPopUpLook(PopUpType.ERROR, footerWidgetList);
	}


	public void showConfirm( Widget content , IMyAnswer answer){
		clearMe();
		
		createConfirmButton(answer);
		creataCancelButton(answer);
		
		List<Widget> footerWidgetList = new ArrayList<Widget>();
		footerWidgetList.add(confirmButtonPanel);
		footerWidgetList.add(cancelButtonPanel);
		contentPanel.add(content);
		setPopUpLook(PopUpType.CONFIRM, footerWidgetList);
	}
	public void showWarning( Widget content, IMyAnswer answer){
		clearMe();
		
		createConfirmButton(answer);
		
		List<Widget> footerWidgetList = new ArrayList<Widget>();
		footerWidgetList.add(confirmButtonPanel);
		
		contentPanel.add(content);
		setPopUpLook(PopUpType.WARNING, footerWidgetList);
		
	}
	public void showSuccess( Widget content, IMyAnswer answer){
		clearMe();
		
		createConfirmButton(answer);
		
		List<Widget> footerWidgetList = new ArrayList<Widget>();
		footerWidgetList.add(confirmButtonPanel);
		
		contentPanel.add(content);
		setPopUpLook(PopUpType.SUCCESS, footerWidgetList);
		
	}
	
	private void createConfirmButton(final IMyAnswer answer){
		confirmButtonPanel = new MyButton(Customization.OK);
		confirmButtonPanel.getElement().setId("confirmButtonPanel");
		confirmButtonPanel.getElement().setId("myPopUpConfirmButtonPanel");
		confirmButtonPanel.setBackGroundImage("img/layout/okButton.png", true, false, "#919191");
		
		confirmButtonPanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				answer.answer(true);
				clearMe();
				TouchGetter.showTouchGetter(false);
			}
		});
	}
	
	private void creataCancelButton(final IMyAnswer answer){
		cancelButtonPanel = new MyButton(Customization.CANCEL);
		cancelButtonPanel.getElement().setId("confirmButtonPanel");
		cancelButtonPanel.getElement().setId("myPopUpCancelButtonPanel");
		cancelButtonPanel.setBackGroundImage("img/layout/anulujButton.png", true, false, "#919191");
		
		cancelButtonPanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				answer.answer(false);
				clearMe();
				TouchGetter.showTouchGetter(false);
			}
		});
	}
}
