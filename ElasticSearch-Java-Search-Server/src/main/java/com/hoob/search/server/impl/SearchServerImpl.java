package com.hoob.search.server.impl;
/**
 * 
 */
import java.util.List;

import javax.annotation.Resource;

import com.hoob.search.dao.ReportSearchDao;
import com.hoob.search.dao.SearchDao;
import com.hoob.search.po.ContentResp;
import com.hoob.search.po.HotWordResp;
import com.hoob.search.po.SuggestPo;
import com.hoob.search.server.SearchServer;
import org.springframework.stereotype.Service;

import com.hoob.search.vo.ContentReq;
import com.hoob.search.vo.HotWordReportReq;
import com.hoob.search.vo.HotWordReq;
import com.hoob.search.vo.SuggestReq;
@Service("searchServer")
public class SearchServerImpl implements SearchServer {
	@Resource
	private SearchDao searchDao;
	
	@Resource
	private ReportSearchDao reportSearchDao;
	

	@Override
	public ContentResp simpleMultiLineHighlightMatchSearch(ContentReq req) throws Exception {
		// TODO Auto-generated method stub
		return searchDao.simpleMultiLineHighlightMatchSearch(req);
	}

	@Override
	public ContentResp simpleMultiLineMatchSearch(ContentReq req) throws Exception {
		// TODO Auto-generated method stub
		return searchDao.simpleMultiLineMatchSearch(req);
	}

	@Override
	public List<SuggestPo> simpleFuzzySuggestSearch(SuggestReq req) throws Exception {
		// TODO Auto-generated method stub
		return searchDao.simpleFuzzySuggestSearch(req);
	}

	@Override
	public HotWordResp hotWordSearch(HotWordReq req) throws Exception {
		// TODO Auto-generated method stub
		return searchDao.hotWordSearch(req);
	}

	@Override
	public boolean hotWordReport(HotWordReportReq req) throws Exception {
		// TODO Auto-generated method stub
		return reportSearchDao.hotWordReport(req);
	}

	@Override
	public List<SuggestPo> simpleFuzzyHighlightSuggestSearch(SuggestReq req) throws Exception {
		// TODO Auto-generated method stub
		return searchDao.simpleFuzzyHighlightSuggestSearch(req);
	}

}
