package com.web.springboot.controller;

import com.web.springboot.dto.ResponseDTO;
import com.web.springboot.dto.TodoDTO;
import com.web.springboot.model.TodoEntity;
import com.web.springboot.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDto) {
        try {
            String userId = "test";
            TodoEntity entity = TodoDTO.toEntity(todoDto);
            entity.setId(null);
            entity.setUserId(userId);
            List<TodoEntity> entities = todoService.create(entity);
            //리턴된 엔티티리스트를 TodoDTO 리스트로 반환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);

        }
    }

}
