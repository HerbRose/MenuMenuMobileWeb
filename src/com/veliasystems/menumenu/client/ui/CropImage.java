package com.veliasystems.menumenu.client.ui;


import java.util.List;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;

public class CropImage extends JQMPage{

	SplitLayoutPanel splitLayoutPanel;
	Image imageToCrop;
	JQMHeader header;
	JQMButton saveButton;
	JQMButton backButton;
	List<Node> cells;
	NodeList<Node> list;
	HTML north;
	HTML south;
	HTML east;
	HTML west;
	HTML content;
	JQMButton cropImage;
	Image croppedImage;
	
	public CropImage(Image image) {
		this.imageToCrop = image;
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
		splitLayoutPanel = new SplitLayoutPanel();
		
		north = new HTML("");
		south = new HTML("");
		west = new HTML("");
		east = new HTML("");
		content = new HTML("");
		

		
		splitLayoutPanel.addEast(east, 0);
		splitLayoutPanel.addWest(west, 0);
		splitLayoutPanel.addNorth(north, 0);
		splitLayoutPanel.addSouth(south, 0);

		splitLayoutPanel.add(content);
		
		String height =  Integer.toString(imageToCrop.getHeight() + 40);
		String width =  Integer.toString(imageToCrop.getWidth() + 40);
		
		splitLayoutPanel.setHeight(height  + "px" );
		splitLayoutPanel.setWidth(width  + "px");
		splitLayoutPanel.getElement().getStyle().setBackgroundImage("url('"+ imageToCrop.getUrl()+"')");
		add(splitLayoutPanel);
		
		cropImage = new JQMButton("crop");
		add(cropImage);
		cropImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
//				int top = north.getAbsoluteTop();
//				int left = west.getAbsoluteLeft();
//				int right = east.getAbsoluteLeft() + content.getOffsetWidth();
//				int bottom = south.getAbsoluteTop() + content.getOffsetHeight();
//				int top = north.getAbsoluteTop();;
//				int left = west.getAbsoluteLeft();
//				int right = left + content.getOffsetWidth();
//				int bottom = top + content.getOffsetHeight();
				
				int top = north.getElement().getAbsoluteTop();
				int left = west.getElement().getAbsoluteLeft();
				int width = east.getElement().getAbsoluteLeft() - west.getElement().getAbsoluteLeft();
				int height = south.getElement().getAbsoluteTop() - north.getElement().getAbsoluteTop();
				
				croppedImage = new Image(imageToCrop.getUrl(),left,top,width,height);
				add(croppedImage);
			}
		});
		
	}
	
	
	
}
