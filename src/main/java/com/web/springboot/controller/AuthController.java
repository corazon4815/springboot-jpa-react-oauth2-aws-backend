package com.web.springboot.controller;

import com.web.springboot.common.dto.ResponseDTO;
import com.web.springboot.common.exception.CustomException;
import com.web.springboot.dto.UserDTO;
import com.web.springboot.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /*
     *    회원 등록
     */
    @PostMapping("/signup")
    public ResponseEntity<?> postUser(@RequestBody UserDTO userDTO) {
        try {
            authService.postUser(userDTO);
            return new ResponseEntity<>(new ResponseDTO<>(1, "회원 등록 성공", null), HttpStatus.CREATED);
        }catch (CustomException e){
            throw new CustomException("회원등록에 실패하였습니다.");
        }
    }

    /*
     *    회원 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
    return new ResponseEntity<>(new ResponseDTO<>(1, "로그인 성공", authService.authenticate(userDTO)), HttpStatus.CREATED);
    }

    //아이디 중복 체크 메소드
}
