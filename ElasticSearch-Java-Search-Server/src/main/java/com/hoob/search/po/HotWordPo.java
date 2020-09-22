package com.hoob.search.po;

public class HotWordPo {
	public String hotword;
	public Integer count;
	public String contentId;
	public String detailPicUrl;
	public String posterPicUrl;
	public Integer updateNum;
	public Integer totalNum;
	public String programType;
	
	public String getHotword() {
		return hotword;
	}
	public void setHotword(String hotword) {
		this.hotword = hotword;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getDetailPicUrl() {
		return detailPicUrl;
	}
	public void setDetailPicUrl(String detailPicUrl) {
		this.detailPicUrl = detailPicUrl;
	}
	public String getPosterPicUrl() {
		return posterPicUrl;
	}
	public void setPosterPicUrl(String posterPicUrl) {
		this.posterPicUrl = posterPicUrl;
	}
	public Integer getUpdateNum() {
		return updateNum;
	}
	public void setUpdateNum(Integer updateNum) {
		this.updateNum = updateNum;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
//	
	public HotWordPo(String hotword, Integer count) {
		this.hotword = hotword;
		this.count = count;
	}
//	
	public HotWordPo() {
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	
}
