package com.hoob.search.vo;

import java.util.List;

import com.hoob.search.model.UserInfo;
import com.hoob.search.utils.ObjectUtils;

public class ContentReq {
	public String terminal = "3";
	public String queryText;
	public String fileName;
	public String programType;
	public UserInfo userInfo;
	public List<OrderFile> orderFile; 
	public Integer start;
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
	
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	@Override
	public String toString() {
		if(ObjectUtils.isNotEmpty(userInfo)){
			return "{\"terminal\":\"" + terminal + "\",\"queryText\":\"" 
		+ queryText + "\",\"fileName\":\"" + fileName+ "\",\"programType\":\""+programType
					+ "\",\"userInfo\":" + userInfo.toString() + ",\"start\":\"" + start + 
					"\",\"size\":\"" + size + "\"}";
		}
		return "{\"terminal\":\"" + terminal + "\",\"queryText\":\"" + queryText 
				+ "\",\"programType\":\""+programType+"\",\"fileName\":\"" + fileName
				+ "\",\"userInfo\":\"" + null + "\",\"start\":\"" + start + 
				"\",\"size\":\"" + size + "\"}";
	}
	
	
	
}
