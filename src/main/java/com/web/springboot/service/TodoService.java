package com.web.springboot.service;

import com.web.springboot.model.TodoEntity;
import com.web.springboot.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<TodoEntity> postTodo(final TodoEntity todoEntity) {
        todoRepository.save(todoEntity);
        return todoRepository.findByUserId(todoEntity.getUserId());
    }

    public List<TodoEntity> getTodoList(final String userId) {
        return todoRepository.findByUserId(userId);
    }


}
