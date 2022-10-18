package com.web.springboot.service;

import com.web.springboot.domain.TodoEntity;
import com.web.springboot.domain.TodoRepository;
import com.web.springboot.dto.ResponseDTO;
import com.web.springboot.dto.TodoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    String userId = "test";

    private final TodoRepository todoRepository;

    /*
     *    투두 저장 후 투두리스트 조회
     */
    @Transactional
    public ResponseDTO<TodoDTO> postTodo(TodoDTO todoDto) {
        TodoEntity entity = TodoDTO.toEntity(todoDto);
        entity.changeId(null);
        entity.changeUserId(userId);
        todoRepository.save(entity);

        return getTodoList(userId);
    }

    /*
     *    투두 리스트 조회
     */
    public ResponseDTO<TodoDTO> getTodoList(String userId) {
        List<TodoEntity> entities = todoRepository.findByUserId(userId);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        return ResponseDTO.<TodoDTO>builder().data(dtos).build();
    }

    /*
     *    투두 수정 후 투두리스트 조회
     */
    @Transactional
    public ResponseDTO<TodoDTO> putTodo(TodoDTO todoDto) {
        TodoEntity todoEntity = todoRepository.findById(todoDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 투두가 없습니다."));

        todoEntity.changeTitle(todoDto.getTitle());
        todoEntity.changeDone(todoDto.isDone());
        return getTodoList(userId); //유저의 모든 todo리스트 반환
    }

    /*
     *    투두 삭제 후 투두리스트 조회
     */
    @Transactional
    public ResponseDTO<TodoDTO> deleteTodo(Long id) {
        todoRepository.deleteById(id);
        return getTodoList(userId); //유저의 모든 todo리스트 반환
    }


}
