package com.bmk.notification.util;

import com.bmk.notification.exceptions.AuthorizationException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class RestClient {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    String FROM_EMAIL = System.getenv("fromEmail");
    String API_KEY = System.getenv("SENDGRID_API_KEY");

    @Autowired
    public RestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void authorize(String jwt, String apiType) throws AuthorizationException, IOException {
            TimeLoggerUtil timeLoggerUtil = new TimeLoggerUtil("authorize", "", this);
            try {
                logger.info("Calling authorize service");
                TokenUtil.getUserType(jwt).equals(apiType);
                Long.parseLong(TokenUtil.getUserId(jwt));
             } catch (Exception e){
                logger.info(e.getMessage());
                throw new AuthorizationException();
            }
            timeLoggerUtil.methodEnd();
    }

    @Async
    public void sendEmailSendGrid(String subject, String toEmail, String body) throws IOException {
        TimeLoggerUtil timeLoggerUtil = new TimeLoggerUtil("sendEmailSendGrid", "", this);
        Email from = new Email(FROM_EMAIL);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", body);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        timeLoggerUtil.methodEnd();
    }
}
