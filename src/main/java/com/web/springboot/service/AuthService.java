package com.web.springboot.service;

import com.web.springboot.common.exception.CustomException;
import com.web.springboot.common.redis.RedisService;
import com.web.springboot.domain.UserEntity;
import com.web.springboot.domain.UserRepository;
import com.web.springboot.dto.UserDTO;
import com.web.springboot.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RedisService redisService;

    /*
     *    회원 등록
     */
    @Transactional(rollbackFor = {SQLException.class, Error.class})
    public void postUser(final UserDTO userDTO) throws CustomException {
        try {
            UserEntity userEntity = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();

            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /*
     *    회원 로그인
     */
    @Transactional
    public UserDTO authenticate(HttpServletResponse response, UserDTO userDTO) throws CustomException {
        try {
            final UserEntity originalUser = userRepository.findByEmail(userDTO.getEmail());
            if (originalUser != null && passwordEncoder.matches(userDTO.getPassword(), originalUser.getPassword())) {
                makeToken(response, originalUser.getId());
                final UserDTO responseUserDTO = UserDTO.builder()
                        .id(originalUser.getId())
                        .email(originalUser.getEmail())
                        .username(originalUser.getUsername())
                        .build();
                return responseUserDTO;
            } else {
                log.warn("로그인에 실패하였습니다.", userDTO.getEmail());
                throw new CustomException("로그인에 실패하였습니다.");
            }
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /*
     *    토큰 재발급
     */
    @Transactional(rollbackFor = {Error.class})
    public void reCreateToken(HttpServletRequest request, HttpServletResponse response) throws CustomException {
        try {
            Cookie refreshCookie = jwtProvider.getCookie(request, jwtProvider.REFRESH_TOKEN_NAME);
            String refreshToken = refreshCookie.getValue();

            //refreshToken 검증
            String id = jwtProvider.validateAndGetId(refreshToken);

            //redis 에 등록된 refreshToken 가져오기
            String findRefreshToken = redisService.getValues(id);

            if (findRefreshToken == null || refreshToken == null || !findRefreshToken.equals(refreshToken)) {
                throw new CustomException();
            }

            makeToken(response, id);

        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /*
     *    access, refesh토큰을 생성해서 쿠키에 넣고 refresh 토큰은 redis에 넣어준다.
     */
    @Transactional(rollbackFor = {Error.class})
    public void makeToken(HttpServletResponse response, String userId) throws CustomException {
        try {
            //accessToken, refreshToken 생성
            String newAccessToken = jwtProvider.createToken(userId, jwtProvider.TOKEN_VALIDATION_SECOND);
            String newRefreshToken = jwtProvider.createToken(userId, jwtProvider.REFRESH_TOKEN_VALIDATION_SECOND);

            //redis에 refresh 토큰 저장
            redisService.setValues(userId, newRefreshToken);

            //쿠키에 accessToken, refreshToken 저장
            jwtProvider.createCookie(newAccessToken, response, jwtProvider.ACCESS_TOKEN_NAME, jwtProvider.TOKEN_VALIDATION_SECOND);
            jwtProvider.createCookie(newRefreshToken, response, jwtProvider.REFRESH_TOKEN_NAME, jwtProvider.REFRESH_TOKEN_VALIDATION_SECOND);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    //아이디 중복 체크 메소드
}
