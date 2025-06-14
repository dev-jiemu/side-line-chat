package com.sideline.chat.room.controller;

import com.sideline.chat.common.dto.BaseResponse;
import com.sideline.chat.room.entity.ChatLog;
import com.sideline.chat.room.service.ChatLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        // TODO :

        response = new BaseResponse<>(null);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/{roomId}")
    public ResponseEntity getChatLogList(@PathVariable Long roomId) {
        log.info("room id log list search : {}", roomId);
        BaseResponse response;

        // TODO :

        response = new BaseResponse<>(null);
        return ResponseEntity.ok(response);
    }
}
