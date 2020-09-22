package com.hoob.search.server;

public interface RedisConfigService {

	public String getString(String key, String defaultValue);

	public Boolean getBoolean(String key, Boolean defaultValue);

}
