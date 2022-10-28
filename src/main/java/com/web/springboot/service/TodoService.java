package com.web.springboot.service;

import com.web.springboot.common.exception.CustomException;
import com.web.springboot.domain.TodoEntity;
import com.web.springboot.domain.TodoRepository;
import com.web.springboot.dto.TodoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void postTodo(String id, TodoDTO todoDto) throws CustomException {
        try {
            TodoEntity entity = TodoDTO.toEntity(todoDto);
            entity.changeUserId(id);
            todoRepository.save(entity);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /*
     *    투두 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<TodoDTO> getTodoList(String userId) throws CustomException {
        try {
            List<TodoEntity> entities = todoRepository.findByUserId(userId);
            return entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /*
     *    투두 수정
     */
    @Transactional(rollbackFor = {SQLException.class, Error.class})
    public void putTodo(TodoDTO todoDto) throws CustomException {
        try {
            TodoEntity todoEntity = todoRepository.findById(todoDto.getId())
                    .orElseThrow(() -> new CustomException("해당 투두가 없습니다."));
            todoEntity.changeTitle(todoDto.getTitle());
            todoEntity.changeDone(todoDto.isDone());
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /*
     *    투두 삭제
     */
    @Transactional(rollbackFor = {SQLException.class, Error.class})
    public void deleteTodo(Long id) throws CustomException {
        try {
            todoRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
