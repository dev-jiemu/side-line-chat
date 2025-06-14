package com.sideline.chat.room.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="chat_log")
@Getter
@Setter
@ToString
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long logSeqno;

    private Long roomId;
    private String message;
    private LocalDateTime sendAt;
    private String deleteYn;
}
