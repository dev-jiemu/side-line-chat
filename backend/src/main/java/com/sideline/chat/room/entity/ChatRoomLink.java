package com.sideline.chat.room.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class ChatRoomLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long linkSeqno;

    private String userId;
    private Long mainRoomId;
    private Long sideRoomId;
}
