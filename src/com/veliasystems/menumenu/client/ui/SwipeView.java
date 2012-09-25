package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class SwipeView extends FlowPanel {
	
	private List<String> imageUrlList;
	
	private FlowPanel scrollerDiv = new FlowPanel();
	
	public SwipeView( List<String> imageUrlList) {
		if(imageUrlList == null){
			this.imageUrlList = new ArrayList<String>();
		}else{
			this.imageUrlList = imageUrlList;
		}
		configuration("wrapper");
		fillImages();
	}
	
	private void fillImages(){
		
		scrollerDiv.getElement().setId("scroller");
		
		MyImage image;
		
		for (String imageUrl : imageUrlList) {	
			image = new MyImage(imageUrl);
			scrollerDiv.add(new Image(imageUrl));
		}

		//only for test
		MyImage image1;
		image1 = new MyImage("img/article1.jpg");
		
		scrollerDiv.add(image1); 
		
		image1 = new MyImage("img/article2.jpg");
		scrollerDiv.add(image1); 
		
		image1 = new MyImage("img/article3.jpg");
		
		scrollerDiv.add(image1); 
		//END - only for test
		
		add(scrollerDiv);
		//initJavascript();
		
	}
	
	private native void initJavascript() /*-{
	  $wnd.myScroll = new $wnd.iScroll('wrapper');
	}-*/;
	
	private void configuration(String wrapperName){
		
		getElement().setId(wrapperName);
		setWidth("300px");
	}

}
