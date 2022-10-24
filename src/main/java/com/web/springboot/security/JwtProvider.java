package com.web.springboot.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@Service
public class JwtProvider {
    private static final String SECRET_KEY = "fddsfdFSDFSDFf3434SDFSDF";

    public final static int TOKEN_VALIDATION_SECOND = 60 * 15; //15분

    public final static int REFRESH_TOKEN_VALIDATION_SECOND = 7 * 24 * 60 * 60; //일주일

    public final static String ACCESS_TOKEN_NAME = "accessToken";

    public final static String REFRESH_TOKEN_NAME = "refreshToken";

    /*
     * JWT를 생성한다
     */
    public String createToken(String id, long expireTime) {
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(id) // sub
                .setIssuedAt(new Date()) // iat
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000)) // exp
                .compact();
    }

    /*
     * 쿠키를 생성한다
     */
    public Cookie createCookie(String token, HttpServletResponse response, String cookieName, int maxAge) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); //추후 주석없애기
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
        return cookie;
    }

    /*
     * 쿠키를 가져온다
     */
    public Cookie getCookie(HttpServletRequest req, String cookieName) {
        final Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }

    /*
     * 쿠키를 검증한다
     */
    public String validateAndGetUserId(String token) {
        // parseClaimsJws메서드가 Base 64로 디코딩 및 파싱.
        // 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용 해 서명 후, token의 서명과 비교.
        // 위조되지 않았다면 페이로드(Claims) 리턴
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}