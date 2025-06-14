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
    private Long sideRoomId;
    private String deleteYn; // chat_room 에서 남기는데 여기도 남기면 join 데이터 좀 줄으려나?
}
