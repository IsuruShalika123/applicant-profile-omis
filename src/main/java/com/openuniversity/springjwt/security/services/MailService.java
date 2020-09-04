package com.openuniversity.springjwt.security.services;

import com.openuniversity.springjwt.bean.Mail;

/**
 * get the Mail object created in the EmailController
 */
public interface MailService {
    public void sendEmail(Mail mail);
}