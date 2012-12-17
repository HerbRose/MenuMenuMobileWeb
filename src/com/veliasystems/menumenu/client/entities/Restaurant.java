package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.PostLoad;
import com.googlecode.objectify.Objectify;
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
	public long getCityId() {
		return cityId;
	}


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

	public Restaurant() {}

	
	public Restaurant( String name, String address, String city ) {
		setName( name );
		setAddress( address );
		setCity( city );
	}

	public long getId() {
		return id;
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		setLatitude(Double.valueOf(lng));
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

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


	public List<ImageBlob> getProfileImages() {
		return profileImages;
	}


	public void setProfileImages(List<ImageBlob> profileImages) {
		this.profileImages = profileImages;
	}


	public List<ImageBlob> getLogoImages() {
		return logoImages;
	}


	public void setLogoImages(List<ImageBlob> logoImages) {
		this.logoImages = logoImages;
	}


	public List<ImageBlob> getMenuImages() {
		return menuImages;
	}


	public void setMenuImages(List<ImageBlob> menuImages) {
		this.menuImages = menuImages;
	}	
	
	public void setMainLogoImageString(String mainLogoImageString) {
		this.mainLogoImageString = mainLogoImageString;
	}
	public void setMainMenuImageString(String mainMenuImageString) {
		this.mainMenuImageString = mainMenuImageString;
	}
	public void setMainProfileImageString(String mainProfileImageString) {
		this.mainProfileImageString = mainProfileImageString;
	}
	
	public String getMainLogoImageString() {
		return mainLogoImageString;
	}
	public String getMainMenuImageString() {
		return mainMenuImageString;
	}
	public String getMainProfileImageString() {
		return mainProfileImageString;
	}
	public String getMailRestaurant() {
		return mailRestaurant;
	}
	public void setMailRestaurant(String mailRestaurant) {
		this.mailRestaurant = mailRestaurant;
	}
	public String getPhoneRestaurant() {
		return phoneRestaurant;
	}
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
	public void setVisibleForApp(boolean isVisibleForApp) {
		this.isVisibleForApp = isVisibleForApp;
	}
	public void setEmptyMenuImageString(String emptyMenuImageString) {
		this.emptyMenuImageString = emptyMenuImageString;
	}
	public String getEmptyMenuImageString() {
		return emptyMenuImageString;
	}
	public boolean isVisibleForApp() {
		return isVisibleForApp;
	}
	public boolean isClearBoard() {
		return clearBoard;
	}
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