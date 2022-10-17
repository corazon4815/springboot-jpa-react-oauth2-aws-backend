package com.web.springboot.controller;

import com.web.springboot.dto.ResponseDTO;
import com.web.springboot.dto.TodoDTO;
import com.web.springboot.model.TodoEntity;
import com.web.springboot.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<?> postTodo(@RequestBody TodoDTO todoDto) {
        try {
            String userId = "test";
            TodoEntity entity = TodoDTO.toEntity(todoDto);
            entity.changeId(null);
            entity.changeUserId(userId);
            List<TodoEntity> entities = todoService.postTodo(entity);
            //리턴된 엔티티리스트를 TodoDTO 리스트로 반환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getTodoList() {
        String userId = "test";
        List<TodoEntity> entities = todoService.getTodoList(userId); //test 사용자의 todo리스트를 가져온다
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> putTodo(@RequestBody TodoDTO todoDto) {
        String userId = "test";
        TodoEntity entity = TodoDTO.toEntity(todoDto);
        entity.changeUserId(userId);
        List<TodoEntity> entities = todoService.putTodo(entity);
        //리턴된 엔티티리스트를 TodoDTO 리스트로 반환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO todoDto) {
        try {
            String userId = "test";
            TodoEntity entity = TodoDTO.toEntity(todoDto);
            entity.changeUserId(userId);
            List<TodoEntity> entities = todoService.deleteTodo(entity);
            //리턴된 엔티티리스트를 TodoDTO 리스트로 반환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }


}
