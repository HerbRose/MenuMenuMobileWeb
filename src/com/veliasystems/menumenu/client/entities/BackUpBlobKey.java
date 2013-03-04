package com.veliasystems.menumenu.client.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

public class BackUpBlobKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id private Long id;
	private String FileBlobKey= null;
	private Date CreateDate;
	private long timeInMiliSec = 0;
	private String address = "";
	{
		id = ((long) (Math.random() * 999999999));
	}
	
	public BackUpBlobKey() {
		// TODO Auto-generated constructor stub
	}

	public Date getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate (Date CreateDate){
	
			this.CreateDate = CreateDate;
			setTimeInMiliSec(CreateDate.getTime());
	}
	
	public long getTimeInMiliSec() {
			return timeInMiliSec;
		}
	    
	private void setTimeInMiliSec(long timeInMiliSec) {
			this.timeInMiliSec = timeInMiliSec;
		}
	public String getFileBlobKey() {
			return FileBlobKey;
		}
	public void setFileBlobKey(String fileBlobKey) {
			FileBlobKey = fileBlobKey;
		}
	
	public String getAddress(){
		return address;
		
	}
	public void setAddress(String address){
		this.address=address;
	}
	 
}
