package com.sideline.chat.room.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ChatRoomHierarchyResponse {
    private Long roomId;
    private String contactId;
    private String senderId;
    private String roomType;
    private LocalDateTime closedAt;
    private List<ChatRoomHierarchyResponse> sideRooms;
}
