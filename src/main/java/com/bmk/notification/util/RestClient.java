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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

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
            logger.info("Calling authorize service");
            String baseUrl = "https://bmkauth.herokuapp.com/api/v1/user/authorize";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("token", jwt);
            headers.set("apiType", apiType);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            try {
                Object object = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, Object.class).getBody();
            } catch (Exception e) {
                throw new AuthorizationException();
            }
            timeLoggerUtil.methodEnd();
    }

    @Async
    public void sendEmail(String subject, String toEmail, String body) throws IOException {
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
    }
}
