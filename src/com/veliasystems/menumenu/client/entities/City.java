package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;

import javax.persistence.Id;

public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id private Long id;
	private String city;
	

	 {
			id = ((long) (Math.random() * 999999999));
	 }
	
	public City() {
		// TODO Auto-generated constructor stub
	}
	 
	public String getCity() {
		return city;
	}
	
	
	public void setCity(String city) {
		this.city = city;
	}
	
	
	
	
}
