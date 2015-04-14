package com.eip.domain;


import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.security.crypto.codec.Base64;



@XmlRootElement
public class UserDo {
	
		private Integer userId;
	    private String firstName;
	    private String lastName;
	    private String displayName;
	    private String password;
	    private String phone;
	    private String email;
	    private Boolean isActive;
	    private Date creationDate;
	    private Date lastModified;
	    private String authToken;
	    private Integer roleId;
	    private String roleCode;
	    private String title;
	    private byte[] userImage;
	    
	    
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getRoleCode() {
			return roleCode;
		}
		public void setRoleCode(String roleCode) {
			this.roleCode = roleCode;
		}
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public Boolean getIsActive() {
			return isActive;
		}
		public void setIsActive(Boolean isActive) {
			this.isActive = isActive;
		}
		public Date getCreationDate() {
			return creationDate;
		}
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}
		public Date getLastModified() {
			return lastModified;
		}
		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}
		public String getAuthToken() {
			return authToken;
		}
		public void setAuthToken(String authToken) {
			this.authToken = authToken;
		}
		
		public Integer getRoleId() {
			return roleId;
		}
		public void setRoleId(Integer roleId) {
			this.roleId = roleId;
		}
		public byte[] getUserImage() {
			return userImage;
		}
		public void setUserImage(byte[] userImage) {
			this.userImage = userImage;
		}
		public String getByteArrayString()
		{
			byte[] encodedBytes = Base64.encode(this.userImage);
		   return new String(encodedBytes);
		}
}
