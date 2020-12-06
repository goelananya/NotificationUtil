package com.bmk.notification.controller;

import com.bmk.notification.dto.EmailDto;
import com.bmk.notification.dto.ResponseDto;
import com.bmk.notification.dto.SmsDto;
import com.bmk.notification.exceptions.AuthorizationException;
import com.bmk.notification.util.RestClient;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("email")
@RestController
public class NotificationController {

    private static RestClient restClient;

    public NotificationController(RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping("email")
    public ResponseEntity sendEmail(@RequestBody EmailDto emailDto, @RequestHeader String token) throws AuthorizationException, IOException {
        restClient.authorize(token, "alpha");
        String FROM_EMAIL = System.getenv("fromEmail");
        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Email from = new Email(FROM_EMAIL);
        String subject = emailDto.getSubject();
        Email to = new Email(emailDto.getToEmail());
        Content content = new Content("text/plain", emailDto.getContent());
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        return ResponseEntity.ok(new ResponseDto("200", "Success"));
    }

    @PostMapping("sms")
    public ResponseEntity sendSms(@RequestBody SmsDto smsDto, @RequestHeader String token) throws AuthorizationException {
        restClient.authorize(token, "alpha");
        String ACCOUNT_SID = System.getenv("twilioSid");
        String AUTH_TOKEN = System.getenv("twilioToken");
        String TWILIO_PHONE = System.getenv("twilioPhone");
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber(smsDto.getToPhone()), // to
                        new PhoneNumber(TWILIO_PHONE), // from
                        smsDto.getMessage())
                .create();
        return ResponseEntity.ok(new ResponseDto("200", "Success"));
    }
}
