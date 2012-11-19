package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;

import javax.persistence.Id;

import com.google.gwt.user.client.ui.Image;

public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id private Long id;
	private String city;

	private boolean isVisable = false;
	

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
	
	
	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof City)) return false;
		
		City c = (City) obj;
		
		if(this.getCity().equalsIgnoreCase(c.getCity())){
			return true;
		}
		return false;
	}
	
	public void setVisable(boolean isVisable) {
		this.isVisable = isVisable;
	}
	public boolean isVisable() {
		return isVisable;
	}
}
