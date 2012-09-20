package com.veliasystems.menumenu.client.ui;

import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;

public class RestInfoScreen extends JQMPage {
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMList list;
	JQMButton backButton;
	
	
	private void init() {
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header = new JQMHeader("[Restaurant]");
		header.setBackButton(backButton);
		header.setFixed(true);
		add(header);
	    
	    removeButton = new JQMButton(Customization.REMOVEPROFILE);
	    removeButton.setIcon(DataIcon.DELETE);
	    removeButton.setIconPos(IconPos.TOP);
	    removeButton.setWidth("50%");
	    
	    saveButton = new JQMButton(Customization.SAVEPROFILE);
	    saveButton.setIcon(DataIcon.CHECK);
	    saveButton.setIconPos(IconPos.TOP);
	    saveButton.setWidth("49%");
	    
	    footer = new JQMFooter(saveButton, removeButton);
	    footer.setFixed(true);
	    
	    add(footer);
	}
	
	
	
	public RestInfoScreen() {
	    init();
	  }
	
	
	public RestInfoScreen( String text ) {
		init();
		setHeaderText(text);
	}
	
	
	public void setHeaderText( String text ) {
		header.setText(text);
	}
	
	
}
