package com.sideline.chat.room.service;

import com.sideline.chat.room.entity.ChatLog;
import com.sideline.chat.room.repository.ChatLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChatLogService {

    @Autowired
    private ChatLogRepository chatLogRepository;

    public List<ChatLog> getLogList(Long roomId) {
        return chatLogRepository.findChatLogByRoomIdAndDeleteYn(roomId, "N");
    }

    public void createChatLog(ChatLog chatLog) {
        chatLogRepository.save(chatLog);
    }

    public void deleteChatLog(Long logSeqno) {
        chatLogRepository.deleteById(logSeqno);
    }

}
