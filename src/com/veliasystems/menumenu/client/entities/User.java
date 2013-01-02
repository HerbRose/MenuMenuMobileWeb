package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import com.veliasystems.menumenu.client.controllers.UserType;
/**
 * Use as entity in database
 * <br/>
 * User class contains:
 * <ul>
 * <li>email</li>
 * <li>phone number</li>
 * <li>name</li>
 * <li>surname</li>
 * <li>password</li>
 * <li>isAdmin</li>
 * <li>List of {@link Restaurant} id's</li>
 * <li>List of {@link City} id's</li>
 * </ul>
 *
 */
public class User implements Serializable {


	private static final long serialVersionUID = 1470907443425339486L;
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
	}
	/**
	 * 
	 * @return phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * Phone number must be valid phone number. Check it before set
	 * @param phoneNumber 
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * 
	 * @return get {@link User} email address, who add given {@link User} to system
	 */
	public String getAddedByUser() {
		return addedByUser;
	}
	/**
	 *  email must be valid e-mail address. Check it before create
	 * @param addedByUser - email of {@link User}
	 */
	public void setAddedByUser(String addedByUser) {
		this.addedByUser = addedByUser;
	}
	/**
	 * 
	 * @return name of {@link User}
	 */
	public String getName() {
		return name;
	}
	/**
	 * set {@link User} name
	 * @param name - name of {@link User}
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return {@link User} surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * set {@link User} surname
	 * @param surname - {@link User} surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * 
	 * @return {@link User} password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * set {@link User} password
	 * @param password - {@link User} password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 
	 * @return <strong>true</strong> if {@link User} is admin, <strong>false</strong> if not
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
	/**
	 * set admin boolean value, default to <strong>false</strong>
	 * @param isAdmin - <strong>true</strong> if user is admin, <strong>false</strong> if not
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	/**
	 * 
	 * @return List of {@link Restaurant}'s id's
	 */
	public List<Long> getRestaurantsId() {
		return restaurantsId;
	}
	/**
	 * Should be called if {@link UserType} is <strong>RESTAURATOR</strong>
	 * <br/>
	 * <br/>
	 * set {@link Restaurant}'s id's to {@link User}
	 * @param restaurantsId - List of {@link Restaurant} id's
	 */
	public void setRestaurantsId(List<Long> restaurantsId) {
		this.restaurantsId = restaurantsId;
	}
	/**
	 * 
	 * @return List of {@link City} id's
	 */
	public List<Long> getCitiesId() {
		return citiesId;
	}
	/**
	 * Should be called if {@link UserType} is <strong>AGENT</strong>
	 * <br/>
	 * <br/>
	 * set {@link City} id's to {@link User}
	 * @param citiesId - List of {@link City} id's
	 */
	public void setCitiesId(List<Long> citiesId) {
		this.citiesId = citiesId;
	}
	/**
	 *Should be called if {@link UserType} is <strong>RESTAURATOR</strong>
	 * <br/>
	 * <br/>
	 * add {@link Restaurant} id to {@link User}
	 * @param restauarantId - id of {@link Restaurant}
	 */
	public void addRestaurantsId(Long restauarantId){
		this.restaurantsId.add(restauarantId);
	}
	/**
	 * Should be called if {@link UserType} is <strong>AGENT</strong>
	 * <br/>
	 * <br/>
	 * add {@link City} id to {@link User}
	 * @param cityId - id of {@link City}
	 */
	public void addCitiesId(Long cityId){
		this.citiesId.add(cityId);
	}
	/**
	 *Should be called if {@link UserType} is <strong>RESTAURATOR</strong>
	 * <br/>
	 * <br/>
	 * remove {@link Restaurant} id to {@link User}
	 * @param restaurantId - id of {@link Restaurant}
	 */
	public void removeRestaurantsId(Long restaurantId){
		this.restaurantsId.remove(restaurantId);
	}
	/**
	 * Should be called if {@link UserType} is <strong>AGENT</strong>
	 * <br/>
	 * <br/>
	 * remove {@link City} id to {@link User}
	 * @param cityId - id of {@link City}
	 */
	public void removeCityId(Long cityId){
		this.citiesId.remove(cityId);
	}
	/**
	 * get email address of {@link User}
	 * @return String
	 */
	public String getEmail() {
		return email;
	}
	
}
