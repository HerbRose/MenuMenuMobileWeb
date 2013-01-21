package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class MyListItem extends FocusPanel {

	private String value = "";
	private String text = "";
	private long order = -1;
	
	private FlowPanel myListItem;
	private boolean isChecked = false;
	private Label textLabel = new Label();
	
	private Image checkBoxImage;
	
	public MyListItem() {
		add(textLabel);
		setStyleName("pointer", true);
	}
	
	public MyListItem(String text) {
		textLabel.setText(text);
		add(textLabel);
		setStyleName("pointer", true);
	}
	public MyListItem( boolean isCheckBox, String text){
		textLabel.setText(text);
		myListItem = new FlowPanel();
		myListItem.getElement().getStyle().setPosition(Position.RELATIVE);
		myListItem.add(textLabel);
		
		if(isCheckBox){
			checkBoxImage = new Image("/img/layout/confirme.png");	
			checkBoxImage.addStyleName("checkBoxImage");
			checkBoxImage.setStyleName("hidden", true);
			myListItem.add(checkBoxImage);
		}
			
		add(myListItem);
		setStyleName("pointer", true);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setOrder(long order) {
		this.order = order;
	}
	public long getOrder() {
		return order;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		textLabel.setText(this.text);
	}
	public Label getTextLabel() {
		return textLabel;
	}
	public void check(){
		isChecked = !isChecked;
		if(checkBoxImage != null){
			checkBoxImage.setStyleName("show", isChecked);
			checkBoxImage.setStyleName("hidden", !isChecked);
		}
	}
	
	public boolean value(){
		return isChecked;
	}
}
