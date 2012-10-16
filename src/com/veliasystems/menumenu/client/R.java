package com.veliasystems.menumenu.client;

public class R {

	public final static String TOKEN = "a1b2c3";
	public final static String GEOCODING_URL = "http://maps.google.com/maps/api/geocode/json?sensor=false&address=";
	
	public final static String UTF8 = "UTF-8";
	public final static String loggedIn = "loggedIn";
	public final static String lastPage = "lastPage";
	
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
    
    public static final String getHostName() {
    	String hostUrl; 
        String environment = System.getProperty("com.google.appengine.runtime.environment");
        if (environment.equalsIgnoreCase("Production")) {
            String applicationId = System.getProperty("com.google.appengine.application.id");
            String version = System.getProperty("com.google.appengine.application.version");
            //hostUrl = "http://"+version+"."+applicationId+".appspot.com/";
            hostUrl = "http://"+applicationId+".appspot.com/"; // without version
        } else {
            hostUrl = "http://localhost:8888";
        }
        return hostUrl;
    }
    
    
}
