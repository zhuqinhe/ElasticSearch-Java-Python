package com.hoob.search.server;

public interface SystemConfigService {

	public String getString(String key, String defaultValue);

	public Boolean getBoolean(String key, Boolean defaultValue);

}
