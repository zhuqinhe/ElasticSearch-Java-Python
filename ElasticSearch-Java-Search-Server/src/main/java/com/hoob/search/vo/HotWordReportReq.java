package com.hoob.search.vo;

import java.io.Serializable;

/**
 * @author zhuqinhe
 */
public class HotWordReportReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String searchTime;
	public String searchWord;
	public String name;
	public String contentId;
	public String userToken;
	public String getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
}
