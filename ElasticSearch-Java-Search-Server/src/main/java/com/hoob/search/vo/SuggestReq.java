package com.hoob.search.vo;

import com.hoob.search.model.UserInfo;
import com.hoob.search.utils.ObjectUtils;

public class SuggestReq {
	public String terminal;
	public String queryText;
	public UserInfo userInfo;
	public String fileName;
	public Integer start = 0;
	public Integer size;
	
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getQueryText() {
		return queryText;
	}
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		if(ObjectUtils.isNotEmpty(userInfo)){
			return "{\"terminal\":\"" + terminal + "\", \"queryText\":\"" + queryText + "\",\"userInfo\":" + userInfo.toString()
			+ ", \"fileName\":\"" + fileName + "\", \"start\":\"" + start + "\", \"size\":\"" + size + "\"}";
		}
		return "{\"terminal\":\"" + terminal + "\", \"queryText\":\"" + queryText + "\",\"userInfo\":\"" + null
		+ "\", \"fileName\":\"" + fileName + "\", \"start\":\"" + start + "\", \"size\":\"" + size + "\"}";
	}
	
}
