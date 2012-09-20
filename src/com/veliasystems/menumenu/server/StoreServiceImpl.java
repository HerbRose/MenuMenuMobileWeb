package com.veliasystems.menumenu.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.veliasystems.menumenu.client.StoreService;
import com.veliasystems.menumenu.client.entities.Restaurant;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class StoreServiceImpl extends RemoteServiceServlet implements StoreService {

	@Override
	public void saveRestaurant(Restaurant r) {
		// TODO Auto-generated method stub
		
	}
	
}
