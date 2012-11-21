package com.veliasystems.menumenu.client.ui.myWidgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

public class MyHeader extends FlowPanel {

	private FlowPanel malinaPanel = new FlowPanel();
	private FlowPanel headerPanel = new FlowPanel();
	
	private String title = "";
	private Button leftButton;
	private Button rightButton;
	private int headerHeight = 0;
	
	public MyHeader(String title) {
		setStyle(title);
	}
	public MyHeader() {
		setStyle("");
	}
	
	private void setStyle(String title){
		this.title = title;
		setStyleName("myHeader", true);
		
		malinaPanel.setStyleName("malinaPanel", true);
		headerPanel.setStyleName("headerPanel", true);
		add(malinaPanel);
		add(headerPanel);
		
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
	public Button getLeftButton() {
		return leftButton;
	}
	public void setLeftButton(Button leftButton) {
		this.leftButton = leftButton;
	}
	public Button getRightButton() {
		return rightButton;
	}
	public void setRightButton(Button rightButton) {
		this.rightButton = rightButton;
	}
	public int getHeaderHeight() {
		return headerHeight;
	}
	
	
}
