package com.web.springboot.service;

import com.web.springboot.common.exception.CustomException;
import com.web.springboot.domain.TodoEntity;
import com.web.springboot.domain.TodoRepository;
import com.web.springboot.dto.TodoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    String userId = "test";

    private final TodoRepository todoRepository;

    /*
     *    투두 저장
     */
    @Transactional(rollbackFor = {SQLException.class, Error.class})
    public void postTodo(TodoDTO todoDto) {
        TodoEntity entity = TodoDTO.toEntity(todoDto);
        entity.changeId(null);
        entity.changeUserId(userId);
        todoRepository.save(entity);
    }

    /*
     *    투두 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<TodoDTO> getTodoList(String userId) {
        List<TodoEntity> entities = todoRepository.findByUserId(userId);
        return entities.stream().map(TodoDTO::new).collect(Collectors.toList());
    }

    /*
     *    투두 수정
     */
    @Transactional(rollbackFor = {SQLException.class, Error.class})
    public void putTodo(TodoDTO todoDto) {
        TodoEntity todoEntity = todoRepository.findById(todoDto.getId())
                .orElseThrow(() -> new CustomException("해당 투두가 없습니다."));

        todoEntity.changeTitle(todoDto.getTitle());
        todoEntity.changeDone(todoDto.isDone());
    }

    /*
     *    투두 삭제
     */
    @Transactional(rollbackFor = {SQLException.class, Error.class})
    public void deleteTodo(Long id) {
        try {
            todoRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new CustomException("해당 투두가 없습니다.");
        }
    }
}
