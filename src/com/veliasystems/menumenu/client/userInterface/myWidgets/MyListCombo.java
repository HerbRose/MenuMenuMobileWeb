package com.veliasystems.menumenu.client.userInterface.myWidgets;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.veliasystems.menumenu.client.R;

/**
 * 
 * Implements of drop-down list on web page.
 * For full functionality on each <code>.html</code> page a <code>div</code> element is required with id parameter sets to: <code>touchGetter</code> and class name
 * sets to: <code>hide</code>. <br> <br>
 * 
 * <code><strong> &lt;div id="touchGetter" class="hide">&lt;/div> </strong></code>
 * 
 * <br> <br>
 * And styles on <code>.css</code> file have to by sets on:<br><br>
 * for <code>touchGetter</code> id: <br><code>
 *	position: fixed;  <br>
 *	width: 100%;  <br>
 *	height: 100%;  <br>
 *	filter:alpha(opacity=0);  <br>
 *	opacity: 0;  <br>
 *	-moz-opacity: 0;  <br>
 *	z-index: 100;  <br><br>
 * </code>
 * for <code>hide</code> class: <br><code>
 * display: none;<br><br>
 * </code>
 * and is also required <code>show</code> class with parameters: <br><br><code>
 * display: block; <br>
 * </code>
 * @author velia-systems
 * @version 0.1 alpha
 */
public class MyListCombo extends FocusPanel {

	private ScrollPanel scrollPanel = new ScrollPanel();
	private FocusPanel panelForArrow = new FocusPanel();
	
	private FlowPanel mainPanel = new FlowPanel();
	private FlowPanel arrows = new FlowPanel();
	private FlowPanel whiteArrow = new FlowPanel();
	private FlowPanel blackArrow = new FlowPanel();
	private FlowPanel expandedPanel = new FlowPanel();
	
	private Map<Long, MyListItem> listItems = new HashMap<Long, MyListItem>();
	private Label textLabel = new Label();
	
	private long selectedItem = -1;
	
	public MyListCombo(boolean isArrow) {
		
		Date date = new Date();
		scrollPanel.getElement().setId("id" + date.getTime());
		
//		addOnClick(scrollPanel.getElement().getId());
	
		
		setStyleName("listCombo noFocus", true);

		if(isArrow){
			panelForArrow.setStyleName("panelForArrow borderLabel noFocus pointer", true);
			panelForArrow.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					showExpandetPanel();
				}
			});
		
			whiteArrow.setStyleName("whiteArrow noFocus", true);
			blackArrow.setStyleName("blackArrow noFocus", true);
		}else{
			addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					showExpandetPanel();
				}
			});
		}
		expandedPanel.setStyleName("expandedPanel noFocus", true);
		
		scrollPanel.add(expandedPanel);
		scrollPanel.setStyleName("listComboScrollPanel hideListComboScrollPanel", true);
		
//		textLabel.setStyleName("comboListLabel comboListItemLabel", true);
		
		if(isArrow){
			arrows.add(blackArrow);
			arrows.add(whiteArrow);
			panelForArrow.add(arrows);
			mainPanel.add(panelForArrow);
		}
		mainPanel.add(textLabel);
		mainPanel.add(scrollPanel);
		add(mainPanel);
	}

	/**
	 * <i>order</i> does not affect the display order
	 * <i>order</i> have to by >=0, if not nothing will change 
	 * @param newItem - item to add
	 * @param order - none duplicate number in list (use isOrderUse() to check). 
	 */
	public void addListItem(final MyListItem newItem, long order){
		if(order<0) return;
		newItem.setStyleName("myListComboItem noFocus");
		newItem.setOrder(order);
		newItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				itemController(newItem.getOrder());
			}
		});
		listItems.put(order, newItem);
		expandedPanel.add(newItem);
	}
	
	/**
	 * return true if order is using
	 * @param order
	 * @return true if order is using
	 */
	public boolean isOrderUse(int order){
		return listItems.containsKey(order);
	}
	
	private void itemController(long orderToSelect){
		MyListItem panel = null;
		if(selectedItem >= 0){
			panel = listItems.get(selectedItem);
		}
		if(panel != null){
			selectPanel(panel, false);
		}
		panel=listItems.get(orderToSelect);
		if(panel != null){
			selectPanel(panel, true);
			selectedItem = orderToSelect;
			textLabel.setText(panel.getText());
		}else selectedItem = -1;
		
	}
	
	private void selectPanel(FocusPanel panel, boolean select){
		panel.setStyleName("selected", select);
	}
	
	public MyListItem getNewItem(String text){
		MyListItem panel = new MyListItem();
		panel.getTextLabel().setStyleName("comboListItemLabel");
		panel.setText(text);
		return panel;
	}
	
	
	public long getSelectedItem() {
		return selectedItem;
	}
	
	/**
	 * show or hide expanded panel
	 */
	private void showExpandetPanel(){
		boolean showPanel = Document.get().getElementById("touchGetter").getClassName().indexOf(R.SHOW) >= 0;
		showPanel = !showPanel;
		scrollPanel.setStyleName("hideListComboScrollPanel", !showPanel);
		showTouchGetter(showPanel);
		addOnClick(scrollPanel.getElement().getId());
	}
	public void selectItem(long order){
		itemController(order);
	}

	private void showTouchGetter(boolean isShow){
		if(isShow) {
			Document.get().getElementById("touchGetter").setClassName(R.SHOW);
		}else{
			Document.get().getElementById("touchGetter").setClassName(R.HIDE);
		}
	}
	
	private native void addOnClick(String scrollPanel)/*-{
		
		var element=$wnd.document.getElementById('touchGetter');
		
		if( element.attachEvent ){
		   element.attachEvent('onclick', 'document.getElementById(\'touchGetter\').className =\'hide\'');
		  
		} else {
		   element.setAttribute('onclick', 'document.getElementById(\'touchGetter\').className =\'hide\'; document.getElementById(\''+scrollPanel+'\').className =\'listComboScrollPanel hideListComboScrollPanel\'' ); 
		  
		}
	}-*/;
}
