package com.veliasystems.menumenu.server.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.mortbay.log.Log;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.DAO;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class GetRestaurantsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2566472678914274709L;
	
	private StoreServiceImpl storeService = new StoreServiceImpl();
	private BlobServiceImpl blobService = new BlobServiceImpl();
	private static final Logger log = Logger.getLogger(GetRestaurantsServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		long start = new Date().getTime();
//		fixAllLinksInRestaurant();
		
		String token = req.getParameter("token");
		String test = req.getParameter("test");
		boolean isTest = false;
		if (test!=null) isTest = true;
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}
		
		
		if (isTest) {
			doTest(req,resp);
			return;
		}
		
		
		
		String cityIdString = req.getParameter("cityId");
		Long cityId;
		
		try {
			cityId = Long.parseLong(cityIdString);
		} catch (NumberFormatException e) {
			resp.getWriter().println("cityId must be Integer");
			resp.flushBuffer();
			return;
		}
		
		
//		List<String> cities = storeService.loadCities();
//		if ( cityId<0) { //cityId>=cities.size() || -> ???????????
//			resp.getWriter().println("Wrong cityId");
//			resp.flushBuffer();
//			return;
//		}
//		
//		String city = cities.get(cityId);
//		
		City city = storeService.findCity(cityId);
		
		if (city == null) {
			resp.getWriter().println("Wrong cityId");
			resp.flushBuffer();
			return;
		}
		
		
		List<Restaurant> rests = storeService.loadRestaurants(cityId);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		
		List< Map<String,Object> > attributes = new ArrayList< Map<String,Object>>();
		ImageBlob emptyDefoultMenu;
		
		if(blobService.getDefaultEmptyMenu().isEmpty()){
			emptyDefoultMenu = new ImageBlob();
		}else{
			emptyDefoultMenu = blobService.getDefaultEmptyMenu().get(0);
		}
		String emptyDefaultMenuString = emptyDefoultMenu.getImageUrl();
		boolean emptyDefaultMenuExist;
		if(emptyDefoultMenu == null) emptyDefaultMenuExist = false;
		else {
			emptyDefaultMenuString = getBlobKey(emptyDefaultMenuString);
			emptyDefaultMenuExist = checkIfBlobExist(emptyDefaultMenuString);
		}
	
		for (Restaurant r : rests) {
			if(r.isVisibleForApp()){
				Map<String,Object> map = new HashMap<String,Object>();
				
				map.put( "id", ""+ r.getId());
				map.put( "name", r.getName());
				map.put( "city", getCityName(r.getCityId()) );
				map.put( "district", r.getDistrict());
				map.put( "address", r.getAddress());
				map.put( "lat", "" + r.getLat());
				map.put( "lng", "" + r.getLng());
				map.put("phoneRestaurant", r.getPhoneRestaurant());
				map.put("openHours", r.getOpenHours());
				map.put("menuPublishTime", r.getMenuPublishTimeInMiliSec());
				
				boolean logoImageExist = true;
				boolean menuImageExist = true;
				boolean profileImageExist = true;
				boolean menuImageScreenSizeExist = true;
				boolean menuImageScaleSizeExist = true;

				//get images
				
				String logoImageString = (r.getMainLogoImageString()!=null) ? r.getMainLogoImageString() : null;
				String menuImageString = (r.getMainMenuImageString()!=null) ?r.getMainMenuImageString() : null;
				String profileImageString = (r.getMainProfileImageString()!=null) ? r.getMainProfileImageString() : null;
				String menuImageScreenString = (r.getMainMenuScreenSizeImageString()!=null) ? r.getMainMenuScreenSizeImageString() : null;
				String menuImageScaleString = (r.getMainMenuScaleSizeImageString()!=null) ? r.getMainMenuScaleSizeImageString() : null;

				logoImageExist = checkIfStringExist(logoImageString); 
				if(logoImageExist){
					logoImageString = getBlobKey(logoImageString);
					logoImageExist = checkIfBlobExist(logoImageString);
				}	
				
				profileImageExist = checkIfStringExist(profileImageString);	
				if(profileImageExist){
					profileImageString = getBlobKey(profileImageString);
					profileImageExist = checkIfBlobExist(profileImageString);
				}
				
				menuImageExist = checkIfStringExist(menuImageString);
				if(menuImageExist){
					menuImageString = getBlobKey(menuImageString);
					menuImageExist = checkIfBlobExist(menuImageString);
				}
				
				menuImageScreenSizeExist = checkIfStringExist(menuImageScreenString);
				if(menuImageScreenSizeExist){
					menuImageScreenString = getBlobKey(menuImageScreenString);
					menuImageScreenSizeExist = checkIfBlobExist(menuImageScreenString);
				}
				
				menuImageScaleSizeExist = checkIfStringExist(menuImageScaleString);
				if(menuImageScaleSizeExist){
					menuImageScaleString = getBlobKey(menuImageScaleString);
					menuImageScaleSizeExist = checkIfBlobExist(menuImageScaleString);
				}

				if(logoImageExist){
					ImageBlob imgBlob = storeService.getImageBlobByBlobKey(logoImageString);
					map.put("logoImage", addHostToUrl("blobServe?blob-key=" + logoImageString));	
					addToMapImageDeatails(map, imgBlob);	
				} else {
					map.put("logoImage", "EMPTY");
					addDefaultImageDetails(map, ImageType.LOGO);
				}
				
				if(profileImageExist){
					ImageBlob imgBlob = storeService.getImageBlobByBlobKey(profileImageString);
					map.put("profileImage", addHostToUrl("blobServe?blob-key=" + profileImageString));
					addToMapImageDeatails(map, imgBlob);
				} else {
					map.put("profileImage", "EMPTY");
					addDefaultImageDetails(map, ImageType.PROFILE);
				}
				
				if(menuImageExist){	
					ImageBlob imgBlob = storeService.getImageBlobByBlobKey(menuImageString);
					map.put("menuImage", addHostToUrl("blobServe?blob-key=" + menuImageString));
					map.put("menuImageDefault", false);
					addToMapImageDeatails(map, imgBlob);
					
					map.put("menuImageScreenSize", (menuImageScreenSizeExist ? addHostToUrl("blobServe?blob-key=" + menuImageScreenString) : 
						addHostToUrl("blobServe?blob-key=" + menuImageString)));
					
					map.put("menuImageScaleSize", (menuImageScaleSizeExist ? addHostToUrl("blobServe?blob-key=" + menuImageScaleString) : 
						addHostToUrl("blobServe?blob-key=" + menuImageString)));

				} else {
					if(emptyDefaultMenuExist){
						map.put("menuImage", addHostToUrl("blobServe?blob-key=" + emptyDefaultMenuString));
						map.put("menuImageScreenSize", addHostToUrl("blobServe?blob-key=" + emptyDefaultMenuString));
						map.put("menuImageScaleSize", addHostToUrl("blobServe?blob-key=" + emptyDefaultMenuString));
						map.put("menuImageDefault", true);
						addToMapImageDeatails(map, emptyDefoultMenu);
					} else {
						map.put("menuImage", "EMPTY");
						map.put("menuImageScreenSize", "EMPTY");
						map.put("menuImageScaleSize", "EMPTY");
						map.put("menuImageDefault", true);
						addDefaultImageDetails(map, ImageType.MENU);
					}
				}			
				attributes.add(map);
			}
		}

		resp.getWriter().print(gson.toJson(attributes));
		log.info("Time of getting restaurant: " + ((new Date().getTime() - start)) + "miliseconds");
		if(jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
		
	}	
	
	private String getBlobKey(String url){
		String[] urlArray = url.split("=");
		if(urlArray.length > 1){
			url = urlArray[1];
		}
		return url;
	}
	private boolean checkIfBlobExist(String url){
		BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
		BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(new BlobKey(url));
		if(blobInfo == null) return false;
		return true;
	}

	private boolean checkIfStringExist(String url){
		if(url == null || url.isEmpty()) return false;
		return true;
	}
	
	private void addToMapImageDeatails(Map<String, Object> map, ImageBlob imageBlob){
		if(imageBlob != null) {
			if(imageBlob.getImageType() != ImageType.EMPTY_MENU){
				map.put(imageBlob.getImageType()+"DateCreate", imageBlob.getDateCreated()+"");
				map.put(imageBlob.getImageType()+"DateCreateInMiliS", imageBlob.getDateCreated().getTime()+"");
				map.put(imageBlob.getImageType()+"Height", imageBlob.getHeight()+"");
				map.put(imageBlob.getImageType()+"Width", imageBlob.getWidth()+"");
			} else {
				map.put(ImageType.MENU+"DateCreate", imageBlob.getDateCreated()+"");
				map.put(ImageType.MENU+"DateCreateInMiliS", imageBlob.getDateCreated().getTime()+"");
				map.put(ImageType.MENU+"Height", imageBlob.getHeight()+"");
				map.put(ImageType.MENU+"Width", imageBlob.getWidth()+"");
			}		
		} else {
			log.warning("image blob is null");
		}
	}
	
	private void addDefaultImageDetails(Map<String, Object> map, ImageType imgType){
		map.put(imgType+"DateCreate", "");
		map.put(imgType+"DateCreateInMiliS", "");
		map.put(imgType+"Height", "");
		map.put(imgType+"Width", "");
	}
	
	private Map<Long, String> cityMap = new HashMap<Long, String>();
		private String getCityName(Long cityId){
			if(cityMap.containsKey(cityId)){
				return cityMap.get(cityId);
			}else{
				City city = storeService.loadCity(cityId);
				if(city == null) return "";
				
				cityMap.put(city.getId(), city.getCity());
				return cityMap.get(cityId);
			}		
		}
	private String addHostToUrl( String url ) {
		if (url.startsWith("http://")) return url;
		return getHostName() + url;
	}
	
	
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
	
	
	
	private void doTest( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		String limit = req.getParameter("limit");
		int n = 10;
		if (limit!= null) {
			try {
				n = Integer.parseInt(limit);
			} catch (NumberFormatException e) {
				n = 10;
			}
		}
		
		List< Map<String,String> > attributes = new ArrayList< Map<String,String>>();
		
		String[] names = { "Pimiento", "Warsztat", "Babcia Malina", "Cosa Nostra", "Ptaszyl", "La Scandale", "El Torro", "Rio Grande", "ChinTin" };
		String[] addresses = { "Ptasia 5", "Malinowa 45", "Borowkowa 2", "Zielona 55", "Lotnikow 4", "Debow 55" };
		
		/*
		 "http://www.shangrestaurant.com/wp-content/uploads/home-restaurant.jpg", 
							"http://alltopmovies.net/wp-content/uploads/2009/06/restaurant.jpg",
							"http://www.nuffy.net/pics/cool/bizarre-restaurants/restaurant_undersea.jpg",
							"http://www.hoteles-silken.com/hotel-berlaymont-brussels/files/2010/09/Restaurant-LObjectif.jpg",
							"http://s3.amazonaws.com/sfb111/story_xlimage_2010_08_R5362_Inwood_Restaurant_Grades.jpg",
							"http://parisparfait.typepad.com/paris_parfait/images/2008/08/31/chinese_restaurant_2.jpg" 
		 */
		
		String[] profileImages = {  "http://upload.wikimedia.org/wikipedia/commons/a/a1/Paris-Marais-p1010711.jpg",
								"http://farm3.static.flickr.com/2271/2228348131_4dab74909a.jpg",
								"http://www.potatomato.com/mt/archives/image2/DSC04902.jpg",
								"http://1.bp.blogspot.com/_Mi5VfhVwrzs/R52BDBuluGI/AAAAAAAAG7g/X-k4yjKcCyA/s400/the%2Bmarais%2Bparis.JPG",
								"http://www.lodjee.com/img/upload/Marais_Apartments_Paris.jpg",
								"http://2.bp.blogspot.com/-JKLFyOV4HMQ/T37uF7oSKVI/AAAAAAAAAlQ/aXH0HKJJO6k/s1600/18386936_8cbf45c009.jpg",
								"http://upload.wikimedia.org/wikipedia/commons/b/bb/Jo_Goldenberg_restaurant_Paris_dsc04019.jpg",
								"http://www.whattoseeinparis.com/images/le-marais-paris.jpg",
								"http://www.stockphotopro.com/photo-thumbs-2/stockphotopro_3968806PFR_paris_marais.jpg",
								"http://www.nouveau-paris-ile-de-france.fr/fichiers/cafeRestaurant/images/227633/fr/standard/petit-marcel-ambiance-bistrot-paris-75004-2.jpg",
								"http://media-cdn.tripadvisor.com/media/photo-s/02/29/67/cf/creperie-beaubourg.jpg",
								"http://www.lafourchette.com/uploads/restaurant_530_270/19898/117bfcac2546e95e46210c8a15656b7e.jpg",
								"http://www.lafourchette.com/uploads/restaurant_530_270/19898/b46d1ee73ccc0f17f87b07a41455ae9f.jpg",
								"http://www.lafourchette.com/uploads/promo/18314.jpg",
								"http://a34.idata.over-blog.com/0/21/17/79/Le-Comptoir-de-Madame-Tomate.jpg",
		};
		
		String[] menuImages = { "http://graphixshare.com/uploads/posts/2011-05-07/1304774534_kedsbf6ujfqpmv5.jpeg",
								"http://www.mightysweet.com/mesohungry/wp-content/uploads/2010/08/02-Ukrainian-East-Village-Restaurant-menu-board.jpg",
								"http://www.mightysweet.com/mesohungry/wp-content/uploads/2011/05/05-La-Norte%C3%B1a-Restaurant-menu.jpg",
								"http://kookykitsch.com/Portals/0/productimages/2790_01c68.jpg",
								"http://www.tannery.ie/assets/images/tasting.JPG",
								"http://farm2.staticflickr.com/1329/665553384_6229ab7047_z.jpg?zz=1" };
								
		String[] logoImages = { "http://www.g-renderings.com/images/Logos/400X250/Corporate/BellavinoLogo.jpg",
								"http://www.hayashi.com.au/logo.gif",
								"http://www.freelanceforum.org/files/imagecache/screenshot_full/portfolio/Sheryl%20Nelson/1-ID-Zazu.gif",
								"http://m.artician.com/pu/OUHFIQYZPWRDLF3Q2CFR4UYGTUPDH3V5.preview.png",
								"http://eatmoredrinkmore.com/wp-content/uploads/2011/12/Toki-Underground-Logo.jpg",
								"http://www.cityhindusnetwork.org.uk/wp-content/uploads/2011/04/CHN-Networking-Dinner-logo-v2.jpg" };
		
		
		for (int i=0; i<n; i++) {
			Map<String,String> map = new HashMap<String,String>();
			map.put( "id", ""+ i);
			map.put( "name", names[(int) Util.getRandom(names.length)]);
			map.put( "city", "Krakow");
			map.put( "district", "District " + i);
			map.put( "address", addresses[(int) Util.getRandom(addresses.length)]);
			map.put( "logoImage", logoImages[(int) Util.getRandom(logoImages.length)]);
			map.put( "menuImage", menuImages[(int) Util.getRandom(menuImages.length)]);
			map.put( "profileImage", profileImages[(int) Util.getRandom(profileImages.length)]);
			map.put( "lat", "" + Util.getRandom(180));
			map.put( "lng", "" + Util.getRandom(180));
			
			attributes.add(map);
		}
		
		resp.getWriter().print(gson.toJson(attributes));
	
		if(jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	
//	private void fixAllLinksInRestaurant(){
//		List<Restaurant> rests = storeService.loadRestaurants();
//		
//		for (Restaurant restaurant : rests) {
//			
//			
//					try {
//						if(restaurant.getMainMenuScaleSizeImageString() != null){
//							URL url1 = new URL(R.HOST_URL  + restaurant.getMainMenuScaleSizeImageString());
//							System.out.println(url1);
//							HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
//							
//							if(connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND || connection.getResponseCode() == HttpURLConnection.HTTP_SERVER_ERROR){
//								restaurant.setMainMenuScaleSizeImageString("");
//							}
//						}
//					
//						if(restaurant.getMainMenuScreenSizeImageString() != null){
//							URL url2 = new URL(R.HOST_URL + restaurant.getMainMenuScreenSizeImageString());
//							HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
//							System.out.println(url2);
//							if(conn2.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND || conn2.getResponseCode() == HttpURLConnection.HTTP_SERVER_ERROR){
//								restaurant.setMainMenuScreenSizeImageString("");
//							}
//						}
//						
//						restaurant.setLogoImages(null);
//						restaurant.setMenuImages(null);
//						restaurant.setProfileImages(null);
//						dao.ofy().put(restaurant);
//						
//					} catch (MalformedURLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		
//			
//			
//		}
//	}
}
