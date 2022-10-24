package com.web.springboot.security;

import com.web.springboot.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;
    private AntPathMatcher antPathMatcher;

    private String pattern = "/auth/**/*";

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.antPathMatcher = new AntPathMatcher();
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //인증이 필요 없는 uri 일 경우 바로 통과
            if (antPathMatcher.match(pattern, request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            Cookie accessCookie = jwtProvider.getCookie(request, JwtProvider.ACCESS_TOKEN_NAME);
            String accessToken = accessCookie.getValue();
            log.info("Filter is running...");
            if (accessToken != null && !accessToken.equalsIgnoreCase("null")) {
                // userId 가져오기. 위조 된 경우 예외 처리 된다.
                String userId = jwtProvider.validateAndGetUserId(accessToken);

                // 인증 완료; SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, // 인증된 사용자의 정보. 문자열이 아니어도 아무거나 넣을 수 있다.
                        null, //
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

}