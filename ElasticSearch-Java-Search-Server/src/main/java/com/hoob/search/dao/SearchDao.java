package com.hoob.search.dao;

import java.util.List;

import com.hoob.search.po.ContentResp;
import com.hoob.search.po.HotWordResp;
import com.hoob.search.po.SuggestPo;
import com.hoob.search.vo.ContentReq;
import com.hoob.search.vo.HotWordReq;
import com.hoob.search.vo.SuggestReq;

public interface SearchDao {

	public List<SuggestPo> simpleFuzzySuggestSearch(SuggestReq req) throws Exception;

	public List<SuggestPo> simpleFuzzyHighlightSuggestSearch(SuggestReq req) throws Exception;

	public ContentResp simpleMultiLineMatchSearch(ContentReq req) throws Exception;

	public ContentResp simpleMultiLineHighlightMatchSearch(ContentReq req) throws Exception;

	public HotWordResp hotWordSearch(HotWordReq req) throws Exception;

}
