package com.veliasystems.menumenu.client.ui;

import com.google.gwt.dom.client.Document;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RollPageView extends JQMPage{
	
	private Restaurant restaurant;
	
	JQMHeader header;
	JQMPanel panel;
	
	JQMButton backButton;
	
	private void init(){
		
		backButton = new JQMButton(Customization.BACK);
		//backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header = new JQMHeader(restaurant.getName());
		header.setBackButton(backButton);
		header.setFixed(true);
		this.add(header);

	
		
	}
	
	public RollPageView() {
		// TODO Auto-generated constructor stub
		init();
	}
	public RollPageView(Restaurant restaurant){
		restaurant = new Restaurant();
		this.restaurant = restaurant;
		init();
	}
	
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		super.onPageShow();
		Document.get().getElementById("load").setClassName(R.LOADED);
	}

}
