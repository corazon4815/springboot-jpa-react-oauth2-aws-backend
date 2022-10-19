package com.web.springboot.common;

import com.web.springboot.common.dto.ResponseDTO;
import com.web.springboot.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ExceptionController {

    //에러 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO<?>> apiException(CustomException e){
        return new ResponseEntity<>(new ResponseDTO<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
    }

}
