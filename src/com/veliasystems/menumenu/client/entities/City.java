package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;

import javax.persistence.Id;

public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * city id, auto generated.
	 */
	@Id private Long id;
	/**
	 * name of the city
	 */
	private String city;
	private String normalizedCityName = null;
	
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
	 * 
	 * @return {@link String} normalized city name
	 */
	public String getNormalizedCityName() {
		return normalizedCityName;
	}
	
	/**
	 * should be used only on <b>server</b> side
	 * @param normalizedCityName {@link String} with normalized city name
	 */
	public void setNormalizedCityName(String normalizedCityName) {
		this.normalizedCityName = normalizedCityName;
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
		
//		String thisCityNameWithOutWhiteSpace = this.getCity().replaceAll(" ", "");
//		String cityToCheckNameWithOutWhiteSpace = c.getCity().replaceAll(" ", "");
		
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
	
	@Override
	public int hashCode() {
		int resoult = 37;
		
		resoult = (resoult << 5) - resoult + city.hashCode();
		resoult = (resoult << 5) - resoult + country.hashCode();
		
		return resoult ;
	}
	
	/**
	 * Set visibility for mobile application. If <code>isVisable</code> is set to true the city will be visible for mobile application
	 * @param isVisable - if <code>true</code> city will be visible for mobile application
	 */
	public void setVisable(boolean isVisableForProducion, boolean isVisableForTests) {
		this.isVisibleForTests = isVisableForTests;
		isVisable = isVisableForProducion;
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
	}
	
	public String getDistrictImageURL() {
		return districtImageURL;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	
}
