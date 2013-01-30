package com.veliasystems.menumenu.client;

public enum Days {
	MONDAY(Customization.MONDAY, 0),
	TUESDAY(Customization.TUESDAY, 1),
	WEDNESDAY(Customization.WEDNESDAY, 2),
	THURSDAY(Customization.THURSDAY, 3),
	FRIDAY(Customization.FRIDAY, 4),
	SATURDAY(Customization.SATURDAY, 5),
	SUNDAY(Customization.SUNDAY, 6);
	
	private String name;
	private int order;
	
	Days(String name, int order){
		this.name = name;
		this.order = order;
	}
	
	public String dayName(){
		return name;
	}
	public int getOrder(){
		return order;
	}

}
