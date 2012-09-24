package com.veliasystems.menumenu.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class StoreServiceImpl extends RemoteServiceServlet implements StoreService {

	private DAO dao = new DAO();
	
	
	@Override
	public List<String> loadCities() {
		List<String> cities = new ArrayList<String>();
		
			cities.add("Krakow");
			cities.add("Warszawa");
			
		return cities;
	}
	
	@Override
	public void saveRestaurant(Restaurant r) {
		// TODO Auto-generated method stub
		System.out.println("saved succes: " + r.getName());
		dao.ofy().put(r);
	}
	
	@Override
	public List<Restaurant> loadRestaurants() {
		
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if (restQuery==null) return new ArrayList<Restaurant>();
		
		return restQuery.order("name").list();  
	}
	
	@Override
	public List<Restaurant> loadRestaurants( String city ) {
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if (restQuery==null) return new ArrayList<Restaurant>();
		
		return restQuery.filter("city =", city).order("name").list();
	}

	@Override
	public List<Restaurant> getRestaurant(String name) {
		// TODO Auto-generated method stub
		Query<Restaurant> restQuery = dao.ofy().query(Restaurant.class);
		if (restQuery==null) return new ArrayList<Restaurant>();

		return restQuery.filter("name =", name).list();
	}
	
	
}
