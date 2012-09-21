package com.veliasystems.menumenu.client.ui;

import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;

public class HomePageScreen extends JQMPage{

	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMList list;
	
	
	public HomePageScreen(){
		header = new JQMHeader(Customization.APPNAME);
		header.setFixed(true);
		header.setText(Customization.MAINTITLE);
		add(header);
	    
		list = new JQMList();
	    list.setInset(false);
	    
	   list.addItem(Customization.CITY, Pages.PAGE_CITY_LIST );
	   list.addItem(Customization.RESTAURANTS, Pages.PAGE_RESTAURANT_LIST );
	   
	   add(list);
	    
	   
	}
}
