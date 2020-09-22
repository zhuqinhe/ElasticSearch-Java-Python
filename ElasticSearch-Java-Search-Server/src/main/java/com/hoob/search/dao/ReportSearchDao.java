package com.hoob.search.dao;

import java.util.List;

import com.hoob.search.vo.HotWordReportReq;

public interface ReportSearchDao {
	
	public boolean hotWordReport(HotWordReportReq req) throws Exception;
	
	public void batchHotWordReport(List<HotWordReportReq> hotWordList) throws Exception;
}
