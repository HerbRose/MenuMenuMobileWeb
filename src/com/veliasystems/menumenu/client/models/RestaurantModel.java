package com.veliasystems.menumenu.client.models;

public class RestaurantModel {

	private String name;
	private String city;
	private String adress;
	
	public RestaurantModel() {
		// TODO Auto-generated constructor stub
	}
	
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
