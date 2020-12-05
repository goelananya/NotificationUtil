package com.bmk.notification.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;

@Log4j2
public class TokenUtil {

    private final static String SECRET_KEY = System.getenv("jwtSecret");

    public static boolean authorizeApi(String token) {
        log.info(SECRET_KEY);
        String userType = getUserType(token);
       log.info(userType);
        return userType==null?false:token.equals("alpha");
    }

    public static String getUserType(String jwt) {
            return getClaim(jwt).getSubject();
    }
    public static Claims getClaim(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }
}