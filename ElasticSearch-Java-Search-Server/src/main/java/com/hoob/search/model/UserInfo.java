package com.hoob.search.model;

public class UserInfo {
	private String userId;
	private String userName;
	private String userIp;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\",\"userName\":\"" + userName + "\",\"userIp\":\"" + userIp + "\"}";
	}
}
