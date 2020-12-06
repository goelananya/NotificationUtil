package com.bmk.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadDto {
    Long expiry;
    String token;
    String signature;
}
