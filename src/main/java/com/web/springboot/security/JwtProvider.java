package com.web.springboot.security;

import com.web.springboot.domain.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class JwtProvider {
    private static final String SECRET_KEY = "fddsfdFSDFSDFf3434SDFSDF";

    public final static int accessTokenExpire = 60 * 30;

    public final static int refreshTokenExpire = 20160;

    public final static String accessTokenName = "accessToken";

    public final static String refreshTokenName = "refreshToken";

    /*
     * JWT를 생성한다
     */
    public String createToken(UserEntity userEntity) {
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(userEntity.getId()) // sub
                .setIssuedAt(new Date()) // iat
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS))) // exp
                .compact();
    }

    /*
     * 쿠키를 생성한다
     */
    public Cookie createCookie(UserEntity userEntity, HttpServletResponse response, String cookieName, int maxAge) {
        String jwt = createToken(userEntity);
        Cookie cookie = new Cookie(cookieName, jwt);
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

    /*
     * token에서 Bearer제외하고 jwt를 리턴한다
     */
//    public String parseBearerToken(String accessToken) {
//		if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
//			return accessToken.substring(7);
//		}
//		return null;
//	}
}