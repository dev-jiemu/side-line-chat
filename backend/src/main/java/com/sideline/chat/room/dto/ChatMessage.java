package com.sideline.chat.room.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private Long roomId;
    private String sender; // user_id 또는 난수(고객일때)
    private String context;
}
