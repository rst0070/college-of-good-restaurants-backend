package com.matjipdaehak.fo.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final String mailFrom = "kwb0711@gmail.com";
    @Autowired
    public EmailServiceImpl(
            JavaMailSender javaMailSender
    ){
        this.javaMailSender = javaMailSender;
    }


    @Override
    public void sendMessage(String to, String subject, String text) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
