package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

public class User implements Serializable {

	
	@Id
	private String email; //login
	private String phoneNumber;
	private String addedByUser;
	
	private String name;
	private String surname;
	private String password;
	private boolean isAdmin = false;
	
	private List<Long> restaurantsId = null;
	private List<Long> citiesId = null;
	
	/**
	 * email must be valid e-mail address. Check it before creating new user
	 * @param email
	 */
	public User( String email) {
		this.email = email;
	}

	private User() {
		// TODO Auto-generated constructor stub
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddedByUser() {
		return addedByUser;
	}

	public void setAddedByUser(String addedByUser) {
		this.addedByUser = addedByUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<Long> getRestaurantsId() {
		return restaurantsId;
	}

	public void setRestaurantsId(List<Long> restaurantsId) {
		this.restaurantsId = restaurantsId;
	}

	public List<Long> getCitiesId() {
		return citiesId;
	}

	public void setCitiesId(List<Long> citiesId) {
		this.citiesId = citiesId;
	}
	public void addRestaurantsId(Long restauarantId){
		this.restaurantsId.add(restauarantId);
	}
	public void addCitiesId(Long cityId){
		this.citiesId.add(cityId);
	}
	public void removeRestaurantsId(Long restaurantId){
		this.restaurantsId.remove(restaurantId);
	}
	public void removeCityId(Long cityId){
		this.citiesId.remove(cityId);
	}
	public String getEmail() {
		return email;
	}
	
}
