package com.sideline.chat.room.websocket;

import com.sideline.chat.room.dto.ChatSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketChatHandler extends TextWebSocketHandler {

    @Autowired
    private ChatSession chatRoom;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uri = session.getUri().toString();

        HashMap<String, String> params = extractParams(session.getUri().toString());

        // 토큰 검증
        if (params.get("room_id") != null && params.get("user_id") != null) {
            Long roomId = Long.valueOf(params.get("room_id"));
            String userId = params.get("user_id");

            chatRoom.addSession(roomId, userId, session);

            session.getAttributes().put("room_id", roomId);
            session.getAttributes().put("user_id", userId);
        } else {
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        Long roomId = (Long) session.getAttributes().get("room_id");
        String userId = (String) session.getAttributes().get("user_id");

        if (roomId != null && userId != null) {
            String payload = message.getPayload();
            log.info("Room {} 에서 userId {} 가 메세지 전달 = {}", roomId,userId, payload);

            chatRoom.sendMessage(roomId, session, payload);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long roomId = (Long) session.getAttributes().get("room_id");
        String userId = (String) session.getAttributes().get("user_id");

        if (roomId != null && userId != null) {
            chatRoom.removeSession(roomId, userId);
            String jsonMessage = String.format(
                    "{\"message\":\"%s\",\"type\":\"system\",\"sender\":\"system\"}",
                    userId + "님이 채팅방을 나갔습니다."
            );
            chatRoom.sendMessage(roomId, session, jsonMessage);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WebSocket 오류: " + exception.getMessage());
    }

    private HashMap<String, String> extractParams(String uri) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
        MultiValueMap<String, String> params = uriComponents.getQueryParams();

        HashMap<String, String> result = new HashMap<>();
        params.forEach((key, values) -> {
            if (!values.isEmpty()) {
                result.put(key, values.get(0));
            }
        });

        return result;
    }



}
