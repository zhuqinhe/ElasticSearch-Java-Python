package com.hoob.search.model;

public class SuggestIndex {
    public String name;
	public String namekeyword;
	public String describe;
	public String createDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNamekeyword() {
		return namekeyword;
	}
	public void setNamekeyword(String namekeyword) {
		this.namekeyword = namekeyword;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
