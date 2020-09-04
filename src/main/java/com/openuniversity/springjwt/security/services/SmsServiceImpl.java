package com.openuniversity.springjwt.security.services;

import com.openuniversity.springjwt.bean.Sms;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service("smsService")
public class SmsServiceImpl implements SmsService {

    /**
     * send the sms using the Sms object which get by SmsService interface
     */
    @Override
    public String sendSms(Sms sms) {

        String mobileNumber = sms.getTo();
        String smsMsg = sms.getMessage();

        try {
            String apiKey = "&key=" + "ouslsecret";
            String message = "&msg=" + smsMsg;
            String numbers = "&mobile=" + mobileNumber;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("http://sms.ousl.lk/payalert.php?").openConnection();
            String data = message + numbers + apiKey;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
        }
    }
}
