package com.veliasystems.menumenu.client.services;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;


public class BlobData implements IsSerializable  {

  private String key;
  
  private String content_type;
  
  private Date creation;
  
  private String filename;
  
  private long size;
  
  /**
   * init contructor - nothing to do here.
   */
  public BlobData() {
  }
  
  public void setKey(String name) {
    this.key = name;
  }
  
  public String getKey() {
    return key;
  }
  
  public void setContentType(String content_type) {
    this.content_type = content_type;
  }
  
  public String getContentType() {
    return content_type;
  }
  
  public void setCreation(Date creation) {
    this.creation = creation;
  }
  
  public Date getCreation() {
    return creation;
  }
  
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  public String getFilename() {
    return filename;
  }
  
  public void setSize(long size) {
    this.size = size;
  }
  
  public long getSize() {
    return size;
  }
}

