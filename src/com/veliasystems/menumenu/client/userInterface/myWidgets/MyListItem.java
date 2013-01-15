package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public class MyListItem extends FocusPanel {

	private String value = "";
	private String text = "";
	private long order = -1;
	
	private Label textLabel = new Label();
	
	public MyListItem() {
		add(textLabel);
		setStyleName("pointer", true);
	}
	
	public MyListItem(String text) {
		textLabel.setText(text);
		add(textLabel);
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
}
