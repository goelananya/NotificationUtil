package com.bmk.notification.util;

import com.bmk.notification.exceptions.AuthorizationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.DatatypeConverter;

@Log4j2
public class TokenUtil {
    public static String getUserId(String jwt) throws AuthorizationException {
        try {
            return getClaim(jwt).getId();
        } catch(Exception exp){
            throw new AuthorizationException();
        }
    }

    public static String getUserType(String jwt) {
        try {
            return getClaim(jwt).getSubject();
        } catch(Exception exp){
            log.info(exp);
            return null;
        }
    }

    public static Claims getClaim(String jwt){
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(System.getenv("jwtSecret")))
                .parseClaimsJws(jwt).getBody();
    }

}