package com.sideline.chat.room.dto;

import com.sideline.chat.room.entity.ChatLog;
import com.sideline.chat.room.service.ChatLogService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Slf4j
@Component
public class ChatRoom {

    @Autowired
    private ChatLogService chatLogService;

    // room_id, session
    // side room 이여도 room_id 가 유니크하게 선언되므로 이런 구조 가능함
    private final HashMap<Long, HashMap<String, WebSocketSession>> sessions = new HashMap<>();

    public void addSession(Long roomId, String userId, WebSocketSession session) {
        sessions.computeIfAbsent(roomId, k -> new HashMap<>()).put(userId, session);
        log.info("create room session - room id {}, user id {}", roomId, userId);
    }

    public void removeSession(Long roomId, String userId) {
        HashMap<String, WebSocketSession> roomSessions = sessions.get(roomId);
        if (roomSessions != null) {
            roomSessions.remove(userId);

            if (roomSessions.isEmpty()) {
                sessions.remove(roomId);
            }
            log.info("remove session - room id {}, user id {}", roomId, userId);
        }
    }

    // 룸 통으로 닫아버림
    public void closeRoom(Long roomId) {
        HashMap<String, WebSocketSession> roomSessions = sessions.get(roomId);
        if (roomSessions != null) {
            roomSessions.values().forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.close();
                    }
                } catch (Exception e) {
                    log.error("session close fail: {}", e.getMessage());
                }
            });

            sessions.remove(roomId);
            log.info("close room - room id {}", roomId);
        }
    }

    public void sendMessage(Long roomId, WebSocketSession senderSession, String message) {
        HashMap<String, WebSocketSession> roomSessions = sessions.get(roomId);
        if (roomSessions != null) {
            roomSessions.values().stream()
                    .filter(session -> !session.equals(senderSession) && session.isOpen())
                    .forEach(session -> {
                        try {
                            session.sendMessage(new TextMessage(message));

                            // 채팅 로그 남김
                            ChatLog chatLog = new ChatLog();
                            chatLog.setRoomId(roomId);
                            chatLog.setMessage(message);
                            chatLog.setSendAt(LocalDateTime.now());
                            chatLog.setDeleteYn("N");

                            chatLogService.createChatLog(chatLog);

                        } catch (Exception e) {
                            log.error("message send fail : {}", e.getMessage());
                        }
                    });
        }
    }

}
