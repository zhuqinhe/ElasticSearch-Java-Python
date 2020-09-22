package com.hoob.search.vo;

public class OrderFile {
	public String file;
	public String type;
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "OrderFile [file=" + file + ", type=" + type + "]";
	}
	
}
