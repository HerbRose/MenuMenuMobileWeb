package com.veliasystems.menumenu.client.userInterface.myWidgets;


import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public class MyButton extends FocusPanel {

	private String text = "";
	private Label textLabel = new Label();
	private boolean isRepeatX = false;
	private boolean isRepeatY = false;
	
	
	public MyButton(String text) {
		setText(text);
		setStyleName("myButton borderButton noFocus" , true);
		add(textLabel);
	}
	
	/**
	 * 
	 * @param image - image to display
	 * @param repeatX - The background image will be repeated horizontally 
	 * @param repeatY - The background image will be repeated vertically
	 * @param backgroundColor - The background color
	 */
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

	public void setText(String text) {
		textLabel.setText(text);
		textLabel.setStyleName("myTextLabel", true);
	}

}
