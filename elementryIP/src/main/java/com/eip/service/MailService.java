package com.eip.service;

import javax.mail.MessagingException;


public interface MailService {
	
	 public void sendMail(String to, String subject, String body) throws MessagingException ;
	 
	 public void sendAlertMail(String alert);
	

}
