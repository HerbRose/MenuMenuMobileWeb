package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.user.client.ui.FocusPanel;

public class MyListItem extends FocusPanel {

	private String value = "";
	private String text = "";
	private int order = -1;
	
	public MyListItem() {
		// TODO Auto-generated constructor stub
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
	}
}
