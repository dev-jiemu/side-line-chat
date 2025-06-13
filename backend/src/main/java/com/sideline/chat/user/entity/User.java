package com.sideline.chat.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_user")
@Getter
@Setter
@ToString
public class User {

    @Id
    private String userId;

    private String password;
    private String authType;
    private LocalDateTime createdAt;
    private String deleteYn;
}
