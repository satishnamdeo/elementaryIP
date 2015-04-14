package com.eip.domain;


public class PasswordDo {

	private Integer userId;
	private String currentPassword;
	private String newPassword;
	private boolean isModified;
	private String message;
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public boolean isModified() {
		return isModified;
	}
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return super.toString();
	}
	
	
    
}
