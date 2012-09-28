package com.veliasystems.menumenu.client.ui;



import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;




public class CropImage extends JQMPage  {

	Image img;
	
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
	
	FlowPanel rect;
	JQMButton rightButtonUp;
	JQMButton rightButtonDown;
	JQMButton bottomButtonLeft;
	JQMButton bottomButtonRight;
	JQMButton zoomInButton;
	JQMButton zoomOutButton;
	JQMButton cropButton;
	
	FlowPanel rightButtonPanel;
	FlowPanel bottomButtonPanel;
	FlowPanel zoomButtonPanel;
	
	
	public CropImage(Image imageInsert) {
		
		this.backgroundImage = imageInsert.getUrl();
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
		
		cropRectWidth = 200;
		cropRectHeight = cropRectWidth * 1.5;
		
		//set background image
		image = new FlowPanel();
		image.getElement().getStyle().setBackgroundImage("url('"+ backgroundImage+"')");
		image.setHeight(height +"px");
		image.setWidth(width + "px");
		add(image);
		
		
		rect = new FlowPanel();
		rect.setStyleName("rect");
		rect.getElement().getStyle().setHeight(cropRectHeight, Unit.PX);
		rect.getElement().getStyle().setWidth(cropRectWidth, Unit.PX);
		rect.getElement().getStyle().setPosition(Position.ABSOLUTE);
		rect.getElement().getStyle().setTop(70.0, Unit.PX);
		rect.getElement().getStyle().setLeft(70.0, Unit.PX);
		
		image.add(rect);
		
		topOffset = 70;
		
		leftOffset = 70;
		
		rightButtonUp = new JQMButton("");
		rightButtonUp.setIcon(DataIcon.UP);
		rightButtonUp.setIconPos(IconPos.NOTEXT);
		
		rightButtonDown = new JQMButton("");
		rightButtonDown.setIcon(DataIcon.DOWN);
		rightButtonDown.setIconPos(IconPos.NOTEXT);
		
		rightButtonPanel = new FlowPanel();
		rightButtonPanel.add(rightButtonUp);
		rightButtonPanel.add(rightButtonDown);
				
		rightButtonPanel.setStylePrimaryName("rightButtons");
		rightButtonPanel.getElement().getStyle().setTop(imgHeight/2, Unit.PX);
		rightButtonPanel.getElement().getStyle().setLeft(imgWidth + 15, Unit.PX);
		add(rightButtonPanel);
		
		
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
		
		bottomButtonPanel = new FlowPanel();
		bottomButtonPanel.add(bottomButtonLeft);
		bottomButtonPanel.add(bottomButtonRight);
				
		bottomButtonPanel.setStylePrimaryName("bottomButtons");
		bottomButtonPanel.getElement().getStyle().setTop(imgHeight + 60, Unit.PX);
		bottomButtonPanel.getElement().getStyle().setLeft(imgWidth/2, Unit.PX);
		add(bottomButtonPanel);
		
		bottomButtonLeft.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(leftOffset > image.getElement().getOffsetLeft() + 2){
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
		
		
		zoomInButton = new JQMButton("");
		zoomInButton.setIcon(DataIcon.PLUS);
		zoomInButton.setIconPos(IconPos.NOTEXT);
		
		zoomOutButton = new JQMButton("");
		zoomOutButton.setIcon(DataIcon.MINUS);
		zoomOutButton.setIconPos(IconPos.NOTEXT);
		
		cropButton = new JQMButton("");
		cropButton.setIcon(DataIcon.CHECK);
		cropButton.setIconPos(IconPos.NOTEXT);
		
		zoomButtonPanel = new FlowPanel();
		zoomButtonPanel.add(zoomInButton);
		zoomButtonPanel.add(zoomOutButton);
		zoomButtonPanel.add(cropButton);
		
		zoomButtonPanel.setStyleName("zoomButtons");
		zoomButtonPanel.getElement().getStyle().setTop(imgHeight + 60, Unit.PX);
		zoomButtonPanel.getElement().getStyle().setLeft(60, Unit.PX);
		add(zoomButtonPanel);
		
		zoomInButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				
				if(cropRectHeight + rect.getElement().getOffsetTop() < imgHeight + image.getElement().getAbsoluteTop()){
					
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
				// TODO Auto-generated method stub
				
				img.setUrlAndVisibleRect(backgroundImage, (int) leftOffset, (int) topOffset, (int) cropRectWidth, (int) cropRectHeight);
				
				img.getElement().getStyle().setTop(imgHeight + 100, Unit.PX);
				
				add(img);
		
			}
		});
		
	
		
	}	
			
}


