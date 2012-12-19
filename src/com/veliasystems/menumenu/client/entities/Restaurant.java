package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import com.veliasystems.menumenu.client.Util;

public class Restaurant implements Serializable {

	private static final long serialVersionUID = -421655496163765996L;

	@Id
	private long id;
	
	private String name;
	private String address;
	private String city;
	private long cityId;
	private String district;
	private String lng;
	private String lat;
	private String mailRestaurant = "";
	private String phoneRestaurant = "";
	private String mailUser = "";
	private String phoneUser = "";
	private String nameUser = "";
	private String surnameUser = "";
	private boolean isVisibleForApp = false;
	private boolean clearBoard = false;
	
	private double latitude = 0;
	private double longitude = 0;
	
	/**Lists of url's with profile images */
	private List<ImageBlob> profileImages;
	/** Lists of url's with board images */
	private List<ImageBlob> logoImages;
	
	/**
	 * 
	 * @return {@link City} id
	 */
	public long getCityId() {
		return cityId;
	}

	/**
	 * set {@link City} id to {@link Restaurant}
	 * @param cityId - id of {@link City}
	 */
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	/** Lists of url's with main images*/
	private List<ImageBlob> menuImages;
	
	private String emptyMenuImageString;
	private String mainProfileImageString;
	private String mainLogoImageString;
	private String mainMenuImageString;
	
	{
		id = Util.getRandom(9999999);
	}
	/**
	 * default constructor
	 */
	public Restaurant() {}

	/**
	 * 
	 * @param name - {@link Restaurant} name
	 * @param address - {@link Restaurant} address
	 * @param city - {@link Restaurant} city name
	 */
	public Restaurant( String name, String address, String city ) {
		setName( name );
		setAddress( address );
		setCity( city );
	}
	/**
	 * 
	 * @return id of {@link Restaurant}
	 */
	public long getId() {
		return id;
	}

	
	/**
	 * get name of {@link Restaurant}
	 * @return name of {@link Restaurant}
	 */
	public String getName() {
		return name;
	}
	/**
	 * set name to {@link Restaurant}
	 * @param name - 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return address of {@link Restaurant}
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * set address of {@link Restaurant}
	 * @param address 
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 
	 * @return {@link City} name of {@link Restaurant}
	 */
	public String getCity() {
		return city;
	}
	/**
	 * set {@link City} name to {@link Restaurant}
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 
	 * @return district of {@link Restaurant}
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * set district to {@link Restaurant}
	 * @param district
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * 
	 * @return longitude {@link String}
	 */
	public String getLng() {
		return lng;
	}
	/**
	 * set longitude and automatically parse String to double value
	 * @param lng - {@link String}
	 */
	public void setLng(String lng) {
		setLatitude(Double.valueOf(lng));
		this.lng = lng;
	}
	/**
	 * 
	 * @return latitude {@link String}
	 */
	public String getLat() {
		return lat;
	}
	/**
	 * set latitude and automatically parse String to double value
	 * @param lat - {@link String}
	 */
	public void setLat(String lat) {
		setLongitude(Double.valueOf(lat));
		this.lat = lat;
	}

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", address="
				+ address + ", city=" + city + ", district=" + district
				+ ", lng=" + lng + ", lat=" + lat + ", mainProfileImage="
				+ mainProfileImageString + ", mainBoardImage=" + mainMenuImageString
				+ ", mainImage=" + mainLogoImageString + "]";
	}

	/**
	 * get Profile Images
	 * @return List of {@link ImageBlob}
	 */
	public List<ImageBlob> getProfileImages() {
		return profileImages;
	}

	/**
	 * set Profile Images
	 * @param profileImages - List of {@link ImageBlob}
	 */
	public void setProfileImages(List<ImageBlob> profileImages) {
		this.profileImages = profileImages;
	}

	/**
	 * get Logo Images
	 * @return List of {@link ImageBlob}
	 */
	public List<ImageBlob> getLogoImages() {
		return logoImages;
	}

	/**
	 * set Logo Imagesss
	 * @param logoImages - List of {@link ImageBlob}
	 */
	public void setLogoImages(List<ImageBlob> logoImages) {
		this.logoImages = logoImages;
	}

	/**
	 * get Menu Images 
	 * @return List of {@link ImageBlob}
	 */
	public List<ImageBlob> getMenuImages() {
		return menuImages;
	}

	/**
	 * set Menu Images
	 * @param menuImages - List of {@link ImageBlob}
	 */
	public void setMenuImages(List<ImageBlob> menuImages) {
		this.menuImages = menuImages;
	}	
	/**
	 * 
	 * @param mainLogoImageString - {@link String} url of main Logo Image 
	 */
	public void setMainLogoImageString(String mainLogoImageString) {
		this.mainLogoImageString = mainLogoImageString;
	}
	/**
	 * 
	 * @param mainMenuImageString - {@link String} url of main Menu Image 
	 */
	public void setMainMenuImageString(String mainMenuImageString) {
		this.mainMenuImageString = mainMenuImageString;
	}
	/**
	 * 
	 * @param mainProfileImageString - {@link String} url of main Profile Image 
	 */
	public void setMainProfileImageString(String mainProfileImageString) {
		this.mainProfileImageString = mainProfileImageString;
	}
	/**
	 * 
	 * @return url of main Logo Image
	 */
	public String getMainLogoImageString() {
		return mainLogoImageString;
	}
	/**
	 * 
	 * @return url of main Menu Image
	 */
	public String getMainMenuImageString() {
		return mainMenuImageString;
	}
	/**
	 * 
	 * @return url of main Profile Image 
	 */
	public String getMainProfileImageString() {
		return mainProfileImageString;
	}
	
	public String getMailRestaurant() {
		return mailRestaurant;
	}
	public void setMailRestaurant(String mailRestaurant) {
		this.mailRestaurant = mailRestaurant;
	}
	/**
	 * 
	 * @return {@link String} phone of {@link Restaurant}
	 */
	public String getPhoneRestaurant() {
		return phoneRestaurant;
	}
	/**
	 * Phone number must be valid before set
	 * @param phoneRestaurant - phone number
	 */
	public void setPhoneRestaurant(String phoneRestaurant) {
		this.phoneRestaurant = phoneRestaurant;
	}
	public String getMailUser() {
		return mailUser;
	}
	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}
	public String getPhoneUser() {
		return phoneUser;
	}
	public void setPhoneUser(String phoneUser) {
		this.phoneUser = phoneUser;
	}
	public String getNameUser() {
		return nameUser;
	}
	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}
	public String getSurnameUser() {
		return surnameUser;
	}
	public void setSurnameUser(String surnameUser) {
		this.surnameUser = surnameUser;
	}
	/**
	 * 
	 * @param isVisibleForApp - true if should be visible, false otherwise
	 */
	public void setVisibleForApp(boolean isVisibleForApp) {
		this.isVisibleForApp = isVisibleForApp;
	}
	/**
	 * 
	 * @param emptyMenuImageString - {@link String} url of empty menu image
	 */
	public void setEmptyMenuImageString(String emptyMenuImageString) {
		this.emptyMenuImageString = emptyMenuImageString;
	}
	/**
	 * 
	 * @return url of empty menu image
	 */
	public String getEmptyMenuImageString() {
		return emptyMenuImageString;
	}
	/**
	 * 
	 * @return true if {@link Restaurant} is visible for application, false if not
	 */
	public boolean isVisibleForApp() {
		return isVisibleForApp;
	}
	
	/**
	 * 
	 * @return value of {@link Restaurant#clearBoard}
	 */
	public boolean isClearBoard() {
		return clearBoard;
	}
	/**
	 * set clear board field
	 * @param clearBoard - boolean
	 */
	public void setClearBoard(boolean clearBoard) {
		this.clearBoard = clearBoard;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(! (obj instanceof Restaurant)){
			return false;
		}
		
		Restaurant r = (Restaurant) obj;
		
		if(this.getName().equalsIgnoreCase(r.getName()) && this.getCity().equalsIgnoreCase(r.getCity())){
			return true;
		}
		
		return false;
		
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	/**
	 * Should not be used by user
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * Should not be used by user
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}