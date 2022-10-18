package com.web.springboot.controller;

import com.web.springboot.dto.TodoDTO;
import com.web.springboot.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /*
     *    투두 저장 후 투두리스트 조회
     */
    @PostMapping
    public ResponseEntity<?> postTodo(@RequestBody TodoDTO todoDto) {
        return ResponseEntity.ok().body(todoService.postTodo(todoDto));
    }

    /*
     *    투두 리스트 조회
     */
    @GetMapping
    public ResponseEntity<?> getTodoList() {
        String userId = "test";
        return ResponseEntity.ok().body(todoService.getTodoList(userId));
    }

    /*
     *    투두 수정
     */
    @PutMapping
    public ResponseEntity<?> putTodo(@RequestBody TodoDTO todoDto) {
        return ResponseEntity.ok().body(todoService.putTodo(todoDto));
    }

    /*
     *    투두 삭제
     */
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestParam Long id) {
        return ResponseEntity.ok().body(todoService.deleteTodo(id));
    }


}
