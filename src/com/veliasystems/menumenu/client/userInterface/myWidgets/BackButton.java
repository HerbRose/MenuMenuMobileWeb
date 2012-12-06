package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public class BackButton extends FocusPanel{
	
	private String text = "";
	private Label textLabel = new Label();
	private boolean isRepeatX = false;
	private boolean isRepeatY = false;
	
	
	private FlowPanel mainPanel = new  FlowPanel();
	private FlowPanel arrowPanel = new FlowPanel();
	private FlowPanel labelPanel = new FlowPanel();
	
	public BackButton(String text) {
		setText(text);
		//setStyleName("myButton borderButton" , true);
		//add(textLabel);
		
		setStyleName("noFocus pointer", true);
		mainPanel.setStyleName("mainPanelBackButton noFocus", true);
		arrowPanel.setStyleName("arrowButtonPart noFocus", true);
		labelPanel.setStyleName("labelBackButton noFocus", true);
		
		labelPanel.add(textLabel);
		
		mainPanel.add(arrowPanel);
		mainPanel.add(labelPanel);
		
		add(mainPanel);
	}
	
	public void setText(String text) {
		textLabel.setText(text);
		textLabel.setStyleName("myTextLabel", true);
	}
	
	public void setBackGroundImage(String imageUrl, boolean isRepeatX, boolean isRepeatY, String backgroundColor){
		this.isRepeatX = isRepeatX;
		this.isRepeatY = isRepeatY;
		
		String backgroundStyleProperty = "";
		
		if(imageUrl.equals("")){
			getElement().getStyle().setProperty("background",backgroundStyleProperty);
			return;
		}
		if(backgroundColor == null){
			backgroundColor = "";
		}
		
		if(isRepeatX && isRepeatY){
			backgroundStyleProperty = "url(\""+imageUrl+"\") repeat "+ backgroundColor;
		}else if(isRepeatX){
			backgroundStyleProperty = "url(\""+imageUrl+"\") repeat-X "+ backgroundColor;
		}else if(isRepeatY){
			backgroundStyleProperty = "url(\""+imageUrl+"\") repeat-Y "+ backgroundColor;
		}else{
			backgroundStyleProperty = "url(\""+imageUrl+"\") "+ backgroundColor;
		}
		//getElement().setAttribute("style", "background: "+ backgroundStyleProperty);
		getElement().getStyle().setProperty("background",backgroundStyleProperty);
	}
	
}
