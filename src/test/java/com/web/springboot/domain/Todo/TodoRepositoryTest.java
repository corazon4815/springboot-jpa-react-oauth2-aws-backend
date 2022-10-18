package com.web.springboot.domain.Todo;

import com.web.springboot.domain.TodoEntity;
import com.web.springboot.domain.TodoRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @After //Junit에서 단위테스트가 끝날때마다 수행되는 메소드를 지정
    public void cleanup() {
        todoRepository.deleteAll();
    }

    //투두 생성
    @Test
    public void postTodo() {
        todoRepository.save(TodoEntity.builder()
                .userId("test")
                .title("title")
                .done(false)
                .build());
    }

    //특정 사용자의 투두 리스트 조회
    @Test
    public void getTodoList() {
        todoRepository.findByUserId("test");
    }
}
