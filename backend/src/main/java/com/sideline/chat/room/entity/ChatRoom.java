package com.sideline.chat.room.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="chat_room")
@Getter
@Setter
@ToString
public class ChatRoom {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long roomId;

    private String contactId;
    private String senderId;
    private String roomType;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private String deleteYn;
}
