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
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
	private final JwtProvider JwtProvider;
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

            final String email = userEntity.getEmail();
            if (userEntity == null || userEntity.getEmail() == null || userRepository.existsByEmail(email)) {
                throw new CustomException();
            }
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new CustomException();
        }
    }

    /*
     *    회원 로그인
     */
    public UserDTO authenticate(HttpServletResponse response, UserDTO userDTO) {
        final UserEntity originalUser = userRepository.findByEmail(userDTO.getEmail());

        if(originalUser != null && passwordEncoder.matches(userDTO.getPassword(), originalUser.getPassword())) {
            //jwt cookie 생성
            String accessToken = JwtProvider.createToken(originalUser.getId());
            String refreshToken = JwtProvider.createRefreshToken(originalUser.getId());

            redisService.setValues(originalUser.getId(), refreshToken);

            //쿠키 생성
            JwtProvider.createCookie(accessToken, response, JwtProvider.accessTokenName, JwtProvider.accessTokenExpire);
            JwtProvider.createCookie(refreshToken, response, JwtProvider.refreshTokenName, JwtProvider.refreshTokenExpire);

            final UserDTO responseUserDTO = UserDTO.builder()
                    .id(originalUser.getId())
                    .email(originalUser.getEmail())
                    .username(originalUser.getUsername())
                    .build();
            return responseUserDTO;
		} else {
          log.warn("로그인에 실패하였습니다.", userDTO.getEmail() );
                throw new CustomException("로그인에 실패하였습니다.");
        }
    }

    //아이디 중복 체크 메소드
}

