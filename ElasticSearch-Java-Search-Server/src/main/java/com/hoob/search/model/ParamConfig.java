package com.hoob.search.model;

import java.io.Serializable;

import com.hoob.search.vo.BaseVO;

public class ParamConfig implements BaseVO, Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	/** 参数类型 */
	private String paramType;

	/** 参数Key */
	private String paramKey;

	/** 参数值 */
	private String paramValue;

	/** 参数描述 */
	private String paramDesc;
//
	public ParamConfig() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

}