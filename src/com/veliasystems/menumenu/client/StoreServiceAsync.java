package com.veliasystems.menumenu.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.entities.Restaurant;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface StoreServiceAsync {
	
	void saveRestaurant(Restaurant r, AsyncCallback<Void> callback);
}
