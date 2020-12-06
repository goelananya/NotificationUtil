package com.bmk.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto {
    String responseCode;
    String response;
}
