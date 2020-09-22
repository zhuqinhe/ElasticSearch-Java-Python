package com.hoob.search.server;

public interface RedisServer {
	
	public void setString(String key,String value);
	
	public String getString(String key);
	
	public void expire(String key,int time);

}
