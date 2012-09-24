package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;


@RemoteServiceRelativePath("blobstore")
public interface BlobService extends RemoteService {
        
        String getBlobStoreUrl();
        String getBlobStoreUrl(String sectionKey);
        List<ImageBlob> getImages( Restaurant r );
        List<ImageBlob> getImages( String restaurantId );
        
        
        BlobData[] getBlobs(BlobDataFilter filter);
         
        boolean deleteBlob(String blobKeyString);
        
}