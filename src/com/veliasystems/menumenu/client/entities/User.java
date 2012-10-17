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
	
	
	
}
