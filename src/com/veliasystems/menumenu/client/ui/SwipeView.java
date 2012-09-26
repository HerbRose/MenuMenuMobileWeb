package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CustomScrollPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class SwipeView extends FlowPanel {
	
	private List<String> imageUrlList;
	
	private CustomScrollPanel wrapper = new CustomScrollPanel();
	private HorizontalPanel scrollerContainer = new HorizontalPanel();
	
	
	private FlowPanel titleDiv = new FlowPanel();
	private FlowPanel cameraDiv = new FlowPanel();
	
	public SwipeView( List<String> imageUrlList, String title) {
		if(imageUrlList == null){
			this.imageUrlList = new ArrayList<String>();
		}else{
			this.imageUrlList = imageUrlList;
		}
		wrapper.addStyleName("wrapper");
		fillImages();
		addStyleName("swipeView");
		setMyTitle(title);
		setCameraImage();
	}
	
	private void fillImages(){
		
		scrollerContainer.addStyleName("scroller");
		
		MyImage image;
		
		for (String imageUrl : imageUrlList) {	
			image = new MyImage(imageUrl);
			scrollerContainer.add(image);
		}

		//only for test
		MyImage image1;
		image1 = new MyImage("img/article1.jpg");
		scrollerContainer.add(image1); 
		image1 = new MyImage("img/article2.jpg");
		scrollerContainer.add(image1); 
		image1 = new MyImage("img/article3.jpg");
		scrollerContainer.add(image1); 
		image1 = new MyImage("img/article1.jpg");
		scrollerContainer.add(image1); 
		image1 = new MyImage("img/article2.jpg");
		scrollerContainer.add(image1); 
		image1 = new MyImage("img/article3.jpg");
		scrollerContainer.add(image1); 
		//END - only for test
		
		//add(scrollerDiv);
		wrapper.setWidget(scrollerContainer);
		add(wrapper);
	}
	
	private void setMyTitle( String title ) {
		titleDiv.addStyleName("titleDiv");
		Element span = DOM.createSpan();
		span.setInnerText(title);
		
		titleDiv.getElement().insertFirst(span);
		add(titleDiv);
	}
	
	private void setCameraImage(){
		cameraDiv.add(new Image("img/camera.png"));
		cameraDiv.addStyleName("cameraDiv");
		add(cameraDiv);
	}
	
	


}
