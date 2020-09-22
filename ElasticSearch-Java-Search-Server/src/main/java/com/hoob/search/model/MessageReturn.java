package com.hoob.search.model;

import java.io.Serializable;


public class MessageReturn implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer resultCode;
	private String description;

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

	//
	public MessageReturn(Integer resultCode, String description) {
		this.resultCode = resultCode;
		this.description = description;

	}
//
	public MessageReturn() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MessageReturn [resultCode=");
		builder.append(resultCode);
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}

}