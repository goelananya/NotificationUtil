package com.bmk.notification.dto;

import lombok.Data;

@Data
public class EmailDto {
    String subject;
    String toEmail;
    String content;
}
