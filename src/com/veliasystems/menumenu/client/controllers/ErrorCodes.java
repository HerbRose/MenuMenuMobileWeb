package com.veliasystems.menumenu.client.controllers;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.veliasystems.menumenu.client.Customization;

public class ErrorCodes implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3346009407151193648L;
	
	public static final int USER_NOT_ALLOWED = 0;
	public static final int USER_DONT_EXIST = 1;
	public static final int RESTAURANT_EXIST_IN_OTHER_CITY = 2;
	public static final int RESTAURANT_EXIST_IN_THIS_CITY = 3;
	public static final int CITY_ALREADY_EXIST = 4;
	public static final int SERVER_ERROR  = 5;
	public static final int ERROR_WHILE_CREATE_NEW_USER = 6;
	
	public ErrorCodes() {
	}
	
	public static String getError(int errorCode){
		switch (errorCode) {
		case 0:
			return Customization.USER_NOT_ALLOWED;
		case 1:
			return Customization.USER_DONT_EXIST;
		case 2:
			return Customization.RESTAURANT_EXIST_IN_OTHER_CITY;
		case 3:
			return Customization.RESTAURANT_EXIST_ERROR;
		case 4:
			return Customization.CITY_EXIST_ERROR;
		case 5:
			return Customization.ERROR;
		case 6:
			return Customization.ERROR_WHILE_CREATE_NEW_USER;
			default:
				return Customization.ERROR;
			}
	}
}
