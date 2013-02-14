package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import com.google.gwt.user.client.ui.Image;
import com.veliasystems.menumenu.client.Util;

public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id private Long id;
	private String city;
	
	private String districtImageURL = "";
	
	/**
	 * visible for production
	 */
	private boolean isVisable = false;
	/**
	 * visible for test flight or android tests
	 */
	private boolean isVisibleForTests = false;
	
	private String country = "";

	 {
			id = ((long) (Math.random() * 999999999));
	 }
	
	public City() {
		setLastDateSync();
	}
	 
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
		setLastDateSync();
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
		
		String thisCityNameWithOutWhiteSpace = this.getCity().replaceAll(" ", "");
		String cityToCheckNameWithOutWhiteSpace = c.getCity().replaceAll(" ", "");
		
//		if(thisCityNameWithOutWhiteSpace.equalsIgnoreCase(cityToCheckNameWithOutWhiteSpace)){
//			return true;
//		}
		
		if(this.city.trim().equalsIgnoreCase(c.city.trim()) && this.country.trim().equalsIgnoreCase(c.country.trim())){
			return true;
		}
		
//		if(this.getCity().equalsIgnoreCase(c.getCity())){
//			return true;
//		}
		return false;
	}
	
	/**
	 * Set visibility for mobile application. If <code>isVisable</code> is set to true the city will be visible for mobile application
	 * @param isVisable - if <code>true</code> city will be visible for mobile application
	 */
	public void setVisable(boolean isVisableForProducion, boolean isVisableForTests) {
		this.isVisibleForTests = isVisableForTests;
		isVisable = isVisableForProducion;
		setLastDateSync();
	}
	
	/**
	 * Check if city is visible for mobile application 
	 * @param isProduction if true the return isVisible for production application value, else return value for tests applications
	 * @return <code>true</code> if is visible
	 */
	public boolean isVisable(boolean isProduction) {
		if(isProduction) return isVisable;
		else return isVisibleForTests;
	}
	
	
	public void setDistrictImageURL(String districtImageURL) {
		this.districtImageURL = districtImageURL;
		setLastDateSync();
	}
	
	public String getDistrictImageURL() {
		return districtImageURL;
	}
	
	public void setCountry(String country) {
		this.country = country;
		setLastDateSync();
	}
	public String getCountry() {
		return country;
	}
	
	private void setLastDateSync(){
		Util.setCityLastDateSync();
	}
}
