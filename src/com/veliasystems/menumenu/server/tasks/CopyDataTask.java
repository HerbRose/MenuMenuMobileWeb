package com.veliasystems.menumenu.server.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;

import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.EmailServiceImpl;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class CopyDataTask extends HttpServlet{

	private static final Logger log = Logger.getLogger(CopyDataTask.class.getName()); 
	private StoreServiceImpl storeService = new StoreServiceImpl();
	private BlobServiceImpl blobService = new BlobServiceImpl();
	private EmailServiceImpl emailService = new EmailServiceImpl();
	private String emailMessageBody = "";
	private String postscript = "-tester";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String token = req.getParameter("token");
		String emailAddress = req.getParameter("emailAddress");
		
		if (emailAddress==null || emailAddress.isEmpty() ) {
			log.warning("Wrong token");
			emailAddress = "mateusz@velia-systems.com";
			emailMessageBody = "MenuMenuMobileWeb. Task: CopyDataTask.java, emailAddressError: no email foun in HttpServletRequest" ;
			sendMail(emailAddress);
			return;
		}
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			log.warning("Wrong token");
			emailAddress = "mateusz@velia-systems.com";
			emailMessageBody = "Internal server error occurred.\n Probably someone from the outside trying to call the task: CopyDataTask";
			sendMail(emailAddress);
			return;
		}
		
		String cityIdFrom = req.getParameter("cityIdFrom");
		
		if (cityIdFrom==null || cityIdFrom.isEmpty()) {
			log.warning("Wrong cityIdFrom, cityIdFrom cannot be empty");
			
			emailMessageBody = "Internal server error occurred.\n Wrong cityIdFrom, cityIdFrom cannot be empty.";
			sendMail("mateusz@velia-systems.com");
			
			emailMessageBody = "When copying a data error occurred.";
			sendMail(emailAddress);
			return;
		}
		String cityIdTo = req.getParameter("cityIdTo");
		
		if (cityIdTo==null || cityIdTo.isEmpty()) {
			log.warning("Wrong cityIdTo, cityIdTo cannot be empty");
			emailAddress = "mateusz@velia-systems.com";
			emailMessageBody = "Internal server error occurred.\n Wrong cityIdTo, cityIdTo cannot be empty." ;
			sendMail(emailAddress);
			
			emailMessageBody = "When copying a data error occurred.";
			sendMail(emailAddress);
			return;
		}
		
		cityIdFrom = req.getParameter("cityIdFrom");
		cityIdTo = req.getParameter("cityIdTo");
		Long cityIdFromLong;
		Long cityIdToLong;
		
		try {
			cityIdFromLong = Long.parseLong(cityIdFrom);
		} catch (NumberFormatException e) {
			log.warning("Wrong cityIdFrom, have to be long");
			
			emailMessageBody = "When copying a data error occurred. Cannot found a source city";
			sendMail(emailAddress);
			return;
		}
		
		try {
			cityIdToLong = Long.parseLong(cityIdTo);
		} catch (NumberFormatException e) {
			log.warning("Wrong cityIdTo, have to be long");
			emailMessageBody = "When copying a data error occurred. Cannot found the destination city";
			sendMail(emailAddress);
			return;
		}
		
		City city = storeService.loadCitie(cityIdToLong);
		
		if(city == null){
			log.warning("Cannot found city:" +cityIdTo);
			emailMessageBody = "When copying a data error occurred. Cannot found a source city";
			sendMail(emailAddress);
			return;
		}
		
		List<Restaurant> restaurantsInSourceCity = new ArrayList<Restaurant>();
		restaurantsInSourceCity.addAll(storeService.loadRestaurants(cityIdFromLong));
		
		if(restaurantsInSourceCity == null || restaurantsInSourceCity.size()<=0){
			log.warning("Cannot found restaurants in source city");
			
			emailMessageBody = "When copying a data error occurred. Cannot found a restaurant in source city";
			sendMail(emailAddress);
			return;
		}
		
		int numberOfFoundrestauration = restaurantsInSourceCity.size(); 
		List<Restaurant> restaurantsInDestinationCity = new CopyOnWriteArrayList<Restaurant>();
		restaurantsInDestinationCity.addAll(storeService.loadRestaurants(cityIdToLong));
		
		//sprawdza czy istnieja te same restauracje
		for (Restaurant restaurant : restaurantsInDestinationCity) {
			for (Restaurant restaurantInSourceCity : restaurantsInSourceCity) {
				if(isTheSame(restaurantInSourceCity, restaurant )){
					restaurantsInSourceCity.remove(restaurantInSourceCity);
				}
			}
		}
		
		List<Restaurant> restaurantsToSave = new ArrayList<Restaurant>();
		
		for (Restaurant restaurant : restaurantsInSourceCity) {
			Restaurant newRestaurant = new Restaurant(restaurant.getName()+postscript, restaurant.getAddress(), city.getCity());
			newRestaurant.setCityId(cityIdToLong);
			newRestaurant.setDistrict(restaurant.getDistrict());
			newRestaurant.setLat(restaurant.getLat());
			newRestaurant.setLng(restaurant.getLng());
			newRestaurant.setMailRestaurant(restaurant.getMailRestaurant());
			newRestaurant.setMailUser(restaurant.getMailUser());
			newRestaurant.setNameUser(restaurant.getNameUser());
			newRestaurant.setPhoneRestaurant(restaurant.getPhoneRestaurant());
			newRestaurant.setPhoneUser(restaurant.getPhoneUser());
			newRestaurant.setSurnameUser(restaurant.getSurnameUser());
			
			copyImages(restaurant.getLogoImages(), newRestaurant.getId()+"");
			copyImages(restaurant.getMenuImages(), newRestaurant.getId()+"");
			copyImages(restaurant.getProfileImages(), newRestaurant.getId()+"");
			
			restaurantsToSave.add(newRestaurant);
			
		}
		
		storeService.saveRestaurants(restaurantsToSave);		
		
		
		emailMessageBody = "The data was copied successful. \nNumber of found restaurant: "+numberOfFoundrestauration+" \nNumber of copied restaurant: " +restaurantsToSave.size()+ "\n";         
		emailMessageBody += "This email was generated automatically. Please do not reply to it.\n";
		sendMail(emailAddress);

	}
	
	private boolean isTheSame( Restaurant source, Restaurant destination) {
		String nameSource = source.getName()+postscript;
		String addressSource = source.getAddress();
		
		String nameDestination = destination.getName();
		String addressDestination = destination.getAddress();
		
		if(nameSource.equals(nameDestination) && addressSource.equals(addressDestination)){
			return true;
		}
		
		return false;
	}

	private void copyImages(List<ImageBlob> logoImages, String newRestaurantId) {		
		for (ImageBlob imageBlob : logoImages) {
			blobService.copyBlob(imageBlob, newRestaurantId);
		}
	}
	
	private void sendMail( String emailAddress ){
		
		String subject = "Message from MenuMenuWeb site" ;
		List<String> emailAddressList = new ArrayList<String>();
		emailAddressList.add(emailAddress);
		
		emailService.sendEmail(emailAddressList, "", emailMessageBody, subject);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doGet(req, resp);
	}
}
