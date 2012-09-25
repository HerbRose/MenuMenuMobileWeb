package com.veliasystems.menumenu.server;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.veliasystems.menumenu.client.services.BlobData;
import com.veliasystems.menumenu.client.services.BlobDataFilter;

public class BlobInfoJdo {

  private PersistenceManager pm = PMF.get().getPersistenceManager();
  
  public BlobData[] getBlobs(BlobDataFilter filter) {
    
    Entity[] entities = null;
    try {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery pq = datastore.prepare(new Query("__BlobInfo__"));
      List<Entity> entList = pq.asList(FetchOptions.Builder.withLimit((int) filter.getRangeFinish()).offset((int) filter.getRangeStart()));
      
      entities = new Entity[entList.size()];
      entList.toArray(entities);
      
    } finally {
      pm.close();
    }
    
    BlobData[] blobData = convert(entities);
    
    return blobData;
  }

  private BlobData[] convert(Entity[] es) {
    if (es == null || es.length == 0) {
      return null;
    }
    
    BlobData[] b = new BlobData[es.length];
    for (int i=0; i < es.length; i++) {
      
      Map<String, Object> p = es[i].getProperties();
      
      long id = es[i].getKey().getId();
      Key key = es[i].getKey();
      String ct = (String) es[i].getProperty("content_type");
      String fn = (String) es[i].getProperty("filename");
      Long size = (Long) es[i].getProperty("size");
      Date creation = (Date) es[i].getProperty("creation");
  
      b[i] = new BlobData();
      b[i].setKey(key.getName());
      b[i].setContentType(ct);
      b[i].setFilename(fn);
      b[i].setSize(size);
      b[i].setCreation(creation);
      
    }
    
    return b;
  }

}