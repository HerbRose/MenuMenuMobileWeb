package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
/**
 * User who was added to database but not confirm yet. 
 * This is not the {@link User} entity. This is used to store information about users which was added (given email) by existing user in system and the message was send 
 * but they not confirm. If user confirm given email this entity should by deleted. 
 *
 */
public class UserToAdd implements Serializable {

	private static final long serialVersionUID = 5694392315830957411L;
	
	@Id
	/**
	 * The user email (and login)
	 */
	private String email; //login
	/**
	 * an id generated when user account was creating. 
	 */
	private String confirmId;
	
	/**
	 * Added date in milliseconds
	 */
	private long addedDete = new Date().getTime();
	/**
	 * email must be valid e-mail address. Check it before creating new user
	 * @param email
	 */
	public UserToAdd( String email) {
		this.email = email;
	}

	private UserToAdd() {} // not used

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmId() {
		return confirmId;
	}

	public void setConfirmId(String confirmId) {
		this.confirmId = confirmId;
	}

	public long getAddedDete() {
		return addedDete;
	}
	
}
