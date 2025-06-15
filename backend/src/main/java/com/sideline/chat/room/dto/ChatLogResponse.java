package com.sideline.chat.room.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatLogResponse {
    private Long roomId;
    private Long logSeqno;
    private String message;
}
