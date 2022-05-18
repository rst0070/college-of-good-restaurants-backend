package com.matjipdaehak.fo.common.service;

public interface EmailService {

    void sendMessage(String to, String subject, String text);
}
