
package com.veliasystems.menumenu.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.StoreService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class StoreServiceImpl extends RemoteServiceServlet implements StoreService {

	private DAO dao = new DAO();
	private BlobService blobService = new BlobServiceImpl();
	
	@Override
	public List<String> loadCities() {
		List<String> cities = new ArrayList<String>();
		
			cities.add("Krakow-Kazimierz");
			cities.add("Paris - Marais");
			cities.add("Paris - Beaubourg");
			cities.add("Paris - Louvre");
			
		return cities;
	}
	
	
	@Override
	public void fillWithData( ) {
		List<Restaurant> rests = getData();
		for (Restaurant r : rests) {
			saveRestaurant(r,true);
		}
	}
	
	private void saveRestaurant(Restaurant r, boolean trimCity) {
		try {
			getGeocoding(r,trimCity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		dao.ofy().put(r);
	}
	
	
	private List<Restaurant> getData() {
		List<Restaurant> r = new ArrayList<Restaurant>();
		
		r.add( new Restaurant("Au Gourmand", "17, Rue Molière", "Paris - Beaubourg") );
		r.add( new Restaurant("Au Père Fouettard", "Rue Pierre Lescot" , "Paris - Beaubourg") );
		r.add( new Restaurant("Flottes", "2, Rue Cambon", "Paris - Louvre") );
		r.add( new Restaurant("Le Baudelaire", "8, Rue Duphot", "Paris - Louvre"));
		r.add( new Restaurant("Le Bistrot Mavrommatis", "18, Rue Duphot", "Paris - Louvre"));
		r.add( new Restaurant("Le Pharamond", "24, rue de la Grande Truanderie" , "Paris - Beaubourg"));
		r.add( new Restaurant("Les Fontaines Saint Honoré", "196, Rue Saint Honoré" , "Paris - Louvre"));
		r.add( new Restaurant("Quai-Quai", "74, quai des Orfèvres", "Paris - Louvre"));
		r.add( new Restaurant("Rotin's Home", "10, Rue des Pyramides" , "Paris - Louvre"));
		r.add( new Restaurant("Aux Lyonnais" , "32, rue St Marc" , "Paris - Beaubourg"));
		r.add( new Restaurant("Bistrot Favart", "1, Rue Favart" , "Paris - Beaubourg"));
		r.add( new Restaurant("Chez Papa", " Rue Montmartre" , "Paris - Beaubourg"));
		r.add( new Restaurant("Divinamente Italiano", " 28, Rue Notre Dame des Victoires" , "Paris - Beaubourg"));
		r.add( new Restaurant("Le Mesturet", "Rue de Richelieu " , "Paris - Beaubourg"));
		r.add( new Restaurant("Les Alchimistes", "16, Rue Favart", "Paris - Beaubourg"));
		r.add( new Restaurant("Liza", "14, rue de la Banque", "Paris - Beaubourg"));
		r.add( new Restaurant("Mori Venice Bar", "27, rue Vivienne", "Paris - Beaubourg"));
		r.add( new Restaurant("Zinc Opéra", "8, Rue de Hanovre", "Paris - Beaubourg"));
		r.add( new Restaurant("Refletsduliban",  "25 Rue de Brantôme", "Paris - Marais"));
		r.add( new Restaurant("Benoit", "20, rue Saint Martin", "Paris - Marais"));
		r.add( new Restaurant("Chez Oscar",  "11-13 , bd. Beaumarchais",  "Paris - Marais"));
		r.add( new Restaurant("Icho, Izakaya a la francaise", " 3, rue des Tournellles", "Paris - Marais"));
		r.add( new Restaurant("Le Colimaçon", " 44, rue Vieille du Temple", "Paris - Marais"));
		r.add( new Restaurant("Bouilion - Chartier", "rue du Faubourg Montmartre", "Paris - Beaubourg"));
		r.add( new Restaurant("Bar a vins Mesturet", "77, rue de Richelieu", "Paris - Beaubourg"));
		r.add( new Restaurant("L’Ami Georges", "5 rue du 4 Septembre", "Paris - Beaubourg"));
		r.add( new Restaurant("Osteria Ruggera", "35, rue Tiquetonne", "Paris - Beaubourg"));
		
		r.add( new Restaurant("Zazie Bistro", "ul. Józefa 34", "Krakow-Kazimierz"));
		r.add( new Restaurant("Les Scandales","Plac Nowy 9","Krakow-Kazimierz"));
		r.add( new Restaurant("Piwnica pod kominkiem","ul. Gołębia 6","Krakow-Kazimierz"));
		r.add( new Restaurant("Restauracja Korsykańska", "ul. Poselska 24","Krakow-Kazimierz"));
		r.add( new Restaurant("Starka", "ul. Józefa 14","Krakow-Kazimierz"));
		r.add( new Restaurant("Stukot Bar","ul. Berka Joselewicza 19","Krakow-Kazimierz"));
		r.add( new Restaurant("Trattoria Pistola","ul. Bożego Ciała 4","Krakow-Kazimierz"));
		r.add( new Restaurant("Trzy Papryczki",  "ul. Poselska 3","Krakow-Kazimierz"));
		r.add( new Restaurant("Paragraf",  "ul. Olszewskiego 2","Krakow-Kazimierz"));
		r.add( new Restaurant("Magiel",   "ul. Beera Meiselsa 9","Krakow-Kazimierz"));
		r.add( new Restaurant("Bombonierka",  "ul. Beera Meiselsa 24","Krakow-Kazimierz"));
		r.add( new Restaurant("Kuchnia i wino",  "ul. Józefa 13","Krakow-Kazimierz"));
		r.add( new Restaurant("Al. Dente",  "ul. Kupa 12","Krakow-Kazimierz"));
		r.add( new Restaurant("Pimiento",   "ul. Józefa 26","Krakow-Kazimierz"));
		r.add( new Restaurant("Wręga Klub",   "ul. Józefa 17","Krakow-Kazimierz"));
		r.add( new Restaurant("Pepe Rosso",   "ul. Kupa 15","Krakow-Kazimierz"));
		r.add( new Restaurant("Luminaa",  "ul. Kupa 24","Krakow-Kazimierz"));
		r.add( new Restaurant("Tajemniczy Ogród",  "Plac Nowy 3","Krakow-Kazimierz"));
		r.add( new Restaurant("Kolanko",  "ul. Józefa 6",	"Krakow-Kazimierz"));
		r.add( new Restaurant("La Petite France",  "ul. Św. Tomasza 15",	"Krakow-Kazimierz"));
		r.add( new Restaurant("Chili&Cynamon Lunch Bar",  "ul. Berka Joselewicza 9",	"Krakow-Kazimierz"));
		r.add( new Restaurant("Deli Bar - rest. węgierska",  "ul. Beera Meiselsa 5","Krakow-Kazimierz"));
		r.add( new Restaurant("Barfly Cafe Bar", 	 "Plac Nowy 4","Krakow-Kazimierz"));
		r.add( new Restaurant("Marchewka z Groszkiem",  "ul. Mostowa 2","Krakow-Kazimierz"));
		r.add( new Restaurant("Sąsiedzi", "ul. Miodowa 25","Krakow-Kazimierz"));
		r.add( new Restaurant("Studnia Życzeń", "ul. Beera Meiselsa 24/Plac Nowy 6",	"Krakow-Kazimierz"));
		r.add( new Restaurant("Satori Cafe Bistro",   "ul. Józefa 25",	"Krakow-Kazimierz"));
		r.add( new Restaurant("Bordo",   "ul. Gołębia 3",	"Krakow-Kazimierz"));
		r.add( new Restaurant("Bohema",  "ul.  Gołębia 2",		"Krakow-Kazimierz"));
		r.add( new Restaurant("Awiw" ,  "ul. Wielopole 3",	"Krakow-Kazimierz"));
		r.add( new Restaurant("Art. Club Cieplarnia",   "ul. Bracka 15",	"Krakow-Kazimierz"));
		r.add( new Restaurant("Cieplarnia" ,	 "Plac Historyczny Galeria Kazimierz, ul. Podgórska 24",	"Krakow-Kazimierz"));
		r.add( new Restaurant("Podkowa",  "Pl. Wolnica 10",	"Krakow-Kazimierz"));
		
		return r;
	}
	
	
	
	@Override
	public void saveRestaurant(Restaurant r) {
		try {
			getGeocoding(r,false);
//			System.out.println( "AFTER GEOCODING: " +  r.toString() );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		dao.ofy().put(r);
//		System.out.println("saved succes: " + r.getName());
	}
	
	private void getGeocoding( Restaurant r, boolean trimCity ) throws Exception {
	//	Geocoder.setConnectionManager(new SimpleHttpConnectionManager());
		final Geocoder geocoder = new Geocoder();
		
		GeocoderRequest geocoderRequest;
		
		if (trimCity) {
			String[] split = r.getCity().split("-");
			String city = split[0].trim();
			System.out.println("City set to: " + city);
			
			geocoderRequest = new GeocoderRequestBuilder().setAddress(r.getAddress() + "," + city).setLanguage("en").getGeocoderRequest();
		} else {
			geocoderRequest = new GeocoderRequestBuilder().setAddress(r.getAddress() + "," + r.getCity()).setLanguage("en").getGeocoderRequest();
		}
		
		
		
		
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		
		List<GeocoderResult> results = geocoderResponse.getResults();
		if (results!=null && !results.isEmpty()) {
			
			// try to pick the result that have 'sublocality'
			for (GeocoderResult res : results) {
				for (GeocoderAddressComponent comp : res.getAddressComponents()) {
					for (String type : comp.getTypes() ) {
						if (type.equals("sublocality")) {
							// this is the ONE!!!!
							
							String lat = "" + res.getGeometry().getLocation().getLat();
							String lng = "" + res.getGeometry().getLocation().getLng();
							r.setLat(lat);
							r.setLng(lng);
							r.setDistrict(comp.getShortName());
							return;
						}
					}
				}
			}
			
			
			// pick 1st one otherwise
			GeocoderResult result = results.get(0); 
			
			// get gps coords for Restaurant
			String lat = "" + result.getGeometry().getLocation().getLat();
			String lng = "" + result.getGeometry().getLocation().getLng();
			r.setLat(lat);
			r.setLng(lng);
			
			List<GeocoderAddressComponent> addrComps = result.getAddressComponents();
			for (GeocoderAddressComponent comp : addrComps) {
				
				for (String type : comp.getTypes() ) {
					if (type.equals("locality")) {
						r.setDistrict(comp.getShortName());
						return;
					}
				}
			}
			
		}
		else System.out.println("No Geocoding results for " + r.getAddress() + ", " + r.getCity());
		
	}
	
	private void getGeocodingOld( Restaurant r ) throws Exception {
		String queryString = r.getCity().trim();
		
		String[] adrs = r.getAddress().split(",");
		for (String s : adrs) {
			queryString += "+" + s.trim();
		}
		
		URL url = new URL( R.GEOCODING_URL + queryString );

		HTTPRequest newReq = new HTTPRequest( url, HTTPMethod.GET, FetchOptions.Builder.withDeadline(30*1000) );                               
		//newReq.setPayload(("token=" + token + "&" + "url="+url).getBytes());

		URLFetchService  service = URLFetchServiceFactory.getURLFetchService();
		HTTPResponse response = service.fetch(newReq);
		byte[] byteContent = response.getContent();
		
		String content = new String(byteContent, R.UTF8).trim();
		
	}
	
	
	@Override
	public List<Restaurant> loadRestaurants() {
		
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if (restQuery==null) return new ArrayList<Restaurant>();
		
		return getImageLists( restQuery.order("name").list() );  
	}
	
	@Override
	public List<Restaurant> loadRestaurants( String city ) {
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if (restQuery==null) return new ArrayList<Restaurant>();
		System.out.println(restQuery.filter("city =", city).order("name").count());
		return getImageLists( restQuery.filter("city =", city).order("name").list() );
	}
	
	private List<Restaurant> getImageLists( List<Restaurant> restaurants ) {
		
		for ( Restaurant r : restaurants ) {
			List<ImageBlob> images = blobService.getAllImages(r);
			
			List<ImageBlob> logoImages = new ArrayList<ImageBlob>();
			List<ImageBlob> menuImages = new ArrayList<ImageBlob>();
			List<ImageBlob> profileImages = new ArrayList<ImageBlob>();
			
			for ( ImageBlob blob : images ) {
				
				switch (blob.getImageType()) {
				case LOGO : {
								logoImages.add(blob);
								break; }
				case MENU : {
								menuImages.add(blob);
								break; }
				case PROFILE : {
								profileImages.add(blob);
								break; }
				default : System.out.println("Unknown ImageType found: " + blob.getImageType().name());
				}
			}
			
			r.setLogoImages(logoImages);
			r.setMenuImages(menuImages);
			r.setProfileImages(profileImages);
		}
		
		return restaurants;
	}

	
	@Override
	public void deleteRestaurant(Restaurant r) {
		dao.ofy().delete(r);
		
	}
	
	@Override
	public void clearStore() {
		dao.ofy().delete( loadRestaurants() );
	}

	@Override
	public void setMainImage(ImageBlob imageBlob) {
		// TODO Auto-generated method stub
		
		Long restId = Long.valueOf(imageBlob.getRestaurantId());
		
		Restaurant r =dao.ofy().query(Restaurant.class).filter("id =", restId).get();
		
//		switch(imageBlob.getImageType()){
//			case LOGO:
//				r.setMainLogoImage(imageBlob);
//				break;
//			case MENU:
//				r.setMainMenuImage(imageBlob);
//				break;
//			case PROFILE:
//				r.setMainProfileImage(imageBlob);
//				break;
//			
//		}
//		
//		dao.ofy().put(r);
		
		System.out.println(r.getName() + " " + r.getId() + " " + r.getCity());
		
		System.out.println(imageBlob.getImageType());
		
		switch(imageBlob.getImageType()){
			case PROFILE:
				r.setMainProfileImageString(imageBlob.getImageUrl());
				break;
			case LOGO:
				r.setMainLogoImageString(imageBlob.getImageUrl());
				break;
			case MENU:
				r.setMainMenuImageString(imageBlob.getImageUrl());
				break;
		}
		
		dao.ofy().put(r);
		
	}


	@Override
	public String uploadRestaurants(String JSON) {
		
	String response = "";
	
		try{
		    JsonObject object = (JsonObject) new com.google.gson.JsonParser().parse(JSON);
		    Set<Map.Entry<String, JsonElement>> set = object.entrySet();
		    Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();

		    Hashtable<String, Restaurant> map = new Hashtable<String, Restaurant>();

		    while (iterator.hasNext()) {
		        Map.Entry<String, JsonElement> entry = iterator.next();

		        String key = entry.getKey();
		        Restaurant value = new Gson().fromJson(entry.getValue(), Restaurant.class);

		        if (value != null) {
		            map.put(key, value);
		        }

		    }   
		    
		  Set<String> restSet=  map.keySet();
		  
		  List<String> city = loadCities();
		  
		  List<Restaurant> restaurants= loadRestaurants();
		  
		  
		 
		  for (String item : restSet) {
			  
			 if(city.contains(map.get(item).getCity())){
				 
				 	if(!restaurants.contains(map.get(item))){
				 		saveRestaurant(map.get(item), true);
				 	}
				 	else{
				 		response += map.get(item).getName() + " " + Customization.DOUBLE_RESTAURANT + "\n";
				 	}
				 
			 }else{
			  response +=  map.get(item).getName() + " " + Customization.CITY_ERROR + "\n";
			 }	
			  
			 
			  
		  }
		}
		catch (Exception e) {
			// TODO: handle exception
			return e.toString();
		}
		return response;
	}
	
	
	
}
