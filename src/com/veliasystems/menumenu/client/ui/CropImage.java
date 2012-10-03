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
	
	FlowPanel rect;
	JQMButton rightButtonUp;
	JQMButton rightButtonDown;
	JQMButton bottomButtonLeft;
	JQMButton bottomButtonRight;
	JQMButton zoomInButton;
	JQMButton zoomOutButton;
	JQMButton cropButton;
	
	
	FlowPanel toolPanel;
	
	Image newImage;
	ImageBlob imageInsert;
	
	private BlobServiceAsync blobService = GWT.create(BlobService.class); 
	
	public CropImage(ImageBlob imageInsert) {
		this.imageInsert = imageInsert;
		newImage = new Image(imageInsert.getImageUrl());
		newImage.getElement().setId("loaded-image");
			
		backgroundImage = newImage.getUrl();
		
		imgHeight = imageInsert.getHeight();
		imgWidth = imageInsert.getWidth();
		
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
		
		cropRectWidth = 100;
		cropRectHeight = cropRectWidth * 1.5;
		
		//set background image
		image = new FlowPanel();
		image.getElement().getStyle().setBackgroundImage("url('"+ backgroundImage+"')");
		image.setHeight(height +"px");
		image.setWidth(width + "px");
		image.getElement().getStyle().setMarginTop(20, Unit.PX);

		add(image);
		
	
		rect = new FlowPanel();
		rect.setStyleName("rect");
		rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
		rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
		rect.getElement().getStyle().setPosition(Position.ABSOLUTE);
		rect.getElement().getStyle().setTop(90.0, Unit.PX);
		rect.getElement().getStyle().setLeft(70.0, Unit.PX);
		
		image.add(rect);
		
		topOffset = 90;
		
		leftOffset = 70;
		
		rightButtonUp = new JQMButton("");
		rightButtonUp.setIcon(DataIcon.UP);
		rightButtonUp.setIconPos(IconPos.NOTEXT);
		
		rightButtonDown = new JQMButton("");
		rightButtonDown.setIcon(DataIcon.DOWN);
		rightButtonDown.setIconPos(IconPos.NOTEXT);
				
		rightButtonUp.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(topOffset > image.getElement().getOffsetTop() + 2){
					topOffset -= 4.0;		
					rect.getElement().getStyle().setTop(topOffset, Unit.PX);
				}
			}
		});
		
		rightButtonDown.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				if(topOffset < image.getElement().getOffsetHeight() + image.getElement().getOffsetTop() - rect.getOffsetHeight() -2){
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
				if(leftOffset > image.getElement().getOffsetLeft() + 3){
					leftOffset -= 4.0;		
					rect.getElement().getStyle().setLeft(leftOffset, Unit.PX);
				}
			}
	
		});
		
		bottomButtonRight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				if(leftOffset < image.getElement().getOffsetWidth() + image.getElement().getOffsetLeft() - rect.getOffsetWidth() -2){ //2 = size of border red rectangle
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
		
		zoomInButton = new JQMButton("");
		zoomInButton.setIcon(DataIcon.PLUS);
		zoomInButton.setIconPos(IconPos.NOTEXT);
		
		zoomOutButton = new JQMButton("");
		zoomOutButton.setIcon(DataIcon.MINUS);
		zoomOutButton.setIconPos(IconPos.NOTEXT);
		
		cropButton = new JQMButton("");
		cropButton.setIcon(DataIcon.CHECK);
		cropButton.setIconPos(IconPos.NOTEXT);
		
		
		toolPanel = new FlowPanel();
		toolPanel.setStyleName("toolPanel");
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
				
				
				if(rect.getOffsetHeight() + rect.getElement().getOffsetTop() < image.getOffsetHeight() + image.getElement().getOffsetTop() - 4 && rect.getOffsetWidth() + rect.getElement().getOffsetLeft() < image.getOffsetWidth() + image.getElement().getOffsetLeft() - 4){
					
					cropRectWidth += 5.0;
					cropRectHeight = cropRectWidth *1.5;
					rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
					rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
				}
			}
		});
		
		zoomOutButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(cropRectWidth > 0){
					
					cropRectWidth -= 5.0;
					cropRectHeight = cropRectWidth *1.5;
					rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
					rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
				}
			}
		});
		
		
		cropButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				int left = (int) leftOffset - image.getElement().getOffsetLeft();
				int top = (int) topOffset - image.getElement().getOffsetTop();
				
				int width = (int) cropRectWidth;			
				int height = (int) cropRectHeight;
			
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
			
			leftC = leftOffset - image.getElement().getOffsetLeft();
			topC =  topOffset - image.getElement().getOffsetTop();	
			widthC =  cropRectWidth;		
			heightC =  cropRectHeight;
	
			double leftXPercentage = leftC/image.getOffsetWidth();
			double topYPercentage = topC/image.getOffsetHeight();
			double rightXPercentage = (leftC+widthC)/image.getOffsetWidth();
			double bottomYPercentage = (topC + heightC)/image.getOffsetHeight();
			
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
