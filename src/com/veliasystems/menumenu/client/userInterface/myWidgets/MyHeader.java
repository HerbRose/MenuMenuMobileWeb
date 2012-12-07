package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MyHeader extends FlowPanel {

	private FlowPanel malinaPanel = new FlowPanel();
	private FlowPanel headerPanel = new FlowPanel();
	private Label titleLabel = new Label();
	private String title = "";
	private FocusPanel leftButton = new MyButton("");
	private FocusPanel rightButton = new MyButton("");
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
		titleLabel.setStyleName("headerTitle", true);
		
		setTitle(title);
		
		add(malinaPanel);
		add(headerPanel);
		
	}
	
	public void setTitle(String title) {
		headerPanel.remove(titleLabel);
		this.title = title;
		titleLabel.setText(this.title);
		headerPanel.add(titleLabel);
		
	}
	public String getTitle() {
		return title;
	}
	
	public FocusPanel getLeftButton() {
		return leftButton;
	}
	public void setLeftButton(FocusPanel leftButton) {
		headerPanel.remove(this.leftButton);
		this.leftButton = leftButton;
		this.leftButton.setStyleName("headerLeftButton", true);
		headerPanel.add(leftButton);
	}
	public FocusPanel getRightButton() {
		return rightButton;
	}
	public void setRightButton(FocusPanel rightButton) {
		headerPanel.remove(this.rightButton);
		this.rightButton = rightButton;
		headerPanel.add(rightButton);
	}
	public int getHeaderHeight() {
		return headerHeight;
	}
	
	public void addImageHeader(Widget w){
		headerPanel.add(w);
	}
	
	
}
