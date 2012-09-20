package com.veliasystems.menumenu.client.ui;

import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;

public class CityListScreen extends JQMPage{
	
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMList list;
	JQMButton backButton;
	
	public CityListScreen() {
		
		header = new JQMHeader(Customization.CITY);
		header.setFixed(true);
		header.setText(Customization.CITY);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header.setBackButton(backButton);
		
		add(header);
		
		list = new JQMList();
	    list.setInset(false);
	    
	    list.addItem("Kraków", new CityInfoScreen("Kraków"));
	    list.addItem("Warszawa", new CityInfoScreen("Warszawa"));
	    
	    add(list);
	    
	}

}
