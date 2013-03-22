package com.veliasystems.menumenu.server;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.LastModified;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.entities.UserToAdd;

public class DAO extends DAOBase {
	/**
	 * Registering entities class to datastore in order to save
	 */
	static {
		ObjectifyService.register( User.class );
        ObjectifyService.register( Restaurant.class );
        ObjectifyService.register( ImageBlob.class );
        ObjectifyService.register( City.class );
        ObjectifyService.register( UserToAdd.class );
        ObjectifyService.register( LastModified.class );
    }
	
	public DAO() {
		super();
	}
	
}