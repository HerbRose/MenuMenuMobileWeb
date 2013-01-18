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
	private MyInfoPanelRow row;
	private List<MyInfoPanelRow> infoPanelRows = new ArrayList<MyInfoPanelRow>();
	
	
	/**
	 * the width of all {@link MyRestaurantInfoPanel}
	 */
	private double width;
	
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
		getElement().getStyle().setWidth(width, Unit.PX);
		
		for (MyInfoPanelRow infoPanelRow : infoPanelRows) {
			infoPanelRow.setWidth(width);
		}
		
	}
	
	/**
	 * adding new item to panel. The <code>leftCell</code> will by added to left container and the <code>rightCell</code>  will by added to right container.
	 * 
	 * @param leftCell the left part of data.
	 * @param rightCell the right part of data.
	 */
	public MyInfoPanelRow addItem(Widget leftCell, Widget rightCell){
		
		row = new MyInfoPanelRow(leftCell, rightCell, width);
		infoPanelRows.add(row);
		add(row);
		return row;
	}
	
//	public void refresh(){
//		
//	}
}
