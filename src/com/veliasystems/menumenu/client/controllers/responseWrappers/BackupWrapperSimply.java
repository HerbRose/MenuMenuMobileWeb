package com.veliasystems.menumenu.client.controllers.responseWrappers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.veliasystems.menumenu.client.entities.BackUpBlobKey;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;

public class BackupWrapperSimply implements IsSerializable {
	
	@SerializedName("User")
	private List<User> users;
	@SerializedName("Restaurant")
	private List<Restaurant> restaurants;
	@SerializedName("ImageBlob")
	private List<ImageBlob> imageBlobs;
	@SerializedName("City")
	private List<City> cities;
	@SerializedName("BackUpBlobKey")
	private List<BackUpBlobKey> backups;
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Restaurant> getRestaurants() {
		return restaurants;
	}
	public void setRestaurants(List<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}
	public List<ImageBlob> getImageBlobs() {
		return imageBlobs;
	}
	public void setImageBlobs(List<ImageBlob> imageBlobs) {
		this.imageBlobs = imageBlobs;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public List<BackUpBlobKey> getBackups() {
		return backups;
	}
	public void setBackups(List<BackUpBlobKey> backups) {
		this.backups = backups;
	}
	public BackupWrapperSimply() {
		users = new ArrayList<User>();
		restaurants = new ArrayList<Restaurant>();
		cities = new ArrayList<City>();
		imageBlobs = new ArrayList<ImageBlob>();
		backups = new ArrayList<BackUpBlobKey>();
	}
	
}
