package com.hoob.search.server.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hoob.search.common.DbQueue;
import com.hoob.search.dao.ReportSearchDao;
import com.hoob.search.server.ProConfigService;
import com.hoob.search.server.SyncSearchReportServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoob.search.utils.ObjectUtils;
import com.hoob.search.vo.HotWordReportReq;
@Service("syncSearchReportServer")
public class SyncSearchReportServerImpl implements InitializingBean, SyncSearchReportServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncSearchReportServerImpl.class);
	@Resource
	private ReportSearchDao reportSearchDao;
	/**
     * 参数配置类
     */
    @Autowired
    private ProConfigService proConfigService;
    
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		init();
		
	}
	private void init() {
		SyncSearchReportThread syncSearchReportThread = new SyncSearchReportThread();
		syncSearchReportThread.start();
	}
	private class SyncSearchReportThread extends Thread{
		@Override
        public void run() {
			while (true) {
				try {
					try{
						List<HotWordReportReq> hotWordList = new ArrayList();
						while(true){
							if(DbQueue.getDBHotWordReportReqQueue().size()<=0){
								break;
							}
							HotWordReportReq hotWord = DbQueue.getDBHotWordReportReqQueue().pop();
							if(ObjectUtils.isNotEmpty(hotWord)){
								hotWordList.add(hotWord);
							}
							if(hotWordList.size()>= Integer.parseInt(proConfigService
											.getString("search_report_batch_mysql_size", "100"))){
								
								reportSearchDao.batchHotWordReport(hotWordList);
								hotWordList.clear();
								LOGGER.info("search report queue size:"
								+DbQueue.getDBHotWordReportReqQueue().size());
							}
							try {
		                        Thread.sleep(1L);
		                    } catch (Exception e) {
		                    	LOGGER.error(e.getMessage()+e);
		                    }
						}
						if(!hotWordList.isEmpty()){
							reportSearchDao.batchHotWordReport(hotWordList);
							LOGGER.info("search report queue size:"+DbQueue.getDBHotWordReportReqQueue().size()); 
						}
					}catch(Exception e) {
						LOGGER.error(e.getMessage()+e);
					}
					
				} catch (Exception e) {
					LOGGER.error(e.getMessage()+e);
				} finally {
					try {
                        Thread.sleep(2000L);
                    } catch (Exception e) {
                    	LOGGER.error(e.getMessage()+e);
                    }
				}
			}
		}
		
	}
}
