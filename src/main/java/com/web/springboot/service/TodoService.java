package com.web.springboot.service;

import com.web.springboot.model.TodoEntity;
import com.web.springboot.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<TodoEntity> putTodo(final TodoEntity todoEntity) {
        final Optional<TodoEntity> original = todoRepository.findById(todoEntity.getId());
        original.ifPresent(todo -> {
            todo.changeTitle(todoEntity.getTitle());
            todo.changeDone(todoEntity.isDone());
        todoRepository.save(todo);
        });
        return getTodoList(todoEntity.getUserId()); //유저의 모든 todo리스트 반환
    }


}
