package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;


public class ImageBlob implements Serializable {

	
	private static final long serialVersionUID = 5523375381187731406L;
	
	
	@Id private String id;
	private String restId;
	
	private String blobKey;
    private Date dateCreated;
    
    private ImageType imageType;
    
    
    {
		id = "" + ((long) (Math.random() * 999999999));
	}
    
    
    public ImageBlob() {
		// TODO Auto-generated constructor stub
	}

    public ImageBlob( String restId, String blobKey, Date dateCreated, ImageType imageType ) {
    	setRestaurantId(restId);
    	setBlobKey(blobKey);
    	setDateCreated(dateCreated);
    	setImageType(imageType);
    }
    
	public String getId() {
		return id;
	}

	public String getRestaurantId() {
		return restId;
	}

	public void setRestaurantId(String restId) {
		this.restId = restId;
	}
	
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}
	
	
	public String getBlobKey() {
		return blobKey;
	}
	
	@Transient
	public String getImageUrl() {
		return "/blobServe?blob-key=" + getBlobKey();
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
    
    public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}
    
    public ImageType getImageType() {
		return imageType;
	}
	
}
