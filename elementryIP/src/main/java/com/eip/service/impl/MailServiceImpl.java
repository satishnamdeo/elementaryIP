package com.eip.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.eip.service.MailService;

@Service("mailService")
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
    private SimpleMailMessage alertMailMessage;

	@Override
	public void sendMail(String to, String subject, String body) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(body, "text/html");
			helper.setTo(to);
			helper.setSubject(subject);
			mailSender.send(mimeMessage);
			sendAlertMail("mail successfully send by you") ;
	}

	@Override
	public void sendAlertMail(String alert) {
		SimpleMailMessage mailMessage = new SimpleMailMessage(alertMailMessage);
	    mailMessage.setText(alert);
		mailSender.send(mailMessage);
	}
	
}
