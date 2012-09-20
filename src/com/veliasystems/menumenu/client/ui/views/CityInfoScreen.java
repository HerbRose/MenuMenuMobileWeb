package com.veliasystems.menumenu.client.ui.views;

import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;

public class CityInfoScreen extends JQMPage {
	
	JQMHeader header;
	JQMFooter footer;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMList list;
	JQMButton backButton;
	
	private void init(){
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		header = new JQMHeader("[Restaurant]");
		header.setBackButton(backButton);
		header.setFixed(true);
		add(header);
	}
	
	public CityInfoScreen() {
		init();
	}
	
	public CityInfoScreen(String city){
		init();
		setHeaderText(city);
	}
	
	public void setHeaderText(String text){
		header.setText(text);
	}
}
