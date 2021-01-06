package com.bmk.notification.util;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class TimeLoggerUtil {

    String methodName;
    String data;
    Long startTime;
    Long endTime;
    RestClient restClient;

    public TimeLoggerUtil(String methodName, String data, RestClient restClient) {
        startTime = System.currentTimeMillis();
        this.methodName = methodName;
        this.data = data;
        this.restClient = restClient;
    }

    public void methodEnd() throws IOException {
        endTime = System.currentTimeMillis();
        Long totalTime = endTime - startTime;
        log.info("METHOD TIME:"+totalTime+" METHOD NAME:"+methodName+" DATA:"+data);
        if(totalTime>1500)
            restClient.sendEmailSendGrid("CRITICAL: API TIME LIMIT EXCEEDED", "contact@bookmykainchi.com", methodName+" TIME LIMIT EXCEEDED "+data+" TIME TAKEN:"+totalTime);
    }
}