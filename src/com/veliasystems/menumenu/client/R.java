package com.veliasystems.menumenu.client;
/**
 * 
 * class with constants
 *
 */
public class R {

	public static boolean isMobile = true;
	
	public final static String TOKEN = "a1b2c3";
	public final static String GEOCODING_URL = "http://maps.google.com/maps/api/geocode/json?sensor=false&address=";
	public final static String DIRECTIONS_MAPS_URL = "http://maps.googleapis.com/maps/api/directions/json?sensor=false";
//	public static final String HOST_URL = "http://menutester.appspot.com/";
	public static final String HOST_URL = "http://menumenu-cms.appspot.com/";
	public static final String FIX_IMAGE_QUEUE_NAME = "imageResizeQueue";
	
	public static final String EMAIL_ADDRES = "mateusz@velia-systems.com";
//	public static final String EMAIL_ADDRES = "applisoleil@gmail.com";
	
	public final static String UTF8 = "UTF-8";
	public final static String LOGGED_IN = "loggedIn";
	public static final String LOADING = "loading";
	public static final String LOADED = "loaded";
	public static final String SHOW = "show";
	public static final String HIDE = "hide";
	
	// languages
    public static final String ARABIC_NAME = "Arabic";
    public static final String ARABIC_CODE = "ar";
    public static final String CHINESE_NAME = "Chinese";
    public static final String CHINESE_CODE = "zv";
    public static final String GERMAN_NAME = "Deutsch";
    public static final String GERMAN_CODE = "de";
    public static final String ENGLISH_NAME = "English";
    public static final String ENGLISH_CODE = "en";
    public static final String FRENCH_NAME = "French";
    public static final String FRENCH_CODE = "fr";
    public static final String POLISH_NAME = "Polski";
    public static final String POLISH_CODE = "pl";
    public static final String LANGUAGE = "gwtLocale";
    public static final String LOGIN = "Login";
    
    public static final String USER_AGENT = getUserAgent();
    
    private native static String getUserAgent()/*-{
		return navigator.userAgent;
	}-*/;

}
