package com.veliasystems.menumenu.client.userInterface.myWidgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.userInterface.myWidgets.TouchGetter.HidePanels;

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

	public interface IMyChangeHendler{
		public void onChange();
	}
	
	private ScrollPanel scrollPanel = new ScrollPanel();
	
	private FlowPanel mainPanel = new FlowPanel();
	private FlowPanel expandedPanel = new FlowPanel();
	
	private Map<Long, MyListItem> listItems = new HashMap<Long, MyListItem>();
	private List<Long> checkedList = new ArrayList<Long>();
	private Label textLabel = new Label();
	
	private long selectedItem = -1;
	
	/**
	 * if with checkbox
	 */
	private boolean isCheck = false;
	
	private TouchGetter touchGetter = PagesController.TOUCH_GETTER;
	private IMyChangeHendler myChangeHendler = null;
	
	public MyListCombo(boolean isChecked) {
		
		isCheck = isChecked;

		setStyleName("listCombo noFocus", true);

		addClickHandler(new ClickHandler() {
				
			@Override
			public void onClick(ClickEvent event) {
				showExpandetPanel();
			}
		});
	
		expandedPanel.setStyleName("expandedPanel noFocus", true);
		
		scrollPanel.add(expandedPanel);
		scrollPanel.setStyleName("listComboScrollPanel hideListComboScrollPanel", true);
		
//		textLabel.setStyleName("comboListLabel comboListItemLabel", true);
		
		
		mainPanel.add(textLabel);
		mainPanel.add(scrollPanel);
		add(mainPanel);
	}

	public void addMyClickHendler( ClickHandler clickHandler){
		addClickHandler(clickHandler);
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
	
	private void itemController(long orderToSelect, boolean select){
		if(isCheck){
			
			if( (checkedList.contains(orderToSelect) && !select) || (!checkedList.contains(orderToSelect) && select) ){
				itemController(orderToSelect);
			}
		}else{
			if(selectedItem != orderToSelect){
				itemController(orderToSelect);
			}
		}
	}
	private void itemController(long orderToSelect){
		MyListItem panelToCheck = null;
		MyListItem oldPanel = null;
		if(!isCheck){
			if(selectedItem >= 0){
				oldPanel = listItems.get(selectedItem);
			}
			if(oldPanel != null){
				selectPanel(oldPanel, false);
			}
		}
		
		panelToCheck=listItems.get(orderToSelect);
		if(panelToCheck != null){
			selectPanel(panelToCheck, true);
			selectedItem = orderToSelect ;
			
		}else selectedItem = -1;
		
		if(myChangeHendler != null){
			if(!isCheck){
				if(oldPanel != null && panelToCheck != null){
					if(oldPanel.getOrder() != panelToCheck.getOrder()){
						myChangeHendler.onChange();
					}
				} else if(oldPanel == null && panelToCheck != null){
					myChangeHendler.onChange();
				}
			} else {
				myChangeHendler.onChange();
			}
			
		}
	}
	
	private void selectPanel(MyListItem panel, boolean select){
	
		if(isCheck){
			if(checkedList.contains(panel.getOrder())){
				checkedList.remove(panel.getOrder());
				if(panel.getText().equals(textLabel.getText())){
					if(checkedList.isEmpty()){
						textLabel.setText("");
					}else{
						textLabel.setText(listItems.get(checkedList.get(0)).getText());
					}
				}
			}else{
				checkedList.add(panel.getOrder());
				textLabel.setText(panel.getText());
			}
			panel.check();
		}else{
			panel.setStyleName("selected", select);
			textLabel.setText(panel.getText());
		}
	}
	
	public MyListItem getNewItem(String text){
		MyListItem panel = new MyListItem();
		panel.getTextLabel().setStyleName("comboListItemLabel");
		panel.setText(text);
		return panel;
	}
	
	public MyListItem getNewCheckBoxItem( String text){
		MyListItem panel = new MyListItem(true,text);
		panel.getTextLabel().setStyleName("comboListItemLabel");
		panel.setText(text);
		return panel;
	}
	
	public long getSelectedOrder() {
		return selectedItem;
	}
	public MyListItem getSelectedItem(){
		return listItems.get(selectedItem);
	}
	
	public List<Long> getCheckedList() {
		return checkedList;
	}
	
	public void clear(){
		listItems.clear();
		checkedList.clear();
		textLabel.setText("");
		expandedPanel.clear();
		
	}
	
	public void clearSelection(){
		if(isCheck){
			
			// to avoid concurrent modification
			List<Long> tmpList = new ArrayList<Long>();
			tmpList.addAll(checkedList);
			for (long item : tmpList) {		
				selectItem(item);
			}
		}else{
			selectItem(selectedItem);
		}
	}
	
	/**
	 * show or hide expanded panel
	 */
	private void showExpandetPanel(){
		boolean showPanel = !(Document.get().getElementById("touchGetter").getClassName().indexOf(R.SHOW) >= 0); //if showPanel is true, panel should by show
		if(isCheck && showPanel){
			scrollPanel.setStyleName("hideListComboScrollPanel", !showPanel);
			TouchGetter.showTouchGetter(showPanel);
		}else if(!isCheck){
			scrollPanel.setStyleName("hideListComboScrollPanel", !showPanel);
			TouchGetter.showTouchGetter(showPanel);
		}
		String id = "id";

		Date date = new Date();
		id+= date.getTime();
		scrollPanel.getElement().setId(id);
		
		
		touchGetter.addClickHandler(new HidePanels() {
			
			@Override
			public void hidePanels() {
				scrollPanel.addStyleName("listComboScrollPanel hideListComboScrollPanel");
			}
		});
	}
	public void selectItem(long order){
		itemController(order);
	}
	
	public void selectItem(long order, boolean select){
		itemController(order, select);
	}

	public void addMyChangeHendler(IMyChangeHendler changeHendler){
		myChangeHendler = changeHendler;
	}
	
}
