package com.bmk.notification.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.bmk.notification.dto.FileUploadDto;
import com.bmk.notification.exceptions.AuthorizationException;
import com.bmk.notification.util.RestClient;
import com.bmk.notification.util.TokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequestMapping("file")
@RestController
public class FileUploadController {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static RestClient restClient;

    public FileUploadController(RestClient restClient) {
        this.restClient = restClient;
    }


    @GetMapping("upload/signature")
    public ResponseEntity getFileUploadDto(@RequestHeader String token) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, AuthorizationException, IOException {
        restClient.authorize(token, "gamma");
        String privateKey = System.getenv("imagekitpk");
        Long expiry = System.currentTimeMillis()+ TimeUnit.HOURS.toMillis(1);
        expiry = expiry/1000;
        String tokenn= UUID.randomUUID().toString();
        String signature = calculateRFC2104HMAC(tokenn+expiry, privateKey);
        return ResponseEntity.ok(new FileUploadDto(expiry, tokenn, signature));
    }
    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    public static String calculateRFC2104HMAC(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(data.getBytes()));
    }

}
