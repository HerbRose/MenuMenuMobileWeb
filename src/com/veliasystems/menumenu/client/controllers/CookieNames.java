package com.veliasystems.menumenu.client.controllers;

import org.omg.DynamicAny.NameValuePairSeqHelper;

public enum CookieNames {
	
	//The data (written by user when adding new restaurant) required to save when picup is using 
	/**
	 * Used when adding new restaurant and picup is using
	 */
	IS_PICUP_USED,
	/**
	 * The restaurant's name (written by user when adding new restaurant) required to save when picup is using 
	 */
	R_NAME,
	/**
	 * The restaurant's address (written by user when adding new restaurant) required to save when picup is using 
	 */
	R_ADDRESS,
	/**
	 * The restaurant's phone (written by user when adding new restaurant) required to save when picup is using 
	 */
	R_PHONE,
	/**
	 * The restaurant's page (written by user when adding new restaurant) required to save when picup is using 
	 */
	R_WWW,
	/**
	 * The restaurant's boss name (written by user when adding new restaurant) required to save when picup is using 
	 */
	R_BOSS_NAME,
	/**
	 * The added users (written by user when adding new restaurant) required to save when picup is using 
	 */
	R_USERS,
	/**
	 * The city id required to save when picup is using 
	 */
	R_CITY_ID,
	// END - new restaurant data
	/**
	 * restaurant id that was last time open, also used to save restaurant id when adding new restaurant and is picup using,
	 * <strong>WORNING</strong> if you change the enum name don't forget change cookie name in <code>picupCollback.html</code> 
	 */
	RESTAURANT_ID,
	/**
	 * used when is downloading photo by picup
	 * <strong>WORNING</strong> if you change the enum name don't forget change cookie name in <code>picupCollback.html</code> 
	 */
	IMAGE_TYPE,
	/**
	 * used when is downloading photo by picup if <code>RESTAURANT_ID_PICUP</code> is set the <code>RESTAURANT_ID</code> is set in <code>picupCollback.html</code> 
	 * <strong>WORNING</strong> if you change the enum name don't forget change cookie name in <code>picupCollback.html</code> 
	 */
	RESTAURANT_ID_PICUP,
	/**
	 * used when is downloading photo by picup, if <code>IMAGE_TYPE_PICUP</code> is set the <code>IMAGE_TYPE</code> is set in <code>picupCollback.html</code> 
	 * <strong>WORNING</strong> if you change the enum name don't forget change cookie name in <code>picupCollback.html</code> 
	 */
	IMAGE_TYPE_PICUP,

	
}
