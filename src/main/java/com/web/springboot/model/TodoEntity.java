package com.web.springboot.model;

import lombok.*;
import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Todo")
public class TodoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;

    @Column(length = 50)
    private String userId;

    @Column(columnDefinition = "TEXT")
    private String title;

    private boolean done;

    public void changeId(Long id) {this.id = id; }
    public void changeUserId(String userId) {this.userId = userId; }
    public void changeTitle(String title) { this.title = title; }
    public void changeDone(boolean done) {
        this.done = done;
    }
}
