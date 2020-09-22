package com.hoob.search.server.impl;

import java.util.HashMap;
import java.util.Map;

import com.hoob.search.model.ParamConfig;
import com.hoob.search.server.IParamConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoob.search.server.SystemConfigService;
@Service("iParamConfigService")
public class IParamConfigServerImpl implements InitializingBean, IParamConfigService {
	
	private static final Map<String, String> SYS_CONFIG_CACHE = new HashMap<String, String>(); 
	/**
     * 参数配置类
     */
    @Autowired
    private SystemConfigService proConfigService;
    
	private void init(){
		SYS_CONFIG_CACHE.clear();
		SYS_CONFIG_CACHE.put("overLoad_TimerStatus", proConfigService.getString("overLoad_TimerStatus", "3"));
		SYS_CONFIG_CACHE.put("overLoad_Timer", proConfigService.getString("overLoad_Timer", "1"));
		SYS_CONFIG_CACHE.put("overLoad_collectionCount", proConfigService.getString("overLoad_collectionCount", "5"));
		SYS_CONFIG_CACHE.put("overLoad_collectionMilli", proConfigService.getString("overLoad_collectionMilli", "5"));
		SYS_CONFIG_CACHE.put("overLoad_olCollectionMilli", proConfigService.getString("overLoad_olCollectionMilli", "60000"));
		SYS_CONFIG_CACHE.put("overLoad_proMemory", proConfigService.getString("overLoad_proMemory", "75,85"));
		SYS_CONFIG_CACHE.put("overLoad_proConcurrency", proConfigService.getString("overLoad_proConcurrency", "5000,6000"));
		SYS_CONFIG_CACHE.put("overLoad_sysMemory", proConfigService.getString("overLoad_sysMemory", "85,95"));
		SYS_CONFIG_CACHE.put("overLoad_sysCPU", proConfigService.getString("overLoad_sysCPU", "75,85"));
		
	}
	
	
	@Override
	public ParamConfig getParamConfigByParamKey(String paramKey) {

		return null;
	}

	@Override
	public String getParamConfigVal(String paramKey) {
		//动态刷新配置
		SYS_CONFIG_CACHE.put("overLoad_Timer", proConfigService.getString("overLoad_Timer", "1"));
		SYS_CONFIG_CACHE.put("overLoad_collectionCount", proConfigService.getString("overLoad_collectionCount", "5"));
		SYS_CONFIG_CACHE.put("overLoad_collectionMilli", proConfigService.getString("overLoad_collectionMilli", "5"));
		SYS_CONFIG_CACHE.put("overLoad_olCollectionMilli", proConfigService.getString("overLoad_olCollectionMilli", "60000"));
		SYS_CONFIG_CACHE.put("overLoad_proMemory", proConfigService.getString("overLoad_proMemory", "75,85"));
		SYS_CONFIG_CACHE.put("overLoad_proConcurrency", proConfigService.getString("overLoad_proConcurrency", "5000,6000"));
		SYS_CONFIG_CACHE.put("overLoad_sysMemory", proConfigService.getString("overLoad_sysMemory", "85,95"));
		SYS_CONFIG_CACHE.put("overLoad_sysCPU", proConfigService.getString("overLoad_sysCPU", "75,85"));
		
		return SYS_CONFIG_CACHE.get(paramKey);
	}

	@Override
	public void updateParamConfig(String paramKey, String paramValue) {

		SYS_CONFIG_CACHE.put(paramKey, paramValue);

	}

	@Override
	public String getParamStringRedis(String paramKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		init();
	}

}
