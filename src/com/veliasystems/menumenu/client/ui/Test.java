package com.veliasystems.menumenu.client.ui;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.sksamuel.jqm4gwt.JQMPage;

public class Test extends JQMPage{

	
	Image image = new Image("/img/emptyBoard.png");
	FlowPanel mainPanel = new FlowPanel();
	
	HTML topPanel = new HTML();
	HTML topHand = new HTML();
	HTML rightPanel = new HTML();
	HTML rightHand = new HTML();
	HTML bottomPanel = new HTML();
	HTML bottomHand = new HTML();
	HTML leftPanel = new HTML();
	HTML leftHand = new HTML();
	
	AbsolutePanel boundaryPanel = new AbsolutePanel();
	AbsolutePanel targetPanel;
	
	int interval = 0;
	public Test() {
		
		
		
		
		mainPanel.add(boundaryPanel);
		
		image.getElement().setId("croapImage");
		mainPanel.setStyleName("mainPanel", true);
		
		topPanel.setStyleName("topPanel", true);
		topPanel.getElement().setId("moveTopPanel");
		topHand.setStyleName("topHand", true);
		topHand.getElement().setId("moveTopHand");
		
		rightPanel.getElement().setId("moveRightPanel");
		rightPanel.setStyleName("rightPanel", true);
		rightHand.setStyleName("rightHand", true);
		rightHand.getElement().setId("moveRightHand");
		
		bottomPanel.setStyleName("bottomPanel", true);
		bottomPanel.getElement().setId("moveBottomPanel");
		bottomHand.setStyleName("bottomHand", true);
		bottomHand.getElement().setId("moveBottomHand");
		
		leftPanel.setStyleName("leftPanel", true);
		leftPanel.getElement().setId("moveLeftPanel");
		leftHand.setStyleName("leftHand", true);
		leftHand.getElement().setId("moveLeftHand");
		
		
		add(mainPanel);
		
		boundaryPanel.setWidth("239px");
		boundaryPanel.setHeight("319px");
		boundaryPanel.add(image);
		boundaryPanel.add(topPanel);
		boundaryPanel.add(topHand);
		boundaryPanel.add(rightPanel);
		boundaryPanel.add(rightHand);
		boundaryPanel.add(bottomPanel);
		boundaryPanel.add(bottomHand);
		boundaryPanel.add(leftPanel);
		boundaryPanel.add(leftHand);
		PickupDragController dragController = new PickupDragController(boundaryPanel, true);
		
		
		DropController dropController = new AbsolutePositionDropController(boundaryPanel);
		
		
		dragController.registerDropController(dropController);
		
		dragController.makeDraggable(rightHand);
		dragController.makeDraggable(topHand);
		dragController.makeDraggable(leftHand);
		dragController.makeDraggable(bottomHand);
		
		
		
		dragController.addDragHandler(new DragHandler() {
			
			@Override
			public void onPreviewDragStart(DragStartEvent event)
					throws VetoDragException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
				if(interval > 0) stopInterval(interval);
				
			}
			
			@Override
			public void onDragStart(DragStartEvent event) {
				
				Object eventSource = event.getSource();
				
				if(! (eventSource instanceof HTML)) return;//dziwne???
				
				HTML element = (HTML) eventSource;
				
				interval = myOnMove(element.getElement().getId());

			}
			
			@Override
			public void onDragEnd(DragEndEvent event) {
				// TODO Auto-generated method stub
			}
			
			
		});
	}
	
	private native int myOnMove(String elementId)/*-{
		var element = $wnd.document.getElementById(elementId);
		if(element === "undefined") return -1;
		
		var image = $wnd.document.getElementById("croapImage");
		
		var width = image.width;
		var height = image.height;
		
		
		$wnd.console.log(width);
		$wnd.console.log(height);
		
		//var positionStart1 = element.parentNode.style.left;
		//positionStart = positionStart1.substring(0, positionStart1.length-2); //wyrzucamy koncowke 'px'
		var interval = -1;
		if(elementId.indexOf("Top") >=0){
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.top;
				//currentPosition=currentPosition.substring(0, currentPosition1.length-2);
				$wnd.document.getElementById("moveTopPanel").style.height = currentPosition ;
			},500);
		}else if(elementId.indexOf("Right") >=0){
			interval = $wnd.setInterval(function(){
				var currentPosition1 = element.parentNode.style.left;
				currentPosition=currentPosition1.substring(0, currentPosition1.length-2);
				$wnd.document.getElementById("moveRightPanel").style.width = width - currentPosition - 10 + "px";
			},500);
		}else if(elementId.indexOf("Bottom") >=0){
			interval = $wnd.setInterval(function(){
				var currentPosition1 = element.parentNode.style.top;
				currentPosition=currentPosition1.substring(0, currentPosition1.length-2);
				$wnd.document.getElementById("moveBottomPanel").style.height = height - currentPosition - 10 + "px";
			},500);
		}else if(elementId.indexOf("Left") >=0){
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.left;
				//currentPosition=currentPosition.substring(0, currentPosition1.length-2);
				$wnd.document.getElementById("moveLeftPanel").style.width = currentPosition ;
			},500);
		}

		return interval;
		
	 }-*/;
	
	private native void stopInterval(int interval)/*-{
		$wnd.clearInterval(interval);
	}-*/ ;
}
