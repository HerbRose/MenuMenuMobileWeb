package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CustomScrollPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.veliasystems.menumenu.client.controllers.ImagesController;

public class SwipeView extends FlowPanel {
	
	private List<String> imageUrlList;
	
	private CustomScrollPanel wrapper = new CustomScrollPanel();
	private FlowPanel scrollerContainer = new FlowPanel();
	private ImagesController imagesController = new ImagesController();
	
	private FlowPanel titleDiv = new FlowPanel();
	private FlowPanel cameraDiv = new FlowPanel();
	private FlowPanel cameraContainerDiv = new FlowPanel();
	
	public SwipeView(String mainImageUrl, List<String> imageUrlList, String title) {
		if(imageUrlList == null){
			this.imageUrlList = new ArrayList<String>();
		}else{
			this.imageUrlList = imageUrlList;
		}
		wrapper.addStyleName("wrapper");
		
		setCameraImage();
		fillImages(mainImageUrl);
		setMyTitle(title);
		
		addStyleName("swipeView");
		
	}
	
	private void fillImages(String mainImageUrl){
		
		scrollerContainer.addStyleName("scroller");
		
		MyImage image;
		
		//only for test
				MyImage image1;
				
				
				imageUrlList.add("img/article1.jpg"); 
				imageUrlList.add("img/article2.jpg"); 
				imageUrlList.add("img/article3.jpg"); 
				imageUrlList.add("img/article1.jpg"); 
				imageUrlList.add("img/article2.jpg"); 
				imageUrlList.add("img/article3.jpg"); 
				imageUrlList.add("img/article1.jpg"); 
				imageUrlList.add("img/article2.jpg"); 
				imageUrlList.add("img/article3.jpg"); 
				
				//END - only for test
		
		
		
		for (String imageUrl : imageUrlList) {	
			
			image = new MyImage(imageUrl, imagesController);
			if(mainImageUrl.equals(imageUrl)){
				imagesController.selectImage(image);
				scrollerContainer.insert(image, 0);
				continue;
			}
			scrollerContainer.add(image);
		}

		
		
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
		
		cameraContainerDiv.add(cameraDiv);
		cameraContainerDiv.addStyleName("cameraContainerDiv");
		
		add(cameraContainerDiv);
	}
	
	


}
