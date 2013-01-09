package com.veliasystems.menumenu.client.userInterface;

import java.util.Map;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.userInterface.myWidgets.BackButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyButton;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;




public class CropImage extends MyPage implements IObserver{
	
	private JQMPage backPage = null;
	
	private BackButton backButton;
	private MyButton saveButton;
	
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
	private PickupDragController dragController;
	private DropController dropController;
	
	private double ratioProfile = 450.0/260.0;
	private double minLogoRatio = 220d/75d;
	private double maxLogoRatio = 220d/55d;
	
	private int interval = 0;
	
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private BlobServiceAsync blobService = GWT.create(BlobService.class); 
	
	public CropImage(ImageBlob imageInsert, JQMPage back){
		super(Customization.CROP);
		backPage = back;
		cropImage(imageInsert);
	}
	
	public CropImage(ImageBlob imageInsert) {
		super(Customization.CROP);
		cropImage(imageInsert);
	}
	
	private void cropImage(ImageBlob imageInsert) {
		imageBlob = imageInsert;
		
		backButton = new BackButton(Customization.BACK);
    	backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				PagesController.showWaitPanel();
				JQMContext.changePage(RestaurantController.restMapView.get(Long.parseLong(imageBlob.getRestaurantId())), Transition.SLIDE);
			}
		});
		
    	saveButton = new MyButton(Customization.SAVE);
		saveButton.setStyleName("rightButton saveButton", true);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				PagesController.showWaitPanel();
				
				double leftXPercentage = (double) getOffsetWidth("moveLeftPanel") / (double) displayWidth;
				double topYPercentage = (double) getHeight("moveTopPanel") / (double) getOffsetHeight("croapImage");
				
				double rightXPercentage = (double) (getOffsetWidth("moveLeftPanel") + getOffsetWidth("centerMovePanel")) / (double) displayWidth;
				double bottomYPercentage = (double) (getHeight("moveTopPanel") + getHeight("centerMovePanel")) / (double) getOffsetHeight("croapImage");
				
				blobService.cropImage(imageBlob, leftXPercentage, topYPercentage, rightXPercentage, bottomYPercentage, new AsyncCallback<Map<String, ImageBlob>>() {

					@Override
					public void onFailure(Throwable caught) {
						PagesController.hideWaitPanel();
					}

					@Override
					public void onSuccess(Map<String, ImageBlob> result) {
						restaurantController.afterCrop(result.get("new"), result.get("old"), backPage);				
					}
					
				});	
				
			}
		});
    	
		getHeader().setLeftButton(backButton);
		getHeader().setRightButton(saveButton);
		
		restaurantId = Long.parseLong(imageInsert.getRestaurantId());
		
		//Image img = new Image(image.getUrl());
		image = new Image(imageInsert.getImageUrl());
		
		boundaryPanel.add(image);
		mainPanel.add(boundaryPanel);
		add(mainPanel);
		
		image.getElement().setId("croapImage");
		mainPanel.setStyleName("mainPanel", true);
		mainPanel.getElement().setId("cropMainPanel");
		
		topPanel.setStyleName("topPanel", true);
		topPanel.getElement().setId("moveTopPanel");
		topHand.setStyleName("topHand cropArrow", true);
		topHand.getElement().setId("moveTopHand");
		
		rightPanel.getElement().setId("moveRightPanel");
		rightPanel.setStyleName("rightPanel", true);
		rightHand.setStyleName("rightHand cropArrow", true);
		rightHand.getElement().setId("moveRightHand");
		
		bottomPanel.setStyleName("bottomPanel", true);
		bottomPanel.getElement().setId("moveBottomPanel");
		bottomHand.setStyleName("bottomHand cropArrow", true);
		bottomHand.getElement().setId("moveBottomHand");
		
		leftPanel.setStyleName("leftPanel", true);
		leftPanel.getElement().setId("moveLeftPanel");
		leftHand.setStyleName("leftHand cropArrow", true);
		leftHand.getElement().setId("moveLeftHand");
		
		centerMovePanel.setStyleName("centerMovePanel", true);
		centerMovePanel.getElement().setId("centerMovePanel");

		displayWidth = PagesController.contentWidth; //temporary 
		image.getElement().getStyle().setProperty("maxWidth", PagesController.contentWidth +"px");

		dragController = new PickupDragController(boundaryPanel, true);
		dropController = new AbsolutePositionDropController(boundaryPanel);
		
		boundaryPanel.add(topPanel);
		boundaryPanel.add(topHand);
		boundaryPanel.add(rightPanel);
		boundaryPanel.add(rightHand);
		boundaryPanel.add(bottomPanel);
		boundaryPanel.add(bottomHand);
		boundaryPanel.add(leftPanel);
		boundaryPanel.add(leftHand);
		boundaryPanel.add(centerMovePanel);

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
					interval = myOnMoveCenter(element.getElement().getId(),getOffsetWidth(centerMovePanel.getElement().getId()), getOffsetHeight(centerMovePanel.getElement().getId()) );
					return;
				}
				
				switch (imageBlob.getImageType()) {
				case LOGO:
					interval = myOnMoveLogo(element.getElement().getId(), minLogoRatio, maxLogoRatio);
					break;
				case PROFILE:
					interval = myOnMoveProfile(element.getElement().getId(), ratioProfile);
					break;
					
				case MENU:
					interval = myOnMoveMenu(element.getElement().getId());
					break;
				default:
					break;
				}
				
				
			}
		
			@Override
			public void onDragEnd(DragEndEvent event) {
	
				checkPositions(imageBlob.getImageType().name());
			}
		});
		
		
		image.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {
				
				displayWidth = getOffsetWidth(image.getElement().getId());
				displayHeight = getOffsetHeight(image.getElement().getId());
				int displayCenterPanelWidth = displayWidth>20 ? displayWidth- 10 : displayWidth;
				int displayCenterPanelHeight = displayHeight>20 ? displayHeight - 10 : displayHeight ;
				
				switch (imageBlob.getImageType()) {
				case MENU:
//					displayWidth = 220;
					centerMovePanel.setWidth(displayCenterPanelWidth +"px");
					centerMovePanel.setHeight(displayCenterPanelHeight+"px");
					break;
				case LOGO:
//					displayWidth = PagesController.contentWidth; //220;
					centerMovePanel.setWidth( displayCenterPanelWidth + "px");
					
//					if(displayCenterPanelHeight > displayWidth/maxLogoRatio){
//						displayCenterPanelHeight = (int) (displayWidth/maxLogoRatio);
//					}else if(displayCenterPanelHeight < displayWidth/minLogoRatio){
//						displayCenterPanelHeight = (int) (displayWidth/minLogoRatio);
//					}
					if(displayCenterPanelWidth/displayCenterPanelHeight < minLogoRatio){
						displayCenterPanelHeight = (int) (displayCenterPanelWidth/minLogoRatio);
					}else if(displayCenterPanelWidth/displayCenterPanelHeight > maxLogoRatio){
						displayCenterPanelHeight = (int) (displayCenterPanelWidth/maxLogoRatio);
					}
					
					centerMovePanel.setHeight(displayCenterPanelHeight +"px");
					break;
				case PROFILE:
//					displayWidth = 450;
					centerMovePanel.setWidth(displayWidth+"px");
					centerMovePanel.setHeight(displayWidth / ratioProfile + "px");
					leftHand.getElement().getStyle().setDisplay(Display.NONE);
					rightHand.getElement().getStyle().setDisplay(Display.NONE);
				}
				
				boundaryPanel.setWidth(displayWidth + "px");
				boundaryPanel.setHeight(getOffsetHeight(image.getElement().getId()) + "px");
				
				int height = getOffsetHeight(image.getElement().getId());
				
				int topPanelHeight = (height - getOffsetHeight(centerMovePanel.getElement().getId())) /2;
				int bottomPanelHeight = height - topPanelHeight - getOffsetHeight(centerMovePanel.getElement().getId());
				
				int leftPanelWidth = (displayWidth - getOffsetWidth(centerMovePanel.getElement().getId()) )/2;
				int rightPanelWidth = displayWidth - leftPanelWidth - getOffsetWidth(centerMovePanel.getElement().getId());
				
				if(topPanelHeight<0) topPanelHeight=0;
				if(bottomPanelHeight<0) bottomPanelHeight=0;
				if(leftPanelWidth<0) leftPanelWidth=0;
				if(rightPanelWidth<0) rightPanelWidth=0;
				
				topPanel.setHeight( topPanelHeight + "px");
				leftPanel.setWidth(leftPanelWidth +"px");
				bottomPanel.setHeight(bottomPanelHeight + "px");
				rightPanel.setWidth(rightPanelWidth + "px");
				centerMovePanel.getElement().getStyle().setTop(topPanelHeight, Unit.PX);
				centerMovePanel.getElement().getStyle().setLeft(leftPanelWidth, Unit.PX);
				checkPositions(imageBlob.getImageType().name());
				PagesController.hideWaitPanel();
			}
		});
	
	}

	@Override
	protected void onPageShow() {
		super.onPageShow();
		
		
		
	//	centerMovePanel.setWidth("100px");
	//	centerMovePanel.setHeight("60px");
		
		
	}

	@Override
	public void onChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPageHide() {
		super.onPageHide();
		
		getElement().removeFromParent();
	}
	
	private native int myOnMoveLogo(String elementId, double minRatio, double maxRatio)/*-{
		var element = $wnd.document.getElementById(elementId);
	
		if(element === "undefined") return -1;
		
		var image = $wnd.document.getElementById("croapImage");
		
		var width = image.width;
		var height = image.height;
	
		var interval = -1;
		
		
		if(elementId.indexOf("Top") >=0 ){ // jeżeli strzałka w górę
			$wnd.document.getElementById("moveTopHand").className = $wnd.document.getElementById("moveTopHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveRightHand").className = $wnd.document.getElementById("moveRightHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveBottomHand").className = $wnd.document.getElementById("moveBottomHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveLeftHand").className = $wnd.document.getElementById("moveLeftHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveTopPanel").className = "topPanel moveChecked" ;
			$wnd.document.getElementById("moveRightPanel").className = "rightPanel moveChecked" ;
			var position;
			
			$wnd.setTimeout(function(){
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 250);
			
//			var bottomPanelHeight = $wnd.document.getElementById("moveBottomPanel").offsetHeight;
			var moveBottomPanelHeight = $wnd.document.getElementById("moveBottomPanel").style.height;
				moveBottomPanelHeight = moveBottomPanelHeight.substring(0, moveBottomPanelHeight.length-2); //wyrzucamy koncowke 'px'
			var moveLeftPanelWidth = $wnd.document.getElementById("moveLeftPanel").style.width;
				moveLeftPanelWidth = moveLeftPanelWidth.substring(0, moveLeftPanelWidth.length-2);
				
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.top; //y
				currentPosition = currentPosition.substring(0, currentPosition.length-2); //wyrzucamy koncowke 'px'
				
//				if(currentPosition >=height || currentPosition<=0){
//					$wnd.document.getElementById("centerMovePanel").style.top = "0px";
//					$wnd.document.getElementById("moveTopPanel").style.height = "0px";
//					 return;
//				}
				var deltaPosition = currentPosition - position; //delta y
				
				var moveTopPanelHeight = $wnd.document.getElementById("moveTopPanel").style.height;
					moveTopPanelHeight = moveTopPanelHeight.substring(0, moveTopPanelHeight.length-2); //wyrzucamy koncowke 'px'
				
				var moveCenterPaneHeight = $wnd.document.getElementById("centerMovePanel").style.height;
					moveCenterPaneHeight = moveCenterPaneHeight.substring(0, moveCenterPaneHeight.length-2); //wyrzucamy koncowke 'px'
				var moveCenterPaneWidth = $wnd.document.getElementById("centerMovePanel").style.width;
					moveCenterPaneWidth = moveCenterPaneWidth.substring(0, moveCenterPaneWidth.length-2); //wyrzucamy koncowke 'px'	
				var centerMovePanelCurrentPosition = $wnd.document.getElementById("centerMovePanel").style.top;
					centerMovePanelCurrentPosition = centerMovePanelCurrentPosition.substring(0, centerMovePanelCurrentPosition.length-2); //wyrzucamy koncowke 'px'

				//var newMoveTopPanelHeight = parseInt(moveTopPanelHeight) + parseInt(deltaPosition);
				var newCenterMovePanelWidth = moveCenterPaneWidth;
				var newCenterMovePanelHeight = parseInt(moveCenterPaneHeight) - parseInt(deltaPosition);
					
				$wnd.console.log(moveCenterPaneWidth/newCenterMovePanelHeight);
				if(moveCenterPaneWidth/newCenterMovePanelHeight > maxRatio){
					newCenterMovePanelWidth = newCenterMovePanelHeight * maxRatio;
				}else if(moveCenterPaneWidth/newCenterMovePanelHeight < minRatio){
					newCenterMovePanelWidth = newCenterMovePanelHeight * minRatio;
				}
				if(newCenterMovePanelWidth <= 1 || newCenterMovePanelHeight<=1 || parseInt(newCenterMovePanelWidth) + parseInt(moveLeftPanelWidth) >= width ||  parseInt(newCenterMovePanelHeight) + parseInt(moveBottomPanelHeight) >= height){      
					return;
				}
				$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px";
				$wnd.document.getElementById("centerMovePanel").style.top = parseInt(centerMovePanelCurrentPosition) + parseInt(deltaPosition) + "px";
				$wnd.document.getElementById("centerMovePanel").style.width = newCenterMovePanelWidth + "px";

				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}else if(elementId.indexOf("Right") >=0){
			$wnd.document.getElementById("moveTopHand").className = $wnd.document.getElementById("moveTopHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveRightHand").className = $wnd.document.getElementById("moveRightHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveBottomHand").className = $wnd.document.getElementById("moveBottomHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveLeftHand").className = $wnd.document.getElementById("moveLeftHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveRightPanel").className = "rightPanel moveChecked" ;
			$wnd.document.getElementById("moveBottomPanel").className = "bottomPanel moveChecked" ;
			
			
			$wnd.setTimeout(function(){
				position = element.parentNode.style.left; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 250);
			
			var moveLeftPanelWidth = $wnd.document.getElementById("moveLeftPanel").style.width;
				moveLeftPanelWidth = moveLeftPanelWidth.substring(0, moveLeftPanelWidth.length-2);
			var moveTopPanelHeight = $wnd.document.getElementById("moveTopPanel").style.height;
				moveTopPanelHeight = moveTopPanelHeight.substring(0, moveTopPanelHeight.length-2); //wyrzucamy koncowke 'px'
			
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.left;
					currentPosition = currentPosition.substring(0, currentPosition.length-2);	
				var moveCenterPaneWidth = $wnd.document.getElementById("centerMovePanel").style.width;
					moveCenterPaneWidth = moveCenterPaneWidth.substring(0, moveCenterPaneWidth.length-2); //wyrzucamy koncowke 'px'

				var deltaPosition = parseInt(currentPosition) - parseInt(position); //delta y
				
				var newCenterMovePanelWidth = parseInt(moveCenterPaneWidth) + parseInt(deltaPosition);
//				var newMoveRightPanelWidth = width - newCenterMovePanelWidth - moveLeftPanelWidth;

				var newCenterMovePanelHeight = $wnd.document.getElementById("centerMovePanel").style.height;
					newCenterMovePanelHeight = newCenterMovePanelHeight.substring(0, newCenterMovePanelHeight.length-2); //wyrzucamy koncowke 'px'
				
				if(newCenterMovePanelWidth/newCenterMovePanelHeight > maxRatio){
					newCenterMovePanelHeight = newCenterMovePanelWidth / maxRatio;
				}else if(newCenterMovePanelWidth/newCenterMovePanelHeight < minRatio){
					newCenterMovePanelHeight = newCenterMovePanelWidth / minRatio;
				}
				
				if(newCenterMovePanelWidth <= 1 || newCenterMovePanelHeight<=1 || parseInt(newCenterMovePanelWidth) + parseInt(moveLeftPanelWidth) >= width ||  parseInt(newCenterMovePanelHeight) + parseInt(moveTopPanelHeight) >= height){      
					return;
				}
				$wnd.document.getElementById("centerMovePanel").style.width = newCenterMovePanelWidth +"px";
				$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight +"px";
				//$wnd.document.getElementById("moveRightPanel").style.width = newMoveRightPanelWidth +"px";
				
				position = element.parentNode.style.left; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}else if(elementId.indexOf("Bottom") >=0){
			$wnd.document.getElementById("moveTopHand").className = $wnd.document.getElementById("moveTopHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveRightHand").className = $wnd.document.getElementById("moveRightHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveBottomHand").className = $wnd.document.getElementById("moveBottomHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveLeftHand").className = $wnd.document.getElementById("moveLeftHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveBottomPanel").className = "bottomPanel moveChecked" ;
			$wnd.document.getElementById("moveRightPanel").className = "rightPanel moveChecked" ;
			var position;
			
			$wnd.setTimeout(function(){ //czekamy aż drag stworzy swoje div'y
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 250);
			
			var moveTopPanelHeight = $wnd.document.getElementById("moveTopPanel").style.height;
				moveTopPanelHeight = moveTopPanelHeight.substring(0, moveTopPanelHeight.length-2); //wyrzucamy koncowke 'px'
			var moveLeftPanelWidth = $wnd.document.getElementById("moveLeftPanel").style.width;
				moveLeftPanelWidth = moveLeftPanelWidth.substring(0, moveLeftPanelWidth.length-2);
			
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.top; //y
					currentPosition = currentPosition.substring(0, currentPosition.length-2); //wyrzucamy koncowke 'px'
				var deltaPosition = currentPosition - position; //delta y

				var moveCenterPaneHeight = $wnd.document.getElementById("centerMovePanel").style.height;
					moveCenterPaneHeight = moveCenterPaneHeight.substring(0, moveCenterPaneHeight.length-2); //wyrzucamy koncowke 'px'
				var moveCenterPaneWidth = $wnd.document.getElementById("centerMovePanel").style.width;
					moveCenterPaneWidth = moveCenterPaneWidth.substring(0, moveCenterPaneWidth.length-2); //wyrzucamy koncowke 'px'	
					
				var newCenterMovePanelHeight = parseInt(moveCenterPaneHeight) + parseInt(deltaPosition);
				var newCenterMovePanelWidth = moveCenterPaneWidth;
				
				if(moveCenterPaneWidth/newCenterMovePanelHeight > maxRatio){
					newCenterMovePanelWidth = newCenterMovePanelHeight * maxRatio;
				}else if(moveCenterPaneWidth/newCenterMovePanelHeight < minRatio){
					newCenterMovePanelWidth = newCenterMovePanelHeight * minRatio;
				}
				if(newCenterMovePanelWidth <= 1 || newCenterMovePanelHeight<=1 || parseInt(newCenterMovePanelWidth) + parseInt(moveLeftPanelWidth) >= width ||  parseInt(newCenterMovePanelHeight) + parseInt(moveTopPanelHeight) >= height){      
					return;
				}		
				$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px"; 
				$wnd.document.getElementById("centerMovePanel").style.width = newCenterMovePanelWidth + "px"; 

				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}else if(elementId.indexOf("Left") >= 0){
			$wnd.document.getElementById("moveTopHand").className = $wnd.document.getElementById("moveTopHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveRightHand").className = $wnd.document.getElementById("moveRightHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveBottomHand").className = $wnd.document.getElementById("moveBottomHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveLeftHand").className = $wnd.document.getElementById("moveLeftHand").className + " moveElementHidden";
			$wnd.document.getElementById("moveLeftPanel").className = "leftPanel moveChecked" ;
			$wnd.document.getElementById("moveBottomPanel").className = "bottomPanel moveChecked" ;
			
			var moveRightPanelWidth = $wnd.document.getElementById("moveRightPanel").style.width;
				moveRightPanelWidth = moveRightPanelWidth.substring(0, moveRightPanelWidth.length-2);
			var moveTopPanelHeight = $wnd.document.getElementById("moveTopPanel").style.height;
				moveTopPanelHeight = moveTopPanelHeight.substring(0, moveTopPanelHeight.length-2); //wyrzucamy koncowke 'px'
				
			$wnd.setTimeout(function(){
				position = element.parentNode.style.left; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 250);
			
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.left;
					currentPosition=currentPosition.substring(0, currentPosition.length-2);
				var deltaPosition = parseInt(position) - parseInt(currentPosition); //delta y

				
				var centerMovePanelWidth = $wnd.document.getElementById("centerMovePanel").style.width;
					centerMovePanelWidth = centerMovePanelWidth.substring(0, centerMovePanelWidth.length-2); //wyrzucamy koncowke 'px'
				var newCenterMovePanelHeight = $wnd.document.getElementById("centerMovePanel").style.height;
					newCenterMovePanelHeight = newCenterMovePanelHeight.substring(0, newCenterMovePanelHeight.length-2); //wyrzucamy koncowke 'px'
					
				var newCenterMovePanelWidth = parseInt(centerMovePanelWidth) + parseInt(deltaPosition);
				var newCenterMovePanelLeft = width - parseInt(newCenterMovePanelWidth) - parseInt(moveRightPanelWidth);
				
				if(newCenterMovePanelWidth/newCenterMovePanelHeight > maxRatio){
					newCenterMovePanelHeight = newCenterMovePanelWidth / maxRatio;
				}else if(newCenterMovePanelWidth/newCenterMovePanelHeight < minRatio){
					newCenterMovePanelHeight = newCenterMovePanelWidth / minRatio;
				}
				
				if(newCenterMovePanelWidth <= 1 || newCenterMovePanelHeight<=1 || parseInt(newCenterMovePanelWidth) + parseInt(moveRightPanelWidth) >= width ||  parseInt(newCenterMovePanelHeight) + parseInt(moveTopPanelHeight) >= height){      
					return;
				}
				$wnd.document.getElementById("centerMovePanel").style.width = newCenterMovePanelWidth +"px";
				$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px";
				$wnd.document.getElementById("centerMovePanel").style.left = newCenterMovePanelLeft + "px";
				
				position = element.parentNode.style.left; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}
	
		return interval;
		
	}-*/;

	private native int myOnMoveProfile( String elementId, double ratio )/*-{
		var element = $wnd.document.getElementById(elementId);
	
		if(element === "undefined") return -1;
		
		var image = $wnd.document.getElementById("croapImage");
		
		$wnd.document.getElementById("moveTopHand").className = $wnd.document.getElementById("moveTopHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveRightHand").className = $wnd.document.getElementById("moveRightHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveBottomHand").className = $wnd.document.getElementById("moveBottomHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveLeftHand").className = $wnd.document.getElementById("moveLeftHand").className + " moveElementHidden";
		
		var width = image.width;
		var height = image.height;
		
		var interval = -1;
		
		if(elementId.indexOf("Top") >=0 ){ // jeżeli strzałka w górę
			
			$wnd.document.getElementById("moveTopPanel").className = "topPanel moveChecked" ;
			$wnd.document.getElementById("moveRightPanel").className = "rightPanel moveChecked" ;
			var position;
			
			$wnd.setTimeout(function(){
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 250);
			
			var bottomPanelHeight = $wnd.document.getElementById("moveBottomPanel").style.height;
				bottomPanelHeight = bottomPanelHeight.substring(0, bottomPanelHeight.length-2);
			var moveLeftPanelWidth = $wnd.document.getElementById("moveLeftPanel").style.width;
				moveLeftPanelWidth = moveLeftPanelWidth.substring(0, moveLeftPanelWidth.length-2);
			
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.top; //y
					currentPosition = currentPosition.substring(0, currentPosition.length-2); //wyrzucamy koncowke 'px'
			
				var deltaPosition = currentPosition - position; //delta y
				
				var moveCenterPaneHeight = $wnd.document.getElementById("centerMovePanel").style.height;
					moveCenterPaneHeight = moveCenterPaneHeight.substring(0, moveCenterPaneHeight.length-2);
				var centerMovePanelCurrentPosition = $wnd.document.getElementById("centerMovePanel").style.top;
					centerMovePanelCurrentPosition = centerMovePanelCurrentPosition.substring(0, centerMovePanelCurrentPosition.length-2); //wyrzucamy koncowke 'px'
				
				var newCenterMovePanelHeight = moveCenterPaneHeight - deltaPosition;
				var newCenterMovePanelWidth = newCenterMovePanelHeight * ratio ;
				
				if(newCenterMovePanelHeight <= 0){
					return;
				}
				if(parseInt(newCenterMovePanelWidth) + parseInt(moveLeftPanelWidth) >= width ){
					return;
				}
				var newMoveCenterPanelTop = height - bottomPanelHeight - newCenterMovePanelHeight;
				
				if(newMoveCenterPanelTop <= 0){
					return;
				}
					$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px";
					$wnd.document.getElementById("centerMovePanel").style.top = newMoveCenterPanelTop + "px";
					$wnd.document.getElementById("centerMovePanel").style.width = newCenterMovePanelWidth + "px";
				
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}else if(elementId.indexOf("Bottom") >=0){
			$wnd.document.getElementById("moveBottomPanel").className = "bottomPanel moveChecked" ;
			$wnd.document.getElementById("moveLeftPanel").className = "leftPanel moveChecked" ;
			
			var moveTopPanelHeight = $wnd.document.getElementById("moveTopPanel").style.height;
				moveTopPanelHeight = moveTopPanelHeight.substring(0, moveTopPanelHeight.length-2); //wyrzucamy koncowke 'px'
			var moveRightPanelWidth = $wnd.document.getElementById("moveRightPanel").style.width;
				moveRightPanelWidth = moveRightPanelWidth.substring(0, moveRightPanelWidth.length-2); //wyrzucamy koncowke 'px'
			var position;
			
			$wnd.setTimeout(function(){ //czekamy aż drag stworzy swoje div'y
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 250);
	
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.top; //y
					currentPosition = currentPosition.substring(0, currentPosition.length-2); //wyrzucamy koncowke 'px'
				
				var deltaPosition = position - currentPosition; //delta y
				
				var moveCenterPaneHeight = $wnd.document.getElementById("centerMovePanel").style.height;
					moveCenterPaneHeight = moveCenterPaneHeight.substring(0, moveCenterPaneHeight.length-2); //wyrzucamy koncowke 'px'
				
				var centerMovePanelLeft = $wnd.document.getElementById("centerMovePanel").style.left;
					centerMovePanelLeft = centerMovePanelLeft.substring(0, centerMovePanelLeft.length-2); //wyrzucamy koncowke 'px'
				
				var newCenterMovePanelHeight = moveCenterPaneHeight - deltaPosition;
				var newCenterMovePanelWidth = newCenterMovePanelHeight * ratio ;
				
				if(newCenterMovePanelHeight <= 0){
					return;
				}
				if(parseInt(newCenterMovePanelHeight) + parseInt(moveTopPanelHeight) >= height ){
					return;
				}
				
				var newMoveCenterPanelLeft = width - moveRightPanelWidth - newCenterMovePanelWidth;
				
				if(newMoveCenterPanelLeft <= 0){
					return;
				}
				
				if(deltaPosition != 0 ){ //zwiekszamy
					$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px";
					$wnd.document.getElementById("centerMovePanel").style.width = newCenterMovePanelWidth + "px";
					$wnd.document.getElementById("centerMovePanel").style.left = newMoveCenterPanelLeft + "px";
				}
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		
		}
	
		return interval;
		
	}-*/;

	private native int myOnMoveMenu(String elementId)/*-{
		var element = $wnd.document.getElementById(elementId);
	
		if(element === "undefined") return -1;
		
		var image = $wnd.document.getElementById("croapImage");
		
		var width = image.width;
		var height = image.height;
	
		$wnd.document.getElementById("moveTopHand").className = $wnd.document.getElementById("moveTopHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveRightHand").className = $wnd.document.getElementById("moveRightHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveBottomHand").className = $wnd.document.getElementById("moveBottomHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveLeftHand").className = $wnd.document.getElementById("moveLeftHand").className + " moveElementHidden";
		
		var interval = -1;
		if(elementId.indexOf("Top") >=0 ){ // jeżeli strzałka w górę
			var position;
			
			$wnd.setTimeout(function(){
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 250);
			
			var bottomPanelHeight = $wnd.document.getElementById("moveBottomPanel").offsetHeight;
			$wnd.document.getElementById("moveTopPanel").className = "topPanel moveChecked" ;
			interval = $wnd.setInterval(function(){
				
				var currentPosition = element.parentNode.style.top; //y
					currentPosition = currentPosition.substring(0, currentPosition.length-2); //wyrzucamy koncowke 'px'
				
				var moveCenterPaneHeight = $wnd.document.getElementById("centerMovePanel").style.height;
					moveCenterPaneHeight = moveCenterPaneHeight.substring(0, moveCenterPaneHeight.length-2); //wyrzucamy koncowke 'px'
					
				if(currentPosition <= 0 ){
					 return;
				}
				
				var deltaPosition = currentPosition - position; //delta y
				var newCenterMovePanelHeight = moveCenterPaneHeight - deltaPosition;
				
				if(newCenterMovePanelHeight < 1 ){
					return;
				}
				
				var newCenterMovePanelTop = height - bottomPanelHeight - newCenterMovePanelHeight;
				if(newCenterMovePanelTop < 0){
					return;
				}
				
			
				$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px";
				$wnd.document.getElementById("centerMovePanel").style.top =  newCenterMovePanelTop + "px";
				
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}else if(elementId.indexOf("Right") >=0){
			$wnd.document.getElementById("moveRightPanel").className = "rightPanel moveChecked" ;
			
			var moveLeftPanelWidth = $wnd.document.getElementById("moveLeftPanel").style.width;
			moveLeftPanelWidth = moveLeftPanelWidth.substring(0, moveLeftPanelWidth.length-2);
			
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.left;
					currentPosition=currentPosition.substring(0, currentPosition.length-2);
					currentPosition = parseInt(currentPosition) + 15;
				if(parseInt(currentPosition) >= parseInt(width) ){
					$wnd.document.getElementById("centerMovePanel").style.width = width - moveLeftPanelWidth;
					$wnd.document.getElementById("moveRightPanel").style.width = "0px";
					return;
				}else if(parseInt(currentPosition) <= parseInt(moveLeftPanelWidth)){
					$wnd.document.getElementById("centerMovePanel").style.width = "1px";
					$wnd.document.getElementById("moveRightPanel").style.width = -1 +parseInt(width) - parseInt(moveLeftPanelWidth)  +"px";
					return;
				}
				var moveRightPanelWidth = $wnd.document.getElementById("moveRightPanel").style.width = width - currentPosition + "px";
				moveRightPanelWidth = moveRightPanelWidth.substring(0, moveRightPanelWidth.length-2);
				
				$wnd.document.getElementById("centerMovePanel").style.width = width - moveLeftPanelWidth - moveRightPanelWidth +"px";
				
			},125);
		}else if(elementId.indexOf("Bottom") >=0){
			$wnd.document.getElementById("moveBottomPanel").className = "bottomPanel moveChecked" ;
			var position;
			
			$wnd.setTimeout(function(){ //czekamy aż drag stworzy swoje div'y
			position = element.parentNode.style.top; //y
			position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			}, 250);
			
			var moveTopPanelHeight = $wnd.document.getElementById("moveTopPanel").offsetHeight;
			
			interval = $wnd.setInterval(function(){
				
				var currentPosition = element.parentNode.style.top; //y
					currentPosition = currentPosition.substring(0, currentPosition.length-2); //wyrzucamy koncowke 'px'
				if(currentPosition >= height){
					return;
				}
				var deltaPosition = parseInt(position) - parseInt(currentPosition); //delta y
	
				var moveCenterPaneHeight = $wnd.document.getElementById("centerMovePanel").style.height;
					moveCenterPaneHeight = moveCenterPaneHeight.substring(0, moveCenterPaneHeight.length-2); //wyrzucamy koncowke 'px'
				var newCenterMovePanelHeight = moveCenterPaneHeight - deltaPosition;
					
				if(moveTopPanelHeight + newCenterMovePanelHeight > height){
					newCenterMovePanelHeight = height - moveTopPanelHeight;
				}
				$wnd.document.getElementById("centerMovePanel").style.height = newCenterMovePanelHeight + "px";
				
				position = element.parentNode.style.top; //y
				position = position.substring(0, position.length-2); //wyrzucamy koncowke 'px'
			},125);
		}else if(elementId.indexOf("Left") >=0){
			
			$wnd.document.getElementById("moveLeftPanel").className = "leftPanel moveChecked" ;
			var moveRightPanelWidth = $wnd.document.getElementById("moveRightPanel").style.width;
				moveRightPanelWidth = moveRightPanelWidth.substring(0, moveRightPanelWidth.length-2);
			
			interval = $wnd.setInterval(function(){
				var currentPosition = element.parentNode.style.left;
					currentPosition=currentPosition.substring(0, currentPosition.length-2);
	
				if(parseInt(currentPosition) >= parseInt(width) - parseInt(moveRightPanelWidth) ){
					$wnd.document.getElementById("centerMovePanel").style.width = "1px";
					return;
				}else if(parseInt(currentPosition) <= 0){
					$wnd.document.getElementById("centerMovePanel").style.width = parseInt(width) - parseInt(moveRightPanelWidth)+" px";
					return;
				}
				$wnd.document.getElementById("centerMovePanel").style.width = width - currentPosition - moveRightPanelWidth +"px";
				$wnd.document.getElementById("centerMovePanel").style.left = currentPosition + "px";
				
			},125);
		}
	
		return interval;
		
	}-*/;

	private native int myOnMoveCenter(String elementId, int moveElementWidth, int moveElementHeight)/*-{
		var element = $wnd.document.getElementById(elementId);
		if(element === "undefined") return -1;
		
		$wnd.document.getElementById("moveTopHand").className = $wnd.document.getElementById("moveTopHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveRightHand").className = $wnd.document.getElementById("moveRightHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveBottomHand").className = $wnd.document.getElementById("moveBottomHand").className + " moveElementHidden";
		$wnd.document.getElementById("moveLeftHand").className = $wnd.document.getElementById("moveLeftHand").className + " moveElementHidden";
		
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
	}-*/ ;
	private native void waitHalfSecond()/*-{
		$wnd.setTimeout(function(){	}, 500);
	}-*/ ;
	
	private native void checkPositions(String isProfile)/*-{
		
		var image = $wnd.document.getElementById("croapImage");
		
		var width = image.width;
		var height = image.height;
		
		$wnd.document.getElementById("moveTopPanel").className = "topPanel" ;
		$wnd.document.getElementById("moveBottomPanel").className = "bottomPanel" ;
		$wnd.document.getElementById("moveLeftPanel").className = "leftPanel" ;
		$wnd.document.getElementById("moveRightPanel").className = "rightPanel" ;
		
		$wnd.document.getElementById("moveTopHand").className = "topHand cropArrow";
		$wnd.document.getElementById("moveRightHand").className = "rightHand cropArrow";
		$wnd.document.getElementById("moveBottomHand").className = "bottomHand cropArrow";
		$wnd.document.getElementById("moveLeftHand").className = "leftHand cropArrow";
		
		var centerPanel = $wnd.document.getElementById("centerMovePanel");
		
		var centerPanelWidth = centerPanel.style.width;
			centerPanelWidth = centerPanelWidth.substring(0, centerPanelWidth.length-2); //usuwa koncowke '-px'
		var centerPanelHeigth = centerPanel.style.height;
			centerPanelHeigth = centerPanelHeigth.substring(0, centerPanelHeigth.length-2); //usuwa koncowke '-px'
		
		var centerPanelTop = centerPanel.style.top;
			centerPanelTop = centerPanelTop.substring(0, centerPanelTop.length-2); //usuwa koncowke '-px'
		var centerPanelLeft = centerPanel.style.left;
			centerPanelLeft = centerPanelLeft.substring(0, centerPanelLeft.length-2); //usuwa koncowke '-px'
		
		//$wnd.console.log(centerPanel.style.top);
		
		//USTAWIANIE STRZALEK
		if(isProfile.indexOf("PROFILE") >= 0){ //w profilowym strzalki sa narozne
			$wnd.document.getElementById("moveTopHand").className = "topHand cropArrow rotation90left";
			$wnd.document.getElementById("moveBottomHand").className = "bottomHand cropArrow rotation90left";
			
			$wnd.document.getElementById("moveTopHand").style.top = centerPanelTop + "px";
			$wnd.document.getElementById("moveTopHand").style.left = -20 + parseInt(centerPanelLeft) +  parseInt(centerPanelWidth) + "px";
			
			$wnd.document.getElementById("moveBottomHand").style.top = -15 + parseInt(centerPanelTop) + parseInt(centerPanelHeigth) + "px";
			$wnd.document.getElementById("moveBottomHand").style.left = -10 + parseInt(centerPanelLeft) + "px";
		}else{
			$wnd.document.getElementById("moveTopHand").style.top = centerPanelTop + "px";
			$wnd.document.getElementById("moveTopHand").style.left = -15 + parseInt(centerPanelLeft) +  parseInt(centerPanelWidth)/2 + "px";
			
			$wnd.document.getElementById("moveRightHand").style.left = -15 + parseInt(centerPanelLeft) + parseInt(centerPanelWidth) + "px";
			$wnd.document.getElementById("moveRightHand").style.top = -15 + parseInt(centerPanelTop) + parseInt(centerPanelHeigth)/2 + "px";
			
			$wnd.document.getElementById("moveBottomHand").style.top = -15 + parseInt(centerPanelTop) + parseInt(centerPanelHeigth) + "px";
			$wnd.document.getElementById("moveBottomHand").style.left = -15 + parseInt(centerPanelLeft) +  parseInt(centerPanelWidth)/2 + "px";
			
			$wnd.document.getElementById("moveLeftHand").style.left = centerPanelLeft + "px";
			$wnd.document.getElementById("moveLeftHand").style.top = -15 + parseInt(centerPanelTop) + parseInt(centerPanelHeigth)/2 + "px";
		}
		// KONIEC - USTAWIANIE STRZALEK
		//$wnd.document.getElementById();
		
		
		
		//USTAWIANIE PRZYCIEMNIANYCH PANELI
		$wnd.document.getElementById("moveTopPanel").style.height = centerPanelTop + "px" ;
		$wnd.document.getElementById("moveBottomPanel").style.height = parseInt(height) - parseInt(centerPanelTop) - parseInt(centerPanelHeigth) + "px";
		$wnd.document.getElementById("moveLeftPanel").style.width =  centerPanelLeft + "px";
		$wnd.document.getElementById("moveRightPanel").style.width = parseInt(width) - parseInt(centerPanelLeft) - parseInt(centerPanelWidth) + "px";
		// KONIEC - USTAWIANIE PRZYCIEMNIANYCH PANELI
		
		
	}-*/;
	

	private native int getOffsetHeight(String elementId)/*-{
		return $wnd.document.getElementById(elementId).offsetHeight;
	}-*/;
	private native int getOffsetWidth(String elementId)/*-{
		return $wnd.document.getElementById(elementId).offsetWidth;
	}-*/;
	
	private native int getHeight(String elementId)/*-{
		var element = $wnd.document.getElementById(elementId);
		var height = element.style.height;
			height = height.substring(0, height.length-2); //usuwa koncowke '-px'
		return parseInt(height);
	}-*/;
	private native int getWidth(String elementId)/*-{
		var element = $wnd.document.getElementById(elementId);
		var width = element.style.width;
			width = width.substring(0, width.length-2); //usuwa koncowke '-px'
		
		return parseInt(width);
	}-*/;
	private native int getMaxWidth(String elementId)/*-{
		var element = $wnd.document.getElementById(elementId);
		var width = element.style.maxWidth;
			width = width.substring(0, width.length-2); //usuwa koncowke '-px'
		
		return parseInt(width);
	}-*/;
	
}
