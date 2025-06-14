package com.sideline.chat.room.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class RoomResponse {
    private Long roomId;

    private String roomType;
    private String contactId;
    private String senderId;
    private LocalDateTime closedAt;
}
