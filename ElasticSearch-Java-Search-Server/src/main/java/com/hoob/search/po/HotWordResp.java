package com.hoob.search.po;

import java.util.List;

public class HotWordResp {
	public Integer resultCode;
	
	public String description;
	
	public List<HotWordPo> hotwordList;

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

	public List<HotWordPo> getHotwordList() {
		return hotwordList;
	}

	public void setHotwordList(List<HotWordPo> hotwordList) {
		this.hotwordList = hotwordList;
	}
	
	
}
