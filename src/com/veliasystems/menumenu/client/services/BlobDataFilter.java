package com.veliasystems.menumenu.client.services;
import com.google.gwt.user.client.rpc.IsSerializable;



public class BlobDataFilter implements IsSerializable {

  private long start = 0;
  private long limit = 50;
  
  private String key;
  
  public void setLimit(long start, long limit) {
    this.start = start;
    this.limit = limit;
  }
  
  /**
   * get start of range
   * @return start
   */
  public long getRangeStart() {
    return start;
  }

  /**
   * get end of range finish - this is a work around for the offset
   * @return start + limit
   */
  public long getRangeFinish() {
    long finish = start + limit;
    return finish;
  }
  
  public void setBlobKey(String key) {
    this.key = key;
  }
  
  public String getBlobKey() {
    return key;
  }
  
  
}