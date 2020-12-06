package com.bmk.notification.util;

import com.bmk.notification.exceptions.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class RestClient {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    @Autowired
    public RestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void authorize(String jwt, String apiType) throws AuthorizationException {
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
    }
}
