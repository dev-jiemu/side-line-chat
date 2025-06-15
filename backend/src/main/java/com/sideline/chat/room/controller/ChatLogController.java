package com.sideline.chat.room.controller;

import com.sideline.chat.common.dto.BaseResponse;
import com.sideline.chat.room.dto.ChatLogResponse;
import com.sideline.chat.room.entity.ChatLog;
import com.sideline.chat.room.service.ChatLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/logs")
public class ChatLogController {

    @Autowired
    private ChatLogService chatLogService;

    @DeleteMapping(value="/{logSeqno}")
    public ResponseEntity<BaseResponse> deleteChatLog(@PathVariable Long logSeqno){
        log.info("delete log : {}", logSeqno);
        BaseResponse response;

        if (logSeqno == 0) {
            response = new BaseResponse<>("log_seqno required");
            return ResponseEntity.badRequest().body(response);
        }

        ChatLog chatLog = chatLogService.getChatLog(logSeqno);
        chatLogService.deleteChatLog(chatLog);

        response = new BaseResponse<>(null);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/{roomId}")
    public ResponseEntity<BaseResponse<List<ChatLogResponse>>> getChatLogList(@PathVariable Long roomId) {
        log.info("room id log list search : {}", roomId);
        BaseResponse response;

        List<ChatLogResponse> chatLogs = new ArrayList<>();

        List<ChatLog> getLogList = chatLogService.getLogList(roomId);
        if (!getLogList.isEmpty()) {
            for (ChatLog chatLog : getLogList) {
                ChatLogResponse chatLogResponse = new ChatLogResponse();
                chatLogResponse.setLogSeqno(chatLog.getLogSeqno());
                chatLogResponse.setRoomId(chatLog.getRoomId());
                chatLogResponse.setMessage(chatLog.getMessage());

                chatLogs.add(chatLogResponse);
            }
        }

        response = new BaseResponse<>(null);
        response.setData(chatLogs);

        return ResponseEntity.ok(response);
    }
}
