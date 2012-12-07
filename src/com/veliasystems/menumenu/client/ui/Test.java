package com.veliasystems.menumenu.client.ui;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.sksamuel.jqm4gwt.JQMPage;
import com.veliasystems.menumenu.client.entities.ImageBlob;

public class Test extends JQMPage{

	
	private Image image;
	private Long restaurantId;
	private ImageBlob imageBlob;
	private FlowPanel mainPanel = new FlowPanel();
	
	private int displayWidth;
	private int displayHeight;
	
	private HTML topPanel = new HTML();
	private HTML topHand = new HTML();
	private HTML rightPanel = new HTML();
	private HTML rightHand = new HTML();
	private HTML bottomPanel = new HTML();
	private HTML bottomHand = new HTML();
	private HTML leftPanel = new HTML();
	private HTML leftHand = new HTML();
	private HTML centerMovePanel = new HTML(); 
	
	private AbsolutePanel boundaryPanel = new AbsolutePanel();
	private AbsolutePanel targetPanel;
	
	private double ratioProfile = 450.0/280.0;
	private double minHeightLogo = 55;
	private double maxHeightLogo = 75;
	
	int interval = 0;
	public Test(ImageBlob imageInsert) {
		imageBlob = imageInsert;
	
		restaurantId = Long.parseLong(imageBlob.getRestaurantId());
		
		image = new Image(imageBlob.getImageUrl());
	
		image.getElement().setId("croapImage");
		mainPanel.setStyleName("mainPanel", true);
		mainPanel.getElement().setId("cropMainPanel");
		
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
		
		centerMovePanel.setStyleName("centerMovePanel", true);
		centerMovePanel.getElement().setId("centerMovePanel");
		
		switch (imageInsert.getImageType()) {
		case MENU:
			displayWidth = 220;
			break;
		case LOGO:
			displayWidth = 220;
			break;
		case PROFILE:
			displayWidth = 450;
		}
		image.setWidth(displayWidth + "px");
		mainPanel.add(boundaryPanel);
		
		boundaryPanel.add(image);
		boundaryPanel.add(topPanel);
		boundaryPanel.add(topHand);
		boundaryPanel.add(rightPanel);
		boundaryPanel.add(rightHand);
		boundaryPanel.add(bottomPanel);
		boundaryPanel.add(bottomHand);
		boundaryPanel.add(leftPanel);
		boundaryPanel.add(leftHand);
		boundaryPanel.add(centerMovePanel);
		
		add(mainPanel);
		
		PickupDragController dragController = new PickupDragController(boundaryPanel, true);
		DropController dropController = new AbsolutePositionDropController(boundaryPanel);

		dragController.registerDropController(dropController);		
		dragController.makeDraggable(rightHand);
		dragController.makeDraggable(topHand);
		dragController.makeDraggable(leftHand);
		dragController.makeDraggable(bottomHand);
		dragController.makeDraggable(centerMovePanel);

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
				
				if(element.getElement().getId().equals(centerMovePanel.getElement().getId())){
					interval = myOnMoveCenter(element.getElement().getId(),getWidth(centerMovePanel.getElement().getId()), getHeight(centerMovePanel.getElement().getId()) );
					return;
				}
				
				switch (imageBlob.getImageType()) {
				case LOGO:
					
					interval = myOnMoveLogo(element.getElement().getId(), (int) minHeightLogo, (int) maxHeightLogo);
					break;

				default:
					break;
				}
				
				
			}
			
			@Override
			public void onDragEnd(DragEndEvent event) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		
		boundaryPanel.setWidth(displayWidth + "px");
		boundaryPanel.setHeight(getHeight(image.getElement().getId()) + "px");
		
		centerMovePanel.setWidth("100px");
		centerMovePanel.setHeight("60px");
		
		int height = getHeight(image.getElement().getId());
		
		int topPanelHeight = (height - getHeight(centerMovePanel.getElement().getId())) /2;
		int bottomPanelHeight = height - topPanelHeight - getHeight(centerMovePanel.getElement().getId());
		
		int leftPanelWidth = (displayWidth - getWidth(centerMovePanel.getElement().getId()) )/2;
		int rightPanelWidth = displayWidth - leftPanelWidth - getWidth(centerMovePanel.getElement().getId());
		
		topPanel.setHeight( topPanelHeight + "px");
		leftPanel.setWidth(leftPanelWidth +"px");
		bottomPanel.setHeight(bottomPanelHeight + "px");
		rightPanel.setWidth(rightPanelWidth + "px");
		centerMovePanel.getElement().getStyle().setTop(topPanelHeight, Unit.PX);
		centerMovePanel.getElement().getStyle().setLeft(leftPanelWidth, Unit.PX);
	}
	
	private native int myOnMoveLogo(String elementId, int minHeightLogo, int maxHeightLogo)/*-{
		var element = $wnd.document.getElementById(elementId);

		if(element === "undefined") return -1;
		
		var image = $wnd.document.getElementById("croapImage");
		
		var width = image.width;
		var height = image.height;

		var interval = -1;
		if(elementId.indexOf("Top") >=0 ){ // jeżeli strzałka w górę
			var position;
			
			$wnd.setTimeout(function(){
			position = element.parentNode.style.top; //y
			position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 500);
			
			var bottomPanelHeight = $wnd.document.getElementById("moveBottomPanel").offsetHeight;
			$wnd.document.getElementById("moveTopPanel").className = "topPanel moveChecked" ;
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.top; //y
				currentPosition = currentPosition.substring(0, currentPosition.length-2); //wyrzucamy koncowke 'px'
				var deltaPosition = currentPosition - position; //delta y
				
				var moveTopPanelHeight = $wnd.document.getElementById("moveTopPanel").style.height;
				moveTopPanelHeight = moveTopPanelHeight.substring(0, moveTopPanelHeight.length-2); //wyrzucamy koncowke 'px'
				
				var moveCenterPaneHeight = $wnd.document.getElementById("centerMovePanel").offsetHeight;
				
				//$wnd.console.log("position: "+position +" currentPosition: "+currentPosition + " deltaPosition: " +deltaPosition );
				if(deltaPosition < 0 && Math.abs(deltaPosition) + moveCenterPaneHeight <= maxHeightLogo ){
					
					var centerMovePanelCurrentPosition = $wnd.document.getElementById("centerMovePanel").style.top;
					centerMovePanelCurrentPosition = centerMovePanelCurrentPosition.substring(0, centerMovePanelCurrentPosition.length-2); //wyrzucamy koncowke 'px'
					
					var newMoveTopPanelHeight = moveTopPanelHeight - Math.abs(deltaPosition);
					
					var newCenterMovePanelHeight = height - newMoveTopPanelHeight - bottomPanelHeight;
					
					$wnd.console.log("newCenterMovePanelHeight" + newCenterMovePanelHeight);
					$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px";
					$wnd.document.getElementById("centerMovePanel").style.top = centerMovePanelCurrentPosition - Math.abs(deltaPosition) + "px";
					$wnd.document.getElementById("moveTopPanel").style.height = newMoveTopPanelHeight + "px";
					
					
				}else if(deltaPosition > 0 && moveCenterPaneHeight - deltaPosition >= minHeightLogo ){
					
					var centerMovePanelCurrentPosition = $wnd.document.getElementById("centerMovePanel").style.top;
					centerMovePanelCurrentPosition = centerMovePanelCurrentPosition.substring(0, centerMovePanelCurrentPosition.length-2); //wyrzucamy koncowke 'px'
					var newMoveTopPanelHeight = parseInt(moveTopPanelHeight) + deltaPosition;
					
					var newCenterMovePanelHeight = height - newMoveTopPanelHeight - bottomPanelHeight;
					
					$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px"; //deltaPosition > 0 
					$wnd.document.getElementById("centerMovePanel").style.top = parseInt(centerMovePanelCurrentPosition) + deltaPosition + "px";
					$wnd.document.getElementById("moveTopPanel").style.height = newMoveTopPanelHeight + "px";
					
					
				}
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}else if(elementId.indexOf("Right") >=0){
			interval = $wnd.setInterval(function(){
				var currentPosition1 = element.parentNode.style.left;
				currentPosition=currentPosition1.substring(0, currentPosition1.length-2);
				$wnd.document.getElementById("moveRightPanel").style.width = width - currentPosition - 10 + "px";
			},500);
		}else if(elementId.indexOf("Bottom") >=0){
			var position;
			
			$wnd.setTimeout(function(){ //czekamy aż drag stworzy swoje div'y
			position = element.parentNode.style.top; //y
			position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 500);
			
			var moveTopPanelHeight = $wnd.document.getElementById("moveTopPanel").offsetHeight;
			
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.top; //y
				currentPosition = currentPosition.substring(0, currentPosition.length-2); //wyrzucamy koncowke 'px'
				var deltaPosition = position - currentPosition; //delta y
				
				var moveBottomPanelHeight = $wnd.document.getElementById("moveBottomPanel").style.height;
				moveBottomPanelHeight = moveBottomPanelHeight.substring(0, moveBottomPanelHeight.length-2); //wyrzucamy koncowke 'px'
				
				var moveCenterPaneHeight = $wnd.document.getElementById("centerMovePanel").offsetHeight;
				
				//$wnd.console.log("position: "+position +" currentPosition: "+currentPosition + " deltaPosition: " +deltaPosition );
				if(deltaPosition < 0 && Math.abs(deltaPosition) + moveCenterPaneHeight <= maxHeightLogo ){ //zwiekszamy
					
					var newMoveBottomPanelHeight = moveBottomPanelHeight - Math.abs(deltaPosition);
					
					var newCenterMovePanelHeight = height - moveTopPanelHeight - newMoveBottomPanelHeight;
					
					$wnd.console.log("newCenterMovePanelHeight" + newCenterMovePanelHeight);
					$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px";
					
					$wnd.document.getElementById("moveBottomPanel").style.height = newMoveBottomPanelHeight + "px";
					
				}else if(deltaPosition > 0 && moveCenterPaneHeight - deltaPosition >= minHeightLogo ){ //zmniejszamy
					
					var newMoveBottomPanelHeight = parseInt(moveBottomPanelHeight) + deltaPosition;
					
					var newCenterMovePanelHeight = height - moveTopPanelHeight - newMoveBottomPanelHeight;
					
					$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px"; //deltaPosition > 0 
					
					$wnd.document.getElementById("moveBottomPanel").style.height = newMoveBottomPanelHeight + "px";
					
					
				}
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}else if(elementId.indexOf("Left") >=0){
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.left;
				//currentPosition=currentPosition.substring(0, currentPosition1.length-2);
				$wnd.document.getElementById("moveLeftPanel").style.width = currentPosition ;
			},500);
		}

		return interval;
		
	}-*/;
	
	private native int myOnMove(String elementId)/*-{
		var element = $wnd.document.getElementById(elementId);
		if(element === "undefined") return -1;
		
		var image = $wnd.document.getElementById("croapImage");
		
		var width = image.width;
		var height = image.height;
		
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
	
	private native int myOnMoveCenter(String elementId, int moveElementWidth, int moveElementHeight)/*-{
		var element = $wnd.document.getElementById(elementId);
		if(element === "undefined") return -1;
		
		var image = $wnd.document.getElementById("croapImage");
		
		var width = image.width;
		var height = image.height;
		
		//var positionStart1 = element.parentNode.style.left;
		//positionStart = positionStart1.substring(0, positionStart1.length-2); //wyrzucamy koncowke 'px'
		var interval = -1;
		
		interval = $wnd.setInterval(function(){
			var currentPositionTop = element.parentNode.style.top;
			var currentPositionLeft = element.parentNode.style.left;
			
			var topPanelHeight = currentPositionTop.substring(0, currentPositionTop.length-2); //wyrzucamy koncowke 'px';
			var deltaTopPanelHeight = $wnd.document.getElementById("moveTopPanel").offsetHeight - topPanelHeight;
			var bottomPanelHeight = height - moveElementHeight - topPanelHeight  //$wnd.document.getElementById("moveTopPanel").offsetHeight + deltaTopPanelHeight;
			
			var leftPanelWidth = currentPositionLeft.substring(0, currentPositionLeft.length-2); //wyrzucamy koncowke 'px';;
			var deltaLeftPanelWidth = $wnd.document.getElementById("moveLeftPanel").offsetWidth - leftPanelWidth;
			var rightPanelWidth = width - moveElementWidth - leftPanelWidth //$wnd.document.getElementById("moveLeftPanel").offsetWidth + deltaLeftPanelWidth;
			
			//currentPosition=currentPosition.substring(0, currentPosition1.length-2);
			$wnd.document.getElementById("moveTopPanel").style.height = currentPositionTop ;
			$wnd.document.getElementById("moveBottomPanel").style.height = bottomPanelHeight + "px";
			$wnd.document.getElementById("moveLeftPanel").style.width = currentPositionLeft;
			$wnd.document.getElementById("moveRightPanel").style.width = rightPanelWidth + "px";
		},100);
		
	
		return interval;
	
 	}-*/;
	
	private native void stopInterval(int interval)/*-{
		$wnd.clearInterval(interval);
		$wnd.document.getElementById("moveTopPanel").className = "topPanel" ;
		$wnd.document.getElementById("moveBottomPanel").className = "bottomPanel" ;
		$wnd.document.getElementById("moveLeftPanel").className = "leftPanel" ;
		$wnd.document.getElementById("moveRightPanel").className = "rightPanel" ;
	}-*/ ;
	
	private native int getHeight(String elementId)/*-{
		return $wnd.document.getElementById(elementId).offsetHeight;
	}-*/;
	private native int getWidth(String elementId)/*-{
		return $wnd.document.getElementById(elementId).offsetWidth;
	}-*/;
}
