package com.sideline.chat.room.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Room {
    private Long roomId;
    private String sender; // 유저 또는 옵저버
    private String target; // 대상 (상담원)
}
