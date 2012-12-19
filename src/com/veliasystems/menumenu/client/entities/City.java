package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;

import javax.persistence.Id;

import com.google.gwt.user.client.ui.Image;

public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id private Long id;
	private String city;

	private boolean isVisable = false;
	

	 {
			id = ((long) (Math.random() * 999999999));
	 }
	
	public City() {}
	 
	/**
	 * Return the city name
	 * @return city name
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Set the city name
	 * @param city - city name
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Return the id of the city
	 * @return id of the city
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return <code>true</code> if <code>cityToCheck</code> have the same name or <code>false</code>
	 */
	@Override
	public boolean equals(Object cityToCheck) {
		if(!(cityToCheck instanceof City)) return false;
		
		City c = (City) cityToCheck;
		
		if(this.getCity().equalsIgnoreCase(c.getCity())){
			return true;
		}
		return false;
	}
	
	/**
	 * Set visibility for mobile application. If <code>isVisable</code> is set to true the city will be visible for mobile application
	 * @param isVisable - if <code>true</code> city will be visible for mobile application
	 */
	public void setVisable(boolean isVisable) {
		this.isVisable = isVisable;
	}
	
	/**
	 * Check if city is visible for mobile application
	 * @return <code>true</code> if is visible
	 */
	public boolean isVisable() {
		return isVisable;
	}
}
