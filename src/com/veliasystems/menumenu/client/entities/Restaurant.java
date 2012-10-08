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
	private String cityId;
	private String district;
	private String lng;
	private String lat;
	
	
	/**Lists of url's with profile images */
	private List<ImageBlob> profileImages;
	/** Lists of url's with board images */
	private List<ImageBlob> logoImages;
	/** Lists of url's with main images*/
	private List<ImageBlob> menuImages;
	

	private ImageBlob mainProfileImage;
	private ImageBlob mainLogoImage;
	private ImageBlob mainMenuImage;
	
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
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	
	
	
	
	
	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", address="
				+ address + ", city=" + city + ", district=" + district
				+ ", lng=" + lng + ", lat=" + lat + ", mainProfileImage="
				+ mainProfileImage + ", mainBoardImage=" + mainMenuImage
				+ ", mainImage=" + mainLogoImage + "]";
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


	public ImageBlob getMainProfileImage() {
		return mainProfileImage;
	}


	public void setMainProfileImage(ImageBlob mainProfileImage) {
		this.mainProfileImage = mainProfileImage;
	}


	public ImageBlob getMainLogoImage() {
		return mainLogoImage;
	}


	public void setMainLogoImage(ImageBlob mainLogoImage) {
		this.mainLogoImage = mainLogoImage;
	}


	public ImageBlob getMainMenuImage() {
		return mainMenuImage;
	}


	public void setMainMenuImage(ImageBlob mainMenuImage) {
		this.mainMenuImage = mainMenuImage;
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
}