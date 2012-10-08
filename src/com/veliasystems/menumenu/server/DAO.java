package com.veliasystems.menumenu.server;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class DAO extends DAOBase {

	static {
        //ObjectifyService.register(User.class);
        ObjectifyService.register( Restaurant.class );
        ObjectifyService.register( ImageBlob.class );
        ObjectifyService.register(City.class);
    }
	
	public DAO() {
		super();
	}
	
}