package com.veliasystems.menumenu.client.ui;



import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;




public class CropImage extends JQMPage implements HasClickHandlers {

	Image img;
	Image original;
	
	JQMHeader header;
	JQMButton saveButton;
	JQMButton backButton;
	String backgroundImage;
	FlowPanel image;
	
	String height;
	String width;
	
	double imgHeight;
	double imgWidth;
	
	double cropRectWidth;
	double cropRectHeight;
	double topOffset;
	double leftOffset;
	
	double leftC;
	double topC;
	double widthC;
	double heightC;
	
	double topBck;
	double leftBck;
	
	FlowPanel rect;
	JQMButton rightButtonUp;
	JQMButton rightButtonDown;
	JQMButton bottomButtonLeft;
	JQMButton bottomButtonRight;
	JQMButton zoomInButton;
	JQMButton zoomOutButton;
	JQMButton cropButton;
	JQMButton moveRightButton;
	JQMButton moveLeftButton;
	Image bckImage;	
	FlowPanel toolPanel;	
	Image newImage;
	ImageBlob imageInsert;
	
	
	private BlobServiceAsync blobService = GWT.create(BlobService.class); 
	
	public CropImage(ImageBlob imageInsert) {
		this.imageInsert = imageInsert;
		newImage = new Image(imageInsert.getImageUrl());
		imgHeight = imageInsert.getHeight();
		imgWidth = imageInsert.getWidth();	
		backgroundImage = newImage.getUrl();
		height = Integer.toString(imageInsert.getHeight());	
		width = Integer.toString(imageInsert.getWidth());			
		init();		
	}
	
	
	private void init(){
		
		header = new JQMHeader("Crop image");
		saveButton = new JQMButton(Customization.SAVEPROFILE);
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		header.setRightButton(saveButton);
		header.setLeftButton(backButton);
		add(header);	
		img = new Image();		
		image = new FlowPanel();
		bckImage = new Image(backgroundImage);
	
		switch (imageInsert.getImageType()) {
		case MENU:
			bckImage.setWidth("220px");
			break;
		case LOGO:
			bckImage.setWidth("220px");
			break;
		case PROFILE:
			bckImage.setWidth("420px");
		}
		image.getElement().getStyle().setMarginTop(20, Unit.PX);
		image.getElement().getStyle().setPosition(Position.RELATIVE);
		add(image);
		image.add(bckImage);
		System.out.println(bckImage.getWidth() + "; tu" + imgWidth);
		switch(imageInsert.getImageType()){
		case PROFILE:				
				cropRectWidth = 420;
				cropRectHeight = 280;		
			break;
		default:	
			if(imgWidth > 220){
				
			double ratioW = imgWidth / 220;
				cropRectWidth = 220 / ratioW;
				cropRectHeight = 30;
//				cropRectWidth = 220;
//				cropRectHeight = 100;
		}	
			else{
				cropRectWidth = imgWidth;
				cropRectHeight = 30;
			}
			break;
			
		}

		rect = new FlowPanel();
		rect.setStyleName("rect");
		rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
		rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
		rect.getElement().getStyle().setPosition(Position.ABSOLUTE);
		
		topBck = bckImage.getAbsoluteTop();
		leftBck = bckImage.getAbsoluteLeft();	
		
		rect.getElement().getStyle().setTop(topBck, Unit.PX);
		rect.getElement().getStyle().setLeft(leftBck, Unit.PX);
		
		image.add(rect);
		
		topOffset = topBck;	
		leftOffset = leftBck;
		
		rightButtonUp = new JQMButton("");
		rightButtonUp.setIcon(DataIcon.UP);
		rightButtonUp.setIconPos(IconPos.NOTEXT);
		
		rightButtonDown = new JQMButton("");
		rightButtonDown.setIcon(DataIcon.DOWN);
		rightButtonDown.setIconPos(IconPos.NOTEXT);
				
		rightButtonUp.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(topOffset > topBck){
					topOffset -= 4.0;		
					rect.getElement().getStyle().setTop(topOffset, Unit.PX);
				}
			}
		});
		
		rightButtonDown.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				if(topOffset < topBck + bckImage.getHeight() - rect.getOffsetHeight()){
					topOffset += 4.0;
					rect.getElement().getStyle().setTop(topOffset, Unit.PX);
				}
			}
		});
		
		bottomButtonLeft = new JQMButton("");
		bottomButtonLeft.setIcon(DataIcon.LEFT);
		bottomButtonLeft.setIconPos(IconPos.NOTEXT);	
		bottomButtonRight = new JQMButton("");
		bottomButtonRight.setIcon(DataIcon.RIGHT);
		bottomButtonRight.setIconPos(IconPos.NOTEXT);
				
		bottomButtonLeft.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(leftOffset > leftBck){
					leftOffset -= 4.0;		
					rect.getElement().getStyle().setLeft(leftOffset, Unit.PX);
				}
			}
	
		});
		
		bottomButtonRight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				if(leftOffset < bckImage.getWidth() - rect.getOffsetWidth() + bckImage.getElement().getOffsetLeft()){
					leftOffset += 4.0;
					rect.getElement().getStyle().setLeft(leftOffset, Unit.PX);
				}
			}
		});
			
		rect.sinkEvents(Event.MOUSEEVENTS);		
		rect.addHandler(new DragHandler() {
			
			@Override
			public void onDrag(DragEvent event) {
				// TODO Auto-generated method stub
				Window.alert("draggin");
			}
		}, DragEvent.getType());
		
		toolPanel = new FlowPanel();
		toolPanel.setStyleName("toolPanel");
		
		switch(imageInsert.getImageType()){
		case PROFILE:
			zoomInButton = new JQMButton("");
			zoomInButton.setIcon(DataIcon.PLUS);
			zoomInButton.setIconPos(IconPos.NOTEXT);
			
			zoomOutButton = new JQMButton("");
			zoomOutButton.setIcon(DataIcon.MINUS);
			zoomOutButton.setIconPos(IconPos.NOTEXT);
			break;
		default:
			zoomInButton = new JQMButton("");
			zoomInButton.setIcon(DataIcon.PLUS);
			zoomInButton.setIconPos(IconPos.NOTEXT);
			
			zoomOutButton = new JQMButton("");
			zoomOutButton.setIcon(DataIcon.MINUS);
			zoomOutButton.setIconPos(IconPos.NOTEXT);
			
			moveLeftButton = new JQMButton("");
			moveLeftButton.setIcon(DataIcon.FORWARD);
			moveLeftButton.setIconPos(IconPos.NOTEXT);
			toolPanel.add(moveLeftButton);
			
			moveRightButton = new JQMButton("");
			moveRightButton.setIcon(DataIcon.ALERT);
			moveRightButton.setIconPos(IconPos.NOTEXT);
			toolPanel.add(moveRightButton);
			break;
				
		}
		
	
		cropButton = new JQMButton("");
		cropButton.setIcon(DataIcon.CHECK);
		cropButton.setIconPos(IconPos.NOTEXT);
				
		toolPanel.add(rightButtonUp);
		toolPanel.add(rightButtonDown);
		toolPanel.add(bottomButtonLeft);
		toolPanel.add(bottomButtonRight);
		toolPanel.add(zoomInButton);
		toolPanel.add(zoomOutButton);
		toolPanel.add(cropButton);
		image.add(toolPanel);
		
		zoomInButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				switch(imageInsert.getImageType()){
					case PROFILE:
						if(rect.getOffsetHeight() + rect.getElement().getOffsetTop() < bckImage.getHeight() + image.getElement().getOffsetTop()  && rect.getOffsetWidth() + rect.getElement().getOffsetLeft() + rect.getAbsoluteLeft()< bckImage.getWidth() + image.getElement().getOffsetLeft()){
							
							cropRectHeight += 5.0;
							cropRectWidth = cropRectHeight *1.5;
							rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
							rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
						}
						
						break;
						
					default: 
						if(rect.getOffsetHeight() + rect.getAbsoluteTop() < bckImage.getHeight() + bckImage.getAbsoluteTop()){			
							cropRectHeight +=5.0;
							rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
						}
					break;
				}
				
				
			}
		});
		
		zoomOutButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				
				double ratioH = imgHeight / bckImage.getHeight();
				double ratioW = imgWidth / bckImage.getWidth();
				
				switch(imageInsert.getImageType()){
					case PROFILE:	
						if(cropRectWidth > 0 && cropRectWidth * ratioW > 420 && cropRectHeight * ratioH > 280){
							
							cropRectHeight -= 5.0;
							cropRectWidth = cropRectHeight *1.5;
							rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
							rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
						}
					break;
					default:
						if(cropRectHeight > 10){
							cropRectHeight -= 5.0;
							rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
							
						}
						break;
				}		
			}
		});
		
	
		switch(imageInsert.getImageType()){
		case PROFILE:
			break;
			default:
				
				
				
				moveLeftButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						
						if(cropRectWidth < bckImage.getOffsetWidth()){
							cropRectWidth += 5.0;
							rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
						}	
					}
				});
				
				moveRightButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						double ratioW = imgWidth / bckImage.getWidth();
						if(cropRectWidth > 0 && cropRectWidth * ratioW > 220 ){
							cropRectWidth -= 5.0;
							rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
						}
						
					}
				});
				
				
				
		}

		
	
		
		
		
		cropButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				double ratioH = bckImage.getHeight() / imgHeight;
				double ratioW = bckImage.getWidth() / imgWidth;
				
				
				int left = (int) ((int) (leftOffset - bckImage.getElement().getOffsetLeft()) / ratioW);
				int top = (int) ((int) (topOffset - bckImage.getElement().getOffsetTop()) / ratioH);
				
				int width = (int) ((int) cropRectWidth / ratioW);			
				int height = (int) ((int) cropRectHeight / ratioW);
				
				
			
				
				img.setUrlAndVisibleRect(backgroundImage, left, top, width, height);
				img.getElement().getStyle().setPosition(Position.ABSOLUTE);
				img.getElement().getStyle().setTop(image.getElement().getOffsetTop(), Unit.PX);
				img.getElement().getStyle().setLeft(imgWidth + image.getElement().getOffsetLeft() + 50, Unit.PX);		
				add(img);
		}
		});	
	}


	{
		this.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {			
					meClicked(event);
			}
		});
	}
	
	private void meClicked(ClickEvent event){
		
		if(isClicked(event, saveButton)){
			
			double ratioH = bckImage.getHeight() / imgHeight;
			double ratioW = bckImage.getWidth() / imgWidth;
			
			leftC = (leftOffset - bckImage.getElement().getOffsetLeft()) / ratioW;
			topC =  (topOffset - bckImage.getElement().getOffsetTop()) /ratioH;	
			widthC =  cropRectWidth / ratioW;		
			heightC =  cropRectHeight / ratioH;
	
			double leftXPercentage = leftC/(bckImage.getOffsetWidth()/ratioW);
			double topYPercentage = topC/(bckImage.getOffsetHeight()/ratioH);
			double rightXPercentage = (leftC+widthC)/(bckImage.getOffsetWidth()/ratioW);
			double bottomYPercentage = (topC + heightC)/(bckImage.getOffsetHeight()/ratioH);
			

			blobService.cropImage(imageInsert, leftXPercentage, topYPercentage, rightXPercentage, bottomYPercentage, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Void result) {
					// TODO Auto-generated method stub
					Window.Location.reload();
				}
				
			});
		
		
		}
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
	
	private boolean isClicked(ClickEvent event, JQMButton button) {

		int clickedX = event.getClientX();
		int clickedY = event.getClientY();

		int ButtonX = (int) button.getElement().getAbsoluteLeft();
		int ButtonY = (int) button.getElement().getAbsoluteTop();
		int ButtonWidth = (int) button.getElement().getClientWidth();
		int ButtonHeight = (int) button.getElement().getClientWidth();

		int ButtonStartX = ButtonX;
		int ButtonStopX = ButtonX + ButtonWidth;
		int ButtonStartY = ButtonY;
		int ButtonStopY = ButtonY + ButtonHeight;

		if (clickedX >= ButtonStartX && clickedX <= ButtonStopX
				&& clickedY >= ButtonStartY && clickedY <= ButtonStopY) {
			return true;
		}
		return false;
	}

}
