package com.eip.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PatentDo {
	
	
	private String patentId;
	private String claimIndependentNo;
	private String keyname;
	private String keyvalue;
	private String comment;
	private String startOffset;
	private String endOffset;
	//private Integer tagCount;
	private String oldTagName;
	private String currentUser;
	//private String oldUser;
	
	
	public String getPatentId() {
		return patentId;
	}
	public void setPatentId(String patentId) {
		this.patentId = patentId;
	}
	
	public String getClaimIndependentNo() {
		return claimIndependentNo;
	}
	public void setClaimIndependentNo(String claimIndependentNo) {
		this.claimIndependentNo = claimIndependentNo;
	}
	
	public String getKeyname() {
		return keyname;
	}
	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
	
	public String getKeyvalue() {
		return keyvalue;
	}
	public void setKeyvalue(String keyvalue) {
		this.keyvalue = keyvalue;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStartOffset() {
		return startOffset;
	}
	public void setStartOffset(String startOffset) {
		this.startOffset = startOffset;
	}
	public String getEndOffset() {
		return endOffset;
	}
	public void setEndOffset(String endOffset) {
		this.endOffset = endOffset;
	}
	/*public Integer getTagCount() {
		return tagCount;
	}
	public void setTagCount(Integer tagCount) {
		this.tagCount = tagCount;
	}*/
	public String getOldTagName() {
		return oldTagName;
	}
	public void setOldTagName(String oldTagName) {
		this.oldTagName = oldTagName;
	}
	
	public String getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	/*public String getOldUser() {
		return oldUser;
	}
	public void setOldUser(String oldUser) {
		this.oldUser = oldUser;
	}*/
	
	
}
