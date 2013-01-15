package com.veliasystems.menumenu.client.userInterface.myWidgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author velia-systems
 * 
 * 
 *
 */
public class MyRestaurantInfoPanel extends FlowPanel {

	/**
	 * single row
	 */
	private FlowPanel row;
	
	
	private List<FlowPanel> leftCelContainerFlowPanels = new ArrayList<FlowPanel>();
	private List<FlowPanel> rightCellContainerFlowPanels = new ArrayList<FlowPanel>();
	
	/**
	 * the width of all {@link MyRestaurantInfoPanel}
	 */
	private int width;
	/**
	 * the width of leftCelContainer
	 */
	private int leftCelContainerWidth;
	
	/**
	 * Create new {@link MyRestaurantInfoPanel} and sets style class name:
	 * <ul>
	 * 	<li>restaurantInfoPanel</ul>
	 *  <li>noFocus</ul>
	 * </ul>
	 */
	public MyRestaurantInfoPanel() {
		
		setStyleName("restaurantInfoPanel noFocus", true);
	}
	
	/**
	 * Sets the width of all {@link MyRestaurantInfoPanel}. Left part will be sets on 30% of <code>width</code>
	 * @param width the width of all {@link MyRestaurantInfoPanel}.
	 */
	public void setWidth(int width){
		this.width = width;
		leftCelContainerWidth = (int) (width * 0.30);
		getElement().getStyle().setWidth(width, Unit.PX);
		
		for (FlowPanel leftCelContainer : leftCelContainerFlowPanels) {
			leftCelContainer.getElement().getStyle().setWidth(leftCelContainerWidth, Unit.PX);
		}
		for (FlowPanel rightCellContainer : rightCellContainerFlowPanels) {
			rightCellContainer.getElement().getStyle().setMarginLeft(leftCelContainerWidth, Unit.PX);
			rightCellContainer.getElement().getStyle().setWidth(width-leftCelContainerWidth-30, Unit.PX);
		}
		
	}
	
	/**
	 * adding new item to panel. The <code>leftCell</code> will by added to left container and the <code>rightCell</code>  will by added to right container.
	 * 
	 * @param leftCell the left part of data.
	 * @param rightCell the right part of data.
	 */
	public void addItem(Widget leftCell, Widget rightCell){
		
		 //in this container left part of some {@link Widget} will be. Usually it will by name of the right part. For example 'Name:', 'Phone', ...
		FlowPanel leftCelContainer = new FlowPanel();; 

		//in this container right part of some {@link Widget} will be. Usually it will by name of the some restaurant data. For example 'Some name:', '636 44 55 66', ...
		FlowPanel rightCellContainer = new FlowPanel();;
		row = new FlowPanel();

		
		leftCelContainerFlowPanels.add(leftCelContainer);
		rightCellContainerFlowPanels.add(rightCellContainer);
		
		row.setStyleName("restaurantInfoPanelRow noFocus", true);
		leftCelContainer.setStyleName("restaurantInfoPanelLeftCelContainer noFocus arialBold", true);
		
		leftCelContainer.getElement().getStyle().setWidth(leftCelContainerWidth, Unit.PX);
		
		rightCellContainer.setStyleName("restaurantInfoPanelRightCellContainer noFocus arialBold", true);
		rightCellContainer.getElement().getStyle().setMarginLeft(leftCelContainerWidth, Unit.PX);
		rightCellContainer.getElement().getStyle().setWidth(width-leftCelContainerWidth-30, Unit.PX);
		
		leftCelContainer.add(leftCell);
		rightCellContainer.add(rightCell);
		row.add(leftCelContainer);
		row.add(rightCellContainer);
		add(row);
	}
	
//	public void refresh(){
//		
//	}
}
