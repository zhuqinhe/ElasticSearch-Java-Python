package com.hoob.search.model;

public class ProgramType {
	private String key;
	private long value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
//	
	public ProgramType(){
		
	}
//	
	public ProgramType(String key,long value){
		this.key = key;
		this.value = value;
	}
	
}
