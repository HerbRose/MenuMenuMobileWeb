package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public class SwipeView extends ScrollPanel   {
	

	private List<String> imageUrlList;
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel hPanel = new HorizontalPanel();
	
	private void init(){
		
		for (String imageUrl : imageUrlList) {
			hPanel.add(new Image(imageUrl));	
		}
		
		vPanel.add(hPanel);
		
		vPanel.addStyleName("myPanel");
		
		addStyleName("myScrollPanel");
		add(vPanel);
		
		refresh();
	}
	
	public SwipeView( List<String> imageUrlList) {
		if(imageUrlList == null){
			this.imageUrlList = new ArrayList<String>();
		}else{
			this.imageUrlList = imageUrlList;
		}
		init();
	}
	
}
