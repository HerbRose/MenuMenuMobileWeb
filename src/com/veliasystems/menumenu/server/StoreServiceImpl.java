
package com.veliasystems.menumenu.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
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
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.StoreService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class StoreServiceImpl extends RemoteServiceServlet implements StoreService {

	private DAO dao = new DAO();
	private BlobServiceImpl blobService = new BlobServiceImpl();
	private EmailServiceImpl emailService = new EmailServiceImpl();
//	private BlobstoreService blobstoreService = BlobstoreServiceFactory
//			.getBlobstoreService();
	private static final Logger log = Logger.getLogger(StoreServiceImpl.class.getName()); 
	
	@Override
	public List<String> loadCities() {
		
		Query<City> cityQuery = dao.ofy().query(City.class);
		
		if(cityQuery == null) return new ArrayList<String>();
		
		List<String> cityListString = new ArrayList<String>();
		
		List<City> cityList = cityQuery.list();
		
		for (City city : cityList) {
			cityListString.add(city.getCity());
		}
		
		return cityListString;
	}
	
//	public City loadCitie(Long cityId) {
//		
////		Query<City> cityQuery = dao.ofy().query(City.class);
////		
////		if(cityQuery == null) return null;
//		
//		City city = dao.ofy().find(City.class, cityId); //cityQuery.filter("id", cityId).get();
//		
//		return city;
//	}
	/**
	 * 
	 * @param cityId - id of {@link City} to find
	 * @return Single {@link City} object or null if is not found
	 */
	public City findCity(Long cityId){
		return dao.ofy().find(City.class, cityId);
	}
	public User findUser(String email){
		return dao.ofy().find(User.class, email);
	}
	public ImageBlob findImageBlob(BlobKey blobKey){
		Query<ImageBlob> query = dao.ofy().query(ImageBlob.class);
		
		if (query == null) {
			return new ImageBlob();
		}
		
		return query.filter("blobKey", blobKey.getKeyString()).get();
	}
	public ImageBlob findImageBlob(String blobKey){
		Query<ImageBlob> query = dao.ofy().query(ImageBlob.class);
		
		if (query == null) {
			return new ImageBlob();
		}
		
		return query.filter("blobKey", blobKey).get();
	}
	/**
	 * 
	 * @param user - Logged {@link User}
	 * @return List of {@link City} available for the {@link User}
	 */
	private List<City> loadCitiesForUser(User user){
		
		List<Long> tmpList = user.getCitiesId();
		
		Query<City> cityQuery = dao.ofy().query(City.class);
		if(cityQuery == null) return null;
		List<City> listCities = cityQuery.filter("id IN", tmpList).list();
		if(listCities == null) return new ArrayList<City>();
		return listCities;
		
	}
	/**
	 * 
	 * @param restList - List of {@link Restaurant} 
	 * @return List of {@link City} found by list of {@link Restaurant}
	 * <br/>
	 * <br/>
	 * <strong>Method used to getting data for user</strong>
	 */
	private List<City> loadCitiesByRestaurant(List<Restaurant> restList){
		Set<Long> citiesId = new HashSet<Long>();		
		for (Restaurant restaurant : restList) {
			citiesId.add(restaurant.getCityId());
		}	
		Query<City> cityQuery = dao.ofy().query(City.class);
		if(cityQuery == null) return null;
		List<City> cityList = cityQuery.filter("id IN", citiesId).list();
		if(cityList == null) return new ArrayList<City>();
		return cityList;	
	}
	private ImageBlob loadImageBlob(String imageBlobKeyString) {
		Query<ImageBlob> imageBlobQuery = dao.ofy().query(ImageBlob.class);
		
		if(imageBlobQuery == null){
			return null;
		}
		return imageBlobQuery.filter("blobKey", imageBlobKeyString).get();
	}
	/**
	 * 
	 * @param citiesList - List of {@link City}
	 * @return List of {@link Restaurant} found by {@link City} list
	 * <br/>
	 * <br/>
	 * <strong>Method used to getting data for user</strong>
	 */
	private List<Restaurant> loadRestaurantsByCities(List<City> citiesList){
		Set<Long> restaurantsId = new HashSet<Long>();
		for (City city : citiesList) {
			restaurantsId.add(city.getId());
		}
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if(restQuery == null) return null;
		//System.out.println("StoreServiceImpl::loadRestaurantsByCities(List<City> citiesList). restaurantsId.size()= " + restaurantsId.size());
		List<Restaurant> restList = restQuery.filter("cityId in", restaurantsId).list();
		return getImageLists(restList);
	} 
	
	@Override
	public List<City> loadCitiesEntity() {
		return dao.ofy().query(City.class).list();
	}
	
	
	@Override
	public void fillWithData( ) {
		List<Restaurant> rests = getData();
		for (Restaurant r : rests) {
			saveRestaurant(r,true);
		}
	}
	/**
	 * 
	 * @param r - {@link Restaurant} to save
	 * @param trimCity - boolean is {@link City} must be trimmed from "city - district" naming convention
	 */
	private void saveRestaurant(Restaurant r, boolean trimCity) {
		try {
			getGeocoding(r,trimCity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		dao.ofy().put(r);
	}
	/**
	 * @deprecated
	 * @return List of test {@link Restaurant}
	 */
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
		
		r.add( new Restaurant("Zazie Bistro", "ul. Józefa 34", "Krakow-KazimsaveRestaurantierz"));
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
	public void saveRestaurants(List <Restaurant> restaurants){
		
		for (Restaurant restaurant : restaurants) {
			restaurant.setLogoImages(null);
			restaurant.setMenuImages(null);
			restaurant.setProfileImages(null);
		}
		
		dao.ofy().put(restaurants);
	}
	
	@Override
	public List<Object> addNewRestaurant(Restaurant r, List<String> usersEmailToAdd, String emailAddingUser) {
		try {
			getGeocoding(r,false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		r.setLogoImages(null);
		r.setMenuImages(null);
		r.setProfileImages(null);
		List<Object> returnList = new ArrayList<Object>();//lista zwracana do przeglądarki z restauracją i informacją
		
		boolean isAdded = false;
		
		if(usersEmailToAdd == null)usersEmailToAdd = new ArrayList<String>();
		
		for (String userEmail : usersEmailToAdd) {
			User user = findUser(userEmail);
			
			if(user == null){
				user = new User(userEmail);

				int i = (int) (Math.random() * 100);
				
				user.setPassword(getLoginFromMail(userEmail)+i);
				if(user.getRestaurantsId() == null){
					user.setRestaurantsId(new ArrayList<Long>());
				}
				user.getRestaurantsId().add(r.getId());
				user.setAddedByUser(emailAddingUser);
				sendMailToUserAfterAddingRestaurant(true, user, r.getName());
				dao.ofy().put(user);
				isAdded = true;
			}else{
				if(user.getRestaurantsId() == null){
					user.setRestaurantsId(new ArrayList<Long>());
				}
				user.getRestaurantsId().add(r.getId());
				sendMailToUserAfterAddingRestaurant(false, user, r.getName());
				dao.ofy().put(user);
				isAdded = true;
			}
		}
		
		String mainLogoImageString = r.getMainLogoImageString();
		if( mainLogoImageString != null && !mainLogoImageString.isEmpty() ){
			String[] imageBlobStringSplitResult = mainLogoImageString.split("=");
			if(imageBlobStringSplitResult.length>1){
				String imageBlobString = imageBlobStringSplitResult[1];
				
				ImageBlob imageBlob = loadImageBlob(imageBlobString);
				if(imageBlob != null){
					imageBlob.setRestaurantId(r.getId()+"");
					dao.ofy().put(imageBlob);
				}else{
					r.setMainLogoImageString("");
				}
			}
		}
		
		dao.ofy().put(r);
		String responseMessage = "New restaurant added ";
		if(isAdded){
			responseMessage += "\nThe message was sent to added user"; 
		}
		
		
		returnList.add(r);
		returnList.add(responseMessage);
		
		return returnList;
	}
	

	private void sendMailToUserAfterAddingRestaurant(boolean isNew, User user, String restaurantName){
		String userName = user.getName();
		if(userName == null || userName.isEmpty()){
			userName = getLoginFromMail(user.getEmail());
		}
		String subject = "Message from website MenuMenu";
		String message = "Hello "+userName+". \n\n";
		
		if(isNew){
			message += "This email address has been given during registration process on MenuMenu website.\n\n"+
					   "Your data needed to login are:\n"+
					   "\tlogin: "+ user.getEmail()+"\n"+
					   "\tpassword: "+ user.getPassword()+"\n\n"+
					   "Remember about changing your password in administration panel.\n\n"+
					   "Now you have granted access to following new restaurant: "+ restaurantName +"\n\n"+
					   "Thank you: MenuMenu team.\n\n"+
					   "This email has been generated automatically. Please do not reply to this email address."; 
		}else{
			message += "Now you have granted access to following new restaurant: "+ restaurantName +"\n\n"+
					   "Thank you: MenuMenu team.\n\n"+
					   "This email has been generated automatically. Please do not reply to this email address."; 
		}
		List<String> toAddress = new ArrayList<String>();
		toAddress.add(user.getEmail());
		emailService.sendEmail(toAddress, userName, message, subject);
	}
	
	private String getLoginFromMail(String email){
		return  ( email.split("@") )[0];
	}
	
	@Override
	public Restaurant saveRestaurant(Restaurant r) {
		try {
			getGeocoding(r,false);
//			System.out.println( "AFTER GEOCODING: " +  r.toString() );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		r.setLogoImages(null);
		r.setMenuImages(null);
		r.setProfileImages(null);
//		r.setCityId(getCityId(r.getCity()));
		dao.ofy().put(r);
		return r;
//		System.out.println("saved succes: " + r.getName());
	}

	
//	private Long getCityId(String city){
//		
//		City newCity = dao.ofy().query(City.class).filter("city", city).get();
//		return newCity.getId();
//	}
	/**
	 * 
	 * @param r - {@link Restaurant}
	 * @param trimCity - boolean is {@link City} must be trimmed from "city - district" naming convention
	 * @throws Exception
	 */
	private void getGeocoding( Restaurant r, boolean trimCity ) throws Exception {
	//	Geocoder.setConnectionManager(new SimpleHttpConnectionManager());
		final Geocoder geocoder = new Geocoder();
		
		GeocoderRequest geocoderRequest;
		
		if (trimCity) {
			String[] split = r.getCity().split("-");
			String city = split[0].trim();
			//System.out.println("City set to: " + city);
			
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
		else log.warning("No Geocoding results for " + r.getAddress() + ", " + r.getCity());
		
		
	}
	/**
	 * @deprecated
	 * @param r - {@link Restaurant}
	 * @throws Exception 
	 */
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
	/**
	 * 
	 * @return List of keys {@link Restaurant}
	 */
	public List<Key<Restaurant>> loadRestaurantsId() {
		
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if(restQuery == null) return null;
		return restQuery.listKeys();
	}
	
	@Override
	public List<Restaurant> loadRestaurants( String city ) {
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if (restQuery==null) return new ArrayList<Restaurant>();
		//System.out.println(restQuery.filter("city =", city).order("name").count());
		return getImageLists( restQuery.filter("city =", city).order("name").list() );
	}
	/**
	 * 
	 * @param cityId - id of {@link City}
	 * @return List of {@link Restaurant} 
	 */
	public List<Restaurant> loadRestaurants( Long cityId ) {
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if (restQuery==null) return new ArrayList<Restaurant>();
		//System.out.println(restQuery.filter("city =", city).order("name").count());
		return getImageLists( restQuery.filter("cityId =", cityId).list() );
	}
	
	@Override
	public Restaurant loadRestaurant( Long id ){
		
		Restaurant restaurantTmp = dao.ofy().query(Restaurant.class).filter("id", id).get();
		
		if(restaurantTmp != null){
			List<Restaurant> listTmp = new ArrayList<Restaurant>();
			listTmp.add(restaurantTmp);
			restaurantTmp = getImageLists(listTmp).get(0);
		}
		
		return restaurantTmp;
	}
	/**
	 * 
	 * @param user - logged {@link User}
	 * @return List of {@link Restaurant} for {@link User}
	 */
	private List<Restaurant> loadRestaurantsForUser(User user){
		
		List<Long> tmpList = user.getRestaurantsId();

		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if(restQuery == null) return null;
		List<Restaurant> listRestaurant = restQuery.filter("id IN", tmpList).list();
		if(listRestaurant == null) return new ArrayList<Restaurant>();
		return getImageLists(listRestaurant);
		 
	}
	/**
	 * 
	 * @param restaurants - list of {@link Restaurant}
	 * @return List of {@link Restaurant} with images
	 */
	private List<Restaurant> getImageLists( List<Restaurant> restaurants ) {
		
		List<City> cityList = dao.ofy().query(City.class).list();
		
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
				default : //System.out.println("Unknown ImageType found: " + blob.getImageType().name());
				}
			}
			
			r.setLogoImages(logoImages);
			r.setMenuImages(menuImages);
			r.setProfileImages(profileImages);
			
			
			
//			for (City item : cityList) {	//?????		
//					if(r.getCity().equalsIgnoreCase(item.getCity())){
//						r.setCityId(item.getId());
//					}	
//			}			
					
		}
		
		return restaurants;
	}

	
	@Override
	public void deleteRestaurant(Restaurant r) {
		String id = String.valueOf(r.getId());
			
		dao.ofy().delete(r);
		Query<ImageBlob> imageBlobQuery = dao.ofy().query(ImageBlob.class);
		if(imageBlobQuery == null) return;
		List<ImageBlob> imageBlobList = new ArrayList<ImageBlob>();
		imageBlobList = imageBlobQuery.filter("restId =",id).list();
		for (ImageBlob imageBlob : imageBlobList) {
			BlobstoreServiceFactory.getBlobstoreService().delete(
					new BlobKey(imageBlob.getBlobKey()));
		}
	}
	
	@Override
	public Long deleteRestaurants(long cityId){
		List<Restaurant> restaurantsList = new ArrayList<Restaurant>();
		List<ImageBlob> imageBlobList;
		Query<Restaurant> restaurantQuery = dao.ofy().query(Restaurant.class);
		if(restaurantQuery != null){
			restaurantsList= restaurantQuery.filter("cityId =", cityId).list();
			for (Restaurant restaurant : restaurantsList) {
				Query<ImageBlob> imageBlobQuery = dao.ofy().query(ImageBlob.class);
				if(imageBlobQuery!=null){
						 imageBlobList = imageBlobQuery.filter("restId =", restaurant.getId()).list();
						 for (ImageBlob imageBlob : imageBlobQuery) {
								BlobstoreServiceFactory.getBlobstoreService().delete(new BlobKey(imageBlob.getBlobKey()));
						 }
				}
			}
		}
		
		dao.ofy().delete(restaurantsList);
		dao.ofy().delete(City.class, cityId);
		return cityId;
	}
	
	@Override
	public void clearStore() {
		dao.ofy().delete( loadRestaurants() );
	}

	@Override
	public Restaurant setMainImage(ImageBlob imageBlob) {
		// TODO Auto-generated method stub
		
		Long restId = Long.valueOf(imageBlob.getRestaurantId());
		
		Restaurant r =dao.ofy().query(Restaurant.class).filter("id =", restId).get();
		
		if(r == null){
			return r; //null
		}
		
		switch(imageBlob.getImageType()){
			case PROFILE:
				r.setMainProfileImageString(imageBlob.getImageUrl());
				break;
			case LOGO:
				r.setMainLogoImageString(imageBlob.getImageUrl());
				break;
			case MENU:
				r.setMainMenuImageString(imageBlob.getImageUrl());
				r.setClearBoard(false);
				break;
		}
		
		dao.ofy().put(r);
		return r;
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
			  
			 if(city.contains(map.get(item).getCity())){ //the city name can not by correct
				 
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


	@Override
	public City addCity(String cityName) {
		List<City> list = loadCitiesEntity();
		for (City city : list) {
			if(city.getCity().equalsIgnoreCase(cityName)) {
				return null;
			}
		}
		City c = new City();
		c.setCity(cityName);
		dao.ofy().put(c);
		return c;
	}
	
	@Override
	public City saveCity( City city){
		
		dao.ofy().put(city);
		
		return city;
	}
	
//	/**
//	 * filling field cityId in Restaurant.java. Comparing restaurant.getCity() with city.getCity(), if equals cityId in restaurant is filling by cityId 
//	 * 
//	 */
//	public void fillCityId(){
//		
//		Query<Restaurant> query = dao.ofy().query(Restaurant.class);
//		List <Restaurant> restaurants = new ArrayList<Restaurant>();
//		if(query != null) restaurants=query.list();
//		List<City> cities = loadCitiesEntity();
//		
//		for (Restaurant restaurant : restaurants) {
//			for (City city : cities) {
//				if(restaurant.getCity().equals(city.getCity())){
//					restaurant.setCityId(city.getId());
//					saveRestaurant(restaurant, false);
//					continue;
//				}
//			}
//		}
//	}
	
	@Override
	public Map<String, Object> getAllData(String login, String password){
		User user = authorization(login);
		if(user == null) return null;
		if(!user.getPassword().equals(password)) return null;
		
		Map<String, Object> allData = new HashMap<String, Object>();

		if(user.isAdmin()){
			allData.put("Restaurants", loadRestaurants());
			allData.put("Cities", loadCitiesEntity());
			allData.put("DefaultEmptyProfile", blobService.getEmptyList());
		}
		else if(user.getRestaurantsId() != null && (user.getCitiesId() == null || user.getCitiesId().isEmpty())){
			List<Restaurant> tmp = loadRestaurantsForUser(user);
			allData.put("Restaurants", tmp);	
			allData.put("Cities", loadCitiesByRestaurant(tmp));
			allData.put("DefaultEmptyProfile", blobService.getDefaultEmptyMenu());
		}else if(user.getCitiesId() != null && (user.getRestaurantsId() == null || user.getRestaurantsId().isEmpty())){
			List<City> tmp = loadCitiesForUser(user);
			allData.put("Cities", tmp);
			allData.put("Restaurants", loadRestaurantsByCities(tmp));
			allData.put("DefaultEmptyProfile", blobService.getDefaultEmptyMenu());
		}

		
		
		List<User> usersList = getUsers();
		for (User user1 : usersList) {
			user1.setPassword("");
		}
		allData.put("Users", usersList);
		return allData;
		
	}
	/**
	 * 
	 * @param login - {@link User} login
	 * @return {@link User} if is permitted or null if not
	 */
	private User authorization(String login){
		Query<User> userQuery = dao.ofy().query(User.class);
		if(userQuery != null){
			for (User user : userQuery) {
				if(user.getEmail().equalsIgnoreCase(login)) return user ;
			}
		}	
		return null;
	}
	@Override
	public Map<String, Object> getAllData(String login){
		User user = authorization(login);
		if(user == null) return null;
		Map<String, Object> allData = new HashMap<String, Object>();
		
		if(user.isAdmin()){
			allData.put("Restaurants", loadRestaurants());
			allData.put("Cities", loadCitiesEntity());
			allData.put("DefaultEmptyProfile", blobService.getEmptyList());
		}
		else if(user.getRestaurantsId() != null && user.getCitiesId() == null){
			List<Restaurant> tmp = loadRestaurantsForUser(user);
			allData.put("Restaurants", tmp);	
			allData.put("Cities", loadCitiesByRestaurant(tmp));
			allData.put("DefaultEmptyProfile", blobService.getDefaultEmptyMenu());
		}else if(user.getCitiesId() != null && user.getRestaurantsId() == null){
			List<City> tmp = loadCitiesForUser(user);
			allData.put("Cities", tmp);
			allData.put("Restaurants", loadRestaurantsByCities(tmp));
			allData.put("DefaultEmptyProfile", blobService.getDefaultEmptyMenu());
		}
		List<User> usersList = getUsers();
		for (User user1 : usersList) {
			user1.setPassword("");
		}
		allData.put("Users", usersList);
		return allData;
		
	}
	
//	private void fixUserFlags(){
//		List<User> userList = getUsers();
//		for (User user : userList) {
//			if(user.getRestaurantsId() != null && user.getCitiesId() == null){
//				user.setRestaurator(true);
//			}
//			if(user.getCitiesId() != null && user.getRestaurantsId() == null){
//				user.setAgent(true);
//			}
//			dao.ofy().put(user);
//		}
//		
//	}
	
	@Override
	public void addUser(User user){
		dao.ofy().put(user);
	}
	
	@Override
	public List<User> getUsers(){
		Query<User> users = dao.ofy().query(User.class);
		if(users == null) return new ArrayList<User>();
		
		return users.order("email").list();
	}
	
	@Override
	public Map<Long, Boolean> setVisibilityRestaurants(List<Restaurant> restaurants){
		
		Map<Long, Boolean> changedRestaurnts = new HashMap<Long, Boolean>();
		
		for (Restaurant restaurant : restaurants) {
			restaurant.setVisibleForApp(!restaurant.isVisibleForApp());
			changedRestaurnts.put(restaurant.getId(), restaurant.isVisibleForApp());
		}
		
		saveRestaurants(restaurants);
		
		return changedRestaurnts;
	}

	@Override
	public Restaurant clearBoard(Restaurant restaurant, ImageType imageType) {
		
//		if(restaurant.getEmptyMenuImageString()==null || restaurant.getEmptyMenuImageString().equals("")){
//			Query<ImageBlob> query = dao.ofy().query(ImageBlob.class);
//			if(query != null){
//				ImageBlob imageBlob = query.filter("imageType", ImageType.EMPTY_MENU).filter("restId", "0").get();
//				if(imageBlob != null) restaurant.setEmptyMenuImageString(imageBlob.getImageUrl());
//			}
//		}
//
//		switch(imageType){
//		case PROFILE:
//			restaurant.setMainProfileImageString(restaurant.getEmptyMenuImageString());
//			break;
//		case LOGO:
//			restaurant.setMainLogoImageString(restaurant.getEmptyMenuImageString());
//			break;
//		case MENU:
//			restaurant.setMainMenuImageString(restaurant.getEmptyMenuImageString());
//			restaurant.setClearBoard(true);
//			break;
//		}
//		
		restaurant.setEmptyMenuImageString(null);
		restaurant.setMainMenuImageString(null);
		restaurant.setClearBoard(true);
		saveRestaurant(restaurant);
		
		return restaurant;
	}

	@Override
	public User changeUserData(User user, String oldPassword, String newPassword) {
		List<User> userList = getUsers();
		User userToChange = null;

		for (User user2 : userList) {
			if(user2.getEmail().equals(user.getEmail())){
				userToChange = user2;
			}
		}
		if(userToChange == null){
			return null;
		}
		
		
		
		List<String> toAddress = new ArrayList<String>();
		toAddress.add(userToChange.getEmail());
		String userName = getLoginFromMail(userToChange.getEmail());
		String subject = "Message from website MenuMenu";
		String message = "Hello "+userName+". \n\n";
		
		
		
		
		if(!newPassword.isEmpty() && !oldPassword.equals(userToChange.getPassword())){
			message += "\n\n Given old password was incorect, all data will not be changed";
			return null;
		} else if(!newPassword.isEmpty() && oldPassword.equals(userToChange.getPassword())){
			userToChange.setPassword(newPassword);
			message += "\n\n Your password has been changed";
			message += "\n\n Your new password is: " + userToChange.getPassword();
			
		} else if(newPassword.isEmpty()){
			message += "\n\n Your personal data have been changed: ";
			message +="\n\n name: " + user.getName();
			message +="\n\n  surname: " + user.getSurname();
			message +="\n\n phone number: " + user.getPhoneNumber();
		}
		
		userToChange.setName(user.getName());
		userToChange.setSurname(user.getSurname());
		userToChange.setPhoneNumber(user.getPhoneNumber());
		
		
		dao.ofy().put(userToChange);
		emailService.sendEmail(toAddress, userName, message, subject);
		
		
		userToChange.setPassword("");
		return userToChange;
	}

	@Override
	public String removeUser(User user){
		if(user == null) return null;
		dao.ofy().delete(user);
		return user.getEmail();
	}

	@Override
	public Map<String, String> copyCityData(String cityIdFrom, String cityIdTo, String email){
		Long cityIdFromLong;
		Long cityIdToLong;
		
		try {
			cityIdFromLong = Long.parseLong(cityIdFrom);
		} catch (NumberFormatException e) {
			return null;
		}
		
		try {
			cityIdToLong = Long.parseLong(cityIdTo);
		} catch (NumberFormatException e) {
			return null;
		}
		

		City city = findCity(cityIdToLong);
		
		if(city == null){
			return null;
		}
		
		List<Restaurant> restaurants = loadRestaurants(cityIdFromLong);
		
		if(restaurants == null){
			return null;
		}
		
		Queue queue = QueueFactory.getDefaultQueue();
	    queue.add(withUrl("/copyDataTask").param("token", "a1b2c3").param("cityIdFrom", cityIdFrom).param("cityIdTo", cityIdTo).param("emailAddress", email));
		
	    Map<String, String> response = new HashMap<String, String>();
	     
	    response.put("cityIdFrom", cityIdFrom);
	    response.put("cityIdTo", cityIdTo);
	    return response;
	}
	/**
	 * 
	 * @param blobkey - List of {@link BlobKey}
	 * @return List of images in range of {@link BlobKey}
	 */
	public List<ImageBlob> getImageBlobs(List<String> blobkey){
		if(blobkey ==null || blobkey.isEmpty()) return new ArrayList<ImageBlob>();
		
		Query<ImageBlob> query = dao.ofy().query(ImageBlob.class);
		
		if(query == null) return new ArrayList<ImageBlob>();

		return query.filter("blobKey IN", blobkey).list();
	}
	/**
	 * 
	 * @param imageBlob - {@link ImageBlob} to save
	 */
	public void saveImageBlob( ImageBlob imageBlob) {
		if(imageBlob != null) dao.ofy().put(imageBlob);
	}
	/**
	 * 
	 * @param latitude - latitude of device 
	 * @return List of {@link Restaurant} in range of 1km in latitude from device
	 */
	public List<Restaurant> getRestaurantsInArea(double latitude){
		List<Restaurant> restaurantsList = new ArrayList<Restaurant>();
		
		double R = 6371;  // earth radius in km

		double radius = 1; // km

	
		double y1 = latitude + Math.toDegrees(radius/R);

		double y2 = latitude - Math.toDegrees(radius/R);
		
		
		Query<Restaurant> restaurantQuery = dao.ofy().query(Restaurant.class);
		
		if(restaurantQuery == null) return restaurantsList;
		updatePositions();
		
			
		
		
		restaurantsList = restaurantQuery.filter("latitude >=", y2).filter("latitude <=", y1).list();
		return restaurantsList;
		
	}
	
	/**
	 * @deprecated
	 * Method to update fields in datastore, from latitude and longitude in String to new double fields
	 */
	private void updatePositions(){
		Query<Restaurant> rest = dao.ofy().query(Restaurant.class);
		
		for (Restaurant restaurant : rest.list()) {
			if(restaurant.getLat() != null && restaurant.getLng() != null){
				restaurant.setLatitude(Double.valueOf(restaurant.getLat()));
				restaurant.setLongitude(Double.valueOf(restaurant.getLng()));
				dao.ofy().put(restaurant);
			}
			
		}
		
	}

	public City loadCity(Long cityId) {
		
		return dao.ofy().find(City.class, cityId);
	}

	public boolean publishRestaurant(long restaurantId, boolean isPublish) {
		Restaurant restaurant = dao.ofy().find(Restaurant.class, restaurantId);
		
		if(restaurant != null){
			restaurant.setVisibleForApp(isPublish);
			dao.ofy().put(restaurant);
			return true;
		}else{
			log.severe("StoreServiceImpl::publishRestaurant(), restaurant (id: "+restaurantId+ ") not found");
			return false;
		}
		
	}
	
}
