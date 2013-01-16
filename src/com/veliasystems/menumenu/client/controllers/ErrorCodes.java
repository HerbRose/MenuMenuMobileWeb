package com.veliasystems.menumenu.client.controllers;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ErrorCodes implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3346009407151193648L;
	
	public static final int USER_NOT_ALLOWED = 0;
	public static final int USER_DONT_EXIST = 1;
	public static final int RESTAURANT_EXIST_IN_OTHER_CITY = 2;

	public ErrorCodes() {
	}
}
