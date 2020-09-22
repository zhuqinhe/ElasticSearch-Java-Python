package com.hoob.search.server;

import com.hoob.search.model.ParamConfig;


public interface IParamConfigService {
    /**
     * 获取指定key值，带缓存
     * @param paramKey key
     * @return value
     */
	ParamConfig getParamConfigByParamKey(String paramKey);
	
	/**
     * 获取指定key值，不带缓存
     * @param paramKey key
     * @return value
     */
	String getParamConfigVal(String paramKey);
	
	/**
     * 更新配置
     * @param paramKey key
     * @param paramValue value
     * @return 行数
     */
	void updateParamConfig(String paramKey, String paramValue);
	
	/**
	 * 
	 * 获取指定key值，带缓存
	 * 
	 * @param paramKey
	 * @return String
	 * @see
	 */
	String getParamStringRedis(String paramKey);
}
