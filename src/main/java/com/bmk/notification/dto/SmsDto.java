package com.bmk.notification.dto;

import lombok.Data;

@Data
public class SmsDto {
    String toPhone;
    String message;
}
