package com.web.springboot.service;

import com.web.springboot.common.dto.ResponseDTO;
import com.web.springboot.common.exception.CustomException;
import com.web.springboot.domain.UserEntity;
import com.web.springboot.domain.UserRepository;
import com.web.springboot.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
}