package com.veliasystems.menumenu.client.ui.myWidgets;

import com.google.gwt.user.client.ui.FlowPanel;
import com.sksamuel.jqm4gwt.JQMPage;

public class MyPage extends JQMPage {

	private MyHeader header;
	private FlowPanel contentPanel = new FlowPanel();
	
	public MyPage(String title) {
		header = new MyHeader(title);
		setContent();
	}
	
	public MyPage() {
		header = new MyHeader();
		setContent();
	}
	
	public FlowPanel getContentPanel() {
		return contentPanel;
	}
	
	private void setContent(){
		contentPanel.setStyleName("contentPanel", true);
		add(contentPanel);
	}
	
	public MyHeader getHeader() {
		return header;
	}
	

}
