package com.hoob.search.common;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoob.search.vo.HotWordReportReq;

/**
 * @author zhuqinhe
 */
public class DbHotWordReportReqQueue {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbHotWordReportReqQueue.class);
    private BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<String>();
    private Map<String, Serializable> entities = new ConcurrentHashMap<String, Serializable>();

    /**
     * 添加到存储队列中
     */
    public void add(HotWordReportReq entity) {
//        String key = entity.getClass().getSimpleName() + "#" + entity.contentId;
    	String key = UUID.randomUUID().toString();
        if (!blockingQueue.contains(key)) {
            try {
                blockingQueue.offer(key, 1, TimeUnit.SECONDS);
            }catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error("db queue is full error...");
            }
        }
        entities.put(key, entity);
    }
    /**
     * 从存储的队列中移除
     */
    public HotWordReportReq pop() {
        String id = null;
		try {
			id = blockingQueue.poll(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return (HotWordReportReq) entities.remove(id);
    }

    /**
     * 队列的大小
     */
    public int size() {
        return blockingQueue.size();
    }

}
