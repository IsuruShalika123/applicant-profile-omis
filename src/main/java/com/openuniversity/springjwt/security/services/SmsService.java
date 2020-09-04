package com.openuniversity.springjwt.security.services;

import com.openuniversity.springjwt.bean.Sms;

/**
 * get the Sms object created in the EmailController
 */
public interface SmsService {
    public String sendSms(Sms sms);
}
