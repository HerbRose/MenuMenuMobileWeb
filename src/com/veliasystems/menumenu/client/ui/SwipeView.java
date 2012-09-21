package com.veliasystems.menumenu.client.ui;

import com.google.gwt.user.client.ui.Image;
import com.googlecode.mgwt.ui.client.widget.Carousel;

public class SwipeView extends Carousel   {
	

	private void init(){
		add(new Image("/img/article1.jpg"));
		add(new Image("/img/article2.jpg"));
		add(new Image("/img/article3.jpg"));
		
		
	}
	
	public SwipeView() {
		init();
	}
	
	public SwipeView(String city){
		init();
	}

}
