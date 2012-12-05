package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public class MyListItem extends FocusPanel {

	private String value = "";
	private String text = "";
	private int order = -1;
	
	private Label textLabel = new Label();
	
	public MyListItem() {
		add(textLabel);
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getOrder() {
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
