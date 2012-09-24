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
	
	
	/**Lists of url's with profile images */
	private List<String> profileImages;
	/** Lists of url's with board images */
	private List<String> boardImages;
	/** Lists of url's with main images*/
	private List<String> mainImages;
	

	private String mainProfileImage;
	private String mainBoardImage;
	private String mainImage;
	
	{
		id = Util.getRandom(9999999);
	}
	
	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	
	public String getMainImage() {
		return mainImage;
	}
	
	public void setMainProfileImage(String mainProfileImage) {
		this.mainProfileImage = mainProfileImage;
	}
	
	public String getMainProfileImage() {
		return mainProfileImage;
	}
	
	public void setMainBoardImage(String mainBoardImage) {
		this.mainBoardImage = mainBoardImage;
	}
	
	public List<String> getProfileImages() {
		return profileImages;
	}

	public void setProfileImages(List<String> profileImages) {
		this.profileImages = profileImages;
	}

	public List<String> getBoardImages() {
		return boardImages;
	}

	public void setBoardImages(List<String> boardImages) {
		this.boardImages = boardImages;
	}

	public List<String> getMainImages() {
		return mainImages;
	}

	public void setMainImages(List<String> mainImages) {
		this.mainImages = mainImages;
	}

	public String getMainBoardImage() {
		return mainBoardImage;
	}
	
	
	
	public Restaurant() {}

	
	public Restaurant( String name, String address, String city ) {
		setName( name );
		setAddress( address );
		setCity( city );
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

	public long getId() {
		return id;
	}
	
	
}