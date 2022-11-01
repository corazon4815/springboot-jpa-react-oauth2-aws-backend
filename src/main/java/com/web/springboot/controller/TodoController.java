package com.web.springboot.controller;

import com.web.springboot.common.dto.ResponseDTO;
import com.web.springboot.common.exception.CustomException;
import com.web.springboot.dto.TodoDTO;
import com.web.springboot.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /*
     *    투두 저장
     */
    @PostMapping("/todo")
    public ResponseEntity<?> postTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO todoDto) throws CustomException {
        todoService.postTodo(userId, todoDto);
        return new ResponseEntity<>(new ResponseDTO<>(1, "todo 등록 성공", null), HttpStatus.OK);
    }

    /*
     *    투두 리스트 조회
     */
    @GetMapping("/todo")
    public ResponseEntity<?> getTodoList(@AuthenticationPrincipal String userId) throws CustomException {
        List<TodoDTO> todoList = todoService.getTodoList(userId);
        return new ResponseEntity<>(new ResponseDTO<>(1, "todo 리스트 조회 성공", todoList), HttpStatus.OK);
    }

    /*
     *    투두 수정
     */
    @PutMapping("/todo")
    public ResponseEntity<?> putTodo(@RequestBody TodoDTO todoDto) throws CustomException {
        todoService.putTodo(todoDto);
        return new ResponseEntity<>(new ResponseDTO<>(1, "todo 수정 성공", null), HttpStatus.OK);
    }

    /*
     *    투두 삭제
     */
    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable("todoId") Long todoId, @AuthenticationPrincipal String userId) throws CustomException {
        todoService.deleteTodo(todoId);
        return new ResponseEntity<>(new ResponseDTO<>(1, "todo 삭제 성공", null), HttpStatus.OK);
    }


}
