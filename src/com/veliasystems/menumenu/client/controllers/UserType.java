package com.veliasystems.menumenu.client.controllers;
/**
 * 
 * User types that can be used
 *
 */
public enum UserType {
	/**
	 * Admin type
	 */
	ADMIN (0), 
	/**
	 * Agent type
	 */
	AGENT (1), 
	/**
	 * Restaurateur type
	 */
	RESTAURATOR (2);
	
	private final int userTypeValue ;
	
	UserType( int userTypeValue) {
		this.userTypeValue = userTypeValue;
	}
	
	public int userTypeValue(){
		return userTypeValue;
	}
	
	@Override
	/**
	 * return name().toLowerCase();
	 */
	public String toString() {
		return name().toLowerCase();
	}
}
