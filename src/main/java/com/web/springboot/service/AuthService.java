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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
	private final TokenProvider tokenProvider;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
}

