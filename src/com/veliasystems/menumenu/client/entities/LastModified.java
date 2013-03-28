package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;

import javax.persistence.Id;

public class LastModified implements Serializable{

	private static final long serialVersionUID = 4563682920871939421L;
	
	public static final String restaurantListPrefix = "restaurantList::";
	public static final String cityListIdString = "cityLists";
	
	@Id
	private String entityIdString;
	private long time = -1;
	
	private LastModified() {}
	
	/**
	 * When change is on the list of restaurant in city
	 * entityIdString should look like {@link LastModified#restaurantListPrefix}+cityId eg.: restaurantList::12345678
	 * 
	 * When change is on the list of City
	 * entityIdString should by: {@link LastModified#cityListIdString}
	 * 
	 * @param entityIdString
	 * @param time
	 */
	public LastModified(String entityIdString, long time){
		this.entityIdString=entityIdString;
		this.time=time;
	}
	
	public String getEntityIdString() {
		return entityIdString;
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
