package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;

import com.veliasystems.menumenu.client.Util;

public class Restaurant implements Serializable {

	private static final long serialVersionUID = -421655496163765996L;

	private long id;
	
	private String name;
	private String address;
	private String city;
	
	
	{
		id = Util.getRandom(9999999);
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