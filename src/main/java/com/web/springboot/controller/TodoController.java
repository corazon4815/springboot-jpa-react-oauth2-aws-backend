package com.web.springboot.controller;

import com.web.springboot.common.dto.ResponseDTO;
import com.web.springboot.dto.TodoDTO;
import com.web.springboot.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /*
     *    투두 저장 후 투두리스트 조회
     */
    @PostMapping
    public ResponseEntity<?> postTodo(@RequestBody TodoDTO todoDto) {
        todoService.postTodo(todoDto);
        return new ResponseEntity<>(new ResponseDTO<>(1, "todo 등록 성공", null), HttpStatus.CREATED);
    }

    /*
     *    투두 리스트 조회
     */
    @GetMapping
    public ResponseEntity<?> getTodoList() {
        String userId = "test";
        List<TodoDTO> todoList = todoService.getTodoList(userId);
        return new ResponseEntity<>(new ResponseDTO<>(1, "todo 리스트 조회 성공", todoList), HttpStatus.OK);
    }

    /*
     *    투두 수정
     */
    @PutMapping
    public ResponseEntity<?> putTodo(@RequestBody TodoDTO todoDto) {
        todoService.putTodo(todoDto);
        return new ResponseEntity<>(new ResponseDTO<>(1, "todo 수정 성공", null), HttpStatus.OK);
    }

    /*
     *    투두 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") Long id) {
        todoService.deleteTodo(id);
        return new ResponseEntity<>(new ResponseDTO<>(1, "todo 삭제 성공", null), HttpStatus.OK);
    }


}
