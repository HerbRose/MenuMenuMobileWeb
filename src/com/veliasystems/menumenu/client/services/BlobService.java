package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;


@RemoteServiceRelativePath("blobstore")
public interface BlobService extends RemoteService {
        
        String getBlobStoreUrl(String sectionKey, ImageType imageType);
        
        List<ImageBlob> getHeaderImages( Restaurant r ); // loga/naglowki restauracji
        List<ImageBlob> getHeaderImages( String restaurantId );
        		
        List<ImageBlob> getBoardImages( Restaurant r );  // tablice/menu
        List<ImageBlob> getBoardImages( String restaurantId );
        
        List<ImageBlob> getProfileImages( Restaurant r ); // zdjecia profilowe
        List<ImageBlob> getProfileImages( String restaurantId );
        
        
        BlobData[] getBlobs(BlobDataFilter filter);
         
        boolean deleteBlob(String blobKeyString);
        
}