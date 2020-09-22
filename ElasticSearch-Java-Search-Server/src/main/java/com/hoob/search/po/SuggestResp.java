package com.hoob.search.po;

import java.util.List;

public class SuggestResp {
	public Integer resultCode;
	
	public String description;
	
	public List<SuggestPo> suggestList;
//
	public SuggestResp() {
	}
//	
	public SuggestResp(Integer resultCode, String description) {
		this.resultCode = resultCode;
		this.description = description;
	}
//	
	public SuggestResp(Integer resultCode, String description, List<SuggestPo> suggestList) {
		this.resultCode = resultCode;
		this.description = description;
		this.suggestList = suggestList;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SuggestPo> getSuggestList() {
		return suggestList;
	}

	public void setSuggestList(List<SuggestPo> suggestList) {
		this.suggestList = suggestList;
	}

	
}
