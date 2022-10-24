package com.web.springboot.service;

import com.web.springboot.common.exception.CustomException;
import com.web.springboot.domain.UserEntity;
import com.web.springboot.domain.UserRepository;
import com.web.springboot.dto.UserDTO;
import com.web.springboot.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
	private final TokenProvider tokenProvider;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*
     *    회원 등록
     */
    public UserDTO authenticate(UserDTO userDTO) {
        final UserEntity originalUser = userRepository.findByEmail(userDTO.getEmail());
        if(originalUser != null && passwordEncoder.matches(userDTO.getPassword(), originalUser.getPassword())) {

            // 토큰 생성
            final String token = tokenProvider.createToken(originalUser);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(originalUser.getUsername())
                    .id(originalUser.getId())
                    .token(token)
                    .build();
            return responseUserDTO;
		} else {
          log.warn("로그인에 실패하였습니다.", userDTO.getEmail() );
                throw new CustomException("로그인에 실패하였습니다.");
        }
    }

    /*
     *    회원 로그인
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

    //아이디 중복 체크 메소드
}

