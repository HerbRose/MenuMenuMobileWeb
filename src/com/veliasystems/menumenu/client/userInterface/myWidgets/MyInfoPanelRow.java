package com.veliasystems.menumenu.client.userInterface.myWidgets;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.veliasystems.menumenu.client.JS;

public class MyInfoPanelRow extends FlowPanel{
	
	/**
	 * in this container left part of some {@link Widget} will be. Usually it will by name of the right part. For example 'Name:', 'Phone', ...
	 */
	private FlowPanel leftCelContainer = new FlowPanel();
	/**
	 * in this container right part of some {@link Widget} will be. Usually it will by name of the some restaurant data. For example 'Some name:', '636 44 55 66', ...
	 */
	private FlowPanel rightCellContainer = new FlowPanel();
	/**
	 * the width of all {@link MyRestaurantInfoPanel}
	 */
	private double width = 0;
	/**
	 * the width of leftCelContainer
	 */
	private double leftCelContainerWidth = 0;
	
	public MyInfoPanelRow( Widget leftCell, Widget rightCell, double width) {
		setWidth(width);
		
		setStyleName("restaurantInfoPanelRow noFocus", true);
		
		leftCelContainer.add(leftCell);
		rightCellContainer.add(rightCell);
		
		leftCelContainer.setStyleName("restaurantInfoPanelLeftCelContainer noFocus arialBold", true);
		rightCellContainer.setStyleName("restaurantInfoPanelRightCellContainer noFocus arialBold", true);
		
		add(leftCelContainer);
		add(rightCellContainer);
	}
	
	public void setWidth(double width){
		this.width = width;
		
		leftCelContainerWidth = width * 0.3;
		
		leftCelContainer.getElement().getStyle().setWidth(leftCelContainerWidth-13, Unit.PX); //-10 because padding-right = 10 px
		rightCellContainer.getElement().getStyle().setMarginLeft(leftCelContainerWidth, Unit.PX);
		rightCellContainer.getElement().getStyle().setWidth(width-leftCelContainerWidth-30, Unit.PX);
	}

	public void setHeight(int height) {
		setHeight(height + "px");
		leftCelContainer.setHeight(height + "px");
		rightCellContainer.setHeight(height + "px");
		
	}
	
	public String getRightCellContainerWidth(){
		return rightCellContainer.getElement().getStyle().getWidth();
	}
}
