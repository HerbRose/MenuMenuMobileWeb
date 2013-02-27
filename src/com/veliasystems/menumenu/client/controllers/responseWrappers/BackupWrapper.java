package com.veliasystems.menumenu.client.controllers.responseWrappers;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.veliasystems.menumenu.client.entities.BackUpBlobKey;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;

public class BackupWrapper implements IsSerializable {
	
	private List<User> users;
	private List<Restaurant> restaurants;
	private List<ImageBlob> imageBlobs;
	private List<City> cities;
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
	public BackupWrapper() {
	}
	
	@Override
	public String toString() {
		return "[ Size of cities: " + this.cities.size() + " ; Size of restaurants: " + this.restaurants.size() + " ; Size of images blobs: " + this.imageBlobs.size() + " ; Size of users: " + this.users.size() + " ; Size of backups: " + this.backups.size() + " ]";
	}
	
}
