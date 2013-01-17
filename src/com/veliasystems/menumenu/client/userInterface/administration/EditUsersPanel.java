package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.controllers.UserType;
import com.veliasystems.menumenu.client.entities.User;

public class EditUsersPanel extends FlowPanel implements IManager{
	
	private CellTable<User> userCellTable;
	private TextColumn<User> loginColumn;
	private Column<User, String> removeColumn;
	private Column<User, Boolean> isAdmin;
	private Column<User, Boolean> isAgent;
	private Column<User, Boolean> isRestaurator;
	private UserController userController = UserController.getInstance();
	private List<User> userList = new ArrayList<User>();
	
	public EditUsersPanel() {

		setStyleName("barPanel", true);
		show(false);
		
		userCellTable = new CellTable<User>();
				
		loginColumn = new TextColumn<User>() {
			
			@Override
			public String getValue(User object) {
				return object.getEmail();
			}
		};
		
		isAdmin = new Column<User, Boolean>(new CheckboxCell()) {
			
			@Override
			public Boolean getValue(User user) {
				return user.isAdmin();
			}
		};
		isAdmin.setFieldUpdater(new FieldUpdater<User, Boolean>() {
			
			@Override
			public void update(int index, User user, Boolean isAdmin) {
				userController.setAs(UserType.ADMIN, isAdmin, user);
				
			}
		});
		isAgent = new Column<User, Boolean>(new CheckboxCell()) {
			
			@Override
			public Boolean getValue(User user) {
				return user.isAgent();
			}
		};
		isAgent.setFieldUpdater(new FieldUpdater<User, Boolean>() {
			
			@Override
			public void update(int index, User user, Boolean isAgent) {
				userController.setAs(UserType.AGENT, isAgent, user);
			}
		});
		isRestaurator = new Column<User, Boolean>(new CheckboxCell()) {
			
			@Override
			public Boolean getValue(User user) {
				return user.isRestaurator();
			}
		};
		isRestaurator.setFieldUpdater(new FieldUpdater<User, Boolean>() {
			
			@Override
			public void update(int index, User user, Boolean isRestaurator) {
				userController.setAs(UserType.RESTAURATOR, isRestaurator, user);
			}
		});
		
		removeColumn = new Column<User, String>(new ButtonCell()) {
			
			@Override
			public String getValue(User object) {
				
				return Customization.DELETE;
			}
			
			@Override
			public void onBrowserEvent(Context context, Element elem,
					User user, NativeEvent event) {
				removeUser(user.getEmail());
				super.onBrowserEvent(context, elem, user, event);
			}
		};
		
		userCellTable.addColumn(loginColumn, "login");
		userCellTable.addColumn(removeColumn, "remove");
		userCellTable.addColumn(isAdmin, "isAdmin");
		userCellTable.addColumn(isAgent, "isAgent");
		userCellTable.addColumn(isRestaurator, "isRestaurator");
		userCellTable.setRowData(userList);
		add(userCellTable);
		

	}
	
	private void removeUser(String userEmail){
		userController.removeUser(userEmail);
	}
	
	@Override
	public void clearData() {
		String userName = userController.getLoggedUser().getEmail();
		userList.clear();
		
		for (User user : userController.getUserList()) {
			if(!user.getEmail().equalsIgnoreCase(userName)) userList.add(user);
		}	
		
		userCellTable.setRowData(userList);
		userCellTable.redraw();
	}

	@Override
	public String getName() {
		return Customization.USERS;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);	
	}

}
