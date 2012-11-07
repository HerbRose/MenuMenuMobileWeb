package com.veliasystems.menumenu.client.ui.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.User;

public class RemoveUsersPanel extends FlowPanel implements IManager{
	
	private CellTable<String> userCellTable;
	private TextColumn<String> loginColumn;
	private Column<String, String> removeColumn;
	private UserController userController = UserController.getInstance();
	private List<String> userList = new ArrayList<String>();
	private List<User> listToDelete = new ArrayList<User>();
	
	public RemoveUsersPanel() {
		
		userCellTable = new CellTable<String>();
				
		loginColumn = new TextColumn<String>() {
			
			@Override
			public String getValue(String object) {
				return object;
			}
		};
		
		
		
		removeColumn = new Column<String, String>(new ButtonCell()) {
			
			@Override
			public String getValue(String object) {
				
				return Customization.DELETE_IMAGE;
			}
			
			@Override
			public void onBrowserEvent(Context context, Element elem,
					String object, NativeEvent event) {
				Document.get().getElementById("load").setClassName(R.LOADING);
				userController.removeUser(object);
				super.onBrowserEvent(context, elem, object, event);
			}
		};
		
		userCellTable.addColumn(loginColumn, "login");
		userCellTable.addColumn(removeColumn, "remove");
		userCellTable.setRowData(userList);
		add(userCellTable);
		

	}
	
	@Override
	public void clearData() {
		String userName = userController.getLoggedUser().getEmail();
		for (String string : userController.getUsers().keySet()) {
			if(!string.equalsIgnoreCase(userName)) userList.add(string);
		}	
		userCellTable.setRowData(userList);
		userCellTable.redraw();
	}

	@Override
	public String getName() {
		return Customization.REMOVE_USERS;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);	
	}

}
