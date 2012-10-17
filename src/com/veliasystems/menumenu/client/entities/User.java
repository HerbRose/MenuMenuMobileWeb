package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

public class User implements Serializable {

	
	@Id
	private String email; //login
	
	
	
	private String name;
	private String surname;
	private String password;
	
	
	private List<Long> restaurantsId;
	
	/**
	 * email must be valid e-mail address. Check it before creating new user
	 * @param email
	 */
	public User( String email) {
		this.email = email;
	}
	
}
