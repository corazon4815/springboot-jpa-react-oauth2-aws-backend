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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity<?> postUser(@RequestBody UserDTO userDTO) throws CustomException {
        authService.postUser(userDTO);
        return new ResponseEntity<>(new ResponseDTO<>(1, "회원 등록 성공", null), HttpStatus.CREATED);
    }

    /*
     *    회원 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response, @RequestBody UserDTO userDTO) throws CustomException {
        return new ResponseEntity<>(new ResponseDTO<>(1, "로그인 성공", authService.authenticate(response, userDTO)), HttpStatus.OK);
    }

    /*
     *    토큰 재발급
     */
    @PostMapping("/recreate")
    public ResponseEntity<?> reCreateToken(HttpServletRequest request, HttpServletResponse response) throws CustomException {
        authService.reCreateToken(request, response);
        return new ResponseEntity<>(new ResponseDTO<>(1, "토큰 재발급 성공", null), HttpStatus.OK);
    }


    //아이디 중복 체크 메소드
}
