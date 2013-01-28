package com.veliasystems.menumenu.client;

public enum Days {
	MONDAY(Customization.MONDAY),
	TUESDAY(Customization.TUESDAY),
	WEDNESDAY(Customization.WEDNESDAY),
	THURSDAY(Customization.THURSDAY),
	FRIDAY(Customization.FRIDAY),
	SATURDAY(Customization.SATURDAY),
	SUNDAY(Customization.SUNDAY);
	
	private String name;
	
	
	Days(String name){
		this.name = name;
	}
	
	public String dayName(){
		return name;
	}
}
