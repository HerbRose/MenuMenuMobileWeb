package com.veliasystems.menumenu.client.controllers;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.veliasystems.menumenu.client.entities.City;

public class ResponseSaveCityWrapper implements IsSerializable {

	private City city;
	private List<Integer> errorCodes;
	
	public ResponseSaveCityWrapper() {
	}
	
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public List<Integer> getErrorCodes() {
		return errorCodes;
	}
	public void setErrorCodes(List<Integer> errorCodes) {
		this.errorCodes = errorCodes;
	}
}
