package com.bmk.notification.controller;

import com.bmk.notification.dto.EmailDto;
import com.bmk.notification.dto.ResponseDto;
import com.bmk.notification.dto.SmsDto;
import com.bmk.notification.exceptions.AuthorizationException;
import com.bmk.notification.util.RestClient;
import com.bmk.notification.util.TimeLoggerUtil;
import com.twilio.Twilio;
import com.twilio.example.TwiMLResponseExample;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;

@RequestMapping("email")
@RestController
@Log
public class NotificationController {

    private static RestClient restClient;

    public NotificationController(RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping("email")
    public ResponseEntity sendEmail(@RequestBody EmailDto emailDto, @RequestHeader String token) throws AuthorizationException, IOException {
        TimeLoggerUtil timeLoggerUtil = new TimeLoggerUtil("sendEmail", emailDto.toString(), restClient);
        restClient.authorize(token, "alpha");
        restClient.sendEmailSendGrid(emailDto.getSubject(), emailDto.getToEmail(), emailDto.getContent());
        timeLoggerUtil.methodEnd();
        return ResponseEntity.ok(new ResponseDto("200", "Success"));
    }

    @PostMapping("sms")
    public ResponseEntity sendSms(@RequestBody SmsDto smsDto, @RequestHeader String token) throws AuthorizationException, IOException {
        TimeLoggerUtil timeLoggerUtil = new TimeLoggerUtil("sendSms", smsDto.toString(), restClient);
        restClient.authorize(token, "alpha");
        String ACCOUNT_SID = System.getenv("twilioSid");
        String AUTH_TOKEN = System.getenv("twilioToken");
        String TWILIO_PHONE = System.getenv("twilioPhone");
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        //TODO: handle in better way
        if(smsDto.getToPhone().length()==12)        smsDto.setToPhone("+"+smsDto.getToPhone());

        Message message = Message
                .creator(new PhoneNumber(smsDto.getToPhone()), // to
                        new PhoneNumber(TWILIO_PHONE), // from
                        smsDto.getMessage())
                .create();
        timeLoggerUtil.methodEnd();
        return ResponseEntity.ok(new ResponseDto("200", "Success"));
    }

    @PostMapping("/receive/sms")
    public ResponseEntity receiveSms(@RequestParam String body) {
        log.info(body);
        return null;
    }
}
