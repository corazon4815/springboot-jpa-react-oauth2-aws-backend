package com.web.springboot.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Todo")
public class TodoEntity {
    @javax.persistence.Id
    @Id
    @GeneratedValue(generator = "system-uuid") //"system-uuid"라는 generator를 사용
    @GenericGenerator(name="system-uuid", strategy = "uuid") //GenericGenerator의 매개변수 strategy로 "uuid"를 넘김
    //"system-uuid"라는 이름의 GenericGenerator를 만들고 Generator는 @GeneratedValue가 참조해 사용한다
    private String id;
    private String userId;
    private String title;
    private boolean done;

    public void changeId(String userId) {this.id = id; }
    public void changeUserId(String userId) {this.userId = userId; }
    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeDone(boolean done) {
        this.done = done;
    }
}
