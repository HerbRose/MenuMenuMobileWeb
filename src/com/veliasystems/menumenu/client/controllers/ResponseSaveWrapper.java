package com.veliasystems.menumenu.client.controllers;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class ResponseSaveWrapper implements IsSerializable{

	private Restaurant restaurant;
	private List<Integer> errorCodes;
	
	
	public ResponseSaveWrapper() {
	}
	
	public List<Integer> getErrorCodes() {
		return errorCodes;
	}
	public void setErrorCodes(List<Integer> errorCodes) {
		this.errorCodes = errorCodes;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	
}
