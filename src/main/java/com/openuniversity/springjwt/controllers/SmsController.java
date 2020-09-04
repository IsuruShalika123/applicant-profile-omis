package com.openuniversity.springjwt.controllers;

import com.openuniversity.springjwt.bean.Sms;
import com.openuniversity.springjwt.configuration.ResponseHandler;
import com.openuniversity.springjwt.security.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
public class SmsController {

    @Autowired
    private ApplicationContext context;

    /**
     * set mobile number and message to sending the sms
     * @return
     */
    @PostMapping(value = "/sendsms")
    public ResponseEntity<Object> sendSms(@RequestBody MultiValueMap<String, String> values) {

        String mobileNumberTo = values.getFirst("mobileNumber");//Post request should include "mobileNumber" and its value(in postman "x-www-form-urlencoded" body format)
        String smsMsg = values.getFirst("smsMsg");//Post request should include "smsMsg" and its value
        Sms sms = new Sms();
        sms.setTo(mobileNumberTo);
        sms.setMessage(smsMsg);
        System.out.println(sms);
        SmsService smsService = (SmsService) context.getBean("smsService");
        smsService.sendSms(sms);

        //return "successfully send the sms";//return a String(text) as success msg
        return ResponseHandler.generateResponse(HttpStatus.OK,"successfully send the sms", null);//return a json object as response with success message
    }
}