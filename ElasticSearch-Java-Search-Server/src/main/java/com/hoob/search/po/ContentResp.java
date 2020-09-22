package com.hoob.search.po;

import java.util.List;

import com.hoob.search.model.ProgramType;

public class ContentResp {

	public Integer resultCode;

	public long total;

	public String description;

	public List<ContentPo> contentList;
	
	public List<CastPo> castList;

	public List<ProgramType> programType;

	//
	public ContentResp() {
	}

	//
	public ContentResp(Integer resultCode, String description) {
		this.resultCode = resultCode;
		this.description = description;
	}

	//
	public ContentResp(Integer resultCode, String description, List<ContentPo> contentList) {
		this.resultCode = resultCode;
		this.description = description;
		this.contentList = contentList;
	}

	//
	public ContentResp(Integer resultCode, String description, List<ContentPo> contentList,
			List<ProgramType> programType) {
		this.resultCode = resultCode;
		this.description = description;
		this.contentList = contentList;
		this.programType = programType;
	}

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

	public List<ContentPo> getContentList() {
		return contentList;
	}

	public void setContenList(List<ContentPo> contentList) {
		this.contentList = contentList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<ProgramType> getProgramType() {
		return programType;
	}

	public void setProgramType(List<ProgramType> programType) {
		this.programType = programType;
	}

	public void setContentList(List<ContentPo> contentList) {
		this.contentList = contentList;
	}

	public List<CastPo> getCastList() {
		return castList;
	}

	public void setCastList(List<CastPo> castList) {
		this.castList = castList;
	}
	
}
