package com.sideline.chat.room.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RoomRequest {
    private Long roomId;
    private String sender; // 유저 또는 옵저버
    private String target; // 대상 (상담원)
    private String roomType; // 채팅방 상태값 (main, side)
}
