package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 
 * @author velia-systems
 *
 *	
 */
public class ImageBlob implements Serializable {

	
	private static final long serialVersionUID = 5523375381187731406L;
	
	/**
	 * The image blob id (auto generated)
	 */
	@Id private String id;
	/**
	 * Restaurant's id (converted to {@link String} ) that is assigned to this {@link ImageBlob}
	 */
	private String restId;
	
	/**
	 * The image blobKey (converted to {@link String}) 
	 */
	private String blobKey;
	/**
	 * Date of creation of image
	 */
    private Date dateCreated;
    /**
     * Date of creation of image in milliseconds. <br> Default is 0
     */
    private long timeInMiliSec = 0;
    /**
     * The {@link ImageType} of image
     */
    private ImageType imageType;
    
    /**
     * The image width 
     */
    private int width;
    /**
     * The image height 
     */
    private int height;
    
    
    {
		id = "" + ((long) (Math.random() * 999999999));
	}
    
    
    public ImageBlob() {
		// TODO Auto-generated constructor stub
	}

    /**
     * Create new {@link ImageBlob} 
     * @param restId - Restaurant's id (converted to {@link String} ) that is assigned to this {@link ImageBlob}
     * @param blobKey - the image blobKey (converted to {@link String}) 
     * @param dateCreated - date of creation of image
     * @param imageType - the {@link ImageType} of image
     */
    public ImageBlob( String restId, String blobKey, Date dateCreated, ImageType imageType ) {
    	setRestaurantId(restId);
    	setBlobKey(blobKey);
    	setDateCreated(dateCreated);
    	setImageType(imageType);
    }
    
    /**
     * 
     * @return the imageBlob id
     */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return the restaurant's id that is assigned to this {@link ImageBlob}
	 */
	public String getRestaurantId() {
		return restId;
	}

	/**
	 * set the restaurant's id that is assigned to this {@link ImageBlob}
	 * @param - restId the restaurant's id that is assigned to this {@link ImageBlob}
	 */
	public void setRestaurantId(String restId) {
		this.restId = restId;
	}
	
	/**
	 * set the image blobKey (converted to {@link String}) 
	 * @param blobKey - the image blobKey (converted to {@link String}) 
	 */
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}
	
	/**
	 * 
	 * @return the image blobKey (converted to {@link String}) 
	 */
	public String getBlobKey() {
		return blobKey;
	}
	
	/**
	 * Return url of image that have to be added to host of url
	 * @return the image url (eg. to use in web)
	 */
	@Transient
	public String getImageUrl() {
		return "/blobServe?blob-key=" + getBlobKey();
	}

	/**
	 * 
	 * @return date of creation of image
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * Set date of creation of image
	 * @param dateCreated - date of creation of image
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		setTimeInMiliSec(dateCreated.getTime());
	}
    
	/**
	 * Set the {@link ImageType} of image
	 * @param imageType - the {@link ImageType} of image
	 */
    public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}
    
    /**
     * 
     * @return the {@link ImageType} of image
     */
    public ImageType getImageType() {
		return imageType;
	}
	
    /**
     * Set the image height 
     * @param height - the image height 
     */
    public void setHeight(int height) {
		this.height = height;
	}
    
    /**
     * 
     * @return the image height 
     */
    public int getHeight() {
		return height;
	}
    
    /**
     * Set the image width 
     * @param width - the image width 
     */
    public void setWidth(int width) {
		this.width = width;
	}
    
    /**
     * 
     * @return the image width 
     */
    public int getWidth() {
		return width;
	}
    
    /**
     * 
     * @return date of creation of image in milliseconds 
     */
    public long getTimeInMiliSec() {
		return timeInMiliSec;
	}
    
    /**
     * Set date of creation of image in milliseconds
     * @param timeInMiliSec - date of creation of image in milliseconds
     */
    private void setTimeInMiliSec(long timeInMiliSec) {
		this.timeInMiliSec = timeInMiliSec;
	}
    
    
    /**
     * @return <code>true</code> if and only if <code>imageBlobToCheck</code> have the {@link ImageBlob#getImageUrl()} and {@link ImageBlob#restId} and {@link ImageBlob#imageType} or <code>false</code>
     */
    @Override
	public boolean equals(Object imageBlobToCheck) {
		if(! (imageBlobToCheck instanceof ImageBlob)){
			return false;
		}
		
		ImageBlob imageBlob = (ImageBlob) imageBlobToCheck;
		
		if(this.getImageUrl().equalsIgnoreCase(imageBlob.getImageUrl()) && this.getRestaurantId().equalsIgnoreCase(imageBlob.getRestaurantId()) && this.getImageType() == imageBlob.getImageType()){
			return true;
		}
		
		return false;
		
	}
}
